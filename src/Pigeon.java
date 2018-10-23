
public class Pigeon implements Runnable {

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  private static int pigeonNb = 0;
  private Thread thread;
  private int x;
  private int y;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Pigeon(ThreadGroup tg, int x, int y) {
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
