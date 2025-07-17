package org.smoliagin.template.util.messageSource;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public class Message {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserMessage {
            public static final String USER_NOT_EXIST = "notSuchEntityException.users.notExist";
            public static final String USER_NOT_EXIST_NAME = "notSuchEntityException.users.notExistName";
            public static final String USER_DELETE = "user.delete";
            public static final String USER_NAME_ALREADY_EXISTS = "user.name.already.exists";
            public static final String USER_EMAIL_ALREADY_EXISTS = "user.email.already.exists";
            public static final String USER_WELCOME = "user.welcome";
            public static final String USER_ACTIVITY_PROHIBITED = "user.activity.prohibited";
    }


}


