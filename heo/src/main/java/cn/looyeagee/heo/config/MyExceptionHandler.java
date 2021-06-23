package cn.looyeagee.heo.config;

import cn.looyeagee.heo.result.ResultFailure;
import cn.looyeagee.heo.result.ResultModel;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@ResponseBody
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultModel handleException(Exception e) {
        e.printStackTrace();
        return new ResultFailure(e.getMessage());
    }
//
//    //自定义异常
//
//    @ExceptionHandler(MyException.class)
//    public ResultModel handleException(MyException e){
//        e.printStackTrace();
//        return new ResultFailure(e.getMessage());
//    }


    @ExceptionHandler({BindException.class, ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public Object handleMethodArgumentNotValidException(Exception e, HttpServletRequest request) {


        StringBuilder sb = new StringBuilder("参数校验失败：");
        Map<String, String> error = new HashMap<>(8);

        String msg = "";
        if (!(e instanceof BindException) && !(e instanceof MethodArgumentNotValidException)) {
            for (ConstraintViolation cv : ((ConstraintViolationException) e).getConstraintViolations()) {
                msg = cv.getMessage();
                sb.append(msg).append("；");

                Iterator<Path.Node> it = cv.getPropertyPath().iterator();
                Path.Node last = null;
                while (it.hasNext()) {
                    last = (Path.Node) it.next();
                }
                /*for(last = null; it.hasNext(); last = (Path.Node)it.next()) {
                }*/
                error.put(last != null ? last.getName() : "", msg);
            }
        } else {
            List<ObjectError> allErrors = null;
            if (e instanceof BindException) {
                allErrors = ((BindException) e).getAllErrors();
            } else {
                allErrors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
            }
            // 拼接错误信息
            for (ObjectError oe : allErrors) {
                msg = oe.getDefaultMessage();
                sb.append(msg).append("；");
                if (oe instanceof FieldError) {
                    error.put(((FieldError) oe).getField(), msg);
                } else {
                    error.put(oe.getObjectName(), msg);
                }
            }
        }
        return new ResultModel(1, sb.toString(), error);

    }
}

