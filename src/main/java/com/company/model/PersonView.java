package com.company.model;

import com.company.persist.domain.User;
import com.company.security.SecurityUtils;
import lombok.Getter;
import lombok.ToString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

@Getter
@ToString
public class PersonView implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(PersonView.class);

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private Date birthDate;
    private String gender;
    private Date created;
    private boolean isMyFriend;
    private boolean isFriendOfMine;

    public PersonView(User person) {
        final User profile = SecurityUtils.currentProfile();

        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.fullName = person.getFullName();
        this.email = person.getEmail();
        this.phone = person.getPhone();
        this.birthDate = person.getBirthDate();
        this.gender = person.getGender().toString();
        this.created = person.getCreated();
        this.isMyFriend = person.isFriendOf(profile);
        this.isFriendOfMine = person.hasFriend(profile);
    }
}
