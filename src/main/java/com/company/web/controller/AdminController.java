package com.company.web.controller;

import com.company.model.PersonView;
import com.company.persist.domain.User;
import com.company.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.company.config.Constants.*;

@Api(tags = "Admin", description = "Admin's operations")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = URI_ADMIN_PREFIX, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserService userServiceImpl;

    @ApiOperation(value = "Users search")
    @GetMapping("/users")
    public Page<PersonView> getUsers(
            @RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {
        log.debug("REST request to get users list (searchTerm:{})", searchTerm);

        // Evaluate page size. If requested parameter is null, return initial page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        final Page<PersonView> people = userServiceImpl.getModelPeople(searchTerm, PageRequest.of(evalPage, evalPageSize));

        return people;
    }
}
