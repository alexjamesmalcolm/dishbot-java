package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.AccountRepository;
import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.groupme.response.Group;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private Group group;

    @Mock
    private Account account;

    @MockBean
    private AccountRepository accountRepo;

    @MockBean
    private GroupMeService groupMe;

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
        String token = "asdf";
        Collection<Account> accounts = Collections.singletonList(account);
        when(accountRepo.findAllByGroupId(groupId)).thenReturn(accounts);
        when(account.getToken()).thenReturn(token);
        when(groupMe.getGroup(token, groupId)).thenReturn(group);
        mvc.perform(get("/group/" + groupId)).andExpect(status().isOk());
    }
}
