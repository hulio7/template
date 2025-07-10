package org.smoliagin.template.util.exceptions;

import lombok.Data;

@Data
public class ErrorInfo {
    private int code;
    private String message;
}