// package com.example.springboot;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;

// @Controller
// public class FrontendController {

//     // This will forward all unmatched routes (except static and API) to index.html
//     @RequestMapping(value = { "/", "/{path:^(?!api|swagger-ui|v3).*}" })
//     public String index() {
//         return "forward:/index.html";
//     }
// }
