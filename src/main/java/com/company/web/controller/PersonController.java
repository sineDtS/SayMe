package com.company.web.controller;

import com.company.model.PersonView;
import com.company.persist.domain.User;
import com.company.security.CurrentProfile;
import com.company.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

import static com.company.config.Constants.*;

@Api(tags = "Person", description = "Operations about persons")
@RestController
@RequestMapping(value = URI_API_PREFIX, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final UserService userServiceImpl;

    @ApiOperation(value = "Find a person by Id")
    @GetMapping("/person/{id}")
    public ResponseEntity<PersonView> getPerson(
            @PathVariable("id") Long id) {
        log.debug("REST request to get person id:{}", id);

        final User person = userServiceImpl.findById(id);
        if (null == person) {
            log.debug("Person id:{} is not signed up", id);

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new PersonView(person));
    }


    @ApiOperation(value = "Persons search")
    @GetMapping("/people")
    public Page<PersonView> getPeople(
            @RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {
        log.debug("REST request to get people list (searchTerm:{})", searchTerm);

        // Evaluate page size. If requested parameter is null, return initial page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<PersonView> people = userServiceImpl.getModelPeople(searchTerm, PageRequest.of(evalPage, evalPageSize));

        return people;
    }

    @ApiOperation(value = "Find friends")
    @GetMapping("/friends")
    public Page<PersonView> getFriends(
            @ApiIgnore @CurrentProfile User profile,
            @RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {
        log.debug("REST request to get person's: {} friend list (searchTerm:{})", profile, searchTerm);

        // Evaluate page size. If requested parameter is null, return initial page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        final Page<PersonView> friends = userServiceImpl.getFriends(profile, searchTerm, PageRequest.of(evalPage, evalPageSize));

        return friends;
    }

    @ApiOperation(value = "Find followers")
    @GetMapping("/friendOf")
    public Page<PersonView> getFriendOf(
            @ApiIgnore @CurrentProfile User profile,
            @RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {
        log.debug("REST request to get person's: {} friend_of list (searchTerm:{})", profile, searchTerm);

        // Evaluate page size. If requested parameter is null, return initial page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        final Page<PersonView> friendOf = userServiceImpl.getFriendOf(profile, searchTerm, PageRequest.of(evalPage, evalPageSize));

        return friendOf;
    }

    @ApiOperation(value = "Add as friend")
    @PutMapping("/friends/add/{personId}")
    public ResponseEntity<Void> addFriend(
            @ApiIgnore @CurrentProfile User profile,
            @PathVariable("personId") Long id) {
        log.debug("REST request to add id:{} as a person's: {} friend", id, profile);

        final User person = userServiceImpl.findById(id);
        if (null == person) {
            return ResponseEntity.notFound().build();
        }

        userServiceImpl.addFriend(profile, person);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Remove friend")
    @PutMapping("/friends/remove/{personId}")
    public ResponseEntity<Void> removeFriend(
            @ApiIgnore @CurrentProfile User profile,
            @PathVariable("personId") Long id) {
        log.debug("REST request to remove id:{} from person: {} friends", id, profile);

        final User person = userServiceImpl.findById(id);
        if (null == person) {
            return ResponseEntity.notFound().build();
        }

        userServiceImpl.removeFriend(profile, person);

        return ResponseEntity.ok().build();
    }


}
