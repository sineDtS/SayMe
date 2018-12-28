package com.company.persist.repo;

import com.company.persist.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY u.fullName")
    Page<User> findPeople(
            @Param("searchTerm") String searchTerm,
            Pageable pageRequest);

    @Query("SELECT u FROM User u " +
            "WHERE (:user) MEMBER OF u.friendOf " +
            "   AND LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY u.fullName")
    Page<User> findFriends(
            @Param("user") User person,
            @Param("searchTerm") String searchTerm,
            Pageable pageRequest);

    @Query("SELECT u FROM User u " +
            "WHERE (:user) MEMBER OF u.friends " +
            "   AND LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY u.fullName")
    Page<User> findFriendOf(
            @Param("user") User person,
            @Param("searchTerm") String searchTerm,
            Pageable pageRequest);

}
