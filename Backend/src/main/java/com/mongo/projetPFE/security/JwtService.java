package com.mongo.projetPFE.security;

import com.mongo.projetPFE.Utilisateur.JWT.Jwt;
import com.mongo.projetPFE.Utilisateur.JWT.JwtRepository;
import com.mongo.projetPFE.Utilisateur.Utilisateur;
import com.mongo.projetPFE.Utilisateur.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j

@Transactional
@AllArgsConstructor
@Service
public class    JwtService {
    public static final String BEARER = "Bearer";

    private UtilisateurService utilisateurService;
    private JwtRepository jwtRepository;
    private final String ENCRIPTION_KEY ="8f1b23a9a4be231cd8e7ca1321e2d8db212e0ca33a7fd001a3e5c1e593218aba";
    public Jwt tokenByValue(String valeur) {

       return  this.jwtRepository.findByValeurAndDesactiveAndExpire(valeur,false,false).orElseThrow(()->new RuntimeException("token invalid ou  Inconnu"));
    }
    public Map<String,String> generate(String username ){
        Utilisateur utilisateur= (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        this.disableTokens(utilisateur);
        final Map<String, String> jwtMap = this.generateJwt(utilisateur);
       final Jwt jwt= Jwt.builder().valeur(jwtMap.get(BEARER)).desactive(false).expire(false).utilisateur(utilisateur).build();
       this.jwtRepository.save(jwt);
        return jwtMap;
    }

    private void disableTokens(Utilisateur utilisateur) {
        final List<Jwt> jwtList = this.jwtRepository.findByUtilisateurEmail(utilisateur.getEmail())
                .peek(jwt -> {
                 // Vérifie si le token appartient à un autre utilisateur
                        jwt.setDesactive(true);
                        jwt.setExpire(true);

                    }

                 )
                .collect(Collectors.toList());

        this.jwtRepository.saveAll(jwtList);
    }

    public String extractUsername(String token) {
        return this.getClaim(token , Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate=getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims=getAllClaims(token);
        return  function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws( token)
                .getBody();
    }

    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        final   long currentTime = System.currentTimeMillis();
        final  long expirationTime =currentTime+120*60*1000;

        final Map<String,Object> claims =Map.of(
                "nom",utilisateur.getNom(),

                Claims.EXPIRATION,new Date(expirationTime),
                Claims.SUBJECT,utilisateur.getEmail()

        );


       final  String bearer  = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        return Map.of("Bearer",bearer);
    }

    private Key getKey() {
      final   byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public void deconnexion(){

        Utilisateur utilisateur=(Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Jwt jwt=  this.jwtRepository.findByUtilisateurEmailAndDesactiveAndExpire(utilisateur.getEmail(),false , false).orElseThrow(()->new RuntimeException("Token Invalide"));
      jwt.setExpire(true);
      jwt.setDesactive(true);
      this.jwtRepository.save(jwt);

    }
   // @Scheduled(cron = "0 * * * * *")
  //@Scheduled(cron="@daily")
    public void removeUselessJwt(){
        log.info ("suppression des tokens a{}", Instant.now());
     this.jwtRepository.deleteByExpireAndDesactive(true,true);
    }



}
