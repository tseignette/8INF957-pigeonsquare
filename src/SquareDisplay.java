import java.util.ArrayList;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

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
	private boolean hasScary = false;
	private Point2D scaryPos;
	private SquareDisplay me = this;

  // ===============================================================================================
  // FUNCTIONS
	// ===============================================================================================
	public static double randomDouble(double rangeMax) {
		Random random = new Random();
		double rangeMin = 0;
		return(rangeMin + (rangeMax - rangeMin) * random.nextDouble());
	}

	public static Point2D randomPoint2D() {
		return new Point2D(
			SquareDisplay.randomDouble(SquareDisplay.WINDOWS_WIDTH),
			SquareDisplay.randomDouble(SquareDisplay.WINDOWS_HEIGHT)
		);
	}

	public boolean hasFood() {
		return false;
	}

	public boolean hasScary() {
		return hasScary;
	}

	public Point2D getScary() {
		return scaryPos;
	}

  public void start(Stage primaryStage) {
		this.root = new Group();
		
	  sceneBuilder(primaryStage);
		pigeonsBuilder(6);
		
		primaryStage.setOnCloseRequest(event -> stopPigeons());
		primaryStage.getScene().addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				Point2D mousePos = new Point2D(mouseEvent.getX(), mouseEvent.getY());

				if(mouseEvent.getButton() == MouseButton.PRIMARY) {
					System.out.println("GAUCHE");
				}
				else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
					if(!hasScary) {
						hasScary = true;
						scaryPos = mousePos;
						int scarySize = 100;
						ImageView scary = new ImageView(new Image("./scary.png", scarySize, 0, true, true));
						scary.setX(mousePos.getX() - scarySize / 2);
						scary.setY(mousePos.getY() - scarySize / 2);
						root.getChildren().add(scary);

						FadeTransition scaryTransition = new FadeTransition(Duration.seconds(5), scary);
						scaryTransition.setFromValue(1.0);
						scaryTransition.setToValue(0.1);
						scaryTransition.setOnFinished(e -> {
							root.getChildren().remove(scary);
							hasScary = false;
						});
				
						scaryTransition.play();
						synchronized(me) {
							me.notifyAll();
						}
					}
				}
			}
		});

		primaryStage.show();
		startPigeons();
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
    // primaryStage.setResizable(false);
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
				this,
				SquareDisplay.randomPoint2D()
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
		this.threadGroup.interrupt();
  }
  
}
