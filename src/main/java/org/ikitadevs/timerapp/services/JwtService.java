package org.ikitadevs.timerapp.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.ikitadevs.timerapp.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration-time}")
    private long jwtAccessTokenExpiration;

    @Value("${security.jwt.refresh-token-expiration-time}")
    private long jwtRefreshTokenExpiration;

    public UUID extractUuid(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public List<String> extractRoles(String token) {
        return (List<String>) extractClaim(token, claims -> claims.get("roles"));
    }

    public String extractEmail(String token) {
        return (String) extractClaim(token, claims -> claims.get("email"));
    }

    public boolean extractActiveState(String token) {
        return Boolean.parseBoolean(extractClaim(token, claims -> claims.get("active")).toString());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(User user) {
        return generateAccessToken(new HashMap<>(), user);
    }

    public String generateRefreshToken(User user) {
        return generateRefreshToken(new HashMap<>(), user.getUuid());
    }


    public String generateAccessToken(Map<String, Object> extraClaims, User user) {
        List<String> rolesList = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        extraClaims.put("roles", rolesList);
        extraClaims.put("email", user.getEmail());
        extraClaims.put("uuid", user.getUuid());
        extraClaims.put("active", user.isActive());
        return buildToken(extraClaims, user.getUuid().toString(), jwtAccessTokenExpiration);
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UUID uuid) {
        extraClaims.put("UUID", uuid);
        return buildToken(extraClaims, uuid.toString(), jwtRefreshTokenExpiration);
    }
    public boolean isTokenValidForUser(String token, User user) {
        final String tokenSubId = extractUuid(token).toString();
        return (tokenSubId.equals(user.getUuid().toString())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

        private Date extractExpiration (String token){
            return extractClaim(token, Claims::getExpiration);
        }

        private String buildToken (
                Map <String, Object> extraClaims,
                String subject,
                long expiration
    ){
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        private Claims extractAllClaims (String token){
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
        throw e;
    }
    }

        private Key getSigningKey () {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }