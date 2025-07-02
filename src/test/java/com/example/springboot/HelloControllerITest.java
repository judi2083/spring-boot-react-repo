// package com.example.springboot;

// import org.junit.jupiter.api.Test;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.ResponseEntity;

// import static org.assertj.core.api.Assertions.assertThat;
// import com.example.springboot.config.TestSecurityConfig; 


// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @Import(TestSecurityConfig.class)
// public class HelloControllerITest {

// 	@Autowired
// 	private TestRestTemplate template;

//     @Test
//     public void getHello() throws Exception {
//         //ResponseEntity<String> response = template.getForEntity("/api/", String.class);
//         ResponseEntity<String> response = template.withBasicAuth("anil", "mysecret123")
//                                             .getForEntity("/", String.class);

//         System.out.println("Status Code: " + response.getStatusCode());
//         System.out.println("Response Body: " + response.getBody());


//         assertThat(response.getBody()).isEqualTo("Greetings from Spring Boot!");
//     }
// }

