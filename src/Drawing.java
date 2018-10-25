import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Drawing {

  // ===============================================================================================
  // ATTRIBUTES
  // ===============================================================================================
  private Group group;
  private int drawingSize;
  private ImageView view;

  // ===============================================================================================
  // CONSTRUCTOR
  // ===============================================================================================
  protected Drawing(Group group, String imgUrl, int drawingSize) {
    this.group = group;
    this.drawingSize = drawingSize;

    Image img = new Image(imgUrl, drawingSize, 0, true, true);
    this.view = new ImageView(img);
  }

  // ===============================================================================================
  // PUBLIC FUNCTIONS
  // ===============================================================================================
  public void setDrawingPosition(Point2D pos) {
    Platform.runLater(
      () -> {
        this.view.setX(pos.getX() - drawingSize / 2);
        this.view.setY(pos.getY() - drawingSize / 2);
      }
    );
  }

  public void draw() {
    group.getChildren().add(view);
  }

  public void erase() {
    group.getChildren().remove(view);
  }

}
