package main.security;

import main.models.Role;
import main.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by Lewis on 26/02/2017.
 */
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Role role;

    // This is for Spring Security to use, where as User is more for us to use

    public UserDetailsImpl(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role.toString());
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
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // this is called a factory method
    public static UserDetails createFromUser(User user) {
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }

}
