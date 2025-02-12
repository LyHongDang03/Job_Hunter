package com.example.Job_Hunter.utill;

import com.example.Job_Hunter.domain.response.ResLoginDTO;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityUtil {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    public final JwtEncoder jwtEncoder;
    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    @Value("${jwt.access_token-validity-in-seconds}")
    private long accessTokenJwtKeyExpiration;
    @Value("${jwt.refresh_token-validity-in-seconds}")
    private long refreshTokenJwtKeyExpiration;
    @Value("${jwt.base64-secret}")
    private String jwtKey;

    //createAccessToken
    public String createAccessToken(String email, ResLoginDTO.UserLogin loginDTO) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenJwtKeyExpiration, ChronoUnit.SECONDS);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
//                .claim("Hi", loginDTO)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }
    //createRefreshToken
    public String createRefreshToken(String email, ResLoginDTO dto){
        Instant now = Instant.now();
        List<String> permission = new ArrayList<>();
        permission.add("read");
        permission.add("write");
        Instant validity = now.plus(this.refreshTokenJwtKeyExpiration, ChronoUnit.SECONDS);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
//                .claim("user", dto.getUserLogin())
//                .claim("permission", permission)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }
    //decodedToken
    public Jwt checkValidRefreshToke(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
            try {
                 return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> Refresh error: " + e.getMessage());
                throw e;
            }
    }

    // getSecretKey
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
    public static Optional<String> getCurrentUserLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }


    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public static Optional<String> getCurrentUserJWT(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }
}
