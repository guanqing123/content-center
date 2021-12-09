package com.itmuch.contentcenter.auth;

import com.itmuch.contentcenter.security.SecurityException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/9 14:26
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorBody> error(SecurityException e){
        log.warn("发送SecurityException异常", e);
        return new ResponseEntity<ErrorBody>(
             ErrorBody.builder()
                .body("Token非法,用户不允许访问!~")
                .status(HttpStatus.UNAUTHORIZED.value())
                .build(),
             HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(Exception.class)
    public String error(Exception e){
        return e.getMessage();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ErrorBody {
    private String body;
    private int status;
}
