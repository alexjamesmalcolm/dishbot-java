package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.AccountRepository;
import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import com.alexjamesmalcolm.groupme.response.Group;
import com.alexjamesmalcolm.groupme.response.Me;
import com.alexjamesmalcolm.groupme.response.Member;
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

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private Account account;

    @Mock
    private Me me;

    @Mock
    private Group group;

    @Mock
    private Wheel wheel;

    @Mock
    private Member member;

    @MockBean
    private AccountRepository accountRepo;

    @MockBean
    private GroupMeService groupMe;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(account.getName()).thenReturn("Alex Malcolm");
        when(accountRepo.save(account)).thenReturn(account);
        when(wheel.getGroup()).thenReturn(group);
        when(wheel.getMembers()).thenReturn(singletonList(member));
    }

    @Test
    public void shouldGetSettings() throws Exception {
        long userId = 1278939L;
        String token = "asdf1234";
        when(accountRepo.findByUserId(userId)).thenReturn(Optional.of(account));
        when(groupMe.getMe(token)).thenReturn(me);
        List<Group> groups = singletonList(group);
        when(groupMe.getAllGroups(token)).thenReturn(groups);
        when(account.getWheels()).thenReturn(singletonList(wheel));
        mvc.perform(get("/account/" + userId + "?token=" + token)).andExpect(status().isOk());
    }
}
