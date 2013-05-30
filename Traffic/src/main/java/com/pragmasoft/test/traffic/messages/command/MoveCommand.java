package com.pragmasoft.test.traffic.messages.command;

import com.pragmasoft.test.traffic.data.Move;

import java.util.List;

public class MoveCommand implements CommandMessage<List<Move>> {
    private final List<Move> moves;

    public MoveCommand(List<Move> moves) {
        this.moves = moves;
    }

    @Override
    public CommandEnum command() {
        return CommandEnum.MOVE;
    }

    @Override
    public List<Move> getPayload() {
        return moves;
    }
}
