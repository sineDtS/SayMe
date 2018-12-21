package com.company.web.controller;

import com.company.model.PagerModel;
import com.company.model.PersonView;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10};

    private final UserService userService;

    @GetMapping("/main")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAdminPage(@CurrentProfile User profile, Model model) {
        model.addAttribute("user", new PersonView(profile));
        return "admin";
    }

    @GetMapping("/users")
    public ModelAndView getPeople(@RequestParam("pageSize") Optional<Integer> pageSize,
                                  @RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("adminUsers");

        // Evaluate page size. If requested parameter is null, return initial page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<User> preparedList = userService.getPeople(PageRequest.of(evalPage, evalPageSize));
        log.debug("PersonView list get total pages " + preparedList.getTotalPages() + " PersonView list get number " + preparedList.getNumber());
        PagerModel pager = new PagerModel(preparedList.getTotalPages(),preparedList.getNumber(),BUTTONS_TO_SHOW);

        // add personList
        modelAndView.addObject("personList", preparedList);

        // evaluate page size
        modelAndView.addObject("selectedPageSize", evalPageSize);

        // add page sizes
        modelAndView.addObject("pageSizes", PAGE_SIZES);

        // add pager
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }
}
