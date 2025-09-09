package com.scm.scm20.userService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.scm20.Helpers.AppConstants;
import com.scm.scm20.Helpers.Helper;
import com.scm.scm20.Helpers.ResourceNotFoundException;
import com.scm.scm20.entity.User;
import com.scm.scm20.userRepo.UserRepository;

@Service
public class UserService { //implement Userservice

  @Autowired
  UserRepository userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService;

  public List<User> getAllUser(){
    
    return userRepo.findAll();

  }

  public User saveUser(User user){

    //user id: automatically generate
     //String userId=UUID.randomUUID().toString();
     // user.setUserId("1");

     //encrypt the password
     user.setPassword(passwordEncoder.encode(user.getPassword()));

     //set the user role
     user.setRoleList(List.of(AppConstants.ROLE_USER));



     String emailToken= UUID.randomUUID().toString();
     user.setEmailToken(emailToken);
     User saveUser=userRepo.save(user);
     String eamilLink=Helper.getLinkForEmailVerification(emailToken);
     emailService.sendEmail(saveUser.getEmail(), "verify Account: Smart Contact Manager", eamilLink);

     return saveUser; 
  }

  public Optional<User> getUserById(int id){

    return userRepo.findById(id);
  }

  public Optional<User> updateUser(User user){

    User user2= userRepo.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("User not found"));

    user2.setName(user.getName());
    user2.setEmail(user.getEmail());
    user2.setPassword(user.getPassword()); 
    user2.setAbout(user.getAbout());
    user2.setPhoneNumber(user.getPhoneNumber());
    user2.setProfilePic(user.getProfilePic());
    user2.setEnabled(user.isEnabled());
    user2.setEmailVerified(user.isEmailVerified());
    user2.setPhoneVerified(user.isPhoneVerified());
    user2.setProvider(user.getProvider());
    user2.setProviderUserId(user.getProviderUserId());

    User save=userRepo.save(user2);

    return Optional.ofNullable(save);
  }

  public void deleteUser(int id){
    User user2= userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
    userRepo.delete(user2);
  }

  public boolean isUserExist(int userId){
    
    User user2=userRepo.findById(userId).orElse(null);
    return user2 != null ? true : false;
  }

  public boolean isUserExistByEmail(String email){

    User user=userRepo.findByEmail(email).orElse(null);

    return user != null ? true :false;
  }

  public User getUserByEmail(String email){

    return userRepo.findByEmail(email).orElse(null);
  }



}
