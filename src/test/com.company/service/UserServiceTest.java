package com.company.service;

import com.company.AbstractApplicationTest;
import com.company.config.DataConfig;
import com.company.config.SecurityConfig;
import com.company.config.WebAppConfig;
import com.company.model.UserRegistration;
import com.company.persist.domain.Gender;
import com.company.persist.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataConfig.class, SecurityConfig.class, WebAppConfig.class})
@Transactional
public class UserServiceTest extends AbstractApplicationTest {

    private UserServiceImpl userService;

    @Test
    public void shouldFindPersonWithCorrectIdAndFields() throws Exception {
        final User person = userService.findById(1L);

        assertThat(person.getId()).isEqualTo(1L);
        assertThat(person.getFirstName()).isEqualTo("Denis");
        assertThat(person.getLastName()).isEqualTo("Streltsov");
        assertThat(person.getFullName()).isEqualTo("Denis Streltsov");
        assertThat(person.getEmail()).isEqualTo("den.strelts@gmail.com");
        assertThat(person.getPhone()).isEqualTo("+375295839006");
        assertThat(person.getBirthDate()).isEqualTo(new GregorianCalendar(1996, 6, 7).getTime());
        assertThat(person.getGender()).isEqualTo(Gender.MALE);
        //...
    }

    @Test
    public void shouldFindPersonWithCorrectEmail() throws Exception {
        final User person = userService.findByEmail("den.strelts@gmail.com");

        assertThat(person.getId()).isEqualTo(1L);
        assertThat(person.getEmail()).isEqualTo("den.strelts@gmail.com");
    }

    @Test
    public void shouldFindAllPeople() throws Exception {
        final Page<User> people = userService.getPeople("", getDefaultPageRequest());

        assertThat(people).hasSize(8);
        assertThat(people)
                .extracting("id", "fullName")
                .contains(
                        tuple(1L, "Denis Streltsov"),
                        tuple(4L, "Natalia Shmatova"));
    }

    @Test
    public void shouldFindAllFriends() throws Exception {
        final User person = userService.findById(1L);
        final Page<User> friends = userService.getFriends(person, "", getDefaultPageRequest());

        assertThat(friends).hasSize(2);
        assertThat(friends)
                .extracting("id", "fullName")
                .contains(
                        tuple(4L, "Natalia Shmatova"),
                        tuple(2L, "And Vor"));
    }

    @Test
    public void shouldFindAllFriendOf() throws Exception {
        final User person = userService.findById(1L);
        final Page<User> friendOf = userService.getFriendOf(person, "", getDefaultPageRequest());

        assertThat(friendOf).hasSize(2);
        assertThat(friendOf)
                .extracting("id", "fullName")
                .contains(
                        tuple(4L, "Natalia Shmatova"),
                        tuple(2L, "And Vor"));
    }

    @Test
    public void shouldFindAPerson() throws Exception {
        final User person = userService.findById(1L);

        assertThat(person)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("fullName", "Denis Streltsov");
    }

    @Test
    public void shouldAddAndRemoveAFriend() throws Exception {
        final User person = userService.findById(1L);
        final User friend = userService.findById(3L);

        // Check preconditions
        assertFalse(person.hasFriend(friend));
        assertFalse(person.isFriendOf(friend));
        assertFalse(friend.hasFriend(person));
        assertFalse(friend.isFriendOf(person));

        // Check when person makes friendship with anotherPerson
        userService.addFriend(person, friend);
        assertTrue(person.hasFriend(friend));
        assertFalse(person.isFriendOf(friend));
        assertFalse(friend.hasFriend(person));
        assertTrue(friend.isFriendOf(person));

        // Check when person severs friendship with anotherPerson
        userService.removeFriend(person, friend);
        assertFalse(person.hasFriend(friend));
        assertFalse(person.isFriendOf(friend));
        assertFalse(friend.hasFriend(person));
        assertFalse(friend.isFriendOf(person));
    }

    @Test
    public void shouldUpdatePersonInformation() throws Exception {
        final User person = userService.findById(1L);
        person.setGender(Gender.UNDEFINED);
        userService.update(person);

        final User result = userService.findById(person.getId());

        assertThat(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("fullName", "Denis Streltsov")
                .hasFieldOrPropertyWithValue("gender", Gender.UNDEFINED);
    }

    @Test
    public void shouldChangePassword() throws Exception {
        final User person = userService.findById(1L);
        final String currentPwd = "7913782o";
        final String newPwd = "12345";

        assertTrue(userService.hasValidPassword(person, currentPwd));
        assertFalse(userService.hasValidPassword(person, newPwd));

        userService.changePassword(person, newPwd);

        assertFalse(userService.hasValidPassword(person, currentPwd));
        assertTrue(userService.hasValidPassword(person, newPwd));
    }

    @Test
    public void shouldCreateNewPerson() throws Exception {
        UserRegistration user = new UserRegistration("Vito","Corleone","vito@corleone.com","12345");
        final User actual = userService.create(user);
        final User expected = userService.findByEmail("vito@corleone.com");

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", expected.getId())
                .hasFieldOrPropertyWithValue("fullName", expected.getFullName())
                .hasFieldOrPropertyWithValue("email", expected.getEmail());
    }
}

