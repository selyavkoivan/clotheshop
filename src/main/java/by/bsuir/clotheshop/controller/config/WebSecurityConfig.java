package by.bsuir.clotheshop.controller.config;

import by.bsuir.clotheshop.model.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
             http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/user/sign-up", "/js/user.js","/css/styles.css", "/product/*", "/goods/{id}/", "/js/goods.js")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                     .formLogin()
                     .loginPage("/login").failureUrl("/user/login-error").successForwardUrl("/user/login")
                     .permitAll()
                     .and()
                     .rememberMe()
                .and()
                     .logout()
                     .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}