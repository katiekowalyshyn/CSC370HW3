//package com.byronlai.nickel.logic.strategies;

//import Action;

/*
 * This strategy predicts the opponent's next move by assuming that he will
 * choose his next move by rotating his last move to the left.
 */
public class ReverseRotationStrategy extends Strategy {
    private Action[] actions;
    private Action last;

    public ReverseRotationStrategy() {
        actions = Action.values();
    }

    /* Record the last move made by the opponent. */
    @Override
    public void addHistory(Action me, Action opponent) {
        last = opponent;
    }

    /* Rotate the last move to the left. Return rock if there is no history. */
    @Override
    protected Action predict() {
        if (last == null)
            return Action.ROCK;

        return actions[(last.ordinal() - 1 + actions.length) % actions.length];
    }
}