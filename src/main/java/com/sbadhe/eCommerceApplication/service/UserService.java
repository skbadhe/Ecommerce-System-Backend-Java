package com.sbadhe.eCommerceApplication.service;

import com.sbadhe.eCommerceApplication.Exception.MailFailureException;
import com.sbadhe.eCommerceApplication.Exception.UserAlreadyExistsException;
import com.sbadhe.eCommerceApplication.Exception.UserNotVerifiedException;
import com.sbadhe.eCommerceApplication.Model.DAO.LocalUserDAO;
import com.sbadhe.eCommerceApplication.Model.DAO.VerificationTokenDAO;
import com.sbadhe.eCommerceApplication.Model.LocalUser;
import com.sbadhe.eCommerceApplication.Model.VerificationToken;
import com.sbadhe.eCommerceApplication.api.model.LoginBody;
import com.sbadhe.eCommerceApplication.api.model.RegistrationBody;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static ch.qos.logback.core.joran.JoranConstants.NULL;

@Service
public class UserService {

    @Autowired
    private LocalUserDAO localUserDAO;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationTokenDAO verificationTokenDAO;

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, MailFailureException {
        if(localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
        || localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        user = localUserDAO.save(user);
        return user;
    }

    private VerificationToken createVerificationToken(LocalUser user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;

    }

    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, MailFailureException {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());

        if (opUser.isPresent()){
            LocalUser user = opUser.get();
            if(encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())){
                if(user.isEmailVerified()){
                    return jwtService.generateJWT(user);
                } else{
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.size() == 0 ||
                            verificationTokens.get(0).getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    if (resend){
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                        System.out.println("EmailSent");
                    }
                    throw new UserNotVerifiedException(resend);
                }

            }
        }

        return NULL;

    }

    @Transactional
    public boolean verifyUser(String token){
        Optional<VerificationToken> opToken = verificationTokenDAO.findByToken(token);
        if (opToken.isPresent()){
            VerificationToken verificationToken = opToken.get();
            LocalUser user = verificationToken.getUser();
            if(!user.isEmailVerified()){
                user.setEmailVerified(true);
                localUserDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }
}
