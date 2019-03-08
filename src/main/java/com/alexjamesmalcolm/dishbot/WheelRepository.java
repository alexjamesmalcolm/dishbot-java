package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface WheelRepository extends CrudRepository<Wheel, Long> {
    Wheel findByGroupId(long groupId);
    Set<Wheel> findAll();
}
