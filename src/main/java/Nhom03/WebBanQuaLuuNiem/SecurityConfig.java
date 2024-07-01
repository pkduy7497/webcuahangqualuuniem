package Nhom03.WebBanQuaLuuNiem;

import Nhom03.WebBanQuaLuuNiem.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/", "/oauth/**", "/register", "/error", "/products", "/cart", "/cart/**")
                .permitAll()
                .requestMatchers("/products/edit/**", "/products/add", "/products/delete","/categories/edit/**", "/categories/add", "/categories/delete","/categories","/Employee/edit/**", "/Employee/add", "/Employee/delete", "/Employee")
                .hasAnyAuthority("ADMIN")
                .requestMatchers("/api/**")
                .permitAll()
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll()
            )
            .rememberMe(rememberMe -> rememberMe
                .key("hutech")
                .rememberMeCookieName("hutech")
                .tokenValiditySeconds(24 * 60 * 60)
                .userDetailsService(userDetailsService())
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/403")
            )
            .sessionManagement(sessionManagement -> sessionManagement
                .maximumSessions(1)
                .expiredUrl("/login")
            )
            .httpBasic(httpBasic -> httpBasic
                .realmName("hutech")
            )
            .build();
    }
}