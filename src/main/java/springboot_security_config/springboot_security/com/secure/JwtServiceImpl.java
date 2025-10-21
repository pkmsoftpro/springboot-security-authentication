package springboot_security_config.springboot_security.com.secure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
//@RefreshScope
public class JwtServiceImpl implements JwtService {

  private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);


  // Renamed from extractUsername to extractUserId
  public String extractUserId(String token) {
    return extractClaim(token, claims -> claims.get("userId", String.class));
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();

    if (userDetails instanceof CustomUserDetailsDto) {
      CustomUserDetailsDto customUserDetails = (CustomUserDetailsDto) userDetails;
      for (Field field : CustomUserDetailsDto.class.getDeclaredFields()) {
        try {
          if ("password".equals(field.getName())) {
            continue;
          }
          field.setAccessible(true);
          Object value = field.get(customUserDetails);
          if (value != null) {
            claims.put(field.getName(), value);
          }
          String sessionId = UUID.randomUUID().toString();
          claims.put("sessionId", sessionId);
        } catch (IllegalAccessException e) {
          logger.error("Error Occered while generating Token", e.getMessage());
        }
      }
    }
    return generateToken(claims, userDetails);
  }

  public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
    return buildToken(claims, userDetails, 1800000);
  }

  String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    if (!(userDetails instanceof CustomUserDetailsDto)) {
      return false;
    }
    CustomUserDetailsDto customUserDetails = (CustomUserDetailsDto) userDetails;
    final String userId = extractUserId(token);
    return userId.equals(customUserDetails.getUserId()) && !isTokenExpired(token);
  }

  boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public Claims extractAllClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSignInKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      logger.error("JWT expired", e.getMessage());
      throw new JwtExpiredException("JWT expired");
    }
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractSessionId(String token) {
    return extractClaim(token, claims -> claims.get("sessionId", String.class));
  }

  public String extractRole(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("role", String.class);
  }

  public String extractUser(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("userId", String.class);
  }

}
