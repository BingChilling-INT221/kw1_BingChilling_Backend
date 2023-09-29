package sit.int221.sas.sit_announcement_system_backend.config;//package sit.int204.classicmodels.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authConfiguration;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/token").permitAll() ///"/api/product"ยอมหมด
                        // all other requests need to be authenticated
                        .anyRequest().authenticated())

                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .httpBasic();
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    //บอกว่า password encoder เราใช้อะไร
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 30, 1, 16, 2);
    }

//    @Bean
//    public UsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
//        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
//        filter.setAuthenticationManager(authenticationManager());
//        filter.setFilterProcessesUrl("/custom-login"); // Custom login endpoint URL
//        return filter;
//    }

}
