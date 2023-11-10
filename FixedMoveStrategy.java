//package com.byronlai.nickel.logic.strategies;

/*
 * This strategy assumes that the opponent will always play the same move. This
 * strategy is not very useful when playing against another bot but is helpful
 * when playing against a human because humans often repeat their last moves.
 */
public class FixedMoveStrategy extends Strategy {
    private Action opponent;

    public FixedMoveStrategy(Action opponent) {
        this.opponent = opponent;
    }

    /* This strategy does not use the history. */
    @Override
    public void addHistory(Action me, Action opponent) {}

    /* Always return the same move for the prediction. */
    @Override
    protected Action predict() {
        return opponent;
    }
}