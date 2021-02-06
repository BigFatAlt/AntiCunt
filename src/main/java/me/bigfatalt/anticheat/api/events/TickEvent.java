package me.bigfatalt.anticheat.api.events;

import cc.funkemunky.api.events.AtlasEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TickEvent extends AtlasEvent {
    private int currentTick;
}