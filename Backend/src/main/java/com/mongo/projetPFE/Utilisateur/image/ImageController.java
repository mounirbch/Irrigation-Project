package com.mongo.projetPFE.Utilisateur.image;

import com.mongo.projetPFE.Utilisateur.Utilisateur;
import com.mongo.projetPFE.Utilisateur.UtilisateurRepository;
import com.mongo.projetPFE.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RestController

@RequestMapping(path = "/image")
public class ImageController {
    @Autowired
    ImageRepository imgRepo;
    @Autowired
    UtilisateurRepository utilisateurRepository;
    private final JwtFilter jwtFilter;

    public ImageController(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upoadImage(@RequestParam("imageFile")MultipartFile file) throws IOException{
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();

        imgRepo.deleteByUtilisateur(utilisateur);
        System.out.println("Original Image Byte Size - " + file.getBytes().length);
        EntityImage img = new EntityImage(file.getOriginalFilename(), file.getContentType(),
                compressBytes(file.getBytes()),utilisateur);
        imgRepo.save(img);
        return ResponseEntity.ok("Image changée avec succès");

    }


    @GetMapping(path = { "/get/{imageName}" })
    public EntityImage getImage(@PathVariable("imageName") String imageName) throws IOException {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();

        final Optional<EntityImage> retrievedImage = imgRepo.findByName(imageName);
        EntityImage img = new EntityImage(retrievedImage.get().getName(), retrievedImage.get().getType(),
                decompressBytes(retrievedImage.get().getPicByte()),utilisateur);
        return img;
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }


    @GetMapping("/get")
    public EntityImage getImageForCurrentUser() throws IOException {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();

            final Optional<EntityImage> retrievedImage = imgRepo.findByUtilisateur(utilisateur);

                return new EntityImage(retrievedImage.get().getName(), retrievedImage.get().getType(),
                        decompressBytes(retrievedImage.get().getPicByte()), utilisateur);
            }
    @GetMapping("/get-by-email/{email}")
    public EntityImage getImageByEmail(@PathVariable("email") String email) throws IOException {
        // Recherche de l'utilisateur par son email
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByEmail(email);
        Utilisateur utilisateur = utilisateurOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur avec l'email " + email + " non trouvé."));

        // Recherche de l'image de l'utilisateur
        final Optional<EntityImage> retrievedImage = imgRepo.findByUtilisateur(utilisateur);

        if (retrievedImage.isPresent()) {
            // Si une image est trouvée, la retourner
            return new EntityImage(retrievedImage.get().getName(), retrievedImage.get().getType(),
                    decompressBytes(retrievedImage.get().getPicByte()), utilisateur);
        } else {
            return new EntityImage("Non image présente", "", new byte[0], utilisateur);
        }
    }








}


