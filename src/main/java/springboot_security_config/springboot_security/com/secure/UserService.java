package springboot_security_config.springboot_security.com.secure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

/**
 * Created by prashant.mod on 22-07-2024 Monday 10:46:55 pm
 *
 */


@Component
public class UserService {

  public static List<UserEntity> getUsers() {
    
    List<UserEntity> userList= new ArrayList<>();
    
    for(int i=1; i<6; i++) {
      userList.add(
            UserEntity.builder()
            .id(new Long(i))
            .userId("user" + i)
            .username("user"+i)
            .password("pass"+i)
            .firstName("firstname"+i)
            .lastName("lastname"+i)
            .createdDate(new Date())
            .modifiedDate(new Date())
            .build()
          );
    }
    userList.stream().forEach(user->user.setDisplayRole("Role_admin"));
    IntStream.range(1, 5).boxed().forEach(j-> userList.get(j).setDisplayRole("Role_user"));
    return userList;
  }
}
