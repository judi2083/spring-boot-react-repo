package com.example.springboot.controller;

import com.example.springboot.config.TestSecurityConfig;
import com.example.springboot.entity.Employee;
import com.example.springboot.repository.EmployeeRepository;
import com.example.springboot.service.MailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.sql.init.mode=never",
    "spring.jpa.show-sql=true",
    "spring.jpa.properties.hibernate.format_sql=true",
    "logging.level.org.hibernate.SQL=DEBUG",
    "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"

})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class EmployeeControllerTest {

    @MockBean
    private MailService mailService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee emp1;
    private Employee emp2;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();

        emp1 = new Employee(null, "John", "Doe", "john.doe@example.com", "Engineering", "Developer");
        emp2 = new Employee(null, "Alice", "Smith", "alice.smith@example.com", "QA", "Tester");

        employeeRepository.saveAll(List.of(emp1, emp2));
    }

    @Test
    @WithMockUser(username = "anil", roles = {"ADMIN"})
    void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/employees"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].firstName", is("John"))); 
    }

    @Test
    @WithMockUser(username = "anil", roles = {"ADMIN"})
    void testCreateEmployee() throws Exception {
        String payload = """
            {
                "firstName": "Bob",
                "lastName": "Marley",
                "email": "bob.marley@example.com",
                "department": "Management",
                "role": "Manager"
            }
            """;

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email", is("bob.marley@example.com")));

        assert employeeRepository.findByEmail("bob.marley@example.com").isPresent();
    }

    @Test
    @WithMockUser(username = "anil", roles = {"ADMIN"})
    void testUpdateEmployee() throws Exception {
        Long id = emp1.getId();

       String payload = String.format("""
        {
            "id": %d,
            "firstName": "John",
            "lastName": "Doe",
            "email": "john.updated@example.com",
            "department": "Engineering",
            "role": "Lead Developer"
        }
        """, id);


        mockMvc.perform(put("/employees/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andDo(print()) // ← shows full stack trace and response
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email", is("john.updated@example.com")));

        Employee updated = employeeRepository.findById(id).orElseThrow();
        assert updated.getEmail().equals("john.updated@example.com");
        assert updated.getRole().equals("Lead Developer");
    }


    @Test
    @WithMockUser(username = "anil", roles = {"ADMIN"})
    void testDeleteEmployee() throws Exception {
        Long id = emp2.getId();

        mockMvc.perform(delete("/employees/" + id))
            .andExpect(status().isNoContent());
            //.andExpect(content().string(containsString("Employee deleted successfully")));

        assert employeeRepository.findById(id).isEmpty();
    }
}
