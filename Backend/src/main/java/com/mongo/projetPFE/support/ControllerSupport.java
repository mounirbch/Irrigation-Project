package com.mongo.projetPFE.support;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@RestController
public class ControllerSupport {

    supportRepository supportRepository;
    @PostMapping("/support")
    public ResponseEntity<?> support(@RequestBody support support) {




            supportRepository.save(support);
                return ResponseEntity.ok((Collections.singletonMap("message", "ajouter diplome avec succès")));

    }

    private boolean isValidEmail(String email) {
        return  email.contains("@") && email.contains(".");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Vérifiez si la longueur du numéro est de 8 caractères et s'il est composé de chiffres uniquement
        return phoneNumber.length() == 8 && phoneNumber.matches("[0-9]+");
    }
}
