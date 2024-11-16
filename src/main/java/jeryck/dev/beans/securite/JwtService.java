package jeryck.dev.beans.securite;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import jeryck.dev.beans.entite.Jwt;
import jeryck.dev.beans.entite.Utilisateur;
import jeryck.dev.beans.repository.JwtRepository;
import jeryck.dev.beans.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@AllArgsConstructor
@Service
@Slf4j
public class JwtService {

    public static final String BEARER = "bearer";
    private final String ENCRYPTION_KEY = "902a055292f10b2cd13f9504dc1b849fbfff778ac57db4cff08cfaba34c42290";
    private UtilisateurService utilisateurService;
    private JwtRepository jwtRepository;

    public Jwt tokenByValue(String value) {
    return this.jwtRepository.findByValueAndDesactivateAndExpire(
            value,
                    false,
                    false)
            .orElseThrow(() -> new RuntimeException("token invalide ou inconnu "));
    }
    public Map<String, String> generate(String username){

        Utilisateur utilisateur = (Utilisateur)this.utilisateurService.loadUserByUsername(username);

        final Map <String, String> jwtMap = this.generateJwt(utilisateur);

       final Jwt jwt = Jwt
                .builder()
                .value(jwtMap.get(BEARER))
                .desactivate(false)
                .expire(false)
                .utilisateur(utilisateur)
                .build();
       this.jwtRepository.save(jwt);
        return jwtMap;
    }
    private void disableTokens(Utilisateur utilisateur){
        final List<Jwt> jwtList = this.jwtRepository.findByUtilisateur(utilisateur.getEmail()).peek(
                jwt -> {
                    jwt.setDesactivate(true);
                    jwt.setExpire(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtList);
    }
    public String extractUsername(String token) {

        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }



    private <T> T getClaim(String token, Function<Claims, T> function) {
    Claims claims = getAllClaims(token);
    return function.apply(claims);
    }

    private Claims getAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        final long currentTime = System.currentTimeMillis();
        final long expiretTime = currentTime + 30 * 60 * 1000;
        final Map<String, Object> claims = Map.of(
                "nom",utilisateur.getNom(),
                Claims.EXPIRATION, new Date(expiretTime),
                Claims.SUBJECT, utilisateur.getEmail()
        );
         final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expiretTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("bearer", bearer);
    }
    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }


    public void deconnexion() {
        Utilisateur utlisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findByUtilisateurValidToken(
                utlisateur.getEmail(),
                false,
                false).orElseThrow(()-> new RuntimeException("Token invalide"));

        jwt.setDesactivate(true);
        jwt.setExpire(true);
        this.jwtRepository.save(jwt);
    }

   // @Scheduled( cron = "5 4 * * *")
    @Scheduled( cron = "0 */1 * * * *")
    public void removeUselessJwt(){
        log.info("Supression des tokens a {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndDesactivate(true,true);


    }
}
