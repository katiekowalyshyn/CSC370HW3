//package com.byronlai.nickel.logic.strategies;

//import Action;

/*
 * Assume the opponent uses the given strategy against us. This strategy
 * exchanges the position of us and our opponent. If the given strategy predicts
 * that we will play rock, the opponent will play paper or spock so we should
 * play lizard instead.
 */
public class SwapStrategy extends Strategy {
    private Strategy strategy;

    public SwapStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /* Exchange the position of us and the opponent. */
    @Override
    public void addHistory(Action me, Action opponent) {
        strategy.addHistory(opponent, me);
    }

    /* This operation is not supported. */
    @Override
    protected Action predict() {
        throw new UnsupportedOperationException();
    }

    /*
     * Assume the opponent uses the given strategy against us. Return moves that
     * defeat that strategy.
     */
    @Override
    public Action[] getNextMoves() {
        Action[] opponentNextMoves = strategy.getNextMoves();
        Action[] nextMoves = new Action[2];

        for (Action action : Action.values())
            if (action.defeats(opponentNextMoves[0]) && action.defeats(opponentNextMoves[1]))
                nextMoves[0] = nextMoves[1] = action;

        return nextMoves;
    }
}