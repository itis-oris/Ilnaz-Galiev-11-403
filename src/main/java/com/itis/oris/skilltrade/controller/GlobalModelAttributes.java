package com.itis.oris.skilltrade.controller;

import com.itis.oris.skilltrade.entity.User;
import com.itis.oris.skilltrade.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.itis.oris.skilltrade.controller")
public class GlobalModelAttributes {

    @ModelAttribute("contextPath")
    public String contextPath(HttpServletRequest request) {
        return request.getContextPath();
    }

    @ModelAttribute("currentUser")
    public User currentUser(@AuthenticationPrincipal UserDetailsImpl principal) {
        return principal == null ? null : principal.user();
    }
}
