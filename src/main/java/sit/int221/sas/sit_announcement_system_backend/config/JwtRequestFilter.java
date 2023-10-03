package sit.int221.sas.sit_announcement_system_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.JwtErrorException;

import java.io.IOException;
//OncePerRequestFilter อยู่ใน Spring framework security


@Component
public class JwtRequestFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint ;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        System.out.println("filter 1 ");
        System.out.println(requestTokenHeader);
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer " )) {
            try {
                    jwtToken = requestTokenHeader.substring(7);
                    Claims claims = (Claims) jwtTokenUtil.getClaims(jwtToken);
                    username = jwtTokenUtil.getSubjectFromToken(jwtToken);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                        if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            usernamePasswordAuthenticationToken
                                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    }
                    else {
                        final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername((String) claims.get("username"));
                        jwtTokenUtil.validateRefreshToken(jwtToken, claims, userDetails);
                        System.out.println("filter 3 ");

                    }
            } catch (IllegalArgumentException e) {
                jwtAuthenticationEntryPoint.commence(request, response, new JwtErrorException("Unable to get JWT Token","token"));
            } catch (ExpiredJwtException e) {
                jwtAuthenticationEntryPoint.commence(request, response,new JwtErrorException("JWT Token has expired","token")) ;
            }catch (MalformedJwtException e){
                jwtAuthenticationEntryPoint.commence(request, response,new JwtErrorException("Unable to read JSON value","format")) ;
            }


        } else {

            logger.warn("JWT Token does not begin with Bearer String");
            System.out.println("-----");
                jwtAuthenticationEntryPoint.commence(request, response, new JwtErrorException("JWT Token does not begin with Bearer String","token"));

        }

        //should be call once
        chain.doFilter(request, response);
    }

}
