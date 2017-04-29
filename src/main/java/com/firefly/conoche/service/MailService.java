package com.firefly.conoche.service;

import com.firefly.conoche.domain.ActionObject;
import com.firefly.conoche.domain.User;

import com.firefly.conoche.service.dto.DetailNotificationDTO;
import io.github.jhipster.config.JHipsterProperties;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String ACTION_OBJECT = "ao";

    private static final String BASE_URL = "baseUrl";

    private static final String BASE_NOTIFY_PROPERTY = "application.notifications-url.";
    private static final String NOTIFICATION_TRANSLATION = "email.notification.";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final Environment env;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
                       MessageSource messageSource, SpringTemplateEngine templateEngine, Environment env) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.env = env;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendSocialRegistrationValidationEmail(User user, String provider) {
        log.debug("Sending social registration validation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable("provider", StringUtils.capitalize(provider));
        String content = templateEngine.process("socialRegistrationValidationEmail", context);
        String subject = messageSource.getMessage("email.social.registration.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendNotificationEmail(User user, ActionObject ao) {

        try {
            log.debug("Sending notification email", user.getEmail());
            Locale locale = Locale.forLanguageTag(user.getLangKey());
            Context context = new Context(locale);
            String baseUrl = jHipsterProperties.getMail().getBaseUrl();

            context.setVariable(USER, user);
            context.setVariable("url", baseUrl + getNotificationUrl(ao));
            context.setVariable("objectId", ao.getObjectId());
            context.setVariable("message", translateNotificationMessage(locale, ao));
            String subject = messageSource.getMessage(NOTIFICATION_TRANSLATION + "title", null, locale);
            String content = templateEngine.process("notificationEmail", context);
            sendEmail(user.getEmail(), subject, content, false, true);
        }catch(MailAuthenticationException exception) {
            log.error(exception.getMessage());
        }
    }

    private String translateNotificationMessage(Locale locale, ActionObject ao) {
        String objectType = ao.getObjectType()
            .name().toLowerCase()
            .replace("_", "");

        String actionType = ao.getActionType()
            .name().toLowerCase();

        String entity = messageSource.getMessage(
            NOTIFICATION_TRANSLATION + objectType,
            null, locale);
        return  messageSource.getMessage(
            NOTIFICATION_TRANSLATION +  actionType,
            new Object[]{entity}, locale);
    }
    private String getNotificationUrl(ActionObject ao) {
        String property = ao.getObjectType()
            .name().replace("_", "-")
            .toLowerCase();
        String url = env.getProperty(BASE_NOTIFY_PROPERTY + property);
        return String.format(url, ao.getObjectId());
    }

}
