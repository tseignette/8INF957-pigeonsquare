import java.util.Random;

import javafx.geometry.Point2D;

public final class Utils {

	public static double randomDouble(double rangeMax) {
		Random random = new Random();
		double rangeMin = 0;
		return(rangeMin + (rangeMax - rangeMin) * random.nextDouble());
	}

	public static Point2D randomPoint2D() {
		return new Point2D(
			Utils.randomDouble(SquareDisplay.WINDOWS_WIDTH),
			Utils.randomDouble(SquareDisplay.WINDOWS_HEIGHT)
		);
	}

}
