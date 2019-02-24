package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
