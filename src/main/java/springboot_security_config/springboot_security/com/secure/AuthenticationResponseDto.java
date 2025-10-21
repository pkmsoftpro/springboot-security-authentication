package springboot_security_config.springboot_security.com.secure;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by prashant.mod on 23-07-2024 Tuesday 1:27:38 pm
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {

  @JsonProperty("access_token")
  private String accessToken;

  private String role;
  private String userName;
}
