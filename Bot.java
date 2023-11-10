//import BothHistoryStrategy;
//import FixedMoveStrategy;
//import FrequencyStrategy;
//import InverseStrategy;
//import OpponentHistoryStrategy;
//import RandomStrategy;
//import ReverseRotationStrategy;
//import RotationStrategy;
//import Strategy;
//import SwapStrategy;
import java.util.ArrayList;

/*
 * A RPSLV bot based on the popular RPS bot Iocaine Powder. It consists of a
 * compilation of strategies and meta-strategies. It chooses from among these
 * strategies based on how well they performed over the last rounds.
 */
public class Bot implements RoShamBot {
    private ArrayList<Strategy> primaryStrategies;
    private ArrayList<Strategy> strategies;
    private int[] scores;

    public Bot() {
        primaryStrategies = new ArrayList<Strategy>();

        /* Add the basic strategies */
        primaryStrategies.add(new FrequencyStrategy());
        primaryStrategies.add(new OpponentHistoryStrategy());
        primaryStrategies.add(new BothHistoryStrategy());
        primaryStrategies.add(new RotationStrategy());
        primaryStrategies.add(new ReverseRotationStrategy());

        /* Assume the opponent uses the same strategies against us */
        primaryStrategies.add(new SwapStrategy(new FrequencyStrategy()));
        primaryStrategies.add(new SwapStrategy(new OpponentHistoryStrategy()));
        primaryStrategies.add(new SwapStrategy(new BothHistoryStrategy()));
        primaryStrategies.add(new SwapStrategy(new RotationStrategy()));
        primaryStrategies.add(new SwapStrategy(new ReverseRotationStrategy()));

        /* Add the fixed-move strategies */
        for (Action shape : Action.values()) {
            primaryStrategies.add(new FixedMoveStrategy(shape));
            primaryStrategies.add(new SwapStrategy(new FixedMoveStrategy(shape)));
        }

        strategies = new ArrayList<Strategy>();

        /* Add the random strategy to avoid a devastating loss */
        strategies.add(new RandomStrategy());

        for (Strategy strategy : primaryStrategies) {
            strategies.add(strategy);

            /* Defeat second-guessing, triple-guessing... */
            for (int i = 0; i < 4; i++) {
                Strategy last = strategies.get(strategies.size() - 1);
                strategies.add(new InverseStrategy(last));
            }
        }

        scores = new int[strategies.size()];
    }

    /* Get our next move using the strategy with the highest score. */
    public Action getNextMove(Action opponent) {
        Action bestMove = Action.ROCK;
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < strategies.size(); i++)
            if (scores[i] > bestScore) {
                bestMove = strategies.get(i).getNextMove();
                bestScore = scores[i];
            }

        return bestMove;
    }

    /* Provide the previous moves made by us and the opponent. */
    public void addHistory(Action me, Action opponent) {
        updateScores(opponent);

        for (Strategy strategy : primaryStrategies)
            strategy.addHistory(me, opponent);
    }

    /*
     * Update the score of each strategy. If a strategy predicted correctly,
     * increment its score. Otherwise reset its score to zero.
     */
    private void updateScores(Action opponent) {
        for (int i = 0; i < strategies.size(); i++) {
            Action[] nextMoves = strategies.get(i).getNextMoves();

            if (nextMoves[0].defeats(opponent) && nextMoves[1].defeats(opponent))
                scores[i]++;
            else
                scores[i] = 0;
        }
    }
}