package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.exception.BotMessageException;
import com.alexjamesmalcolm.dishbot.exception.SystemMessageException;
import com.alexjamesmalcolm.dishbot.physical.Group;
import com.alexjamesmalcolm.dishbot.physical.Message;
import com.alexjamesmalcolm.dishbot.physical.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;

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
    private URL avatar_url;

    @Test
    public void shouldSaveMessage() throws SystemMessageException, BotMessageException {
        String json = "{\"attachments\":[],\"avatar_url\":\"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445\",\"created_at\":1545438872,\"group_id\":\"46707218\",\"id\":\"154543887253121474\",\"name\":\"Alex Malcolm\",\"sender_id\":\"19742906\",\"sender_type\":\"user\",\"source_guid\":\"b59709300225e65ebbecfb27ad36eb2a\",\"system\":false,\"text\":\"test\",\"user_id\":\"19742906\"}";
        Message message = new Message(json);
        long id = messageRepo.save(message).getId();
        em.flush();
        em.clear();
        message = messageRepo.findById(id).get();
    }

    @Test
    public void shouldAttachMessageToUser() throws SystemMessageException, BotMessageException {
        long userId = 19742906;
        String name = "Alex Malcolm";
        userRepo.save(new User(name, userId, avatar_url));
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
    public void shouldAttachTwoMessagesToUser() throws SystemMessageException, BotMessageException {
        long userId = 19742906;
        String name = "Alex Malcolm";
        userRepo.save(new User(name, userId, avatar_url));
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
    public void shouldAttachUserToGroupIfUserMessagesInGroup() throws SystemMessageException, BotMessageException {
        long userId = 19742906;
        String name = "Alex Malcolm";
        userRepo.save(new User(name, userId, avatar_url));
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

    @Test
    public void shouldAttachGroupToUser() throws SystemMessageException, BotMessageException {
        long userId = 19742906;
        String name = "Alex Malcolm";
        userRepo.save(new User(name, userId, avatar_url));
        em.flush();
        em.clear();
        String json = "{\"attachments\":[],\"avatar_url\":\"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445\",\"created_at\":1545438872,\"group_id\":\"46707218\",\"id\":\"154543887253121474\",\"name\":\"Alex Malcolm\",\"sender_id\":\"19742906\",\"sender_type\":\"user\",\"source_guid\":\"b59709300225e65ebbecfb27ad36eb2a\",\"system\":false,\"text\":\"test\",\"user_id\":\"19742906\"}";
        Message message = new Message(json);
        messageRepo.save(message);
        em.flush();
        em.clear();
        long groupId = 46707218;
        User user = userRepo.findById(userId).get();
        boolean hasUser = user.getGroups().stream().anyMatch(group -> group.getId() == groupId);
        assertTrue(hasUser);
    }

    @Test(expected = BotMessageException.class)
    public void shouldThrowBotMessageException() throws SystemMessageException, BotMessageException {
        String json = "{\"attachments\":[],\"avatar_url\":null,\"created_at\":1545542775,\"group_id\":\"46707218\",\"id\":\"154554277559043579\",\"name\":\"Test bot\",\"sender_id\":\"752564\",\"sender_type\":\"bot\",\"source_guid\":\"313e09e0e8a1013627de22000bf8e41a\",\"system\":false,\"text\":\"test\",\"user_id\":\"752564\"}";
        Message message = new Message(json);
    }
}
