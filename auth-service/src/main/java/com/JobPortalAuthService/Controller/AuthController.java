package com.JobPortalAuthService.Controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.JobPortalAuthService.DTOS.AuthUserRequest;
import com.JobPortalAuthService.DTOS.AuthUserResponse;
import com.JobPortalAuthService.DTOS.LoginRequest;
import com.JobPortalAuthService.DTOS.refreshTokenRequest;
import com.JobPortalAuthService.Services.AuthUserService;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserService authService;

    public AuthController(AuthUserService authService) {
        this.authService = authService;
    }

    /* ================= SIGNUP ================= */

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AuthUserRequest  request) {

        authService.signup(request);

        return ResponseEntity.ok("Signup successful");
    }

    /* ================= LOGIN ================= */

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        AuthUserResponse authResponse = authService.login(request);

        ResponseCookie accessCookie = ResponseCookie.from(
                        "accessToken", authResponse.getAccessToken())
                .httpOnly(true)
                .secure(false)          
                .sameSite("Lax")        
                .path("/")
                .maxAge(900)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(
                        "refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok("LOGIN_SUCCESS");
    }
    /* ================= REFRESH ================= */

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {

        AuthUserResponse authResponse = authService.refresh(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from(
                        "accessToken", authResponse.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(900)
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());

        return ResponseEntity.ok("TOKEN_REFRESHED");
    }



    /* ================= LOGOUT ================= */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        ResponseCookie clearAccess = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)          
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie clearRefresh = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")             
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());

        return ResponseEntity.noContent().build();
    }

}
