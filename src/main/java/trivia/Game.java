package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game implements IGame {
   private List<Player> players = new ArrayList<>();
   boolean[] inPenaltyBox = new boolean[6];
   int[] purses = new int[6];

   List<String> popQuestions = new LinkedList<>();
   List<String> scienceQuestions = new LinkedList<>();
   List<String> sportsQuestions = new LinkedList<>();
   List<String> rockQuestions = new LinkedList<>();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
      for (int i = 0; i < 50; i++) {
         popQuestions.add("Pop Question " + i);
         scienceQuestions.add(("Science Question " + i));
         sportsQuestions.add(("Sports Question " + i));
         rockQuestions.add(createRockQuestion(i));
      }
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

   public boolean add(String playerName) {
      players.add(new Player(playerName));
      purses[howManyPlayers()] = 0;
      inPenaltyBox[howManyPlayers()] = false;

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {
      System.out.println(currentPlayer().name() + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (currentPlayer().isInPenaltyBox()) {
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            System.out.println(currentPlayer().name() + " is getting out of the penalty box");
            movePlayer(roll);

            System.out.println(currentPlayer().name()
                               + "'s new location is "
                               + currentPlayer().place());
            System.out.println("The category is " + currentCategory());
            askQuestion();
         } else {
            System.out.println(currentPlayer().name() + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {
         movePlayer(roll);
         System.out.println(currentPlayer().name()
                            + "'s new location is "
                            + currentPlayer().place());
         System.out.println("The category is " + currentCategory());
         askQuestion();
      }

   }

   private void movePlayer(int roll) {
      currentPlayer().move(roll);
   }

   private void askQuestion() {
      System.out.println(extractNextQuestion());
   }

   private String extractNextQuestion() {
      return switch (currentCategory()) {
         case "Pop" -> popQuestions.remove(0);
         case "Science" -> scienceQuestions.remove(0);
         case "Sports" -> sportsQuestions.remove(0);
         case "Rock" -> rockQuestions.remove(0);
         default -> throw new IllegalStateException("Unexpected value: " + currentCategory());
      };
   }


   private String currentCategory() {
      if (currentPlayer().place() == 0) return "Pop";
      if (currentPlayer().place() == 4) return "Pop";
      if (currentPlayer().place() == 8) return "Pop";
      if (currentPlayer().place() == 1) return "Science";
      if (currentPlayer().place() == 5) return "Science";
      if (currentPlayer().place() == 9) return "Science";
      if (currentPlayer().place() == 2) return "Sports";
      if (currentPlayer().place() == 6) return "Sports";
      if (currentPlayer().place() == 10) return "Sports";
      return "Rock";
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

         System.out.println("Answer was corrent!!!!");
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