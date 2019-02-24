package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, Long> {
}
