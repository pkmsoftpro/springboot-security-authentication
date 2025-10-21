package springboot_security_config.springboot_security.com.secure;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

/**
 * Created by prashant.mod on 22-07-2024 Monday 2:45:58 pm
 *
 */
@Data
public class CustomUserDetailsDto implements UserDetails {

    private String userId;
    private String password;
    private String role;
    private String username;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + role);
//      Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
//      authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//      return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement your logic here
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
