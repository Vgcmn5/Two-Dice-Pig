public class Player {
  
  // Player variables
  private String name;
  protected boolean isComputer = false;
  //private int playerNum;
  protected int turnScore;
  protected int storedScore;
  
  // Player constructors
  public Player(String n) {
    this.name = n;
    //this.isComputer = isCom;
    this.turnScore = 0;
    this.storedScore = 0;
  }

  public Player() {
    //this.name = n;
    this.turnScore = 0;
    this.storedScore = 0;
  }


  // Player Methods
  public String getName() {return name;}
  //public int getPlayerNum() {return playerNum;}
  public boolean isComputer() {return isComputer;}
  public int getTurnScore() {return turnScore;}
  public int getStoredScore() {return storedScore;}

  public void setname(String newName) {this.name = newName;}
  public void setTurnScore(int newTurnScore) {this.turnScore = newTurnScore;}
  public void setStoredScore(int newStoredScore) {this.storedScore = newStoredScore;}

  public void resetTurnScore() {this.turnScore = 0;}
  public void resetStoredScore() {this.storedScore = 0;}
  public void updateStoredScore() {this.storedScore += this.turnScore;}

  
}