import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SquareDisplay extends Application {

	// ===============================================================================================
	// CONSTANTS
	// ===============================================================================================
	public final static int WINDOWS_WIDTH = 600;
	public final static int WINDOWS_HEIGHT = 600;
	private final static Color WINDOW_BACKGROUND_COLOR = Color.BURLYWOOD;

	// ================================================================================================
	// ATTRIBUTES
	// ================================================================================================
	private Group root;
	private ArrayList<Pigeon> pigeons;
	private ThreadGroup threadGroup;
	private boolean hasScary = false;
	private Point2D scaryPos;
	private ArrayList<Food> foodList;
	private SquareDisplay me = this;

	// ===============================================================================================
	// FUNCTIONS
	// ===============================================================================================
	public boolean hasFood() {
		return !foodList.isEmpty();
	}

	public boolean hasScary() {
		return hasScary;
	}
	
	public ArrayList<Food> getFood() {
		return foodList;
	}

	public void removeFood(Food food) {
		foodList.remove(food);
	}

	public boolean eatFood(Food food) {
		boolean ret = food.eat();
		if(ret) {
			System.out.println(Thread.currentThread().getName()+" a mangé "+food+" !");
			foodList.remove(food);
			food.erase();
		}

		return ret;
	}

	public Point2D getScary() {
		return scaryPos;
	}

	public void removeScary() {
		hasScary = false;
	}

	public void start(Stage primaryStage) {
		this.root = new Group();
		this.foodList = new ArrayList<Food>();
		
		sceneBuilder(primaryStage);
		pigeonsBuilder(10);
		
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
		this.threadGroup = new ThreadGroup("Pigeons");
		this.pigeons = new ArrayList<Pigeon>();
		
		for(int i = 0; i < pigeonNb; i++) {
			// TODO: enlever le position dans le pigeon
			Pigeon pigeon = new Pigeon(
				this.threadGroup,
				root,
				this,
				Utils.randomPoint2D()
			);

			this.pigeons.add(pigeon);
			pigeon.draw();
		}
	}
  
	public void spawnScary(Point2D mousePos) {
		if(!hasScary) {
			hasScary = true;
			scaryPos = mousePos;

			BadGuy scary = new BadGuy(root, me);
			scary.setDrawingPosition(scaryPos)
			.draw();

			synchronized(me) {
				me.notifyAll();
			}
		}
	}
	
	public void spawnFood(Point2D mousePos) {
		Food food = new Food(root, me);
		food.setDrawingPosition(mousePos)
		.draw();

		foodList.add(food);

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
