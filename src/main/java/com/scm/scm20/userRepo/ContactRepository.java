package com.scm.scm20.userRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.scm20.entity.Contact;
import com.scm.scm20.entity.User;

import java.util.List;


public interface ContactRepository extends JpaRepository<Contact,Integer>{

    Page<Contact> findByUser(User user,PageRequest pageRequest);

    @Query("select c from Contact c where c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") int userId);

    Page<Contact> findByUserAndNameContaining(User user,String namekeyword, Pageable pageable);
    Page<Contact> findByUserAndPhoneNumberContaining(User user,String phonekeyword, Pageable pageable);
    Page<Contact> findByUserAndEmailContaining(User user,String emailkeyword, Pageable pageable);
    
}
