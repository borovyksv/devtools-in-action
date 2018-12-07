package com.example.devtools;

import com.example.devtools.domain.User;
import com.example.devtools.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = DevtoolsApplication.class)
@AutoConfigureMockMvc
public class DevtoolsApplicationIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsersService usersService;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void testCreateUser() throws Exception {
        User user = getNewTestUser("Create");

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.id", notNullValue()));

        User userByEmail = usersService.findByEmail(user.getEmail());
        Assert.assertThat(user.getName(), is(userByEmail.getName()));
    }

    @Test
    public void testRemoveUser() throws Exception {
        User user = getNewTestUser("Remove");
        User savedUser = usersService.save(user);

        mvc.perform(delete("/users")
                .content(mapper.writeValueAsString(savedUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThrows(EntityNotFoundException.class,
                () -> usersService.findById(savedUser.getId()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = getNewTestUser("Update");
        User savedUser = usersService.save(user);
        String newUserName = "NEW NAME";
        savedUser.setName(newUserName);

        mvc.perform(put("/users")
                .content(mapper.writeValueAsString(savedUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newUserName)));
    }

    @Test
    public void testGetUsers() throws Exception {
        int initialSize = usersService.getAllUsers().size();
        User user = getNewTestUser("Get");
        usersService.save(user);


        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(initialSize + 1)));
    }

    private static User getNewTestUser(String name) {
        return new User(name, "last" + name, name + "@mail.com");
    }
}
