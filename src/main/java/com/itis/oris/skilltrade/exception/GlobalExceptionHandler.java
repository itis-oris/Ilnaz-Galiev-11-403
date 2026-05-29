package com.itis.oris.skilltrade.exception;

import com.itis.oris.skilltrade.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class, NoHandlerFoundException.class})
    public Object notFound(Exception ex, HttpServletRequest request) {
        log.warn("404 на {}: {}", request.getRequestURI(), ex.getMessage());
        if (wantsJson(request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.of(404, "Not Found", ex.getMessage()));
        }
        return errorView("error/404", HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler({com.itis.oris.skilltrade.exception.AccessDeniedException.class,
            AccessDeniedException.class})
    public Object accessDenied(Exception ex, HttpServletRequest request) {
        log.warn("403 на {}: {}", request.getRequestURI(), ex.getMessage());
        if (wantsJson(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiErrorResponse.of(403, "Forbidden", ex.getMessage()));
        }
        return errorView("error/403", HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public Object unauthorized(AuthenticationException ex, HttpServletRequest request) {
        if (wantsJson(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiErrorResponse.of(401, "Unauthorized", ex.getMessage()));
        }
        ModelAndView mv = new ModelAndView("redirect:/login");
        return mv;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .toList();
        log.debug("400 валидация на {}: {}", request.getRequestURI(), details);
        if (wantsJson(request)) {
            return ResponseEntity.badRequest()
                    .body(ApiErrorResponse.of(400, "Bad Request", "Ошибка валидации", details));
        }
        return errorView("error/400", HttpStatus.BAD_REQUEST, String.join("; ", details), request);
    }

    @ExceptionHandler({BadOperationException.class, UserAlreadyExistsException.class,
            IllegalArgumentException.class})
    public Object badOperation(RuntimeException ex, HttpServletRequest request) {
        log.debug("400 бизнес на {}: {}", request.getRequestURI(), ex.getMessage());
        if (wantsJson(request)) {
            return ResponseEntity.badRequest()
                    .body(ApiErrorResponse.of(400, "Bad Request", ex.getMessage()));
        }
        return errorView("error/400", HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public Object internal(Exception ex, HttpServletRequest request) {
        log.error("500 на {}", request.getRequestURI(), ex);
        if (wantsJson(request)) {
            return ResponseEntity.internalServerError()
                    .body(ApiErrorResponse.of(500, "Internal Server Error", ex.getMessage()));
        }
        return errorView("error/500", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    private boolean wantsJson(HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/api/")) return true;
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("application/json");
    }

    private ModelAndView errorView(String view, HttpStatus status, String message, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(view);
        mv.setStatus(status);
        mv.addObject("status", status.value());
        mv.addObject("error", status.getReasonPhrase());
        mv.addObject("message", message);
        
        
        mv.addObject("contextPath", request.getContextPath());
        return mv;
    }

    private String formatFieldError(FieldError e) {
        return e.getField() + ": " + e.getDefaultMessage();
    }
}
