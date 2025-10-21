package springboot_security_config.springboot_security.com.secure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Created by prashant.mod on 22-07-2024 Monday 2:47:38 pm
 *
 */
@Service
@RefreshScope
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
      UserEntity ue = UserService.getUsers().get(Integer.parseInt(userId.substring(userId.length()-1))-1);
      return fetchUserDetails(ue);
    }

    CustomUserDetailsDto fetchUserDetails(UserEntity user) throws UsernameNotFoundException {
      CustomUserDetailsDto userDetails = new CustomUserDetailsDto();
      userDetails.setPassword(user.getPassword());
      userDetails.setUsername(user.getUsername());
      userDetails.setRole(user.getDisplayRole());
      userDetails.setUserId(user.getUserId());
      return userDetails;
    }

}
