import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pigeon implements Runnable {

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  private static int pigeonNb = 0;
  private Thread thread;
  private Point2D pos;
  private ImageView pigeon;
  private final static double speed = 5;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  public Pigeon(ThreadGroup tg, double x, double y, Group root) {
    this.thread = new Thread(tg, this, "Pigeon "+pigeonNb++);
    this.pos = new Point2D(x, y);
    this.pigeon =  new ImageView(new Image("./pigeon.png", 40, 0, true, true));
    updatePigeon(this.pos);
	root.getChildren().add(this.pigeon);
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
	  this.pos = pos;
	  this.pigeon.setX(pos.getX());
	  this.pigeon.setY(pos.getY());
  }
  
  public Point2D getVector(Point2D depart, Point2D arrivee) {
	  return new Point2D(arrivee.getX() - depart.getX(), arrivee.getY() - depart.getY());
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

	  Point2D midle = new Point2D(250, 250);
	  
	  while(true) {
	      System.out.println(this.thread.getName()+" here!");
	      fear(midle);   
	      
	      try {
	    	  Thread.sleep(100);
	      } catch(InterruptedException e) {
		          Thread.currentThread().interrupt();
		          }
	  }
  }

}
