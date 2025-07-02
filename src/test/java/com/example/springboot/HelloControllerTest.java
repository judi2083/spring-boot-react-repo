// package com.example.springboot;

// import static org.hamcrest.Matchers.equalTo;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;


// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;



// import com.example.springboot.config.TestSecurityConfig; 

// //@WebMvcTest(HelloController.class)
// @Import(TestSecurityConfig.class)
// @SpringBootTest
// @AutoConfigureMockMvc
// //@TestPropertySource(properties = "server.servlet.context-path=/api")
// public class HelloControllerTest {

// 	@Autowired
// 	private MockMvc mvc;

// 	@Test
// 	public void getHello() throws Exception {
// 		// mvc.perform(MockMvcRequestBuilders.get("/api/").accept(MediaType.APPLICATION_JSON))
// 		// 	.with(httpBasic("anil", "mysecret123"))
// 		// 	.andExpect(status().isOk())
// 		// 	.andExpect(content().string(equalTo("Greetings from Spring Boot!")));

// 		mvc.perform(get("/")
// 			.with(httpBasic("anil", "mysecret123"))
// 			.accept(MediaType.APPLICATION_JSON))
// 			.andExpect(status().isOk())
// 			.andExpect(content().string(equalTo("Greetings from Spring Boot!")));

// 	}
// }
