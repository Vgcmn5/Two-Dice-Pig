public class ComPlayer extends Player {

  private boolean isHard = true;
  private double rollProbability = 0.0; 

  public double getRollProbability() {return rollProbability;}
  public void setHardSetting(boolean bool) {this.isHard = bool;}
  public void resetRollProbability() {
    if (this.isHard) {
      this.rollProbability = 0.90;
    } else {
      this.rollProbability = 0.80;
    }
  }
  
  public ComPlayer(int num) {
    super("Player " + num + Text.PURPLE + " (COM)" + Text.RESET);
    this.isComputer = true;
  }

  public boolean rollAgain() { // decide wether to roll again or end turn

    // if the computer decides to end its turn
    if (Math.random() > this.rollProbability) {return false;}

    // otherwise it rolls again and increases probability of ending turn
    if (isHard) {
      this.rollProbability -= 0.10;
    } else {
      this.rollProbability -= 0.25;
    }
    return true;
    
  }

  
}