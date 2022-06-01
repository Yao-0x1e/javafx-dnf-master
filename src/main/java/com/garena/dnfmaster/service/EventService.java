package com.garena.dnfmaster.service;

import com.garena.dnfmaster.annotation.DatabaseRequired;
import com.garena.dnfmaster.database.TriggerManager;
import com.garena.dnfmaster.mapper.EventLogMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private TriggerManager triggerManager;
    @Autowired
    private EventLogMapper eventLogMapper;

    @DatabaseRequired
    @Transactional
    public void unlimitCharacterCreation() {
        triggerManager.deleteCharacterCreationTrigger();
        triggerManager.createCharacterCreationTrigger();
    }

    @DatabaseRequired
    @SneakyThrows
    public void enableMultipleDrops() {
        List<Integer> eventTypes = eventLogMapper.findAllEventTypes();
        if (eventTypes.contains(7)) {
            throw new Exception("多倍掉落活动已经存在");
        }
        eventLogMapper.insert(7, 2000, 0);
    }

    @DatabaseRequired
    @SneakyThrows
    public void unlimitFatiguePoint() {
        List<Integer> eventTypes = eventLogMapper.findAllEventTypes();
        if (eventTypes.contains(1)) {
            throw new Exception("无限疲劳活动已经存在");
        }
        eventLogMapper.insert(1, 1, 0);
    }
}
