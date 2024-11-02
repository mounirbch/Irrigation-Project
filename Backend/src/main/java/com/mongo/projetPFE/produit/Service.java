package com.mongo.projetPFE.produit;

import com.mongo.projetPFE.Utilisateur.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {
    private  final ProductRepository repository ;





    public String save(Product product){
      Utilisateur utilisateur= (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        product.setUtilisateurNom(utilisateur.getEmail());

        return repository.save(product).getId();
    }
    public Product finById(String id){
        return repository.findById(id).orElse(null);

    }
    public List<Product> findAll(){
        return repository.findAll();
    }
    public void delete (String id){
        repository.deleteById(id);

    }




}

