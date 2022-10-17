package com.devillage.teamproject.service.auth;

import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.service.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailAuthServiceImpl implements EmailAuthService{
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String from;
    @Override
    public void sendEmail(String email) {
        Random random = new Random();
        String authKey = String.valueOf((random.nextInt(888888) +111111));

        String subject = "Devillage 회원가입 인증번호 입니다.";
        String content = "인증번호 : " + authKey;

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content,true);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new BusinessLogicException(ExceptionCode.FAIL_TO_SEND_EMAIL);
        }
        redisUtil.deleteData(email);
        redisUtil.setDataExpire(email,authKey,60 * 3L);

    }

    @Override
    public boolean verifyAuthKey(String email,String authKey){
        String value = redisUtil.getData(email);
//        try {
//            if(value.equals(authKey)){
//                redisUtil.deleteData(email);
//                return true;
//            }
//            else return false;
//        }catch (Exception e){
//            throw new BusinessLogicException(ExceptionCode.NOT_VALID_AUTH_KEY);
//        }

        if(value.equals(authKey)){
                redisUtil.deleteData(email);
                return true;
        }
        else{
            throw new BusinessLogicException(ExceptionCode.NOT_VALID_AUTH_KEY);
        }
    }
}
