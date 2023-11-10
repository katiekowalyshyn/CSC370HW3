//package com.byronlai.nickel.logic.strategies;

//import Action;
import java.util.Random;

/*
 * This strategy predicts the opponent's next move by randomly choosing a Action.
 * When combining with SwapStrategy, this strategy can prevent us from having a
 * devastating loss because there is no way to guess our next move if we choose
 * at random.
 */
public class RandomStrategy extends Strategy {
    private Action[] actions;
    private Random random;

    public RandomStrategy() {
        actions = Action.values();
        random = new Random();
    }

    /* This strategy does not use the history. */
    @Override
    public void addHistory(Action me, Action opponent) {}

    /* Return a move randomly. */
    @Override
    protected Action predict() {
        return actions[random.nextInt(actions.length)];
    }
}