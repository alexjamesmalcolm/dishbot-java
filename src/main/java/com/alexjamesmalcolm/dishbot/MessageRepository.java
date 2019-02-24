package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
