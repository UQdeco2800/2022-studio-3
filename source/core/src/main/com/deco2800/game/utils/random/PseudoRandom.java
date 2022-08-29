package com.deco2800.game.utils.random;
import java.util.List;
import java.util.Random;
import static java.lang.Math.random;

/**
 * This class provides an API for seeding Pseduo-Generated Random features.
 */
public class PseudoRandom {
    /**
     * This method generates a pseudo-randomly generated number between 0 and 1.
     * @return  double between 0 and 1.
     */
    public static double randomUnitNumberGenerator () {
        return random();
    }

    /**
     * This method generates pseudo-randomly generated floating point number
     * between the specified lower-bound and upper-bound.
     *
     * @param lowerBound: Floating point lower bound.
     * @param upperBound: Floating point upper bound.
     *
     * @return double between lowerBound and upperBound.
     */
    public static double seedRandomDouble (double lowerBound, double upperBound) {
        return lowerBound + new Random().nextDouble(upperBound - lowerBound);
    }

    /**
     * This method generates pseudo-randomly generated integers between
     * the specified lowerBound and upperBound.
     * NOTE: lowerBound must be less than upperBound.
     *
     * @param lowerBound: Integer lower bound.
     * @param upperBound: Integer upper bound.
     *
     * @return Integer value between lowerBound and upperBound.
     */
    public static int seedRandomInt (int lowerBound, int upperBound) {
        return lowerBound + new Random().nextInt(upperBound - lowerBound);
    }

    /**
     * This method selects an item from a list by pseudo-randomly generating
     * an integer between 0 and the length of the list, and returning the
     * element at this index.
     *
     * @param items A list of items.
     * @return      An item chosen pseudo-randomly from items
     * @param <T>   Can be any Class.
     */
    public static <T> T getRandomItem(List<T> items) {
        int lenItems = items.size();
        int i = seedRandomInt(0, lenItems);
        return items.get(i);
    }
}
