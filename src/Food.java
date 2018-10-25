import java.util.concurrent.locks.ReentrantLock;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class Food {

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  private ReentrantLock lock = new ReentrantLock();
  private boolean hasBeenEaten = false;
  private ImageView view;
  private Point2D pos;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Food(Point2D pos) {
    this.pos = pos;
  }

  public Food(Point2D pos, ImageView view) {
    this.pos = pos;
    this.view = view;
  }

  // ===============================================================================================
  // FUNCTIONS
  // ===============================================================================================
  public ImageView getView() {
    return view;
  }

  public Point2D getPos() {
    return pos;
  }

  /**
   * Return true if can be eaten, false otherwise
   */
  public boolean eat() {
    lock.lock();
    boolean ret = false;

    try {
      if(!hasBeenEaten) {
        hasBeenEaten = true;
        ret = true;
      }
    }
    finally {
      lock.unlock();
    }

    return ret;
  }

}
