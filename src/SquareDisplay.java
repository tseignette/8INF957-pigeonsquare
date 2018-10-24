import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
  private final static int WINDOWS_WIDTH = 580;
  private final static int WINDOWS_HEIGHT = 580;
  private final static Color WINDOW_BACKGROUND_COLOR = Color.BURLYWOOD;

  private Group root;
  private ArrayList<Pigeon> pigeons;
  private ThreadGroup threadGroup;

  // ===============================================================================================
  // FUNCTIONS
  // ===============================================================================================
  public void start(Stage primaryStage) {
	  this.root = new Group();
	  sceneBuilder(primaryStage);
    
	  pigeonsBuilder(10);
	  startPigeons();
	  pigeonsDisplayer();
    
	  primaryStage.show();
	  
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
	  primaryStage.getIcons().add(new Image("./pigeon.png"));
	  
	  // Provisoire : Si on arrive à rendre la fenêtre responsive on peut enlever
	  primaryStage.setMinHeight(WINDOWS_HEIGHT);
	  primaryStage.setMaxHeight(WINDOWS_HEIGHT);
	  primaryStage.setMinWidth(WINDOWS_WIDTH);
	  primaryStage.setMaxWidth(WINDOWS_WIDTH);
  }
  
  public void pigeonsBuilder(int pigeonNb) {
	  Random random = new Random();
	  this.threadGroup = new ThreadGroup("Pigeons");
	  this.pigeons = new ArrayList<Pigeon>();
	
	  for(int i = 0; i < pigeonNb; i++) {
		  double x = (random.nextDouble()  * 1000)/2;
		  double y = (random.nextDouble()  * 1000)/2;
		  Pigeon pigeon = new Pigeon(
				  this.threadGroup,
				  x,
				  y);
	  this.pigeons.add(pigeon);
	  }
  }
  
  public void pigeonsDisplayer() {	  
	  Image imgPigeon = new Image("./pigeon.png", 40, 0, true, true);
	  
	  for (Pigeon pigeon : pigeons) {
		  ImageView pigeonView =  new ImageView(imgPigeon);
		  pigeonView.setX(pigeon.getX());
		  pigeonView.setY(pigeon.getY());
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
		  pigeon.getThread().stop();
	  }
  }
  
}
