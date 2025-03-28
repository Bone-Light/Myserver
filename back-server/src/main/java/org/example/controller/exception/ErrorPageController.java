package org.example.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.DAO.entity.RestBean;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorPageController extends AbstractErrorController {
    public ErrorPageController(ErrorAttributes errorAttributes) {super(errorAttributes);}

    @RequestMapping
    public RestBean<Void> error(HttpServletRequest request) {
        HttpStatus status = this.getStatus(request);
        Map<String, Object> errorAttributes = this.getErrorAttributes(request, this.getAttributeOption());
        String message = errorAttributes.get("message").toString();
        return RestBean.failure(status.value(), message);
    }

    private Optional<String> convertErrorMessage(HttpStatus status){
        String value = switch (status.value()){
            case 400 -> "请求参数有误";
            case 403 -> "禁止访问";
            case 404 -> "接口不存在";
            case 405 -> "请求方法错误";
            case 500 -> "服务器内部故障，请联系管理员";
            default -> null;
        };
        return Optional.ofNullable(value);
    }

    private ErrorAttributeOptions getAttributeOption(){
        return ErrorAttributeOptions
                .defaults()
                .including(ErrorAttributeOptions.Include.MESSAGE,
                            ErrorAttributeOptions.Include.EXCEPTION);
    }
}
