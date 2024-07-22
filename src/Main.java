// INFO:
// - You are first given the rules, and then asked how many real/computer players should be in the game.
// - Computer players are set to hard by default (because otherwise they are too easy)
// BUG: if two players have the same name, it will cause a bug where both names are highlighted at the same time
import java.util.*;
class Main {
  public static void main(String[] args) throws InterruptedException {

    String rules = "The goal of the game is to be the first to reach a total score of 100. On your turn, roll the dice as many times as you like. The sum of your two dice will be added to your total. You may decide to stop whenever you like and let the next player start rolling.\n\nThe catch: Everytime you roll a one on either die, your turn ends and you score zero for the round. If you roll two ones, your turn ends and your total score goes to zero! OUCH!!\n\nYou will be asked how many real players there are, and how many computer players you want (if any). Enjoy!";

    Dice d1 = new Dice(6);
    Dice d2 = new Dice(6);
    int maxRealPlayers = 500000, maxComPlayers = 500000; // max possible players of each type is 500,000
    // max TOTAL players is 1,000,000 (500k real + 500k COM)
    int pointsGoal = 100;
    int numRealPlayers = 0, numComPlayers = 0;
    Player winner = new Player();
    Scanner scan = new Scanner(System.in);
    
    println(Text.CLEAR + "Let's Play Two Dice Pig!\n\n" + rules + "\n");
    waitToContinue();

    while (true) { // ask for number of real players
      print(Text.CLEAR + "Number of human players: ");
      String input = scan.nextLine().trim();
      try {
        numRealPlayers = Integer.parseInt(input);
        numComPlayers = numRealPlayers == 1 ? 1 : numComPlayers; // if there's only one player, a computer player is added
        if (numRealPlayers < 1 || numRealPlayers > maxRealPlayers) {int e=0/0;} // forced error
        break;
      } catch (Exception e) {
        if (numRealPlayers > maxRealPlayers) {
          Text.showError("Too many players\n", 1000);
        } else if (!input.isEmpty()) {
          Text.showError("Invalid number\n", 1000);
        }
      }
    } // end of while loop

    while (numRealPlayers > 1) { // ask for number of computer players
      print(Text.CLEAR + "Number of human players: " + numRealPlayers + "\n" + "Number of computer players (0 for none): ");
      String input = scan.nextLine().trim();
      try {
        numComPlayers = Integer.parseInt(input);
        if (numComPlayers < 0 || numComPlayers > maxComPlayers) {int e=0/0;} // forced error
        break;
      } catch (Exception e) {
        if (numComPlayers > maxComPlayers) {
          Text.showError("Too many players\n", 1000);
        } else if (!input.isEmpty()) {
          Text.showError("Invalid number\n", 1000);
        }
      }
    } // end of while loop

    Player[] players = new Player[numRealPlayers + numComPlayers];
    
    boolean useCustomNames = false;
    while (true) {
      print(Text.CLEAR);
      if (numRealPlayers > 15) {
        Text.warning("There are " + numRealPlayers + " players to name. It's recommended NOT to use custom names.\n");
      }
      print("Use custom names? (y/n): ");
      String input = scan.nextLine().toLowerCase().trim();
      if (input.matches("^y$|^yes$") || input.matches("^n$|^no$")) {
        useCustomNames = input.matches("^y$|^yes$");
        break;
      }
    }
    
    for (int i=0; i < numRealPlayers; i++) {
      if (useCustomNames) {
        while (true) {
          System.out.print(Text.CLEAR + "Player " + (i+1) + " name: ");
          String name = scan.nextLine().trim();
          if (!name.isEmpty()) {
            players[i] = new Player(name);
            break;
          }
        }
      } else {
        players[i] = new Player("Player "+(i+1));
      }
    }
    print("\nGame Starting...");

    for (int i=0; i < numComPlayers; i++) {
      players[numRealPlayers + i] = new ComPlayer(numRealPlayers+i+1);
      //if (isHard) { ((ComPlayer)players[numRealPlayers + i]).setHardSetting(true); }
    }
    
    String playerNames = "";
    if (players.length <= 4) {
      for (int i=0; i < players.length; i++) {
        playerNames += ("  " + players[i].getName());
      }
      playerNames += " ";
    }

    // Game round loop
    int round = 0;
    while (!gameOver(players,pointsGoal)) {
      round++;
      // Player turn loop
      for (int i=0; i < players.length && !gameOver(players,pointsGoal); i++) { 
        Player cPlayer = players[i];

        String playerDisplay = "";
        if (players.length <= 4) {
          playerDisplay = playerNames.replace(" "+cPlayer.getName()+" ", Text.BLACK+Text.WHITE_BACKGROUND+" "+cPlayer.getName()+" "+Text.RESET).replace("Player "+(i+1)+Text.PURPLE+" (COM)", "Player "+(i+1)+Text.PURPLE+Text.WHITE_BACKGROUND+" (COM)");
        } else {
          playerDisplay = ("  "+Text.BLACK+Text.WHITE_BACKGROUND+" "+cPlayer.getName()+" "+Text.RESET).replace("Player "+(i+1)+Text.PURPLE+" (COM)", "Player "+(i+1)+Text.PURPLE+Text.WHITE_BACKGROUND+" (COM)");
        }

        if (!cPlayer.isComputer()) {
          print(Text.CLEAR + Text.WHITE_UNDERLINED + "Round " + round + Text.RESET);
          println(playerDisplay + "\n\nTotal Score: " + (cPlayer.getTurnScore() + cPlayer.getStoredScore())); // TurnScore should be 0
          waitToContinue("\nPress Enter to roll");
        }
        // Dice roll loop
        while (true) {
          print(Text.CLEAR + Text.WHITE_UNDERLINED + "Round " + round + Text.RESET);
          println(playerDisplay);
          int roll1 = d1.roll();
          int roll2 = d2.roll();

          if (roll1 == 1) {println("\nRoll One: " + Text.RED + roll1 + Text.RESET);}
          else {println("\nRoll One: " + roll1);}
          if (roll2 == 1) {println("Roll Two: " + Text.RED + roll2 + Text.RESET);}
          else {println("Roll Two: " + roll2);}
          
          boolean rolledDouble1 = roll1 == 1 && roll2 == 1;
          boolean rolledSingle1 = !rolledDouble1 && (roll1 == 1 || roll2 == 1);
          
          if (rolledDouble1) { 
            cPlayer.resetTurnScore();
            cPlayer.resetStoredScore();
          } else if (rolledSingle1) {
            cPlayer.resetTurnScore();
          } else { // if the player didn't roll a 1
            cPlayer.setTurnScore(cPlayer.getTurnScore() + roll1 + roll2);
          }

          println("Turn Score: " + cPlayer.getTurnScore());
          if (cPlayer.getTurnScore() + cPlayer.getStoredScore() < pointsGoal) {
            println("Total Score: " + (cPlayer.getTurnScore() + cPlayer.getStoredScore()) + "\n");
          } else {
            println("Total Score: " + Text.GREEN + (cPlayer.getTurnScore() + cPlayer.getStoredScore()) + Text.RESET + "\n");
          }
          if (cPlayer.isComputer()) {
            print("The computer is playing...");
          }
          
          if (rolledDouble1) {
            cPlayer.setTurnScore(0);
            cPlayer.setStoredScore(0);
            rolledDouble1 = false;
            if (cPlayer.isComputer()) {
              ((ComPlayer)cPlayer).resetRollProbability();
              Thread.sleep(2000);
            } else {
              println("Oh No! You rolled two 1s, so your total score has been reset.");
              waitToContinue();
            }
            break;
          } else if (rolledSingle1) {
            cPlayer.setTurnScore(0);
            rolledSingle1 = false;
            if (cPlayer.isComputer()) {
              ((ComPlayer)cPlayer).resetRollProbability();
              Thread.sleep(2000);
            } else {
              println("Whoops! You rolled a 1, so your turn is over!");
              waitToContinue();
            }
            break;
          } else if (cPlayer.getTurnScore() + cPlayer.getStoredScore() >= pointsGoal) { // if the current player has reached the winning score
            cPlayer.updateStoredScore();
            winner = cPlayer;
            if (cPlayer.isComputer()) {
              println("The computer won!");
            } else {
              println("You win!");
            }
            waitToContinue("Press Enter to see results");
            break;
          }

          if (cPlayer.isComputer()) {
            if (!((ComPlayer)cPlayer).rollAgain()) { // if computer player decides to end turn
              cPlayer.updateStoredScore();
              ((ComPlayer)cPlayer).resetRollProbability();
              Thread.sleep(2000);
              break;
            }
            Thread.sleep(1000);
          } else {
            print(Text.CYAN + "Enter " + Text.YELLOW + "Q" + Text.CYAN + " to end turn, or any input to roll again > " + Text.RESET);
            if (scan.nextLine().toLowerCase().trim().matches("^q$|^quit$")) {
              cPlayer.updateStoredScore();
              break;
            }
          }
          
        } //  end of player turn loop
        cPlayer.setTurnScore(0);
      } // end of round
      
    } // end of game loop

    println(Text.CLEAR + Text.WHITE_UNDERLINED + "Results" + Text.RESET);
    println(winner.getName() + " won the game on round " + round + "!");
    
    
  } // END OF MAIN METHOD


  
  // Other Methods

  public static void waitToContinue() { // require input to continue
    Scanner tempScan = new Scanner(System.in);
    print(Text.CYAN + "Press Enter to continue > " + Text.RESET);
    tempScan.nextLine();
  }

  public static void waitToContinue(String message) { // require input to continue with custom message
    Scanner tempScan = new Scanner(System.in);
    print(Text.CYAN + message + " > " + Text.RESET);
    tempScan.nextLine();
  }
  
  public static boolean gameOver(Player[] players, int pointsGoal) { // check if a player has won the game
    for (Player p: players) {
      if (p.getStoredScore() >= pointsGoal) {return true;}
    }
    return false;
  }
  
  public static void print(String str) { // print String
    System.out.print(str);
  }
  
  public static void println(String str) { // print String and add new line
    System.out.println(str);
  }
  
}