package sit.int221.sas.sit_announcement_system_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.JwtErrorException;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
//OncePerRequestFilter อยู่ใน Spring framework security


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        System.out.println("11");
        try {
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                Claims claims = null;
                System.out.println("1.1");
                System.out.println(jwtToken);
                System.out.println("1.2");
                try {
                    claims = (Claims) jwtTokenUtil.getClaims(jwtToken);
                } catch (Exception e) {

                    System.out.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
                    String publicKey = request.getHeader("publicKey");
                   JwtTokenVerifier jwtTokenVerifier = new JwtTokenVerifier(publicKey) ;
                    System.out.println(jwtTokenVerifier.isTokenValid(jwtToken));
                    System.out.println("Jwt ms ");

                }
                if (claims != null) {
                    username = jwtTokenUtil.getSubjectFromToken(jwtToken);
                    Object email = claims.get("preferred_username");
                    System.out.println(email);
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        System.out.println("22");
                        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                        if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            usernamePasswordAuthenticationToken
                                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    } else if (email != null) {
                        System.out.println(email);
                    } else {

                        final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername((String) claims.get("username"));
                        jwtTokenUtil.validateRefreshToken(jwtToken, claims, userDetails);


                    }

                }
                System.out.println("33");
                String requestTokenHeaderOtp = request.getHeader("AuthorizationOtp");
                String tokenOtp = null;
                Claims claimsOtp = null;

                if (requestTokenHeaderOtp != null && requestTokenHeaderOtp.startsWith("Bearer ")) {
                    System.out.println("44");
                    tokenOtp = requestTokenHeaderOtp.substring(7);
                    try {
                        claimsOtp = (Claims) jwtTokenUtil.getClaims(tokenOtp);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    if (claimsOtp != null) {
                        if (Objects.equals(claimsOtp.get("type"), "OTP")) {
                            String otpRequest = request.getHeader("Otp");
                            if (!jwtTokenUtil.validateOtpEmail(Integer.valueOf(otpRequest), tokenOtp, claimsOtp)) {
                                throw new AuthenticationException();
                            }
                        }
                    }

                }

            }
            //should be call once
            chain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            jwtAuthenticationEntryPoint.commence(request, response, new JwtErrorException("Unable to get JWT Token", "token"));
        } catch (ExpiredJwtException e) {
            jwtAuthenticationEntryPoint.commence(request, response, new JwtErrorException("JWT Token has expired", "token"));
        } catch (MalformedJwtException e) {
            jwtAuthenticationEntryPoint.commence(request, response, new JwtErrorException("Unable to read JSON value", "format"));
        } catch (AuthenticationException e){
            jwtAuthenticationEntryPoint.commence(request, response, new JwtErrorException("You can not access the resources. !", "token"));
        }

    }


}
