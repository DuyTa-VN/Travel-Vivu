package vn.duyta.Travel_Vivu.util;

import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
@RequiredArgsConstructor
public class SecurityUtil {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${jwt.secret-key}")
    private String jwtKey;

    @Value("${jwt.token-validity-in-seconds}")
    private String jwtExpiration;

    public void createToken(Authentication authentication){

    }
}
