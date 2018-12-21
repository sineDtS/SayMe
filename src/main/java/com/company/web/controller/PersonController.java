package com.company.web.controller;

import com.company.model.ChangePassword;
import com.company.model.ContactInformation;
import com.company.model.PersonView;
import com.company.model.UserRegistration;
import com.company.persist.domain.Gender;
import com.company.persist.domain.User;
import com.company.security.CurrentProfile;
import com.company.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
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
    public String getPersonPage(@CurrentProfile User profile, Model model) {
        model.addAttribute("user", new PersonView(profile));
        return "profile";
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


}
