package com.BrainBlitz.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.BrainBlitz.entity.Role;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.repository.UserRepository;
import com.BrainBlitz.util.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // ✅ Extract user details from Google
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // ✅ Check if user exists, if not create one
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            // User exists — just login
            user = existingUser.get();
        } else {
            // New user — save to DB
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(""); // No password for OAuth users
            user.setRole(Role.USER);
            user.setScore(0.0);
            user.setLevel(1);
            user.setStreak(0);
            user.setActive(true);
            user.setEmailVerified(true); // ✅ Google already verified email
            userRepository.save(user);
        }

        // ✅ Generate JWT token
        String token = jwtService.generateToken(email, user.getRole());

        // ✅ Redirect to frontend with token
        String redirectUrl = frontendUrl + "/oauth2/callback?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}