package com.company.web.controller;

import com.company.model.ChangePassword;
import com.company.model.ContactInformation;
import com.company.model.PagerModel;
import com.company.model.PersonView;
import com.company.persist.domain.Gender;
import com.company.persist.domain.User;
import com.company.security.CurrentProfile;
import com.company.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.company.config.Constants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = URI_API_PREFIX)
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final UserService userService;

    @ModelAttribute("changePassword")
    public ChangePassword changePasswordDto() {
        return new ChangePassword();
    }

    @ModelAttribute("changeContact")
    public ContactInformation changeContactDto() {
        return new ContactInformation();
    }

    @GetMapping("/person")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCurrentPersonPage(@CurrentProfile User profile, Model model) {
        model.addAttribute("user", new PersonView(profile));
        return "profile";
    }

    @GetMapping("/person/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getPersonPage(@CurrentProfile User profile,
                                @PathVariable("id") Long id, Model model) {
        if(id.equals(profile.getId())) {
            return "redirect:/api/person";
        }

        log.debug("Request to get person id : {}", id);
        final User person = userService.findById(id);
        if (null == person) {
            log.debug("Person id:{} is not signed up", id);
        }
        model.addAttribute("user", new PersonView(person));
        return "anyPerson";
    }

    @GetMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getPasswordPage(@CurrentProfile User profile, Model model) {
        model.addAttribute("user", new PersonView(profile));
        return "changePassword";
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String changePassword(@CurrentProfile User profile,
                                 @ModelAttribute("changePassword") @Valid ChangePassword changePasswordDto,
                                 BindingResult result) {

        if(!userService.hasValidPassword(profile, changePasswordDto.getCurrentPassword())) {
            result.rejectValue("currentPassword", null, "Current password is not correct");
        }
        if (result.hasErrors()){
            return "changePassword";
        }
        userService.changePassword(profile,changePasswordDto.getPassword());
        return "redirect:/api/changePassword?success";
    }

    @GetMapping("/changeContact")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getContactsPage(@CurrentProfile User profile,
                                  Model model) {
        model.addAttribute("user", new PersonView(profile));
        model.addAttribute("genders", Gender.values());
        return "changeContacts";
    }

    @PostMapping("/changeContact")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String changeContact(@CurrentProfile User profile,
                                @ModelAttribute("changeContact") @Valid ContactInformation contact,
                                BindingResult result) {
        final String oldEmail = profile.getEmail();
        final String newEmail = contact.getEmail();
        if (!oldEmail.equals(newEmail)) {
            final User resultOfCheck = userService.findByEmail(newEmail);
            if (null != resultOfCheck) {
                log.debug("Attempt to change email value from: {} to {} failed! " +
                        "E-mail is already used by another contact : {}", resultOfCheck);
                result.rejectValue("email", null, "E-mail is already used by another contact");
            }
        }
        if (result.hasErrors()){
            return "changeContacts";
        }
        profile.setFirstName(contact.getFirstName());
        profile.setLastName(contact.getLastName());
        profile.setEmail(contact.getEmail());
        profile.setPhone(contact.getPhone());
        profile.setBirthDate(contact.getBirthDate());
        profile.setGender(contact.getGender());
        userService.update(profile);
        return "redirect:/api/changeContact?success";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ModelAndView getUsersPage(@CurrentProfile User profile,
                                     @RequestParam("pageSize") Optional<Integer> pageSize,
                                     @RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("users");

        // Evaluate page size. If requested parameter is null, return initial page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<PersonView> preparedList = userService.getModelPeople(PageRequest.of(evalPage, evalPageSize));
        log.debug("PersonView list get total pages " + preparedList.getTotalPages() + ", PersonView list get number " + preparedList.getNumberOfElements());
        PagerModel pager = new PagerModel(preparedList.getTotalPages(),preparedList.getNumberOfElements(),BUTTONS_TO_SHOW);

        // add personList
        modelAndView.addObject("personList", preparedList);

        // evaluate page size
        modelAndView.addObject("selectedPageSize", evalPageSize);

        // add page sizes
        modelAndView.addObject("pageSizes", PAGE_SIZES);

        // add pager
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("user", profile);
        return modelAndView;
    }

    @PutMapping("/friends/add/{personId}")
    public String addFriend() {

    }

    @PutMapping("/friends/remove/{personId}")
    public String removeFriend() {

    }
}
