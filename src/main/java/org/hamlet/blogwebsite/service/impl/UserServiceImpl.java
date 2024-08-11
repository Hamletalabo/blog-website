package org.hamlet.blogwebsite.service.impl;

import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.entity.model.User;
import org.hamlet.blogwebsite.payload.request.EditUserDetailsRequest;
import org.hamlet.blogwebsite.payload.response.ApiResponse;
import org.hamlet.blogwebsite.payload.response.EditUserDetailsResponse;
import org.hamlet.blogwebsite.repository.UserRepository;
import org.hamlet.blogwebsite.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;


    @Override
    public ApiResponse resetPassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ApiResponse.builder()
                .responseCode("000")
                .responseMessage("Password updated successfully")
                .build();
    }

    @Override
    public EditUserDetailsResponse editUserDetails(String username, EditUserDetailsRequest editUserDetailsRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setFirstname(editUserDetailsRequest.getFirstname());
        user.setLastname(editUserDetailsRequest.getLastname());
        user.setPhoneNumber(editUserDetailsRequest.getPhoneNumber());
        user.setCountry(editUserDetailsRequest.getCountry());
        user.setBio(editUserDetailsRequest.getBio());
        user.setProfilePicture(editUserDetailsRequest.getProfilePicture());

        userRepository.save(user);

        return EditUserDetailsResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .country(user.getCountry())
                .phoneNumber(user.getPhoneNumber())
                .bio(user.getBio())
                .build();
    }
}
