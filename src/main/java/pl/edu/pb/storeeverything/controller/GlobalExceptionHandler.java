package pl.edu.pb.storeeverything.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.pb.storeeverything.exception.EmailAlreadyExists;
import pl.edu.pb.storeeverything.exception.LoginAlreadyExists;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({
            LoginAlreadyExists.class,
            EmailAlreadyExists.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleDuplicateUserException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("errorImage", "https://httpcats.com/409.jpg");
        modelAndView.addObject("buttonLink", "/register");
        return modelAndView;
    }
}
