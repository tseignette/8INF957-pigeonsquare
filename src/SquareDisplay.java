import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class SquareDisplay extends Application {

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  public final static int WINDOWS_WIDTH = 580;
  public final static int WINDOWS_HEIGHT = 580;
  private final static Color WINDOW_BACKGROUND_COLOR = Color.BURLYWOOD;

  private Group root;
  private ArrayList<Pigeon> pigeons;
  private ThreadGroup threadGroup;

  // ===============================================================================================
  // FUNCTIONS
	// ===============================================================================================
	public static double randomDouble(double rangeMax) {
		Random random = new Random();
		double rangeMin = 0;
		return(rangeMin + (rangeMax - rangeMin) * random.nextDouble());
	}

  public void start(Stage primaryStage) {
		this.root = new Group();
		
	  sceneBuilder(primaryStage);
		pigeonsBuilder(3);
		
	  primaryStage.show();
	  startPigeons();
	  
	  primaryStage.setOnCloseRequest(event -> stopPigeons());
  }

  public void sceneBuilder(Stage primaryStage) {
	  Scene scene = new Scene(
			this.root,
			SquareDisplay.WINDOWS_WIDTH,
			SquareDisplay.WINDOWS_HEIGHT,
			SquareDisplay.WINDOW_BACKGROUND_COLOR
		);
	  
	  primaryStage.setTitle("Pigeon Square");
	  primaryStage.setScene(scene);
	  primaryStage.setScene(scene);
    primaryStage.setResizable(false);
	  primaryStage.getIcons().add(new Image("./pigeon.png"));
  }
  
  public void pigeonsBuilder(int pigeonNb) {
		Image pigeonImage = new Image("./pigeon.png", Pigeon.IMAGE_SIZE, 0, true, true);
	  this.threadGroup = new ThreadGroup("Pigeons");
	  this.pigeons = new ArrayList<Pigeon>();
		
	  for(int i = 0; i < pigeonNb; i++) {
			ImageView pigeonView = new ImageView(pigeonImage);
		  Pigeon pigeon = new Pigeon(
				this.threadGroup,
				pigeonView,
				SquareDisplay.randomDouble(SquareDisplay.WINDOWS_WIDTH),
				SquareDisplay.randomDouble(SquareDisplay.WINDOWS_HEIGHT)
			);

			this.pigeons.add(pigeon);
			root.getChildren().add(pigeonView);
	  }
  }
  
  public void startPigeons() {
	  for(Pigeon pigeon: this.pigeons) {
		  pigeon.getThread().start();
	  }
  }
  
  public void stopPigeons() {
	  for(Pigeon pigeon: this.pigeons) {
		  pigeon.getThread().interrupt();
	  }
  }
  
}
