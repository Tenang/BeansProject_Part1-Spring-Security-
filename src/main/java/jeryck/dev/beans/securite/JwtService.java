package jeryck.dev.beans.securite;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jeryck.dev.beans.entite.Utilisateur;
import jeryck.dev.beans.service.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class JwtService {

    private final String ENCRYPTION_KEY = "902a055292f10b2cd13f9504dc1b849fbfff778ac57db4cff08cfaba34c42290";
    private UtilisateurService utilisateurService;
    public Map<String, String> generate(String username){

        Utilisateur utilisateur = (Utilisateur)this.utilisateurService.loadUserByUsername(username);
        return  this.generateJwt(utilisateur);
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


}
