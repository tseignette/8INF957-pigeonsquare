import java.util.ArrayList;
import java.util.Random;

public class Square {

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  private ArrayList<Pigeon> pigeons;
  private ThreadGroup threadGroup;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Square(int pigeonNb) {
    Random random = new Random();
    this.threadGroup = new ThreadGroup("Pigeons");
    this.pigeons = new ArrayList<Pigeon>();

    for(int i = 0; i < pigeonNb; i++) {
      Pigeon pigeon = new Pigeon(
        this.threadGroup,
        random.nextInt(100 + 1),
        random.nextInt(100 + 1)
      );
      this.pigeons.add(pigeon);
    }
  }

  // ===============================================================================================
  // FUNCTIONS
  // ===============================================================================================
  public void startPigeons() {
    for(Pigeon pigeon: this.pigeons) {
      pigeon.getThread().start();
    }
  }

}
