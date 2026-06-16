package com.scm.scm20.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @Column(name="user_name",nullable = false)
    @NotBlank(message = "username is required")
    @Size(min=3,message = "min 3 char required")
    private String name;

    @Column(unique = true,nullable = false)
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "about section is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String about;

    private String profilePic;

    @Size(min=8,max=12,message = "Invalid phone number")
    private String phoneNumber;

    //informatiion
    private boolean enabled=false;
    private boolean emailVerified=false;
    private boolean phoneVerified=false;


    //SELF GOOGLE, FACEBOOK,LINKDIN,GITHUB

    @Enumerated(value = EnumType.STRING)
    private Providers provider=Providers.SELF;
    private String providerUserId;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Contact>contacts=new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList=new ArrayList<>();

    private String emailToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       //list of roles[user,admin]
       //collection of simpleGrantedAuthority[ADMIN,USER]
       Collection<SimpleGrantedAuthority> roles=roleList.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList());
       return roles;
    }

    @Override
    public String getUsername() {
       
        return this.email;
    }

   
    
}
