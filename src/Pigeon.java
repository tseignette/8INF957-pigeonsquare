import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pigeon implements Runnable {

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  private static int pigeonNb = 0;
  private Thread thread;
  private double x;
  private double y;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Pigeon(ThreadGroup tg, double x, double y) {
    this.thread = new Thread(tg, this, "Pigeon "+pigeonNb++);
    this.x = x;
    this.y = y;
  }

  // ===============================================================================================
  // FUNCTIONS
  // ===============================================================================================
  public Thread getThread() {
    return this.thread;
  }
  
  public double getX() {
	  return this.x;
  }
  
  public double getY() {
	  return this.y;
  }

  public void run() {
    while(true) {
      System.out.println(this.thread.getName()+" here!");
      
      try {
        Thread.sleep(2000);
      } catch(InterruptedException e) {
          Thread.currentThread().interrupt();
      }
    }
  }

}
