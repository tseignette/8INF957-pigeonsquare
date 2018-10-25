import java.util.concurrent.locks.ReentrantLock;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.util.Duration;

public class Food extends Drawing {

  // ===============================================================================================
  // CONSTANTS
  // ===============================================================================================
  private final static int IMG_SIZE = 40;

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  private ReentrantLock lock = new ReentrantLock();
  private boolean hasBeenEaten = false;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Food(Group group, SquareDisplay square) {
    super(group, "./food.png", Food.IMG_SIZE);

		PauseTransition foodTransition = new PauseTransition(Duration.seconds(2));
		foodTransition.setOnFinished(e -> {
			if(!hasBeenEaten) {
        square.removeFood(this);
        
        setImage("./expiredFood.png");
				
				FadeTransition expiredFoodTransition = new FadeTransition(Duration.seconds(3), getView());
				expiredFoodTransition.setFromValue(1.0);
				expiredFoodTransition.setToValue(0.0);
				expiredFoodTransition.setOnFinished(event -> {
          erase();
				});
				
				expiredFoodTransition.play();
			}
    });
    
    setTransition(foodTransition);
  }

  // ===============================================================================================
  // FUNCTIONS
  // ===============================================================================================
  public Point2D getPos() {
    return new Point2D(getView().getX(), getView().getY());
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
