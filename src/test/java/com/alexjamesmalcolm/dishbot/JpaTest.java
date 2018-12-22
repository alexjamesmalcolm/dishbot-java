package com.alexjamesmalcolm.dishbot;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaTest {

    @Resource
    private TestEntityManager em;

    @Resource
    private MessageRepository messageRepo;

    @Resource
    private UserRepository userRepo;

    @Resource
    private GroupRepository groupRepo;

    @Test
    public void shouldSaveMessage() throws IOException, SystemMessageException {
        String json = "{\"attachments\":[],\"avatar_url\":\"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445\",\"created_at\":1545438872,\"group_id\":\"46707218\",\"id\":\"154543887253121474\",\"name\":\"Alex Malcolm\",\"sender_id\":\"19742906\",\"sender_type\":\"user\",\"source_guid\":\"b59709300225e65ebbecfb27ad36eb2a\",\"system\":false,\"text\":\"test\",\"user_id\":\"19742906\"}";
        Message message = new Message(json);
        long id = messageRepo.save(message).getId();
        em.flush();
        em.clear();
        message = messageRepo.findById(id).get();
    }

    @Test
    public void shouldAttachMessageToUser() throws MalformedURLException, SystemMessageException {
        long userId = 19742906;
        String name = "Alex Malcolm";
        userRepo.save(new User(name, userId));
        em.flush();
        em.clear();
        String json = "{\"attachments\":[],\"avatar_url\":\"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445\",\"created_at\":1545438872,\"group_id\":\"46707218\",\"id\":\"154543887253121474\",\"name\":\"Alex Malcolm\",\"sender_id\":\"19742906\",\"sender_type\":\"user\",\"source_guid\":\"b59709300225e65ebbecfb27ad36eb2a\",\"system\":false,\"text\":\"test\",\"user_id\":\"19742906\"}";
        Message message = new Message(json);
        messageRepo.save(message);
        em.flush();
        em.clear();
        User user = userRepo.findById(userId).get();
        Collection<Message> messages = user.getMessages();
        Assert.assertThat(messages, hasSize(1));
    }

    @Test
    public void shouldAttachTwoMessagesToUser() throws MalformedURLException, SystemMessageException {
        long userId = 19742906;
        String name = "Alex Malcolm";
        userRepo.save(new User(name, userId));
        em.flush();
        em.clear();
        String json = "{\"attachments\":[],\"avatar_url\":\"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445\",\"created_at\":1545438872,\"group_id\":\"46707218\",\"id\":\"154543887253121474\",\"name\":\"Alex Malcolm\",\"sender_id\":\"19742906\",\"sender_type\":\"user\",\"source_guid\":\"b59709300225e65ebbecfb27ad36eb2a\",\"system\":false,\"text\":\"test\",\"user_id\":\"19742906\"}";
        Message message = new Message(json);
        messageRepo.save(message);
        em.flush();
        em.clear();
        String secondJson = "{\"attachments\":[],\"avatar_url\":\"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445\",\"created_at\":1545514587,\"group_id\":\"46707218\",\"id\":\"154551458770040552\",\"name\":\"Alex Malcolm\",\"sender_id\":\"19742906\",\"sender_type\":\"user\",\"source_guid\":\"99bf0f1b4467104764bd7c0a4e79e70d\",\"system\":false,\"text\":\"asdf\",\"user_id\":\"19742906\"}";
        messageRepo.save(new Message(secondJson));
        em.flush();
        em.clear();
        User user = userRepo.findById(userId).get();
        Collection<Message> messages = user.getMessages();
        Assert.assertThat(messages, hasSize(2));
    }

    @Test
    public void shouldAttachUserToGroupIfUserMessagesInGroup() throws MalformedURLException, SystemMessageException {
        long userId = 19742906;
        String name = "Alex Malcolm";
        userRepo.save(new User(name, userId));
        em.flush();
        em.clear();
        String json = "{\"attachments\":[],\"avatar_url\":\"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445\",\"created_at\":1545438872,\"group_id\":\"46707218\",\"id\":\"154543887253121474\",\"name\":\"Alex Malcolm\",\"sender_id\":\"19742906\",\"sender_type\":\"user\",\"source_guid\":\"b59709300225e65ebbecfb27ad36eb2a\",\"system\":false,\"text\":\"test\",\"user_id\":\"19742906\"}";
        Message message = new Message(json);
        messageRepo.save(message);
        em.flush();
        em.clear();
        long groupId = 46707218;
        Group group = groupRepo.findById(groupId).get();
        boolean hasUser = group.getUsers().stream().anyMatch(user -> user.getId() == userId);
        assertTrue(hasUser);
    }
}
