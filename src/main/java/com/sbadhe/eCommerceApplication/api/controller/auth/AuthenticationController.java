package com.sbadhe.eCommerceApplication.api.controller.auth;

import com.sbadhe.eCommerceApplication.Exception.MailFailureException;
import com.sbadhe.eCommerceApplication.Exception.UserAlreadyExistsException;
import com.sbadhe.eCommerceApplication.Exception.UserNotVerifiedException;
import com.sbadhe.eCommerceApplication.Model.LocalUser;
import com.sbadhe.eCommerceApplication.api.model.LoginBody;
import com.sbadhe.eCommerceApplication.api.model.LoginResponse;
import com.sbadhe.eCommerceApplication.api.model.RegistrationBody;
import com.sbadhe.eCommerceApplication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static ch.qos.logback.core.joran.JoranConstants.NULL;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody){
        try{
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (MailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token){
        if(userService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) throws UserNotVerifiedException, MailFailureException {

        String jwt = null;
        try{
            jwt = userService.loginUser(loginBody);
        }catch(UserNotVerifiedException e){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setSuccess(false);
            String reason = "USER_NOT_VERIFIED";
            if ( e.isNewEmailSent()) {
                reason += "_EMAIL_RESENT";
            }
            loginResponse.setFailureReason(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponse);
        }catch(MailFailureException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if(jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user)
    {
        return user;
    }
}
