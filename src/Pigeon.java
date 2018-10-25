import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Group;

public class Pigeon extends Drawing implements Runnable {

  // ===============================================================================================
  // CONSTANTS
  // ===============================================================================================
  private final static double SPEED = 1;
  private final static int IMG_SIZE = 60;

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  private static int pigeonNb = 0;

  private Thread thread;
  private SquareDisplay square;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Pigeon(ThreadGroup tg, Group group, SquareDisplay square) {
    super(group, "./pigeon.png", Pigeon.IMG_SIZE);

    this.thread = new Thread(tg, this, "Pigeon "+pigeonNb++);
    this.square = square;
  }

  // ===============================================================================================
  // FUNCTIONS
  // ===============================================================================================
  public Thread getThread() {
    return this.thread;
  }

  public Point2D getPos() {
    return new Point2D(getView().getX(), getView().getY());
  }

  public void updatePigeon(Point2D pos) {
    double newX = (pos.getX() > 0 && pos.getX() < SquareDisplay.WINDOWS_WIDTH) ? pos.getX() : getPos().getX();
    double newY = (pos.getY() > 0 && pos.getY() < SquareDisplay.WINDOWS_HEIGHT) ? pos.getY() : getPos().getY();

    setPosition(new Point2D(newX, newY));
  }

  public Point2D getVector(Point2D depart, Point2D arrivee) {
    return new Point2D(arrivee.getX() - depart.getX(), arrivee.getY() - depart.getY());
  }
  
  private Food getNearest(List<Food> list) {
    Food sol = list.get(0);
    
    double distanceMin = 2000;
    for (Food food : list) {
      Point2D point = food.getPos();
      double distance = this.getPos().distance(point);
      if (distance < distanceMin) {
        distanceMin = distance;
        sol = food;
      }
    }

	  return sol;
  }

  private boolean isNear(Point2D foodPos) {
    return(this.getPos().distance(foodPos) < 2);
  }

  public void move(Point2D arrivee) {
    Point2D vector = getVector(this.getPos(), arrivee).normalize();
    Point2D newPos = this.getPos().add(vector.multiply(SPEED));
    updatePigeon(newPos);
  }

  public void fear(Point2D originFear) {
	  Point2D vector = getVector(originFear, this.getPos()).normalize();
	  Point2D newPos = this.getPos().add(vector.multiply(SPEED));
	  updatePigeon(newPos);
  }

  public void run() {
    // Try catch block to catch possible InterruptedException when stopping the thread
    try {
      while(!thread.isInterrupted()) {
        try {
          synchronized(square) {
            square.wait();
          }

          while(square.hasScary() || square.hasFood()) {
            if(square.hasScary()) {
              fear(square.getScary());
            }
            else if(square.hasFood()) {
              ArrayList<Food> foodList = square.getFood();
              try {
                Food nearestFood = getNearest(foodList);
                move(nearestFood.getPos());
                if(isNear(nearestFood.getPos())) square.eatFood(nearestFood);
              }
              finally { }
            }

            Thread.sleep(10);
          }
        }
        catch(InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    } finally { }
  }

}
