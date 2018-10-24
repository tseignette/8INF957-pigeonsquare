import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pigeon implements Runnable {

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  public final static int IMAGE_SIZE = 60;
  private final static double speed = 5;

  private static int pigeonNb = 0;

  private Thread thread;
  private Point2D pos;
  private ImageView view;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Pigeon(ThreadGroup tg, ImageView pigeonView, double x, double y) {
    this.thread = new Thread(tg, this, "Pigeon "+pigeonNb++);
    this.pos = new Point2D(x, y);
    this.view = pigeonView;
    updatePigeon(this.pos);
  }

  // ===============================================================================================
  // FUNCTIONS
  // ===============================================================================================
  public Thread getThread() {
    return this.thread;
  }

  public Point2D getPos() {
    return this.pos;
  }

  public void updatePigeon(Point2D pos) {
    this.pos = pos;
    Platform.runLater(
      () -> {
        this.view.setX(pos.getX() - Pigeon.IMAGE_SIZE / 2);
        this.view.setY(pos.getY() - Pigeon.IMAGE_SIZE / 2);
      }
    );
  }

  public Point2D getVector(Point2D depart, Point2D arrivee) {
    return new Point2D(arrivee.getX() - depart.getX(), arrivee.getY() - depart.getY());
  }

  public void move(Point2D arrivee) {
    Point2D vector = getVector(this.pos, arrivee).normalize();
    Point2D newPos = this.pos.add(vector.multiply(speed));
    updatePigeon(newPos);
  }

  public void fear(Point2D originFear) {
    Point2D vector = getVector(originFear, this.pos).normalize();
    Point2D newPos = this.pos.add(vector.multiply(speed));
    updatePigeon(newPos);
  }

  public void run() {
    Point2D middle = new Point2D(250, 250);

    // Try catch block to catch possible InterruptedException when stopping the thread
    try {
      while(!thread.isInterrupted()) {
        System.out.println(this.thread.getName() + " here!");
        fear(middle);

        try {
          Thread.sleep(100);
        }
        catch(InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    } finally { }
  }

}
