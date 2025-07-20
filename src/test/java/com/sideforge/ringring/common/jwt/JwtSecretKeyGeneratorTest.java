package com.sideforge.ringring.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.Key;

public class JwtSecretKeyGeneratorTest {

    @Test
    public void generateJwtSecretKey() {
        // 1. HMAC-SHA256용 비밀 키 생성 (256비트 이상)
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // 2. Base64로 인코딩
        String base64Secret = Encoders.BASE64.encode(key.getEncoded());

        // 3. 출력
        System.out.println("Generated JWT Secret Key (Base64):");
        System.out.println(base64Secret);

        // 💡 생성된 키는 application.yml 또는 환경 변수에 설정하세요.
    }
}
