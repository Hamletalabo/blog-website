package org.hamlet.blogwebsite.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.hamlet.blogwebsite.config.JwtService;
import org.hamlet.blogwebsite.entity.enums.Roles;
import org.hamlet.blogwebsite.entity.model.ConfirmationToken;
import org.hamlet.blogwebsite.entity.model.JwtToken;
import org.hamlet.blogwebsite.entity.model.User;
import org.hamlet.blogwebsite.exception.CustomException;
import org.hamlet.blogwebsite.payload.request.AuthRequest;
import org.hamlet.blogwebsite.payload.request.EmailDetails;
import org.hamlet.blogwebsite.payload.request.LoginRequest;
import org.hamlet.blogwebsite.payload.response.AuthResponse;
import org.hamlet.blogwebsite.payload.response.LoginResponse;
import org.hamlet.blogwebsite.repository.ConfirmationTokenRepository;
import org.hamlet.blogwebsite.repository.JwtTokenRepository;
import org.hamlet.blogwebsite.repository.UserRepository;
import org.hamlet.blogwebsite.service.AuthService;
import org.hamlet.blogwebsite.service.EmailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final JwtTokenRepository jwtTokenRepository;


    @Override
    public AuthResponse register(AuthRequest authRequest) throws MessagingException {
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new IllegalStateException("Email already taken");
        }
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRoles(Roles.USER);

        User savedUser = userRepository.save(user);


    ConfirmationToken confirmationToken = new ConfirmationToken(savedUser);
        confirmationTokenRepository.save(confirmationToken);
        System.out.println(confirmationToken.getToken());

//        String confirmationUrl = EmailTemplate.getVerificationUrl(baseUrl, confirmationToken.getToken());

    //        String confirmationUrl = baseUrl + "/confirmation/confirm-token-sucess.html?token=" + confirmationToken.getToken();
    String confirmationUrl = "http://localhost:8080/api/v1/auth/confirm?token=" + confirmationToken.getToken();

    //        send email alert

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("ACCOUNT CREATION SUCCESSFUL")
                .messageBody("Congratulations! You account has been successfully created \n "
                        + user.getEmail() + "\n" + user.getFirstname() + "\n" + user.getLastname())
                .build();
        emailService.sendSimpleMailMessage(emailDetails,savedUser.getFirstname(),savedUser.getLastname(),confirmationUrl);

        return AuthResponse.builder()
                .responseCode("001")
                .responseMessage("Confirm your email")
                .build();
    }

    private void saveUserToken(User userModel, String jwtToken) {
        var token = JwtToken.builder()
                .user(userModel)
                .token(jwtToken)
                .tokenType("BEARER")
                .expired(false)
                .revoked(false)
                .build();
        jwtTokenRepository.save(token);
    }

    public void revokeAllUserTokens(User userModel) {
        List<JwtToken> validUserTokens = jwtTokenRepository.findAllValidTokenByUser(userModel.getId());
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        jwtTokenRepository.saveAll(validUserTokens);
    }


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );


            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new CustomException("User not found with username: " + loginRequest.getUsername()));


            if (!user.isEnabled()) {
                throw new CustomException("User account is not enabled. Please check your email to confirm your account.");
            }

            var jwtToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

            return LoginResponse.builder()
                    .responseCode("002")
                    .responseMessage("Login Successfully")
                    .token(jwtToken)
                    .build();

        }catch (AuthenticationException e) {
            throw new CustomException("Invalid username or password!!", e);
        }

    }

    @Override
    public String forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenCreationDate(LocalDateTime.now());
        userRepository.save(user);

        // Send email
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;
        EmailDetails emailDetails = EmailDetails.builder()
                            .recipient(user.getEmail())
                                    .subject("FORGET PASSWORD")
                                            .messageBody(resetUrl).build();
        emailService.sendEmailAlert(emailDetails);
        return "A reset password link has been sent to your account email address:          " + resetUrl;

    }

    @Override
    public String newResetPassword(String token, String newPassword) {

        User user = userRepository.findByResetToken(token).orElseThrow(()-> new CustomException("Invalid token"));
        if (isTokenExpired(user.getTokenCreationDate())){
            throw new CustomException("Token has expired");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenCreationDate(null);
        userRepository.save(user);


        return "Password reset successfully";
    }

    private boolean isTokenExpired(LocalDateTime tokenCreationDate) {
        // Assume token is valid for 24 hour
        return tokenCreationDate.plusHours(24).isBefore(LocalDateTime.now());
    }
}
