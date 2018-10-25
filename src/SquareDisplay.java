import java.util.ArrayList;
import java.util.List;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class SquareDisplay extends Application {

	// ===============================================================================================
	// ATTRIBUTES
	// ===============================================================================================
  
	public final static int WINDOWS_WIDTH = 600;
	public final static int WINDOWS_HEIGHT = 600;
	private final static Color WINDOW_BACKGROUND_COLOR = Color.BURLYWOOD;

	private Group root;
	private ArrayList<Pigeon> pigeons;
	private ThreadGroup threadGroup;
	private boolean hasScary = false;
	private Point2D scaryPos;
	private int hasFood = 0;
	private ArrayList<Point2D> foodPos;
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
		return hasFood > 0;
	}

	public boolean hasScary() {
		return hasScary;
	}
	
	public ArrayList<Point2D> getFood() {
		return foodPos;
	}

	public Point2D getScary() {
		return scaryPos;
	}

	public void start(Stage primaryStage) {
		this.root = new Group();
		this.foodPos = new ArrayList<Point2D>();
		
		sceneBuilder(primaryStage);
		pigeonsBuilder(6);
		
		primaryStage.setOnCloseRequest(event -> stopPigeons());
		
		primaryStage.getScene().addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				Point2D mousePos = new Point2D(mouseEvent.getX(), mouseEvent.getY());

				if(mouseEvent.getButton() == MouseButton.PRIMARY) {
					spawnFood(mousePos);
				}
				else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
					spawnScary(mousePos);
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
				this,
				SquareDisplay.randomPoint2D()
			);

			this.pigeons.add(pigeon);
			root.getChildren().add(pigeonView);
		}
	}
  
	public void spawnScary(Point2D mousePos) {
		if(!hasScary) {
			hasScary = true;
			scaryPos = mousePos;
			int scarySize = 100;
			ImageView scary = new ImageView(new Image("./scary.png", scarySize, 0, true, true));
			scary.setX(mousePos.getX() - scarySize / 2);
			scary.setY(mousePos.getY() - scarySize / 2);
			root.getChildren().add(scary);

			FadeTransition scaryTransition = new FadeTransition(Duration.seconds(2), scary);
			scaryTransition.setFromValue(1.0);
			scaryTransition.setToValue(0);
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
	
	public void spawnFood(Point2D mousePos) {
		hasFood++;
		foodPos.add(mousePos);
		int foodSize = 40;
		ImageView food = new ImageView(new Image("./food.png", foodSize, 0, true, true));
		food.setX(mousePos.getX() - foodSize / 2);
		food.setY(mousePos.getY() - foodSize / 2);
		root.getChildren().add(food);

		FadeTransition foodTransition = new FadeTransition(Duration.seconds(2), food);
		foodTransition.setFromValue(1.0);
		foodTransition.setToValue(0.5);
		foodTransition.setOnFinished(e -> {
			root.getChildren().remove(food);
			foodPos.remove(mousePos);
			hasFood--;
			
			ImageView expiredFood = new ImageView(new Image("./expiredFood.png", foodSize, 0, true, true));
			expiredFood.setX(mousePos.getX() - foodSize / 2);
			expiredFood.setY(mousePos.getY() - foodSize / 2);
			root.getChildren().add(expiredFood);
			
			FadeTransition expiredFoodTransition = new FadeTransition(Duration.seconds(3), food);
			expiredFoodTransition.setOnFinished(event -> {
				root.getChildren().remove(expiredFood);
			});
			
			expiredFoodTransition.play();
		});
		
		foodTransition.play();
		synchronized(me) {
			me.notifyAll();
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
