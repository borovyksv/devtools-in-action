package com.example.devtools;

import com.example.devtools.domain.User;
import com.example.devtools.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class DevtoolsApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UsersService usersService;

    @Test
    public void testCreateUser() throws Exception {
        User user = getNewTestUser();
        User savedUser = getNewTestUser();
        savedUser.setId(1L);

        given(usersService.save(user)).willReturn(savedUser);

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.id", is(1)));

        verify(usersService, times(1)).save(any(User.class));
    }

    @Test
    public void testRemoveUser() throws Exception {
        User user = getNewTestUser();

        mvc.perform(delete("/users")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(usersService, times(1)).delete(any(User.class));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = getNewTestUser();

        given(usersService.update(user)).willReturn(user);

        mvc.perform(put("/users")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())));

        verify(usersService, times(1)).update(any(User.class));
    }

    @Test
    public void testGetUsers() throws Exception {
        User user = getNewTestUser();
        given(usersService.getAllUsers()).willReturn(Arrays.asList(user));

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(user.getName())));
    }

    private User getNewTestUser() {
        return new User("name", "lastname", "email@mail.com");
    }
}
