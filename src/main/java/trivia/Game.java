package trivia;

import java.util.ArrayList;
import java.util.List;

public class Game implements IGame {
    private final List<Player> players = new ArrayList<>();
    private final Questions questions = new Questions();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {
    }


    public void add(String playerName) {
        players.add(new Player(playerName));

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    public void roll(int roll) {
        System.out.println(currentPlayer().name() + " is the current player");
        System.out.println("They have rolled a " + roll);
        if (currentPlayer().isInPenaltyBox()) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(currentPlayer().name() + " is getting out of the penalty box");
                currentPlayer().move(roll);

                System.out.println(currentPlayer().name() + "'s new location is " + currentPlayer().place());
                System.out.println("The category is " + questions.currentCategory(currentPlayer().place()).getLabel());
                questions.askQuestion(currentPlayer().place());
            } else {
                System.out.println(currentPlayer().name() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {
            currentPlayer().move(roll);
            System.out.println(currentPlayer().name() + "'s new location is " + currentPlayer().place());
            System.out.println("The category is " + questions.currentCategory(currentPlayer().place()).getLabel());
            questions.askQuestion(currentPlayer().place());
        }

    }

    private Player currentPlayer() {
        return players.get(currentPlayer);
    }

    public boolean wasCorrectlyAnswered() {
        if (currentPlayer().isInPenaltyBox()) {
            if (isGettingOutOfPenaltyBox) {
                // TODO possible bug - exit penalty box? ask the business how to get out of the penalty box
                System.out.println("Answer was correct!!!!");
                currentPlayer().addCoin();
                System.out.println(currentPlayer().name()
                        + " now has "
                        + currentPlayer().coins()
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                moveToNextPlayer();

                return winner;
            } else {
                moveToNextPlayer();
                return true;
            }


        } else {

            System.out.println("Answer was correct!!!!");
            currentPlayer().addCoin();
            System.out.println(currentPlayer().name()
                    + " now has "
                    + currentPlayer().coins()
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            moveToNextPlayer();

            return winner;
        }
    }

    private void moveToNextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().name() + " was sent to the penalty box");
        currentPlayer().moveToPenaltyBox();

        moveToNextPlayer();
        return true;
    }


    private boolean didPlayerWin() {
        return !(currentPlayer().coins() == 6);
    }
}


/**
 * extract method (replace duplicate code)
 * Inline
 * renamed
 * baby steps: don't break compilation at any moment while refactoring
 * Move Method ---> Player.move(roll) -- with invariants
 * switch [expression]: 1) one lines/case, 2) default 3) no extra code in that method
 * Alt-J
 * Any field you create let it be private final at the start. NOT creating getters and setters. If you have to create them, create them individually
 * records Java 16+ - immutable structs
 */