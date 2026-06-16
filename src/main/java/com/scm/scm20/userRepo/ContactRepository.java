package com.scm.scm20.userRepo;

import com.scm.scm20.entity.Contact;
import com.scm.scm20.entity.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    Page<Contact> findByUser(User user, Pageable pageable);

    List<Contact> findByUser(User user);

    @Query("select c from Contact c where c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") int userId);

    // 🔥 AI SEARCH
    @Query("SELECT c FROM Contact c WHERE c.user = :user AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Contact> findByUserAndNameLike(@Param("user") User user, @Param("name") String name);

    @Query("SELECT c FROM Contact c WHERE c.user = :user AND LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Contact> findByUserAndEmailLike(@Param("user") User user, @Param("email") String email);

    List<Contact> findByUserAndFavoriteTrue(User user);

    Page<Contact> findByUserAndNameContaining(User user, String namekeyword, Pageable pageable);

    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumberKeyword, PageRequest pageable);

    Page<Contact> findByUserAndEmailContaining(User user, String emailKeyword, PageRequest pageable);
}
