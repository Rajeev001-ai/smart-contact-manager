package com.scm.scm20.userRepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.scm20.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email,String password);

    Optional<User>findByEmailToken(String id);
}
