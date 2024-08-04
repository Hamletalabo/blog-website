package org.hamlet.blogwebsite.service.impl;

import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.entity.enums.Roles;
import org.hamlet.blogwebsite.entity.model.User;
import org.hamlet.blogwebsite.payload.request.AuthRequest;
import org.hamlet.blogwebsite.payload.request.EmailDetails;
import org.hamlet.blogwebsite.payload.response.AuthResponse;
import org.hamlet.blogwebsite.repository.UserRepository;
import org.hamlet.blogwebsite.service.AuthService;
import org.hamlet.blogwebsite.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Override
    public AuthResponse register(AuthRequest authRequest) {
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new IllegalStateException("Email already taken");
        }

        User user = new User();
        user.setFirstname(authRequest.getFirstname());
        user.setLastname(authRequest.getLastname());
        user.setUsername(authRequest.getUsername());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setPhoneNumber(authRequest.getPhoneNumber());
        user.setCountry(authRequest.getCountry());
        user.setState(authRequest.getState());
        user.setRoles(Roles.USERS);

        userRepository.save(user);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("ACCOUNT CREATION SUCCESSFUL")
                .messageBody("Congratulations! You account has been successfully created \n "
                        + user.getEmail() + "\n" + user.getFirstname()+ "\n"  + user.getLastname() )
                .build();
        emailService.sendEmailAlert(emailDetails);

        return AuthResponse.builder()
                .responseCode("001")
                .responseMessage("Account created successfully")
                .build();


//        ConfirmationToken confirmationToken = new ConfirmationToken(savedUser);
//        confirmationTokenRepository.save(confirmationToken);
//        System.out.println(confirmationToken.getToken());
//
////        String confirmationUrl = EmailTemplate.getVerificationUrl(baseUrl, confirmationToken.getToken());
//
////        String confirmationUrl = baseUrl + "/confirmation/confirm-token-sucess.html?token=" + confirmationToken.getToken();
//        String confirmationUrl = "http://localhost:8080/api/v1/auth/confirm?token=" + confirmationToken.getToken();
//
////        send email alert
//        EmailDetails emailDetails = EmailDetails.builder()
//                .recipient(savedUser.getEmail())
//                .subject("ACCOUNT CREATION SUCCESSFUL")
//                .build();
//        emailService.sendSimpleMailMessage(emailDetails, savedUser.getFirstName(), savedUser.getLastName(), confirmationUrl);
//        return "Confirmed Email";
    }
}
