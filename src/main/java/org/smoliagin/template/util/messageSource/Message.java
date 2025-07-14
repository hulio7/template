package org.smoliagin.template.util.messageSource;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public class Message {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserMessage {
            public static final String USER_NOT_EXIST = "notSuchEntityException.users.notExist";
            public static final String USER_DELETE = "user.delete";
    }


}


