package com.company.persist.domain;

import com.company.persist.domain.Converter.GenderConverter;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name="users")
@NoArgsConstructor
@EqualsAndHashCode(of = {"email"})
@ToString(of = {"id", "fullName"})
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "first_name", length = 30)
    @Getter
    private String firstName;

    @Column(name = "last_name", length = 30)
    @Getter
    private String lastName;

    @Column(unique = true, nullable = false)
    @Getter @Setter
    private String email;

    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String password;

    @Column(insertable = false)
    @Getter
    @Formula(value = "concat(first_name, ' ', last_name)")
    private String fullName;

    @Column(length = 15)
    @Getter @Setter
    private String phone;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @Getter @Setter
    private Date birthDate;

    @Convert(converter = GenderConverter.class)
    @Getter @Setter
    private Gender gender = Gender.UNDEFINED;

    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private Date created = new Date();

    @ManyToMany(fetch = FetchType.LAZY)
    @Getter @Setter
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    @Getter @Setter
    private Set<User> friends = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "friend_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Getter @Setter
    private Set<User> friendOf = new HashSet<>();

    public boolean hasFriend(User friend) {
        return friends.contains(friend);
    }

    public void addFriend(User friend) {
        friends.add(friend);
        friend.friendOf.add(this);
    }

    public void removeFriend(User friend) {
        friends.remove(friend);
        friend.friendOf.remove(this);
    }

    public boolean isFriendOf(User person) {
        return friendOf.contains(person);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        setFullName();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        setFullName();
    }

    private void setFullName() {
        this.fullName = String.format("%s %s", this.firstName, this.lastName);
    }

    public User(Long id, String firstName, String lastName, String email, String password,
                String phone, Date birthDate, Gender gender, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.roles = roles;
        setFullName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @ToString(of = {"id", "firstName", "lastName"})
    public static class UserBuilder {

        private Long id;
        private String firstName = "";
        private String lastName = "";
        private String email = "";
        private String password = "";
        private String phone = "";
        private Date birthDate;
        private Gender gender = Gender.UNDEFINED;
        private Set<Role> roles = Collections.singleton(Role.USER);

        UserBuilder() {
        }

        public UserBuilder id(@NonNull Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder firstName(@NonNull String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(@NonNull String lastName) {
            this.lastName = lastName;
            return this;
        }


        public UserBuilder email(@NonNull String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(@NonNull String password) {
            this.password = password;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public UserBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public UserBuilder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(id, firstName, lastName, email, password, phone, birthDate, gender, roles);
        }
    }
}
