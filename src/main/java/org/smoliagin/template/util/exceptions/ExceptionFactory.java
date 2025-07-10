package org.smoliagin.template.util.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.smoliagin.template.util.messageSource.MessageSourceFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionFactory {

    public static EntityNotFoundException entityNotFoundException(String label, Object... args){
        return new EntityNotFoundException(MessageSourceFactory.getMessage(label, args));
    }

    public static BusinessLogicException businessLogicException(String label, Object... args){
        return new BusinessLogicException(MessageSourceFactory.getMessage(label, args));
    }

    public static BadRequestException badRequestException(String label, Object... args) {
        return new BadRequestException(MessageSourceFactory.getMessage(label, args));
    }
}
