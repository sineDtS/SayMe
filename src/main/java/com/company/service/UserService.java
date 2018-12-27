package com.company.service;

import com.company.model.PersonView;
import com.company.model.UserRegistration;
import com.company.persist.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User findById(Long id);
    User findByEmail(String email);
    Page<User> getPeople(Pageable pageRequest);
    Page<PersonView> getModelPeople(Pageable pageRequest);
    void addFriend(User person, User friend);
    void removeFriend(User person, User friend);
    void update(User person);
    void create(UserRegistration registration);
    boolean hasValidPassword(User person, String pwd);
    void changePassword(User person, String pwd);
}
