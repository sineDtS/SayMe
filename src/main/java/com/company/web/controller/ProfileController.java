package com.company.web.controller;

import com.company.model.ChangePassword;
import com.company.model.ContactInformation;
import com.company.model.PersonView;
import com.company.model.UserRegistration;
import com.company.persist.domain.User;
import com.company.security.CurrentProfile;
import com.company.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static com.company.config.Constants.*;

@Api(tags = "Profile", description = "User settings")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = URI_API_PREFIX, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final UserService userServiceImpl;

    @ApiOperation(value = "Login")
    @GetMapping("/login")
    public ResponseEntity<PersonView> login(@ApiIgnore @CurrentProfile User profile) {
        log.debug("REST request to get current profile: {}", profile);

        if (null == profile) {
            log.warn("Attempt getting unauthorised profile information failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(new PersonView(profile));
    }

    @ApiOperation(value = "Sign-Up")
    @PostMapping(value="/registration")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserRegistration person) throws URISyntaxException {
        log.debug("REST request to sign up a new profile: {}", person);

        final User result = userServiceImpl.findByEmail(person.getEmail());
        if (null != result) {
            log.debug("Attempt sign up email: {} failed! E-mail is already used by another contact: {}",
                    person.getEmail(), result);

            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(ERROR_SIGN_UP_EMAIL);
        }

        final User profile = userServiceImpl.create(person);

        return ResponseEntity.created(new URI(URI_API_PREFIX + "/person/" + profile.getId())).build();
    }

    @ApiOperation(value = "Change contact information")
    @PutMapping("/changeContact")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> updatePerson(
            @ApiIgnore @CurrentProfile User profile,
            @Valid @RequestBody ContactInformation contact) {
        log.debug("REST request to update current profile: {} contact information", profile);

        if (!profile.getId().equals(contact.getId())) {
            log.error("Updating profile: {} doesn't match the current one: {}", contact, profile);

            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(ERROR_UPDATE_PROFILE);
        }

        final String oldEmail = profile.getEmail();
        final String newEmail = contact.getEmail();
        if (!oldEmail.equals(newEmail)) {
            final User result = userServiceImpl.findByEmail(newEmail);
            if (null != result) {
                log.debug("Attempt to change email value from: {} to  {} failed! " +
                        "E-mail is already used by another contact : {}", result);

                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(ERROR_UPDATE_EMAIL);
            }
        }

        profile.setFirstName(contact.getFirstName());
        profile.setLastName(contact.getLastName());
        profile.setEmail(contact.getEmail());
        profile.setPhone(contact.getPhone());
        profile.setBirthDate(contact.getBirthDate());
        profile.setGender(contact.getGender());
        userServiceImpl.update(profile);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Change password")
    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changePassword(
            @ApiIgnore @CurrentProfile User profile,
            @Valid @RequestBody ChangePassword pwd) throws URISyntaxException {
        log.debug("REST request to change pwd: {}", pwd);

        if (null == profile) {
            log.warn("Attempt to change unauthorised profile password failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final String currentPwd = pwd.getCurrentPassword();
        final String newPwd = pwd.getPassword();
        if (!userServiceImpl.hasValidPassword(profile, currentPwd)) {
            log.warn("Current password: {} doesn't match profile's one: {}", currentPwd, profile);
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(ERROR_PASSWORD_CONFIRMATION);
        }

        userServiceImpl.changePassword(profile, newPwd);

        return ResponseEntity.ok().build();
    }
}
