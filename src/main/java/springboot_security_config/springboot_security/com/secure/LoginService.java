package springboot_security_config.springboot_security.com.secure;

public interface LoginService {
  AuthenticationResponseDto authenticate(AuthenticationRequestDto request);


}
