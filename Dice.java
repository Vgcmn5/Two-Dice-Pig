public class Dice {
  
  // Dice variables
  private int numSides;

  public int getSides() {return this.numSides;}

  // Dice constructors
  public Dice(int sides) {
    this.numSides = sides;
  }

  public Dice() {
    this.numSides = 6;
  }

  // Dice Methods
  public int roll() {
    return (int)(Math.random() * numSides + 1);
  }

  
}