package com.company.security;

import com.company.persist.domain.Role;
import com.company.persist.domain.User;
import com.company.persist.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@Transactional(readOnly=true)
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        log.debug("Authenticating request: {}", email);

        final User domainUser = userRepository.findByEmail(email);
        if (domainUser == null) {
            throw new UsernameNotFoundException("Profile is not found:" + email);
        }
        log.debug("Profile was found by e-mail: {}", email);

        domainUser.getFriends().isEmpty();
        domainUser.getFriendOf().isEmpty();
        domainUser.getRoles().isEmpty();

        return domainUser;
    }
}
