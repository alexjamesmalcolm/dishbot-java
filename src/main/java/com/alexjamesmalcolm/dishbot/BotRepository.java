package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Bot;
import org.springframework.data.repository.CrudRepository;

public interface BotRepository extends CrudRepository<Bot, Long> {
}
