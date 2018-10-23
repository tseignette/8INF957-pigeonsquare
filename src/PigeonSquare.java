import javafx.application.Application;;

public class PigeonSquare {

  // ===============================================================================================
  // MAIN
  // ===============================================================================================
  public static void main(String[] args) {
    Square square = new Square(10);

    Application.launch(SquareDisplay.class, args);

    square.startPigeons();
  }

}
