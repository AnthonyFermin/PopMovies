package anthonyfdev.com.popmovies.common;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class MathUtils {

    static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

}