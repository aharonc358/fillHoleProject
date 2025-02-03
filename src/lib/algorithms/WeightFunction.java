package lib.algorithms;

import lib.entities.Pixel;

/**
 * Functional interface for defining a weight function used in the hole-filling algorithm.
 * The weight function determines the influence of neighboring pixels on hole pixels.
 */

@FunctionalInterface
public interface WeightFunction
{
    /**
     * Calculates the weight between two pixels based on their distance and other properties.
     *
     * @param u The first pixel.
     * @param v The second pixel.
     * @return The computed weight as a float value.
     */

    float calculateWeight(Pixel u, Pixel v);
}
