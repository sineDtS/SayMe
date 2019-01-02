package com.company.service;

import com.company.AbstractApplicationTest;
import com.company.config.DataConfig;
import com.company.config.SecurityConfig;
import com.company.config.WebAppConfig;
import com.company.model.PersonView;
import com.company.model.UserRegistration;
import com.company.persist.domain.Gender;
import com.company.persist.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

    @Autowired
    private UserService userService;

    @Test
    public void shouldFindPersonWithCorrectIdAndFields() {
        final User person = userService.findById(1L);

        assertThat(person.getId()).isEqualTo(1L);
        assertThat(person.getFirstName()).isEqualTo("Den");
        assertThat(person.getLastName()).isEqualTo("Streltsov");
        assertThat(person.getFullName()).isEqualTo("Den Streltsov");
        assertThat(person.getEmail()).isEqualTo("den.strelts@gmail.com");
        assertThat(person.getPhone()).isEqualTo("+375295839006");
        assertThat(person.getBirthDate()).isEqualTo(new GregorianCalendar(1996, 5, 7).getTime());
        assertThat(person.getGender()).isEqualTo(Gender.MALE);
        //...
    }

    @Test
    public void shouldFindPersonWithCorrectEmail() {
        final User person = userService.findByEmail("den.strelts@gmail.com");

        assertThat(person.getId()).isEqualTo(1L);
        assertThat(person.getEmail()).isEqualTo("den.strelts@gmail.com");
    }

    @Test
    public void shouldFindAllPeople() {
        final Page<PersonView> people = userService.getModelPeople("", getDefaultPageRequest());

        assertThat(people).hasSize(8);
        assertThat(people)
                .extracting("id", "fullName")
                .contains(
                        tuple(1L, "Den Streltsov"),
                        tuple(2L, "Natalia Shmatova"));
    }

    @Test
    public void shouldFindAllFriends() {
        final User person = userService.findById(1L);
        final Page<PersonView> friends = userService.getFriends(person, "", getDefaultPageRequest());

        assertThat(friends).hasSize(2);
        assertThat(friends)
                .extracting("id", "fullName")
                .contains(
                        tuple(4L, "Michael Corleone"),
                        tuple(5L, "Ilya Streltsov"));
    }

    @Test
    public void shouldFindAllFriendOf() {
        final User person = userService.findById(1L);
        final Page<PersonView> friendOf = userService.getFriendOf(person, "", getDefaultPageRequest());

        assertThat(friendOf).hasSize(2);
        assertThat(friendOf)
                .extracting("id", "fullName")
                .contains(
                        tuple(2L, "Natalia Shmatova"),
                        tuple(4L, "Michael Corleone"));
    }

    @Test
    public void shouldFindAPerson() {
        final User person = userService.findById(1L);

        assertThat(person)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("fullName", "Den Streltsov");
    }

    @Test
    public void shouldAddAndRemoveAFriend() {
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
    public void shouldUpdatePersonInformation() {
        final User person = userService.findById(1L);
        person.setGender(Gender.UNDEFINED);
        userService.update(person);

        final User result = userService.findById(person.getId());

        assertThat(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("fullName", "Den Streltsov")
                .hasFieldOrPropertyWithValue("gender", Gender.UNDEFINED);
    }

    @Test
    public void shouldChangePassword() {
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
    public void shouldCreateNewPerson() {
        UserRegistration user = new UserRegistration("Vito","Corleone","12345","vito@corleone.com");
        final User actual = userService.create(user);
        final User expected = userService.findByEmail("vito@corleone.com");

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", expected.getId())
                .hasFieldOrPropertyWithValue("fullName", expected.getFullName())
                .hasFieldOrPropertyWithValue("email", expected.getEmail());
    }
}

