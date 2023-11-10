//package com.byronlai.nickel.logic.strategies;


/*
 * This strategy examines the opponent's history to find his most frequent move
 * and predicts that he will choose it.
 */
public class FrequencyStrategy extends Strategy {
    private Action[] Actions;
    private int[] frequencies;

    public FrequencyStrategy() {
        Actions = Action.values();
        frequencies = new int[Actions.length];
    }

    /* Record the frequencies of each kind of move the opponent made */
    @Override
    public void addHistory(Action me, Action opponent) {
        frequencies[opponent.ordinal()]++;
    }

    /*
     * Return the most frequent move as the prediction. Return good old rock if
     * there is no history yet.
     * */
    @Override
    protected Action predict() {
        Action action = Action.ROCK;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < frequencies.length; i++)
            if (frequencies[i] > max) {
                action = Actions[i];
                max = frequencies[i];
            }

        return action;
    }
}