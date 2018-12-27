package com.company.web.controller;

import com.company.model.UserRegistration;
import com.company.persist.domain.User;
import com.company.security.CurrentProfile;
import com.company.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final UserService userServiceImpl;

    @ModelAttribute("userRegister")
    public UserRegistration userRegistrationDto() {
        return new UserRegistration();
    }

    @GetMapping(value={"/", "/index"})
    public String getHomePage(){
        return "index";
    }

    @GetMapping(value="/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping(value="/logout-success")
    public String getLogoutPage(){
        return "logout";
    }

    @GetMapping(value="/registration")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping(value="/registration")
    public String registerUserAccount(@ModelAttribute("userRegister") @Valid UserRegistration userDto,
                                      BindingResult result){

        User existing = userServiceImpl.findByEmail(userDto.getEmail());
        if (existing != null){
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()){
            return "register";
        }
        userServiceImpl.create(userDto);
        return "redirect:/registration?success";
    }

    @GetMapping(value={"/403"})
    public ModelAndView getAccessDeniedPage(@CurrentProfile User profile){
        return new ModelAndView("403", "profile", profile);
    }
}
