import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MyBot implements RoShamBot {

    @Override
    public Action getNextMove(Action lastOpponentMove) {
        if (lastOpponentMove == null) {
            // Initial move, return a random action
            Action[] actions = Action.values();
            return actions[(int) (Math.random() * actions.length)];
        }

        String myHistory = "RPSLV"; // Define your own history string here
        String otherHistory = "RPSLV"; // Define your opponent's history string here

        double rScore, pScore, sScore, lScore, vScore;
        rScore = pScore = sScore = lScore = vScore = 0;
        lScore = 0.001;
        String myUsableHistory = myHistory;
        String otherUsableHistory = otherHistory;
        if (myHistory.length() > 4) {
            int startIndex = findLastInstanceOfXLosses(myHistory, otherHistory, 3);
            myUsableHistory = myHistory.substring(startIndex);
            otherUsableHistory = otherHistory.substring(startIndex);
        }

        if (lostLastXRounds(myHistory, otherHistory) >= 1) {
            Action[] randLetter = new Action[]{Action.ROCK, Action.PAPER, Action.SCISSORS, Action.LIZARD, Action.SPOCK};
            return randLetter[(int) (Math.random() * 5)];
        }

        ArrayList<ArrayList<Integer> > moveHits = new ArrayList<ArrayList<Integer> >();
        for (int g = 0; g < 2; g++) {
            for (int i = 1; i < (myUsableHistory.length() / 2) + 1; i++) {
                if (g == 0) {
                    moveHits.add(findAll(myUsableHistory.substring(myUsableHistory.length() - i), myUsableHistory));
                } else {
                    moveHits.add(findAll(otherUsableHistory.substring(otherUsableHistory.length() - i), otherUsableHistory));
                }
            }
            for (int i = 0; i < moveHits.size(); i++) {
                int matchingMoves = i + 1;
                ArrayList<Integer> moveIndexes = moveHits.get(i);
                for (Integer index : moveIndexes) {
                    if (index + matchingMoves + 1 <= otherUsableHistory.length()) {
                        char nextMove = otherUsableHistory.charAt(index + matchingMoves - 1);
                        if (nextMove == 'R') {
                            rScore = rScore + matchingMoves;
                        }
                        if (nextMove == 'P') {
                            pScore = pScore + matchingMoves;
                        }
                        if (nextMove == 'S') {
                            sScore = sScore + matchingMoves;
                        }
                        if (nextMove == 'L') {
                            lScore = lScore + matchingMoves;
                        }
                        if (nextMove == 'V') {
                            vScore = vScore + matchingMoves;
                        }
                    }
                }
            }
        }

        HashMap<Character, Double> scores = new HashMap<Character, Double>();
        scores.put('R', rScore);
        scores.put('P', pScore);
        scores.put('S', sScore);
        scores.put('L', lScore);
        scores.put('V', vScore);
        ArrayList<Character> winners = orderHashMap(scores);
        char number1Char = winners.get(winners.size() - 1);
        char number2Char = winners.get(winners.size() - 2);
        
        Action number1;
        Action number2;

        if (number1Char == 'R') {
            number1 = Action.ROCK;
        } else if (number1Char == 'P') {
            number1 = Action.PAPER;
        } else if (number1Char == 'S') {
            number1 = Action.SCISSORS;
        } else if (number1Char == 'L') {
            number1 = Action.LIZARD;
        } else {
            number1 = Action.SPOCK;
        }

        if (number2Char == 'R') {
            number2 = Action.ROCK;
        } else if (number2Char == 'P') {
            number2 = Action.PAPER;
        } else if (number2Char == 'S') {
            number2 = Action.SCISSORS;
        } else if (number2Char == 'L') {
            number2 = Action.LIZARD;
        } else {
            number2 = Action.SPOCK;
        }

        Action charToPlay = beatsMove(number1, number2);
        
        // Return the Action enum directly
        return charToPlay;
    }

    public static int findLastInstanceOfXLosses(String myHistory, String otherHistory, int losses) {
        int index = myHistory.length();
        while (lostLastXRounds(myHistory.substring(0, index), otherHistory.substring(0, index)) < losses && index > 1) {
            index--;
        }
        return index;
    }

    public static ArrayList<Character> orderHashMap(HashMap<Character, Double> scores) {
        ArrayList<Double> orderedScores = new ArrayList<Double>();
        orderedScores.add(scores.get('R'));
        orderedScores.add(scores.get('P'));
        orderedScores.add(scores.get('S'));
        orderedScores.add(scores.get('L'));
        orderedScores.add(scores.get('V'));
        Collections.sort(orderedScores);

        ArrayList<Character> orderedKeys = new ArrayList<Character>();
        for (Double value : orderedScores) {
            for (Character key : scores.keySet()) {
                if (value.equals(scores.get(key))) {
                    orderedKeys.add(key);
                }
            }
        }
        return orderedKeys;
    }

    public static ArrayList<Integer> findAll(String substring, String realString) {
        ArrayList<Integer> occurrences = new ArrayList<Integer>();
        Integer index = realString.indexOf(substring);
        if (index == -1) {
            return occurrences;
        }
        occurrences.add(index + 1);
        while (index != -1) {
            index = realString.indexOf(substring, index + 1);
            if (index != -1) {
                occurrences.add(index + 1);
            }
        }
        return occurrences;
    }

    public static int lostLastXRounds(String myHistory, String otherHistory) {
        int index = myHistory.length() - 1;
        int lost = 0;
        while (ifCharBeatsOther(otherHistory.charAt(index), myHistory.charAt(index))) {
            index--;
            lost++;
            if (index == -1) {
                break;
            }
        }
        return lost;
    }

    public static boolean ifCharBeatsOther(char char1, char char2) {
    if (char1 == 'R') {
        return char2 == 'S' || char2 == 'L';
    } else if (char1 == 'P') {
        return char2 == 'R' || char2 == 'V';
    } else if (char1 == 'S') {
        return char2 == 'P' || char2 == 'L';
    } else if (char1 == 'L') {
        return char2 == 'P' || char2 == 'V';
    } else if (char1 == 'V') {
        return char2 == 'R' || char2 == 'S';
    }
    // If char1 is not a valid character or doesn't beat char2, return false
    return false;
}

public static Action beatsMove(Action moveToBeBeaten, Action runnerUp) {
    if (moveToBeBeaten == Action.ROCK) {
        if (runnerUp == Action.SCISSORS || runnerUp == Action.LIZARD) {
            return Action.ROCK;
        } else {
            return Action.SPOCK;
        }
    } else if (moveToBeBeaten == Action.PAPER) {
        if (runnerUp == Action.ROCK || runnerUp == Action.SPOCK) {
            return Action.PAPER;
        } else {
            return Action.LIZARD;
        }
    } else if (moveToBeBeaten == Action.SCISSORS) {
        if (runnerUp == Action.PAPER || runnerUp == Action.LIZARD) {
            return Action.SCISSORS;
        } else {
            return Action.SPOCK;
        }
    } else if (moveToBeBeaten == Action.LIZARD) {
        if (runnerUp == Action.SPOCK || runnerUp == Action.PAPER) {
            return Action.LIZARD;
        } else {
            return Action.ROCK;
        }
    } else if (moveToBeBeaten == Action.SPOCK) {
        if (runnerUp == Action.SCISSORS || runnerUp == Action.ROCK) {
            return Action.SPOCK;
        } else {
            return Action.PAPER;
        }
    }
    // Return a default action if none of the above conditions match (for example, when input is invalid)
    return Action.ROCK;
    }
}