package com.scm.scm20.controller;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm20.Forms.ContactForm;
import com.scm.scm20.Helpers.AppConstants;
import com.scm.scm20.Helpers.Helper;
import com.scm.scm20.Helpers.Message;
import com.scm.scm20.Helpers.messageType;
import com.scm.scm20.entity.Contact;
import com.scm.scm20.entity.User;
import com.scm.scm20.userService.ContactService;
import com.scm.scm20.userService.ImageServiceImple;
import com.scm.scm20.userService.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class ContactController {


    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageServiceImple imageServiceImple;

    @Autowired
    private UserService userService;

   //Add contact section
    @GetMapping("/addContact")
    public String addContact(Authentication authentication,Model model){
        
        model.addAttribute("contactForm", new ContactForm());
        
        return "/user/addContact";
    }

    @PostMapping("/do-addContact")
    public String doAddContact(@ModelAttribute ContactForm contactForm,Authentication authentication,HttpSession httpSession) {
      
    
        String username=Helper.getEmailOfLoggedinUser(authentication);
        User user=userService.getUserByEmail(username);

        Contact contact=new Contact();

        contact.setEmail(contactForm.getEmail());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setName(contactForm.getName());
        contact.setFavorite(true);
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setAddress(contactForm.getAddress());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setUser(user);

        //image process

         String filename=UUID.randomUUID().toString();
         String fileURL= imageServiceImple.uploadImage(contactForm.getContactImage(),filename);

         contact.setPicture(fileURL);
         contact.setCloudinaryImagePublicId(filename);

         contactService.saveContact(contact);

         //message
         Message message=Message.builder().content("Contact Added Successfully !! ").type(messageType.success).build();
         httpSession.setAttribute("message",message);
        
        return "redirect:/user/addContact";
    }
    
    @GetMapping("/myContact")
    public String myContact(Authentication authentication,Model model,
      @RequestParam(value="page",defaultValue = "0")int page,
      @RequestParam(value = "size",defaultValue = "4")int size,
      @RequestParam(value = "sortBy",defaultValue = "name")String sortBy,
      @RequestParam(value = "direction",defaultValue = "asc")String direction
    
    ) {

        String username=Helper.getEmailOfLoggedinUser(authentication);

        User user=userService.getUserByEmail(username);
        
        Page<Contact>pageContact= contactService.getByUser(user,page,size,sortBy,direction);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/myContact";
    }

    @GetMapping("/contacts/search")
    public String searchHandler(@RequestParam ("field")String field,
    @RequestParam("keyword") String value,
    @RequestParam(value="page",defaultValue = "0")int page,
    @RequestParam(value = "size",defaultValue =AppConstants.PAGE_SIZE+" ")int size,
    @RequestParam(value = "sortBy",defaultValue = "name")String sortBy,
    @RequestParam(value = "direction",defaultValue = "asc")String direction,
    Model model,Authentication authentication) 
    
    {
       
        var user=userService.getUserByEmail(Helper.getEmailOfLoggedinUser(authentication));

        Page<Contact>pageContact=null;
        
        if(field.equalsIgnoreCase("name")){
            pageContact=contactService.searchByName(value,size,page,sortBy,direction,user);
        }
        else if(field.equalsIgnoreCase("email")){
            pageContact=contactService.searchByEmail(value, size, page, sortBy, direction,user);
        }
        else if(field.equalsIgnoreCase("phone")){
            pageContact=contactService.searchByPhoneNumber(value, size, page, sortBy, direction,user);
        }

        model.addAttribute("pageContact", pageContact);

        return "user/searchPage";
    }
    

    //view contact
    @GetMapping("/viewContact/{id}")
    public String viewContact(@PathVariable("id")Integer id,Model model){

        Optional<Contact>contactOptional =contactService.getById(id);
  
        Contact contact=contactOptional.get();

        model.addAttribute("contact", contact);
        
        return "user/viewContact";
    }

    //delete contact
    @GetMapping("/deleteContact/{id}")
    public String deleteContact(@PathVariable("id") Integer id,HttpSession httpSession) {
        
        contactService.deleteContact(id);
         //message
         Message message=Message.builder().content("Contact Deleted Successfully !! ").type(messageType.success).build();
         httpSession.setAttribute("message",message);
        
        return "redirect:/user/myContact";
    }

    //update contact details
    @GetMapping("/updateContact/{contactId}")
    public String getMethodName(@PathVariable("contactId")Integer contactId,Model model){
        
        Optional<Contact>contactoOptional=contactService.getById(contactId);

        Contact contact=contactoOptional.get();

        ContactForm contactForm=new ContactForm();

        contactForm.setName(contact.getName());
        contactForm.setAddress(contact.getAddress());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedinLink(contact.getLinkedinLink());
        contactForm.setDescription(contact.getDescription());
        contactForm.setPicture(contact.getPicture());
        contactForm.setFavorite(contact.isFavorite());
        
    
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contact.getId());

        return "user/update_contact_view";
    }

    //update contact
  
    @PostMapping("/do-updateContact/{contactId}")
    public String doUpdateContact(@ModelAttribute ContactForm contactForm,@PathVariable("contactId")Integer contactId
    ,HttpSession httpSession) {
        
        Optional<Contact>contactoOptional=contactService.getById(contactId);

        Contact contact=contactoOptional.get();
        contact.setName(contactForm.getName());
        contact.setAddress(contactForm.getAddress());
        contact.setEmail(contactForm.getEmail());
        contact.setDescription(contactForm.getDescription());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setFavorite(contactForm.isFavorite());

        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
       
        String fileName=UUID.randomUUID().toString();
        String imageUrl=imageServiceImple.uploadImage(contactForm.getContactImage(), fileName);
        contact.setPicture(imageUrl);
        contactForm.setPicture(imageUrl);
        }


        contactService.updateContact(contact,contactId);

         //message
         Message message=Message.builder().content("Contact Updated Successfully !! ").type(messageType.success).build();
         httpSession.setAttribute("message",message);

        return "redirect:/user/updateContact/"+contactId;
    }
    
    
    
    

    
}
