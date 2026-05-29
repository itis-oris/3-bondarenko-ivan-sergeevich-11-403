package com.itis.cryptotracker.controller.handler;

import com.itis.cryptotracker.dto.response.ApiError;
import com.itis.cryptotracker.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public Object handleService(ServiceException ex, HttpServletRequest req) {
        if (ex.getStatus().is5xxServerError()) {
            log.error("Service exception [{}]: {}", ex.getStatus(), ex.getMessage(), ex);
        } else {
            log.warn("Service exception [{}]: {}", ex.getStatus(), ex.getMessage());
        }
        return respond(req, ex.getStatus(), ex.getStatus().getReasonPhrase(), ex.getMessage(), null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoStaticResource(NoResourceFoundException ex, HttpServletRequest req) {
        log.debug("Static resource not found: {}", ex.getResourcePath());
        return respond(req, HttpStatus.NOT_FOUND, "Not Found",
                "Resource not found: %s".formatted(ex.getResourcePath()), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ApiError.FieldError> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return respond(req, HttpStatus.BAD_REQUEST, "Validation Failed", "Request validation failed", fields);
    }

    @ExceptionHandler(BindException.class)
    public Object handleBind(BindException ex, HttpServletRequest req) {
        List<ApiError.FieldError> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return respond(req, HttpStatus.BAD_REQUEST, "Validation Failed", "Form validation failed", fields);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return respond(req, HttpStatus.FORBIDDEN, "Forbidden", "Access denied", null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public Object handleAuth(AuthenticationException ex, HttpServletRequest req) {
        return respond(req, HttpStatus.UNAUTHORIZED, "Unauthorized", "Authentication required", null);
    }

    @ExceptionHandler(Exception.class)
    public Object handleAny(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception at {}", req.getRequestURI(), ex);
        return respond(req, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Something went wrong on our side", null);
    }

    private Object respond(HttpServletRequest req, HttpStatus status, String error, String message,
                           List<ApiError.FieldError> fields) {
        if (wantsJson(req)) {
            ApiError body = fields == null
                    ? ApiError.of(status.value(), error, message, req.getRequestURI())
                    : ApiError.of(status.value(), error, message, req.getRequestURI(), fields);
            return ResponseEntity.status(status).body(body);
        }
        ModelAndView mav = new ModelAndView();
        mav.setStatus(status);
        mav.setViewName(viewFor(status));
        mav.addObject("status", status.value());
        mav.addObject("error", error);
        mav.addObject("message", message);
        mav.addObject("path", req.getRequestURI());
        return mav;
    }

    private static String viewFor(HttpStatus status) {
        return switch (status) {
            case NOT_FOUND -> "error/404";
            case FORBIDDEN, UNAUTHORIZED -> "error/403";
            default -> "error/500";
        };
    }

    private static boolean wantsJson(HttpServletRequest req) {
        if (req.getRequestURI().startsWith("/api/")) {
            return true;
        }
        String accept = req.getHeader("Accept");
        if (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return true;
        }
        String requested = req.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(requested);
    }
}
