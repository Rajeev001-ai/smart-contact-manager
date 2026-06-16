package com.scm.scm20.userService;
import com.scm.scm20.userRepo.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.scm20.Helpers.ResourceNotFoundException;
import com.scm.scm20.entity.Contact;
import com.scm.scm20.entity.User;
import com.scm.scm20.userRepo.ContactRepository;

@Service
public class ContactService {

    private final UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    ContactService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //save contact
    public void saveContact(Contact contact){
    
      contactRepository.save(contact);
        
    }

    public List<Contact> getAllContact(){
      return contactRepository.findAll();
    }
    
    //getContact by id
    public Optional<Contact> getById(int id){
      return contactRepository.findById(id);
    }

    //update Contact
    public void updateContact(Contact contact,int cid){
      
      var contactOld=contactRepository.findById(cid).orElseThrow(()-> new ResourceNotFoundException("contact not found"));
      
      contactOld.setName(contact.getName());
      contactOld.setAddress(contact.getAddress());
      contactOld.setEmail(contact.getEmail());
      contactOld.setPhoneNumber(contact.getPhoneNumber());
      contactOld.setDescription(contact.getDescription());
      contactOld.setFavorite(contact.isFavorite());
      contactOld.setLinkedinLink(contact.getLinkedinLink());
      contactOld.setPicture(contact.getPicture());
      contactOld.setWebsiteLink(contact.getWebsiteLink());
      contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

      contactRepository.save(contact);
    }
    
    public void deleteContact(int id){

      contactRepository.deleteById(id);
    }

    //user get by userId
    
    public List<Contact> getByUserId(int userId){

      return contactRepository.findByUserId(userId);
    }

    //pagination

    public Page<Contact> getByUser(User user,int page,int size,String sortBy,String direction){
      
       Sort sort=direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

       var pageable=PageRequest.of(page, size,sort);

       return contactRepository.findByUser(user,pageable);
    }

    //serach page 

    public Page<Contact>searchByName(String nameKeyword,int size,int page,String sortBy,String order,User user){

        Sort sort=order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable=PageRequest.of(page, size,sort);

        return contactRepository.findByUserAndNameContaining(user,nameKeyword, pageable);
    }
   public Page<Contact>searchByPhoneNumber(String phoneNumberKeyword,int size,int page,String sortBy,String order,User user){

       Sort sort=order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable=PageRequest.of(page, size,sort);

        return contactRepository.findByUserAndPhoneNumberContaining(user,phoneNumberKeyword, pageable);
    }

   public Page<Contact>searchByEmail(String emailKeyword,int size,int page,String sortBy,String order,User user){

      Sort sort=order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable=PageRequest.of(page,size,sort);

        return contactRepository.findByUserAndEmailContaining(user,emailKeyword,pageable);
    }



}
