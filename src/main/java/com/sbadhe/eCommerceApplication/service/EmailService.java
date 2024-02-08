package com.sbadhe.eCommerceApplication.service;

import com.sbadhe.eCommerceApplication.Exception.MailFailureException;
import com.sbadhe.eCommerceApplication.Model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${app.frontend.url}")
    private String url;

    @Value("${email.from}")
    private String fromAddress;

    private SimpleMailMessage makeMailMessage(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        return simpleMailMessage;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws MailFailureException {
        SimpleMailMessage message = makeMailMessage();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("verify your email to activate your account.");
        message.setText("Please follow the link below to verify your email to activate your account. \n" +
                url + "/auth/verify?token=" + verificationToken.getToken());
        try{
            javaMailSender.send(message);
        }catch(MailException ex){
            throw new MailFailureException();
        }
    }
}
