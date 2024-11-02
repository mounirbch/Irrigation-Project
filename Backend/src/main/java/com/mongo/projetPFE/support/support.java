package com.mongo.projetPFE.support;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "support")
public class support {
    @Id
    private String id;

    private String nom;
    @NotBlank(message = "Le champ mail ne peut pas être vide")
    @Email(message = "Veuillez entrer une adresse email valide")
    private String email;
    @Pattern(regexp="\\d{8}", message="Phone number must be 8 digits")
    private String telephone;
    private String message;



}
