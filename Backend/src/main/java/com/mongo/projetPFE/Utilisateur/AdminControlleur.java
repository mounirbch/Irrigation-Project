package com.mongo.projetPFE.Utilisateur;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongo.projetPFE.Utilisateur.TypeDeRole.utilisateur;

@RestController
@AllArgsConstructor
public class AdminControlleur {
    private UtilisateurRepository utilisateurRepository;



    @GetMapping("/utilisateurs")
    public List<Utilisateur> getAllUtilisateurs() {
        // Récupérer tous les utilisateurs de la base de données
        Iterable<Utilisateur> utilisateursIterable = utilisateurRepository.findAll();
        List<Utilisateur> utilisateursList = new ArrayList<>();
        utilisateursIterable.forEach(utilisateursList::add);

        // Filtrer les utilisateurs pour exclure l'administrateur avec le nom "admin" et l'email "admin@gmail.com"
        // Et ne retourner que les utilisateurs avec userArchive=false
        List<Utilisateur> utilisateursExclus = utilisateursList.stream()
                .filter(u -> !u.getNom().equals("admin") && !u.getEmail().equals("admin@gmail.com") && !u.isUserArchive())
                .collect(Collectors.toList());

        utilisateursExclus.forEach(u -> {
            if (u.isActif()) {
                u.setConnecte("Compte activé");
            } else {
                u.setConnecte("Compte non activé");
            }
        });

        return utilisateursExclus;
    }

    @DeleteMapping("/deleteUtilisateur/{email}")
    public ResponseEntity<String> supprimerUtilisateurParEmail(@PathVariable("email") String email) {
       Optional< Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);
        if (utilisateur .isPresent()) {
            utilisateurRepository.delete(utilisateur.get());
            return ResponseEntity.ok().body("Utilisateur supprimé avec succès");        }else{
            return ResponseEntity.badRequest().body(" Utilisateur non trouvé");
        }
    }
    @GetMapping( "/getUser/{email}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable("email") String email) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByEmail(email);

        // Check if user exists
        if (utilisateurOptional.isPresent()) {
            Utilisateur utilisateur = utilisateurOptional.get();
            return ResponseEntity.ok().body(utilisateur);
        } else {
            // Return NOT_FOUND for user not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping ("/archiveUtilisateur/{email}")
    public ResponseEntity<String> archiveUtilisateurParEmail(@PathVariable("email") String email) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);

        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.setUserArchiver(true);

            utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok().body("Utilisateur archivé avec succès");
        } else {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }
    }
    @DeleteMapping ("/desarchiveUtilisateur/{email}")
    public ResponseEntity<String> desarchiveUtilisateurParEmail(@PathVariable("email") String email) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);

        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.setUserArchiver(false);

            utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok().body("Utilisateur archivé avec succès");
        } else {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }
    }
    @GetMapping("/utilisateursArchive")
    public List<Utilisateur> getAllUtilisateursArchive() {
        Iterable<Utilisateur> utilisateursIterable = utilisateurRepository.findAll();
        List<Utilisateur> utilisateursList = new ArrayList<>();
        utilisateursIterable.forEach(utilisateursList::add);


        List<Utilisateur> utilisateursExclus = utilisateursList.stream()
                .filter(u -> !u.getNom().equals("admin") && !u.getEmail().equals("admin@gmail.com") && u.isUserArchive())
                .collect(Collectors.toList());

        utilisateursExclus.forEach(u -> {
            if (u.isActif()) {
                u.setConnecte("Compte activé");
            } else {
                u.setConnecte("Compte non activé");
            }
        });

        return utilisateursExclus;
    }

    @GetMapping("/pourcentageUtilisateursArchives")
    public ResponseEntity<Double> pourcentageUtilisateursArchives() {
        long totalUtilisateurs = utilisateurRepository.count();
        totalUtilisateurs=totalUtilisateurs-1;
        long utilisateursArchives = utilisateurRepository.countByUserArchive(true);

        double pourcentage = (double) utilisateursArchives / totalUtilisateurs * 100;

        return ResponseEntity.ok().body(pourcentage);
    }

    @GetMapping("/pourcentageUtilisateursActifs")
    public ResponseEntity<Double> pourcentageUtilisateursActifs() {
        long totalUtilisateurs = utilisateurRepository.count();

        long utilisateursActifs = utilisateurRepository.countByActif(true);

        double pourcentage = (double) utilisateursActifs / totalUtilisateurs * 100;

        return ResponseEntity.ok().body(pourcentage);
    }
    @GetMapping("/pourcentageUtilisateursGoogle")
    public ResponseEntity<Double> pourcentageUtilisateursGoogle() {
        long totalUtilisateurs = utilisateurRepository.count();

        long utilisateursArchives = utilisateurRepository.countByGoogleUser(true);

        double pourcentage = (double) utilisateursArchives / totalUtilisateurs * 100;

        return ResponseEntity.ok().body(pourcentage);
    }

    @GetMapping("/Tousutilisateurs")
    public List<String> getUtilisateursByMonthAndYear(@RequestParam("date") String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        YearMonth date = YearMonth.parse(dateString, formatter);

        LocalDate today = LocalDate.now();
        int currentDayOfMonth = today.getDayOfMonth();

        Iterable<Utilisateur> utilisateursIterable = utilisateurRepository.findAll();
        List<Utilisateur> utilisateursList = new ArrayList<>();
        utilisateursIterable.forEach(utilisateursList::add);

        List<Utilisateur> utilisateursExclus = utilisateursList.stream()
                .filter(u -> !"admin".equals(u.getNom()) && !"admin@gmail.com".equals(u.getEmail()))
                .collect(Collectors.toList());

        List<Utilisateur> utilisateursFiltres = utilisateursExclus.stream()
                .filter(u -> {
                    YearMonth dateInscription = YearMonth.from(u.getDateInscription());
                    return dateInscription.getMonth() == date.getMonth() && dateInscription.getYear() == date.getYear();
                })
                .collect(Collectors.toList());

        Map<Integer, Integer> utilisateurParJour = new HashMap<>();
        for (Utilisateur utilisateur : utilisateursFiltres) {
            int jour = utilisateur.getDateInscription().getDayOfMonth();
            if (jour <= currentDayOfMonth) {
                utilisateurParJour.put(jour, utilisateurParJour.getOrDefault(jour, 0) + 1);
            }
        }

        for (int day = 1; day <= currentDayOfMonth; day++) {
            utilisateurParJour.putIfAbsent(day, 0);
        }

        int cumulativeSum = 0;
        for (Map.Entry<Integer, Integer> entry : utilisateurParJour.entrySet()) {
            int jour = entry.getKey();
            cumulativeSum += entry.getValue();
            utilisateurParJour.put(jour, cumulativeSum);
        }

        List<String> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : utilisateurParJour.entrySet()) {
            result.add(entry.getKey() + ";" + entry.getValue());
        }

        return result;
    }








}
