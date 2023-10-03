package sit.int221.sas.sit_announcement_system_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sit.int221.sas.sit_announcement_system_backend.properties.JwtProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenUtil {
    private static final long serialVersionUID = -2550185165626007488L;

    @Autowired
    private JwtProperties jwtProperties;
    public static final long JWT_TOKEN_VALIDITY = 60*1000;



    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateAccessToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("type","AccessToken");
        return doGenerateToken(claims, userDetails.getUsername());
    }
    public String generateRefreshToken(String accesstoken) {

        Map<String,Object> claims = new HashMap<>() ;
        claims.put("type","RefreshToken");
        claims.put("accessToken",accesstoken) ;
        claims.put("username",getSubjectFromToken(accesstoken));
   return doGenerateRefreshToken(claims);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) { //subject ก็แล้วแต่ว่าเราจะแอดอะไรเข้าไปใน map


        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))

                .setExpiration(new Date( Math.round(System.currentTimeMillis() + (JWT_TOKEN_VALIDITY * jwtProperties.getTokenInterval()))))


                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey()).compact(); // compact คือทำให้เป็นรูปแบบ encrypt => 3...
    }

    public String doGenerateRefreshToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date( Math.round(System.currentTimeMillis() + (JWT_TOKEN_VALIDITY * jwtProperties.getRefreshExpirationDateInMs()))))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey()).compact();

    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getSubjectFromToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public  Boolean validateRefreshToken(String token,Claims claims,UserDetails userDetails){

     return  (!isTokenExpired(token)) &&   (claims.get("type").equals("RefreshToken") ) &&   (claims.get("username").equals(userDetails.getUsername())) ;
   //( isTokenExpired((String) claims.get("accessToken") ) )&&
    }

    public  Object getClaims(String token ){
        Claims claims = getAllClaimsFromToken(token);// Use the appropriate signing key
        return claims ;
    }




}
