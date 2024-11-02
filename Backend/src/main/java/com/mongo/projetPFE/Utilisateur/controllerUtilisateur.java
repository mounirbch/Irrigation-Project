package com.mongo.projetPFE.Utilisateur;

import com.mongo.projetPFE.DTO.AuthentificationDTO;
import com.mongo.projetPFE.Utilisateur.image.EntityImage;
import com.mongo.projetPFE.Utilisateur.image.ImageRepository;
import com.mongo.projetPFE.security.JwtFilter;
import com.mongo.projetPFE.security.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongo.projetPFE.Utilisateur.TypeDeRole.utilisateur;
import static com.mongo.projetPFE.Utilisateur.TypeDeRole.administrateur;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor

public class controllerUtilisateur {
    private UtilisateurService utilisateurService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UtilisateurRepository utilisateurRepository;
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private ImageRepository imageRepository;







    /*  @PostMapping("/tuto")
      public ResponseEntity<String> save(@RequestBody Utilisateur utilisateur) {
        log.info("inscription");
          return ResponseEntity.ok(utilisateurService.save(utilisateur).getId());

      @PostMapping("/autorised")
      public void inscription(){
          log.info("inscription");
      }
  }*/
    @PostMapping("/mounir")
    public ResponseEntity<String> inscription(@RequestBody Utilisateur utilisateur) {
        String email = utilisateur.getEmail();
        if (!email.contains("@") || !email.contains(".")) {
            return ResponseEntity.badRequest().body("Votre mail est invalide");
        }


        Optional<Utilisateur> optionalUtilisateur = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (optionalUtilisateur.isPresent()) {
            return ResponseEntity.badRequest().body("Votre email existe déjà");
        }
        utilisateur.setDateInscription(LocalDate.now());

        try {
            utilisateurService.inscription(utilisateur);
            return ResponseEntity.ok("Inscription réussie");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    @PostMapping("/activation")
    public ResponseEntity<String> activation(@RequestBody Map<String, String> activation) {
        try {
            utilisateurService.activation(activation);
            return ResponseEntity.ok("Activation réussie veuillez vous-connecter");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/modifier-mot-de-passe")
    public ResponseEntity<String> modifierMotDePasse(@RequestBody Map<String, String> activation) {

        try {
            this.utilisateurService.modifierMotDePasse(activation);
            return ResponseEntity.ok("utilisateur reconnue");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/nouveau-mot-de-passe")
    public ResponseEntity<String> nouveauMotDePasse(@RequestBody Map<String, String> activation) {


        try {
            this.utilisateurService.nouveauMotDePasse(activation);
            return ResponseEntity.ok("mot de passe changé avec succes");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
   /* @PostMapping("/connexion")
    public Map<String,String> connexion (@RequestBody AuthentificationDTO authentificationDTO){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.username(), authentificationDTO.password())
        );
        if(authenticate.isAuthenticated()){
          return this.jwtService.generate(authentificationDTO.username());
        }
        return null;

    }  */

    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
        try {
            /*  teb3a modification tw
            // Vérifiez d'abord si l'utilisateur existe
            Utilisateur utilisateur = (Utilisateur) userDetailsService.loadUserByUsername(authentificationDTO.username());

            // Récupérez le rôle de l'utilisateur à partir de l'objet Utilisateur
            String userRole = utilisateur.getRole().getLibelle().toString();
code lekdim*/
            // Tentez ensuite d'authentifier l'utilisateur
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authentificationDTO.username(), authentificationDTO.password())
            );

            if (authenticate.isAuthenticated()) {
                //2 ligne jdod
                UserDetails Utilisateur = (Utilisateur) authenticate.getPrincipal();
                String userRole = Utilisateur.getAuthorities().iterator().next().getAuthority();
                Utilisateur utilisateur1 = utilisateurRepository.findByEmail(authentificationDTO.username()).orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

                // Vérifier l'attribut spécifique de l'utilisateur (par exemple, userArchive)
                if (utilisateur1.isUserArchive()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur a été archivé");
                }


                if (userRole.equals("ROLE_administrateur")) {
                    userRole = "administrateur";
                }
                // Manipulation du rôle ici
                if (userRole.equals("ROLE_utilisateur")) {
                    userRole = "utilisateur";
                }
                //lena toufa 2 ligne jdod
                String jwtToken = this.jwtService.generate(authentificationDTO.username()).toString().replace("Bearer=", "");
                return ResponseEntity.ok().body(Map.of("token", jwtToken, "role", userRole));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Échec de l'authentification");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Adresse e-mail incorrecte");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Problème d'authentification");
        }
    }


    @PostMapping("/deconnexion")
    public void deconnexion() {

        this.jwtService.deconnexion();
    }

    @GetMapping(value = "/get/{id}")
    public Optional<Utilisateur> getById(@PathVariable String id) {
        return this.utilisateurService.getUtilisateurById(id);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<Utilisateur> getCurrentAuthenticatedUtilisateur() {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();
        if (utilisateur != null) {
            return ResponseEntity.ok().body(utilisateur);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }





    private boolean isValidPhoneNumber(String phoneNumber) {
        // Vérifiez si la longueur du numéro est de 8 caractères et s'il est composé de chiffres uniquement
        return  phoneNumber == null||phoneNumber == ""  || (phoneNumber.length() == 8 && phoneNumber.matches("[0-9]+"));
    }

        @PostMapping(value = "/modifier")
        public ResponseEntity<String> modifier(@RequestBody Map<String, String> requestBody) {
            Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();
            Optional<EntityImage> image = imageRepository.findByUtilisateur(utilisateur);

            String adresseActuelle = utilisateur.getAdresse();
            String telephoneActuel = utilisateur.getTelephone();

            String nouvelleAdresse = requestBody.get("adresse");
            String nouveauTelephone = requestBody.get("telephone");

            if (((nouveauTelephone == null) && (telephoneActuel == null) && (nouvelleAdresse == null) && (adresseActuelle == null)) ||
                    ((telephoneActuel != null) && (nouveauTelephone.equals(telephoneActuel)) && (adresseActuelle != null) && (nouvelleAdresse.equals(adresseActuelle)))) {
                return ResponseEntity.ok("rien n'est changé");}



            if (nouveauTelephone != null && !isValidPhoneNumber(nouveauTelephone)) {
                return ResponseEntity.ok("Numéro de téléphone invalide");
            }


            utilisateur.setAdresse(nouvelleAdresse);
            utilisateur.setTelephone(nouveauTelephone);

            utilisateurRepository.save(utilisateur);
            if (image.isPresent()){
                EntityImage image1 = image.get();
            image1.setUtilisateur(utilisateur);
                imageRepository.save(image1);
            }


            return ResponseEntity.ok("Coordonnées modifiées avec succès");
        }



    @PostMapping("/utilisateursGoogle")
    public ResponseEntity<?> saveUser(@RequestBody Utilisateur utilisateur) {

        Optional<Utilisateur> existingUserOptional = utilisateurRepository.findByEmail(utilisateur.getEmail());

        if (existingUserOptional.isPresent()) {
            Map<String, String> jwtMap = jwtService.generate(utilisateur.getEmail());
            String jwtToken = jwtMap.get("Bearer");

            String userRole = "utilisateur";

            // Retourner la même réponse que l'API de connexion
            return ResponseEntity.ok().body(Map.of("token", jwtToken, "role", userRole));
        }
        utilisateur.setDateInscription(LocalDate.now());
        Role roleUtilisateur = new Role();
        roleUtilisateur.setLibelle(TypeDeRole.utilisateur);
        utilisateur.setRole(roleUtilisateur);
        utilisateur.setGoogleUser(true);
        utilisateur.setActif(true);
        utilisateurRepository.save(utilisateur);

        Map<String, String> jwtMap = jwtService.generate(utilisateur.getEmail());
        String jwtToken = jwtMap.get("Bearer");

        String userRole = "utilisateur";

        // Retourner la même réponse que l'API de connexion
        return ResponseEntity.ok().body(Map.of("token", jwtToken, "role", userRole));

    }





}