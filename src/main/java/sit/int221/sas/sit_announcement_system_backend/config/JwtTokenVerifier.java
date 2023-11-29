package sit.int221.sas.sit_announcement_system_backend.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@AllArgsConstructor
@Getter
@Setter
public class JwtTokenVerifier {
    // please input your jwt token & public key here
    public  String publicKey = "";

    public boolean isTokenValid(String token) {
        try {
            buildJWTVerifier().verify(token);
            // if token is valid no exception will be thrown
            return true;
        } catch (CertificateException e) {
            //if CertificateException comes from buildJWTVerifier()
            System.out.println(e.getMessage());
            return false;
        } catch (JWTVerificationException e) {
            // if JWT Token in invalid
            System.out.println(e.getMessage());
            return false;
        } catch (Exception e) {
            // If any other exception comes
            System.out.println(e.getMessage());
            return false;
        }
    }


    private JWTVerifier buildJWTVerifier() throws CertificateException {
        var algo = Algorithm.RSA256(getRSAPublicKey(), null);
        return JWT.require(algo).build();
    }

    private RSAPublicKey getRSAPublicKey() throws CertificateException {
        var decode = Base64.getDecoder().decode(publicKey);
        var certificate = CertificateFactory.getInstance("X.509")
                .generateCertificate(new ByteArrayInputStream(decode));
        var publicKey = (RSAPublicKey) certificate.getPublicKey();
        return publicKey;
    }
}
