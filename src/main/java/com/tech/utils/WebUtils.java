package com.tech.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils {

    public static String renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public  static String getErrorMessages(BindingResult bindingResult){
        return bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .reduce((msg1, msg2) -> String.format("%s %s", msg1, msg2))
                .orElse("Invalid fields");
    }
}
