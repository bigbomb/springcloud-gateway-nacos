package com.deng.gateway.route.handle;

import com.deng.gateway.constants.StatusCodeConstants;
import com.deng.gateway.entity.Result;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.web.server.ResponseStatusException;


public class AbstractExceptionHandler {

    protected String formatMessage(Throwable ex) {
        String errorMessage = null;
        if (ex instanceof NotFoundException) {
            String reason = ((NotFoundException) ex).getReason();
            errorMessage = reason;
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            errorMessage = responseStatusException.getMessage();
        } else {
            errorMessage = ex.getMessage();
        }
        return errorMessage;
    }

    protected Result buildErrorMap(String errorMessage) {

        Result result = Result.builder()
                .message("网络正忙，请稍后再试")
                .code(StatusCodeConstants.STATUS_BUSY)
                .body(null)
                .build();

        return result;
    }
}
