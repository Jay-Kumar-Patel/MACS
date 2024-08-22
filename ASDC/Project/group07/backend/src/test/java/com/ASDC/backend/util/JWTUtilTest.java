package com.ASDC.backend.util;
import com.ASDC.backend.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JWTUtilTest {
    @Mock
    private JWTUtil jwtUtil;

    @Test
    public void generateTokenTest() {
        String email = "demo@demo.com";
        String token = "generatedToken";
        when(jwtUtil.generateToken(email)).thenReturn(token);
        assertEquals(token, jwtUtil.generateToken(email));
    }

    @Test
    public void validateTokenValidTest() {
        String validToken = "validToken";
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        assertTrue(jwtUtil.validateToken(validToken));
    }

    @Test
    public void validateTokenInvalidTest() {
        String invalidToken = "invalidToken";
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);
        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    public void getEmailFromTokenTest() {
        String token = "testToken";
        String email = "demo@demo.com";
        when(jwtUtil.getEmail(token)).thenReturn(email);
        assertEquals(email, jwtUtil.getEmail(token));
    }

}
