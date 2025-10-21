package springboot_security_config.springboot_security.com.secure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by prashant.mod on 23-07-2024 Tuesday 12:57:23 pm
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    private LoginService authenticationService;


    public LoginController(LoginService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
      validateLoginRequest(authenticationRequestDto);
      AuthenticationResponseDto authenticationResponse = authenticationService
              .authenticate(authenticationRequestDto);
      return ResponseEntity.ok(authenticationResponse);
    }
    
    void validateLoginRequest(AuthenticationRequestDto request) {
      if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
              request.getPassword() == null || request.getPassword().trim().isEmpty()) {
          logger.warn("UserId and Password are mandatory");
          throw new IllegalArgumentException("UserID and Password are mandatory");
      }
  }
}
