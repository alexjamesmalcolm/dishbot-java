package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.springframework.data.repository.CrudRepository;

public interface WheelRepository extends CrudRepository<Wheel, Long> {
    Wheel findByGroupId(long groupId);
}
