package com.sideforge.ringring.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Base64;

public class JwtSecretKeyGeneratorTest {

    @Test
    public void generateJwtSecretKey() {
        String secureKey = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
        System.out.println(secureKey);

        // 💡 생성된 키는 application.yml 또는 환경 변수에 설정하세요.
    }
}
