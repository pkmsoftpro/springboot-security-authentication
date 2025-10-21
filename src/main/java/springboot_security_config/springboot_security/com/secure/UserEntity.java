package springboot_security_config.springboot_security.com.secure;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * Created by prashant.mod on 22-07-2024 Monday 10:34:21 pm
 *
 */
@Data
@Builder
public class UserEntity {

  private Long id;
  private String userId;
  private String password;
  private String username;
  private String firstName;
  private String lastName;
  private String displayRole;
  private Date createdDate;
  private Date modifiedDate;
}
