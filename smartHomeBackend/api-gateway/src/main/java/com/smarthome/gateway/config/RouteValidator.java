package com.smarthome.gateway.config;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class RouteValidator {

    // ONLY truly public endpoints
    public static final List<String> openApiEndpoints = Arrays.asList(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/devices/getAll",
            "/api/v1/devices/getBy/**",
            "/api/v1/stock/device/**",
            "/api/v1/devices/search/**",
            "/actuator/**",
            "/eureka/**"
    );

    // Role-based access control
    public static final Map<String, List<String>> roleBasedRoutes = Map.of(
            "ADMIN", Arrays.asList(
                    "/api/v1/devices", "/api/v1/devices/**",
                    "/api/v1/stock", "/api/v1/stock/**",
                    "/api/v1/customers/**",
                    "/api/v1/orders","/api/v1/orders/**", 
                    "/api/v1/payments", "/api/v1/payments/**",
                    "/api/v1/invoices", "/api/v1/invoices/**",
                    "/api/v1/reports/**"
            ),
            "CUSTOMER", Arrays.asList(
                    "/api/v1/devices",  // Browse products (GET only in real app)
                    "/api/v1/customers/register",
                    "/api/v1/devices/**",
                    "/api/v1/customers/**",
                    "/api/v1/orders/**",
                    "/api/v1/stock", "/api/v1/stock/**",
                    "/api/v1/orders/create",
                    "/api/v1/payments/process",
                    "/api/v1/payments/order/**",
                    "/api/v1/invoices/order/**",
                    "/api/v1/reports/**"
//                    "api/v1/reports/invoice/order/**"
            )
    );

    public boolean isSecured(String path) {
        // Check if path is in truly public endpoints
        for (String endpoint : openApiEndpoints) {
            if (endpoint.endsWith("/**")) {
                String basePath = endpoint.replace("/**", "");
                if (path.startsWith(basePath)) {
                    return false;
                }
            } else if (path.equals(endpoint)) {
                return false;
            }
        }
        return true; // All other paths are secured
    }

    public boolean hasPermission(String role, String path, String method) {
        List<String> allowedPaths = roleBasedRoutes.get(role);
        if (allowedPaths == null) return false;

        // Check exact path match or pattern match
        for (String allowedPath : allowedPaths) {
            if (allowedPath.endsWith("/**")) {
                String basePath = allowedPath.replace("/**", "");
                if (path.startsWith(basePath)) {
                    return true;
                }
            } else if (path.equals(allowedPath)) {
                return true;
            }
        }
        return false;
    }
}
