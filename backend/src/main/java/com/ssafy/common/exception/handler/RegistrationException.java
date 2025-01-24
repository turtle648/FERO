// com.ssafy.common.exception 패키지 내에 생성
package com.ssafy.common.exception.handler;

public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}