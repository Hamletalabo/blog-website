package org.hamlet.blogwebsite.service;

import org.hamlet.blogwebsite.payload.request.EditUserDetailsRequest;
import org.hamlet.blogwebsite.payload.response.ApiResponse;
import org.hamlet.blogwebsite.payload.response.EditUserDetailsResponse;

public interface UserService {

    ApiResponse resetPassword(String username, String oldPassword, String newPassword);
    EditUserDetailsResponse editUserDetails(String username, EditUserDetailsRequest editUserDetailsRequest);

}
