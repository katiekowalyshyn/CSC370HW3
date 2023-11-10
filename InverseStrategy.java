//package com.byronlai.nickel.logic.strategies;


/*
 * This strategy is used to defeat second-guessing, triple-guessing and so on.
 * Assume the opponent predicts that we will use the given strategy. If the
 * given strategy predicts rock, he will know that our next move will be paper
 * or spock so he will play lizard. Therefore we should play rock or scissors
 * instead.
 */
public class InverseStrategy extends Strategy {
    private Strategy strategy;

    public InverseStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /* Forward the call to the wrapped strategy. */
    @Override
    public void addHistory(Action me, Action opponent) {
        strategy.addHistory(me, opponent);
    }

    /* Return the move that defeats the wrapped strategy. */
    @Override
    protected Action predict() {
        Action[] nextMoves = strategy.getNextMoves();

        for (Action action : Action.values())
            if (action.defeats(nextMoves[0]) && action.defeats(nextMoves[1]))
                return action;

        return Action.ROCK;
    }
}