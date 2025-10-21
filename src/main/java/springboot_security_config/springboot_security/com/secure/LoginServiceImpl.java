package springboot_security_config.springboot_security.com.secure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Created by prashant.mod on 23-07-2024 Tuesday 1:30:27 pm
 *
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
  private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Qualifier("customUserDetailsServiceImpl")
  private final UserDetailsService customUserDetailsService;


  public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    } catch (BadCredentialsException e) {
      logger.error("Incorrect username or Password", e.getMessage());
      throw new BadCredentialsException("Incorrect username or password");
    }


    UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());
    if (!(userDetails instanceof CustomUserDetailsDto)) {
      logger.error("UserDetails is not an instance of CustomUserDetails");
      throw new IllegalStateException("UserDetails is not an instance of CustomUserDetails");
    }



    String jwtToken = jwtService.generateToken(userDetails);


    CustomUserDetailsDto customUserDetails = (CustomUserDetailsDto) userDetails;
    return buildAuthenticationResponse(jwtToken, customUserDetails);
  }

  AuthenticationResponseDto buildAuthenticationResponse(String jwtToken,
      CustomUserDetailsDto customUserDetails) {
    return AuthenticationResponseDto.builder()
        .accessToken(jwtToken)
        .role(customUserDetails.getRole())
        .userName(customUserDetails.getUsername())
        .build();
  }
}
