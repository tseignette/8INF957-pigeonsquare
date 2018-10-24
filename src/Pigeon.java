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
  private final static double speed = 1;

  private static int pigeonNb = 0;

  private Thread thread;
  private Point2D pos;
  private ImageView view;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Pigeon(ThreadGroup tg, ImageView pigeonView, Point2D pos) {
    this.thread = new Thread(tg, this, "Pigeon "+pigeonNb++);
    this.pos = pos;
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
    double newX = (pos.getX() > 0 && pos.getX() < SquareDisplay.WINDOWS_WIDTH) ? pos.getX() : this.pos.getX();
    double newY = (pos.getY() > 0 && pos.getY() < SquareDisplay.WINDOWS_WIDTH) ? pos.getY() : this.pos.getY();

    this.pos = new Point2D(newX, newY);
    Platform.runLater(
      () -> {
        this.view.setX(this.pos.getX() - Pigeon.IMAGE_SIZE / 2);
        this.view.setY(this.pos.getY() - Pigeon.IMAGE_SIZE / 2);
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
    // Try catch block to catch possible InterruptedException when stopping the thread
    try {
      while(!thread.isInterrupted()) {
        // fear(middle);

        try {
          Thread.sleep(10);
        }
        catch(InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    } finally { }
  }

}
