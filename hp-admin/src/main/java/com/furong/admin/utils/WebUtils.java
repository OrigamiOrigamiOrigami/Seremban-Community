package com.furong.admin.utils;

import com.furong.exception.ErrorCode;
import com.furong.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @author Origami
 * @date 2025/9/10
 */
@Slf4j
public class WebUtils {

    public static Result getResult(BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder sb = new StringBuilder();
            errors.forEach(objectError -> {
                String message = objectError.getDefaultMessage();
                sb.append(message).append(",");
            });
            log.error("校验失败,错误信息:{}", sb.toString());
            return Result.buildFail(ErrorCode.BAD_REQUEST.getCode(), sb.toString());
        }
        return null;
    }

}
