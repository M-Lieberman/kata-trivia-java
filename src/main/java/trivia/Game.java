package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game implements IGame {
    private List<Player> players = new ArrayList<>();


    private final Questions questions = new Questions();
    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {


    }
    // static/nested class
    static class Questions {
        private final List<String> popQuestions = new LinkedList<>();
        private final List<String> scienceQuestions = new LinkedList<>();
        private final List<String> sportsQuestions = new LinkedList<>();
        private final List<String> rockQuestions = new LinkedList<>();

        public Questions() {
            for (int i = 0; i < 50; i++) {
                popQuestions.add("Pop Question " + i);
                scienceQuestions.add(("Science Question " + i));
                sportsQuestions.add(("Sports Question " + i));
                rockQuestions.add(createRockQuestion(i));
            }
        }

        private void askQuestion(int index) {
            System.out.println(extractNextQuestion(index));
        }


        private String extractNextQuestion(int index) {
            return switch (currentCategory(index)) {
                case POP -> popQuestions.remove(0);
                case SCIENCE -> scienceQuestions.remove(0);
                case SPORTS -> sportsQuestions.remove(0);
                case ROCK -> rockQuestions.remove(0);
                default -> throw new IllegalStateException("Unexpected value: " + currentCategory(index).getLabel());
            };
        }

        private QuestionCategory currentCategory(int index) {
            return switch (index % 4) {
                case 0 -> QuestionCategory.POP;
                case 1 -> QuestionCategory.SCIENCE;
                case 2 -> QuestionCategory.SPORTS;
                case 3 -> QuestionCategory.ROCK;
                default -> throw new IllegalStateException("Unexpected value: " + index % 4);
            };
        }

        public String createRockQuestion(int index) {
            return "Rock Question " + index;
        }

        enum QuestionCategory {
            POP("Pop"),
            SCIENCE("Science"),
            SPORTS("Sports"),
            ROCK("Rock");

            private final String label;

            QuestionCategory(String label) {
                this.label = label;
            }

            public String getLabel() {
                return label;
            }
        }
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
                movePlayer(roll);

                System.out.println(currentPlayer().name() + "'s new location is " + currentPlayer().place());
                System.out.println("The category is " + questions.currentCategory(currentPlayer().place()).getLabel());
                questions.askQuestion(currentPlayer().place());
            } else {
                System.out.println(currentPlayer().name() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {
            movePlayer(roll);
            System.out.println(currentPlayer().name() + "'s new location is " + currentPlayer().place());
            System.out.println("The category is " + questions.currentCategory(currentPlayer().place()).getLabel());
            questions.askQuestion(currentPlayer().place());
        }

    }

    private void movePlayer(int roll) {
        currentPlayer().move(roll);
    }


    private Player currentPlayer() {
        return players.get(currentPlayer);
    }

    public boolean wasCorrectlyAnswered() {
        // TODO possible bug - exit penalty box?
        if (currentPlayer().isInPenaltyBox()) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
                currentPlayer().addCoin();
                System.out.println(currentPlayer().name()
                        + " now has "
                        + currentPlayer().coins()
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;

                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;
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
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        }
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().name() + " was sent to the penalty box");
        currentPlayer().moveToPenaltyBox();

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
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
 * Any field you create let it be private final at the start. NOT creating gettters and setters. If you have to create them, create them individually
 * records Java 16+ - immutable structs
 */