package org.smoliagin.template.util.messageSource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageSourceFactory {
    private static MessageSource messageSource;

    public MessageSourceFactory(MessageSource messageSource) {
        MessageSourceFactory.messageSource = messageSource;
    }

    public static String getMessage (String code, Object... arg) {
        return messageSource.getMessage(code, arg, Locale.forLanguageTag("ru"));
    }

    public static void setMessageSource(MessageSource messageSource) {
        MessageSourceFactory.messageSource = messageSource;
    }
}

