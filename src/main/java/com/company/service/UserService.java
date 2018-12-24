package com.company.service;

import com.company.model.PersonView;
import com.company.model.UserRegistration;
import com.company.persist.domain.User;
import com.company.persist.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Transactional(readOnly = true)
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            user.getFriends().isEmpty();
            user.getFriendOf().isEmpty();
            user.getRoles().isEmpty();
            return user;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Page<User> getPeople(Pageable pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<PersonView> getModelPeople(Pageable pageRequest) {
        return userRepository.findAll(pageRequest).map(PersonView::new);
    }

    @Transactional(readOnly = true)
    public Page<User> getFriends(User person, String searchTerm, Pageable pageRequest) {
        return userRepository.findFriends(person, searchTerm, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<User> getFriendOf(User person, String searchTerm, Pageable pageRequest) {
        return userRepository.findFriendOf(person, searchTerm, pageRequest);
    }

    @Transactional
    public void addFriend(User person, User friend) {
        if (!person.hasFriend(friend)) {
            person.addFriend(friend);
        }
    }

    @Transactional
    public void removeFriend(User person, User friend) {
        if (person.hasFriend(friend)) {
            person.removeFriend(friend);
        }
    }

    @Transactional
    public void update(User person) {
        userRepository.save(person);
    }

    @Transactional
    public User create(UserRegistration registration){

        final User person = User.builder()
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .email(registration.getEmail())
                .password(passwordEncoder.encode(registration.getPassword()))
                .build();

        return userRepository.save(person);
    }

    public boolean hasValidPassword(User person, String pwd) {
        return passwordEncoder.matches(pwd, person.getPassword());
    }

    public void changePassword(User person, String pwd) {
        person.setPassword(passwordEncoder.encode(pwd));
        userRepository.save(person);
    }

}
