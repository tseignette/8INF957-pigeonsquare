import javafx.animation.Transition;
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
  private Transition transition;

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
  public ImageView getView() {
    return view;
  }

  public Drawing setDrawingPosition(Point2D pos) {
    Platform.runLater(
      () -> {
        this.view.setX(pos.getX() - drawingSize / 2);
        this.view.setY(pos.getY() - drawingSize / 2);
      }
    );

    return this;
  }

  public Drawing setImage(String imgUrl) {
    view.setImage(new Image(imgUrl, drawingSize, 0, true, true));
    
    return this;
  }

  public Drawing setTransition(Transition transition) {
    this.transition = transition;

    return this;
  }

  public Drawing draw() {
    Platform.runLater(
      () -> {
        group.getChildren().add(view);
        if(transition != null) transition.play();
      }
    );

    return this;
  }

  public Drawing erase() {
    Platform.runLater(
      () -> {
        group.getChildren().remove(view);
      }
    );

    return this;
  }

}
