package org.smoliagin.template.service.userService.dto;

import lombok.Data;

@Data
public class UserDtoResponse {

    private Long id;

    private String name;

    private String surname;

    private String email;

}
