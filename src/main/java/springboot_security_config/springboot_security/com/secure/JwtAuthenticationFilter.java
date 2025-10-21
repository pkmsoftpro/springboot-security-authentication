package springboot_security_config.springboot_security.com.secure;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by prashant.mod on 22-07-2024 Monday 2:16:37 pm
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Autowired
  @Qualifier("customUserDetailsServiceImpl")
  private final CustomUserDetailsServiceImpl customUserDetailsService;

  private final ObjectMapper objectMapper;

  private final List<String> WHITE_LIST_URL = List.of("/api/login");

  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      if (!requiresAuthentication(request)) {
        filterChain.doFilter(request, response);
        return;
      }

      String authHeader = request.getHeader("Authorization");
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        log.warn("JWT Token is missing or invalid");
        sendErrorResponse(response, "JWT Token is missing or invalid");
        return;
      }

      String jwt = authHeader.substring(7);
      String userId = jwtService.extractUserId(jwt);
      if (userId == null || SecurityContextHolder.getContext().getAuthentication() != null) {
        log.warn("JWT Token is missing or invalid");
        sendErrorResponse(response, "JWT Token is missing or invalid");
        return;
      }

      CustomUserDetailsDto customUserDetails = (CustomUserDetailsDto) customUserDetailsService.loadUserByUsername(userId);

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              customUserDetails, null, customUserDetails.getAuthorities());
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.error("An error occurred while processing the JWT Token", e);
      sendErrorResponse(response, e.getMessage());
    }
  }


  private boolean requiresAuthentication(HttpServletRequest request) {
    String requestPath = request.getRequestURI();
    return WHITE_LIST_URL.stream().noneMatch(url -> pathMatcher.match(url, requestPath));
    }

  private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
    ErrorResponse errorResponse = new ErrorResponse(errorMessage);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
