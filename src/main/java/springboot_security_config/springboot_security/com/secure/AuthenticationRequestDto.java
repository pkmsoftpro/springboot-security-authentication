package springboot_security_config.springboot_security.com.secure;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by prashant.mod on 23-07-2024 Tuesday 1:25:52 pm
 *
 */
@Data
@Builder
@NoArgsConstructor
public class AuthenticationRequestDto {

  private String username;
  private String password;
  
  public AuthenticationRequestDto(String username, String password) {
    this.username = username;
    this.password = password;
  }

}
