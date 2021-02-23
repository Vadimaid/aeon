package com.vadimaid.aeon.component;

import com.vadimaid.aeon.exception.ApiException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Component
public class TokenHelper {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration-time.seconds}")
    private Long expirationTime;

    private final static SignatureAlgorithm SIGN_ALGORITHM = SignatureAlgorithm.HS256;

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime * 1000);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SIGN_ALGORITHM, secret)
                .compact();
    }

    public String extractUsername(HttpServletRequest request) throws ApiException {
        try {
            String token = extractToken(request);
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                throw new ApiException(HttpStatus.FORBIDDEN, "token_expired", "Token is expired!");
            }
            return claims.getBody().getSubject();

        } catch (final SignatureException e) {
            throw new ApiException(HttpStatus.FORBIDDEN, "invalid_token", "Token is invalid!");
        } catch (final ExpiredJwtException e) {
            throw new ApiException(HttpStatus.FORBIDDEN, "token_expired", "Token is expired!");
        }
    }

    private String extractToken(HttpServletRequest request) throws ApiException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer")) {
            throw new ApiException(HttpStatus.FORBIDDEN, "invalid_token", "Token is invalid!");
        }

        return authHeader.substring(7);
    }
}
