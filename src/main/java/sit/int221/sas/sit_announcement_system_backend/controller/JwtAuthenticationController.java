package sit.int221.sas.sit_announcement_system_backend.controller;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.config.JwtTokenUtil;
import sit.int221.sas.sit_announcement_system_backend.config.JwtUserDetailsService;
import sit.int221.sas.sit_announcement_system_backend.entity.JwtRequest;
import sit.int221.sas.sit_announcement_system_backend.entity.JwtResponse;
import sit.int221.sas.sit_announcement_system_backend.entity.JwtResponseOnlyAccessToken;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.JwtErrorException;

import java.util.Collection;

@RestController
@RequestMapping("api/token")
@CrossOrigin(origins = {
        "http://intproj22.sit.kmutt.ac.th",
        "https://intproj22.sit.kmutt.ac.th",
        "http://localhost:5173",
        "https://localhost:5173",
        "http://ip22kw1.sit.kmutt.ac.th",
        "https://ip22kw1.sit.kmutt.ac.th",
        "http://ip22kw1.sit.kmutt.ac.th:800",
        "https://ip22kw1.sit.kmutt.ac.th:800",
})
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletRequest request) throws Exception {
        String error = (String) request.getAttribute("error");
        if (request.getAttribute("error") != null) {
            throw new JwtErrorException(error, "token");
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword(), userDetails.getAuthorities());

        final String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(accessToken, refreshToken));
    }

    // get ก็สามารถส่ง  json data ได้
    @GetMapping("")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {
        String error = (String) request.getAttribute("error");
        if (request.getAttribute("error") != null) {
            throw new JwtErrorException(error, "token");
        }
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        Claims claims = (Claims) jwtTokenUtil.getClaims(jwtToken);
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername((String) claims.get("username"));
        final String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponseOnlyAccessToken(accessToken));

    }


    private void authenticate(String username, String password, Collection<? extends GrantedAuthority> authority) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, authority));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

    }
}
