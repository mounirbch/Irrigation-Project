package com.mongo.projetPFE.Utilisateur;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="utilisateur")
public class Utilisateur implements UserDetails {

    @Id
    private String id;
    private String nom;
    private String email;
    private String password;
    private boolean actif=false;
    private String adresse;
    private String telephone;
    private String profileImageUrl;
    private  String connecte;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean userArchive=false;
    private LocalDate dateInscription;
    private boolean googleUser=false;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+this.role.getLibelle()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isEnabled() {
        return this.actif;
    }


    public String getTelephone() {
        return this.telephone;
    }


    public void setUserArchiver(boolean userArchive) {
        this.userArchive = userArchive;
    }
    public boolean isUserArchive() {
        return this.userArchive;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public boolean isGoogleUser() {
        return googleUser;
    }

    public void setGoogleUser(boolean googleUser) {
        this.googleUser = googleUser;
    }
}
