package com.example.Job_Hunter.config;

import com.example.Job_Hunter.domain.Entity.Permission;
import com.example.Job_Hunter.domain.Entity.Role;
import com.example.Job_Hunter.domain.Entity.User;
import com.example.Job_Hunter.service.UserService;
import com.example.Job_Hunter.utill.SecurityUtil;
import com.example.Job_Hunter.utill.exception.PermissionEx;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : null;
        if (email != null) {
            User user = userService.getUserByUsername(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(item ->
                            item.getApiPath().equals(path) && item.getMethod().equals(httpMethod));
                    System.out.println(">>> isAllow= " + isAllow);
                    if (isAllow == false) {
                        throw new PermissionEx("Không đủ quyền truy cập");
                    }
                }
            }
        }
        return true;
    }
}
