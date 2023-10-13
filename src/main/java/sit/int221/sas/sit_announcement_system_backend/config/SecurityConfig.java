package sit.int221.sas.sit_announcement_system_backend.config;//package sit.int204.classicmodels.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authConfiguration;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAccessDeniedEntryPoint jwtAccessDeniedEntryPoint;
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
                        .requestMatchers("/api/token").permitAll()
                        //allow all  self and child
                                .requestMatchers(POST, "api/users/announcer").permitAll()
                                .requestMatchers("api/users/announcer/**").hasAnyAuthority(Role.admin.toString(), Role.announcer.toString())
                                .requestMatchers("api/users/**").hasAuthority(Role.admin.name())
                                .requestMatchers(POST,"api/announcements/**").hasAnyAuthority(Role.admin.toString(),Role.announcer.toString())
                                .requestMatchers(PUT,"api/announcements/**").hasAnyAuthority(Role.admin.toString(),Role.announcer.toString())
                                .requestMatchers(DELETE,"api/announcements/**").hasAnyAuthority(Role.admin.toString(),Role.announcer.toString())
                                .requestMatchers("api/announcer/announcements/**").hasAuthority(Role.announcer.toString())
                                .requestMatchers(GET,"api/announcements/**").permitAll()
                                .requestMatchers(GET,"api/categories/**").permitAll()
                                .anyRequest().authenticated()

                        // all other requests need to be authenticated
                        )
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedEntryPoint);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        //.exceptionHandling() แล้ว ไม่ต้อง http.basic แล้ว
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
