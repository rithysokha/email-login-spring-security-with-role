package com.ecomerce.auth;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtSecretMakerTest {
    @Test
    public void testCreateSecret() {
        SecretKey key = Jwts.SIG.HS512.key().build();
        String secretKey = DatatypeConverter.printHexBinary(key.getEncoded());
        System.out.println(secretKey);
    }
}
