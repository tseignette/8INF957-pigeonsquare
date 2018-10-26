import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
	private SquareDisplay me = this;
	private ThreadGroup threadGroup;
	private Group root;
	private ArrayList<Pigeon> pigeonList = new ArrayList<Pigeon>();
	private ArrayList<Food> foodList = new ArrayList<Food>();
	private Point2D scaryPos;

	// ===============================================================================================
	// PRIVATE FUNCTIONS
	// ===============================================================================================
	private void sceneBuilder(Stage primaryStage) {
		Scene scene = new Scene(
			this.root,
			SquareDisplay.WINDOWS_WIDTH,
			SquareDisplay.WINDOWS_HEIGHT,
			SquareDisplay.WINDOW_BACKGROUND_COLOR
		);
	  
		primaryStage.setTitle("Pigeon Square");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("./pigeon.png"));
	}
  
	private void pigeonsBuilder(int pigeonNb) {
		this.threadGroup = new ThreadGroup("Pigeons");
		
		for(int i = 0; i < pigeonNb; i++) {
			Pigeon pigeon = new Pigeon(this.threadGroup, root, this);
			pigeon.setDrawingPosition(Utils.randomPoint2D())
			.draw();

			this.pigeonList.add(pigeon);
		}
	}
  
	private void spawnScary(Point2D mousePos) {
		if(!hasScary()) {
			scaryPos = mousePos;

			BadGuy scary = new BadGuy(root, me);
			scary.setDrawingPosition(scaryPos)
			.draw();

			synchronized(me) {
				me.notifyAll();
			}
		}
	}
	
	private void spawnFood(Point2D mousePos) {
		Food food = new Food(root, me);
		food.setDrawingPosition(mousePos)
		.draw();

		foodList.add(food);

		synchronized(me) {
			me.notifyAll();
		}
	}
	
	private void startPigeons() {
		for(Pigeon pigeon: this.pigeonList) {
			pigeon.getThread().start();
		}
	}
  
	private void stopPigeons() {
		this.threadGroup.interrupt();
	}

	// ===============================================================================================
	// PUBLIC FUNCTIONS
	// ===============================================================================================
	public boolean hasFood() {
		return !foodList.isEmpty();
	}

	public boolean hasScary() {
		return scaryPos != null;
	}
	
	public ArrayList<Food> getFood() {
		return foodList;
	}

	public Point2D getScary() {
		return scaryPos;
	}

	public void removeFood(Food food) {
		foodList.remove(food);
	}

	public void removeScary() {
		scaryPos = null;
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

	public void start(Stage primaryStage) {
		// On crée la scène et les pigeons
		this.root = new Group();
		sceneBuilder(primaryStage);
		pigeonsBuilder(10);

		// On crée un thread responsable de l'apparition du bad guy
		final double limit = Utils.randomDouble(60);
		System.out.println("Chances d'apparition de la méchante personne : "+(int)(100 - limit)+"%");
		ScheduledExecutorService scaryScheduler = Executors.newScheduledThreadPool(1);
    Runnable toRun = new Runnable() {
			public void run() {
				if(Utils.randomDouble(100) > limit) spawnScary(Utils.randomPoint2D());
			}
    };
		
		// On gère le clic gauche et droit
		primaryStage.getScene().addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				Point2D mousePos = new Point2D(mouseEvent.getX(), mouseEvent.getY());

				// Clic gauche -> nourriture
				if(mouseEvent.getButton() == MouseButton.PRIMARY) {
					spawnFood(mousePos);
				}
				// Clic droite -> bad guy
				else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
					spawnScary(mousePos);
				}
			}
		});

		// On lance tout
		primaryStage.show();
		startPigeons();
		ScheduledFuture<?> scaryHandle = scaryScheduler.scheduleAtFixedRate(toRun, 5, 5, TimeUnit.SECONDS);

		// On arrête les threads à la fermeture de la fenêtre
		primaryStage.setOnCloseRequest(event -> {
			stopPigeons();
			scaryHandle.cancel(true);
			scaryScheduler.shutdown();
		});
	}
  
}
