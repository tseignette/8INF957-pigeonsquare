import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.util.Duration;

public class BadGuy extends Drawing {

  // ===============================================================================================
  // CONSTANTS
  // ===============================================================================================
  private final static int IMG_SIZE = 100;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public BadGuy(Group group, SquareDisplay square) {
    super(group, "./scary.png", BadGuy.IMG_SIZE);

    FadeTransition badGuyTransition = new FadeTransition(Duration.seconds(2), getView());
    badGuyTransition.setFromValue(1.0);
    badGuyTransition.setToValue(0.1);
    badGuyTransition.setOnFinished(e -> {
      erase();
      square.removeScary();
    });
    setTransition(badGuyTransition);
  }

}
