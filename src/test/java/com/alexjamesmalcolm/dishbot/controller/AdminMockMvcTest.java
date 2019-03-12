package com.alexjamesmalcolm.dishbot.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetAllGroupsOk() throws Exception {
        mvc.perform(get("/group")).andExpect(status().isOk());
    }

    @Test
    public void shouldGetIndexOk() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    public void shouldGetSingleGroupOk() throws Exception {
        long groupId = 46707218;
        mvc.perform(get("/group/" + groupId)).andExpect(status().isOk());
    }
}
