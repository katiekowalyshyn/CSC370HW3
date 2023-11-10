//package com.byronlai.nickel.logic.strategies;
//package CSC370HW3;
//import com.byronlai.nickel.logic.Shape;
//import CSC370HW3.Action;

/*
 * This strategy predicts the opponent's next move by finding repeating patterns
 * in the opponent's history and our history. For example, if the last 4 moves
 * we made are rock, paper, scissors and lizard then we find the location in
 * the past when these 4 moves occurred. This strategy assumes that the opponent
 * will play the move after that location.
 */
public class BothHistoryStrategy extends Strategy {
    private class Pair {
        Action me;
        Action opponent;

        public Pair(Action me, Action opponent) {
            this.me = me;
            this.opponent = opponent;
        }

        @Override
        public boolean equals(Object obj) {
            Pair pair = (Pair) obj;
            return me == pair.me && opponent == pair.opponent;
        }
    }

    private History<Pair> history;

    public BothHistoryStrategy() {
        history = new History<Pair>();
    }

    /* Record the moves we and the opponent made in each round. */
    @Override
    public void addHistory(Action me, Action opponent) {
        history.add(new Pair(me, opponent));
    }

    /* Predict by finding repeating patterns in the past. */
    @Override
    protected Action predict() {
        return history.match(new Pair(Action.ROCK, Action.ROCK)).opponent;
    }
}