package com.scm.scm20.Forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactForm {

    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private MultipartFile contactImage;
    private String description;
    private boolean favorite=false;
    private String websiteLink;
    private String linkedinLink;
    public String picture;

}
