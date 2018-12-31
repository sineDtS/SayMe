package com.company.web.controller;

import com.company.model.PersonView;
import com.company.persist.domain.User;
import com.company.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.company.config.Constants.*;

@Api(tags = "Admin", description = "Admin's operations")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = URI_ADMIN_PREFIX, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserService userServiceImpl;

    @GetMapping("/users")
    public Page<PersonView> getUsers
            (@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
             @PageableDefault(size = 20) Pageable pageRequest) {
        log.debug("REST request to get users list (searchTerm:{}, pageRequest:{})", searchTerm, pageRequest);

        final Page<User> people = userServiceImpl.getPeople(searchTerm, pageRequest);

        return people.map(PersonView::new);
    }
}
