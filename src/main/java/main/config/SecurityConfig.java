package main.config;

import main.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Created by Lewis on 25/02/2017.
 */
@Configuration
@EnableWebSecurity // Spring Security Defaults
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsServiceBean() {
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // This makes sure anyone can access the index page and login page, and making sure the formLogin
        // is setup and permits all, and all can logout, pretty basic but we can add to it as needed

        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/upload").permitAll()
                .antMatchers("/download").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/admin/user/create").permitAll()
                .antMatchers("/testRest").permitAll()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN").anyRequest().authenticated()
        .and().formLogin()
                .loginPage("/login").permitAll().and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").permitAll();
    }

    // Using BCrypt to hash the password so that it's not viewed in plain text

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

}
