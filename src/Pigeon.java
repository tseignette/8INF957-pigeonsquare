import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Point2D;
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
  private SquareDisplay square;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Pigeon(ThreadGroup tg, ImageView pigeonView, SquareDisplay square, Point2D pos) {
    this.thread = new Thread(tg, this, "Pigeon "+pigeonNb++);
    this.pos = pos;
    this.view = pigeonView;
    this.square = square;
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
    double newY = (pos.getY() > 0 && pos.getY() < SquareDisplay.WINDOWS_HEIGHT) ? pos.getY() : this.pos.getY();

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
  
  private Food getNearest(List<Food> list) {
	  Food sol = new Food(this.getPos());
	  double distanceMin = 2000;
	  for (Food food : list) {
      Point2D point = food.getPos();
		  double distance = this.pos.distance(point);
		  if (distance < distanceMin) {
			  distanceMin = distance;
			  sol = food;
		  }
	  }
	  return sol;
  }

  private boolean isNear(Point2D foodPos) {
    return(this.pos.distance(foodPos) < 2);
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
              Food nearestFood = getNearest(foodList);
              move(nearestFood.getPos());
              if(isNear(nearestFood.getPos())) square.eatFood(nearestFood);
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
