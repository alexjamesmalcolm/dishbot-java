package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Group;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminControllerMockTest {

    @InjectMocks
    private AdminController underTest;

    @Mock
    private Model model;

    @Mock
    private Group group;

    @Mock
    private Group anotherGroup;

    @Mock
    private GroupRepository groupRepo;

    private List<Group> groups = new ArrayList<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(groupRepo.findAll()).thenReturn(groups);
    }

    @Test
    public void shouldDisplayHomePage() {
        String expected = "home";
        String resource = underTest.displayHomePage();
        assertThat(resource, is(expected));
    }

    @Test
    public void shouldDisplayHelpMeSetUpBotPage() {
        String expected = "setup";
        String resource = underTest.displaySetupPage();
        assertThat(resource, is(expected));
    }

    @Test
    public void shouldDisplayManageGroupPage() {
        String expected = "group";
        Long groupId = 1L;
        when(groupRepo.findById(groupId)).thenReturn(Optional.of(group));
        String resource = underTest.displayManageGroupPage(model, groupId);
        assertThat(resource, is(expected));
    }

    @Test
    public void shouldAddGroupToModelWhenGettingManageGroupPage() {
        Long groupId = 1L;
        when(groupRepo.findById(groupId)).thenReturn(Optional.of(group));
        underTest.displayManageGroupPage(model, groupId);
        verify(model).addAttribute("group", group);
    }

    @Test
    public void shouldDisplayAllGroupPage() {
        String expected = "group-all";
        String resource = underTest.displayAllGroupsPage(model);
        assertThat(resource, is(expected));
    }

    @Test
    public void shouldAttachAllGroupsToModelWhenGettingAllGroupsPage() {
        underTest.displayAllGroupsPage(model);
        verify(model).addAttribute("groups", groups);
    }

    @Test
    public void shouldAttachTwoGroupsToModelWhenGettingAllGroupsPage() {
        groups.add(group);
        groups.add(anotherGroup);
        underTest.displayAllGroupsPage(model);
        verify(model).addAttribute("groups", groups);
    }
}
