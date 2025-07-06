// // SimpleCorsFilter.java
// package com.example.springboot.filter;

// import jakarta.servlet.*;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.stereotype.Component;

// import java.io.IOException;

// @Component
// public class SimpleCorsFilter implements Filter {

//     @Override
//     public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//             throws IOException, ServletException {

//         HttpServletResponse response = (HttpServletResponse) res;
//         HttpServletRequest request = (HttpServletRequest) req;

//         response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//         response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//         response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
//         response.setHeader("Access-Control-Allow-Credentials", "true");

//         if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//             response.setStatus(HttpServletResponse.SC_OK);
//         } else {
//             chain.doFilter(req, res);
//         }
//     }
// }
