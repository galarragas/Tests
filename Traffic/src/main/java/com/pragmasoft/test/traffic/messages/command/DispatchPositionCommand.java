package com.pragmasoft.test.traffic.messages.command;

import com.pragmasoft.test.traffic.data.Move;

public class DispatchPositionCommand implements CommandMessage<Move> {
    private final Move move;

    public DispatchPositionCommand(Move move) {
        this.move = move;
    }

    @Override
    public CommandEnum command() {
        return CommandEnum.DISPATCH;
    }

    @Override
    public Move getPayload() {
        return move;
    }

    @Override
    public String toString() {
        return "DispatchPositionCommand{" +
                "move=" + move +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DispatchPositionCommand that = (DispatchPositionCommand) o;

        if (move != null ? !move.equals(that.move) : that.move != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return move != null ? move.hashCode() : 0;
    }
}
