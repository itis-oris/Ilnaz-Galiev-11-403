package com.itis.oris.skilltrade.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int status = statusCode == null ? 500 : Integer.parseInt(statusCode.toString());
        HttpStatus httpStatus = HttpStatus.resolve(status);
        String reason = httpStatus == null ? "Error" : httpStatus.getReasonPhrase();

        model.addAttribute("status", status);
        model.addAttribute("error", reason);
        model.addAttribute("message", message == null ? "" : message.toString());

        return switch (status) {
            case 403 -> "error/403";
            case 404 -> "error/404";
            case 400 -> "error/400";
            default -> "error/500";
        };
    }

    @GetMapping("/error/{code}")
    public String staticErrorPage(@PathVariable int code, Model model) {
        HttpStatus httpStatus = HttpStatus.resolve(code);
        String reason = httpStatus == null ? "Error" : httpStatus.getReasonPhrase();
        model.addAttribute("status", code);
        model.addAttribute("error", reason);
        model.addAttribute("message", "");
        return switch (code) {
            case 403 -> "error/403";
            case 404 -> "error/404";
            case 400 -> "error/400";
            default -> "error/500";
        };
    }
}
