import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class SquareDisplay extends Application {

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  private final static int WINDOWS_WIDTH = 600;
  private final static int WINDOWS_HEIGHT = 600;
  private final static Color WINDOW_BACKGROUND_COLOR = Color.BURLYWOOD;

  private Group root;

  // ===============================================================================================
  // FUNCTIONS
  // ===============================================================================================
  public void start(Stage primaryStage) {
    this.root = new Group();
    Scene scene = new Scene(
      this.root,
      SquareDisplay.WINDOWS_WIDTH,
      SquareDisplay.WINDOWS_HEIGHT,
      SquareDisplay.WINDOW_BACKGROUND_COLOR
    );

    primaryStage.setTitle("Pigeon Square");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}
