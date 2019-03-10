package com.alexjamesmalcolm.dishbot.groupme;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class MessageTest {

    String json = "{\"attachments\":[],\"avatar_url\":\"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445\"," +
            "\"created_at\":1545438872,\"group_id\":\"46707218\",\"id\":\"154543887253121474\",\"name\":\"Alex Malcolm\"," +
            "\"sender_id\":\"19742906\",\"sender_type\":\"user\",\"source_guid\":\"b59709300225e65ebbecfb27ad36eb2a\"," +
            "\"system\":false,\"text\":\"test\",\"user_id\":\"19742906\"}";
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldGetNameAsAlexMalcolm() throws IOException {
        String expected = "Alex Malcolm";
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getName(), is(expected));
    }

    @Test
    public void shouldGetAvatarUrl() throws IOException {
        String expected = "https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445";
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getAvatarUrl(), is(URI.create(expected)));
    }

    @Test
    public void shouldGetCreatedAt() throws IOException {
        Instant expected = Instant.ofEpochMilli(1545438872);
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getCreatedAt(), is(expected));
    }

    @Test
    public void shouldGetGroupId() throws IOException {
        Long expected = 46707218L;
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getGroupId(), is(expected));
    }

    @Test
    public void shouldGetId() throws IOException {
        Long expected = 154543887253121474L;
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getId(), is(expected));
    }

    @Test
    public void shouldGetSenderId() throws IOException {
        Long expected = 19742906L;
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getSenderId(), is(expected));
    }

    @Test
    public void shouldGetSenderType() throws IOException {
        String expected = "user";
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getSenderType(), is(expected));
    }

    @Test
    public void shouldGetSourceGuid() throws IOException {
        String expected = "b59709300225e65ebbecfb27ad36eb2a";
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getSourceGuid(), is(expected));
    }

    @Test
    public void shouldGetSystem() throws IOException {
        Boolean expected = false;
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getSystem(), is(expected));
    }

    @Test
    public void shouldGetText() throws IOException {
        String expected = "test";
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getText(), is(expected));
    }

    @Test
    public void shouldGetUserid() throws IOException {
        Long expected = 19742906L;
        Message message = mapper.readValue(json, Message.class);
        assertThat(message.getUserId(), is(expected));
    }

}
