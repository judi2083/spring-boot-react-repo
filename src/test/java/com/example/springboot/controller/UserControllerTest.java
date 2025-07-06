package com.example.springboot.controller;

import com.example.springboot.config.TestSecurityConfig;
import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.service.MailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setup() {
        // userRepository.deleteAll();

        //  adminUser = new User(null, "anil", passwordEncoder.encode("mysecret123"), "ADMIN");
        //  normalUser = new User(null, "demo1", passwordEncoder.encode("demo123"), "USER");


        // userRepository.saveAll(List.of(adminUser, normalUser));

        // String rawPassword = "mysecret123"; // Try the actual password
        // String hashedPassword = "$2a$10$iV0HzMLGjaDa2BSM8V8IKeKL3/x22M9Qx.RbnDprN8BaNViA7c8ay";

        // BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // boolean isMatch = encoder.matches(rawPassword, hashedPassword);

        // System.out.println("Password match: " + isMatch);
    }

    // @Test
    // @WithMockUser(username = "anil", roles = {"ADMIN"})
    // void testGetUsersAsAdmin() throws Exception {
    //     mockMvc.perform(get("/users")
    //                     .with(httpBasic("anil", "mysecret123")))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$", hasSize(2)))
    //             .andExpect(jsonPath("$[0].username", not(emptyOrNullString())));
    // }

    // @Test
    // void testUpdateUser() throws Exception {
    //     Long id = normalUser.getId();

    //     String updatePayload = """
    //         {
    //             "username": "john_updated",
    //             "role": "ADMIN"
    //         }
    //         """;

    //     mockMvc.perform(put("/users/" + id)
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(updatePayload)
    //                     .with(httpBasic("anil", "mysecret123")))
    //             .andExpect(status().isOk())
    //             .andExpect(content().string("User updated successfully"));

    //     User updated = userRepository.findById(id).orElseThrow();
    //     assert updated.getUsername().equals("john_updated");
    //     assert updated.getRole().equals("ADMIN");
    // }

    // @Test
    // @WithMockUser(username = "anil", roles = {"ADMIN"})
    // void testGetUsersAsAdmin() throws Exception {
    //     System.out.println("testGetUsersAsAdmin is calling....");
    //     mockMvc.perform(get("/users"))
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$", hasSize(2)))
    //         .andExpect(jsonPath("$[0].username", not(emptyOrNullString())));
    // }

    // @Test
    // @WithMockUser(username = "anil", roles = "ADMIN")    
    // void testAddNewUser() throws Exception {
    //     System.out.println("testAddNewUser is calling....");
    //     String payload = """
    //         {
    //             "username": "newuser",
    //             "password": "newpass123",
    //             "role": "USER"
    //         }
    //         """;
    //     mockMvc.perform(post("/auth/register")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(payload))
    //             .andExpect(status().isCreated())
    //             .andExpect(content().string(containsString("User registered successfully")));

    //     // Validate user in DB
    //     assert userRepository.findByUsername("newuser").isPresent();
    // }

    // @Test
    // @WithMockUser(username = "anil", roles = "ADMIN")
    // void testUpdateUser() throws Exception {
    //     Long id = normalUser.getId();
    //     System.out.println("testUpdateUser is calling....id=="+id);

    //     String updatePayload = """
    //         {
    //             "username": "john_updated",
    //             "role": "ADMIN"
    //         }
    //         """;
    //     // no need for httpBasic() now
    //     mockMvc.perform(put("/users/" + id)
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(updatePayload))
    //         .andExpect(status().isOk())
    //         .andExpect(content().string("User updated successfully"));

    //     User updated = userRepository.findById(id).orElseThrow();
    //     assert updated.getUsername().equals("john_updated");
    //     assert updated.getRole().equals("ADMIN");
    // }


    // @Test
    // @WithMockUser(username = "anil", roles = {"ADMIN"})
    // void testChangePasswordAsAdmin() throws Exception {
    //     System.out.println("testChangePasswordAsAdmin is calling....");
    //     Long id = normalUser.getId();
    //     String payload = """
    //         {
    //           "newPassword": "newsecret123"
    //         }
    //         """;

    //     mockMvc.perform(put("/users/" + id + "/change-password")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(payload)
    //                     //.with(httpBasic("anil", "mysecret123"))
    //                     )
    //             .andExpect(status().isOk())
    //             .andExpect(content().string(containsString("Password updated successfully")));
    // }

    // @Test
    // @WithMockUser(username = "anil", roles = {"ADMIN"})
    // void testDeleteUser() throws Exception {
    //     System.out.println("testDeleteUser is calling....");
    //     Long id = normalUser.getId();

    //     mockMvc.perform(delete("/users/" + id)
    //                     //.with(httpBasic("anil", "mysecret123"))
    //                     )
    //             .andExpect(status().isOk())
    //             .andExpect(content().string("User deleted successfully"));

    //     assert userRepository.findById(id).isEmpty();
    // }

    // @Test
    // @WithMockUser(username = "anil", roles = {"ADMIN"})
    // void testDeleteSelfShouldFail() throws Exception {
    //     System.out.println("testDeleteSelfShouldFail is calling....");
    //     Long adminId = userRepository.findByUsername("anil").get().getId();

    //     mockMvc.perform(delete("/users/" + adminId))
    //         .andExpect(status().isForbidden()) // ✅ Corrected from isBadRequest()
    //         .andExpect(content().string(containsString("Admins cannot delete their own account.")));
    // }

}
