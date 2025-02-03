package lib.algorithms;

import lib.entities.Pixel;
import lib.entities.ProcessedImageFields;

import java.util.*;

/**
 * Implements an alternative approximate hole-filling algorithm.
 * Uses boundary pixel sampling and weighted interpolation to estimate missing pixel values.
 */

public class ApproximateAlgorithm extends FillingAlgorithm {
    private static final String name = "ApproximateAlgorithm";
    private final int connectivity;
    private final int[][] DIRECTIONS = {
            {-1, 0}, {0, 1}, {1, 0}, {0, -1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    /**
     * Constructs an AnotherApproxAlgorithm with a specified weight function.
     *
     * @param connectivity the connectivity of the pixels for the algorithm.
     */

    public ApproximateAlgorithm(int connectivity) {
        this.connectivity = connectivity;
    }

    /**
     * Returns the name of the algorithm.
     *
     * @return A string representing the algorithm name.
     */

    public String getAlgorithmName() {
        return name;
    }

    /**
     * Evaluates and fills the holes in the image using an approximate method.
     *
     * @param fields The processed image data containing hole and boundary pixels.
     * @return A 2D array of Pixels representing the filled image.
     */

    public Pixel[][] evaluatePixels(ProcessedImageFields fields, WeightFunction weightFunc)
    {
        HashSet<Pixel> B = fields.getBoundaryPixels();
        HashSet<Pixel> C = new HashSet<>(B); // Shallow copy
        ArrayList<Pixel> approxBoundary = new ArrayList<>();
        Pixel[][] pixels = fields.getPixelArray();
        Pixel currRandBound = B.iterator().next();
        int count = 0;
        float currIntensity = 0;
        int avgX = 0, avgY = 0;
        int pixelsPerPoint = B.size() / connectivity;
        boolean foundNeighbor;
        HashSet<Pixel> seen = new HashSet<>();
        while (seen.size() < B.size() && approxBoundary.size() < connectivity)
        {
            foundNeighbor = false;
            for (int[] direction : DIRECTIONS)
            {
                int row = currRandBound.getX() + direction[0];
                int col = currRandBound.getY() + direction[1];
                if (row < 0 || col < 0 || row >= pixels.length || col >= pixels[0].length) {
                    continue;
                }
                Pixel neighbor = pixels[row][col];
                if (B.contains(neighbor) && !seen.contains(neighbor))
                {
                    currRandBound = neighbor;
                    currIntensity += neighbor.getValue();
                    avgX += neighbor.getX();
                    avgY += neighbor.getY();
                    count++;
                    foundNeighbor = true;
                    if (count >= pixelsPerPoint) {
                        avgX /= count;
                        avgY /= count;
                        currIntensity /= count;
                        approxBoundary.add(new Pixel(avgX, avgY, currIntensity));
                        count = 0;
                        avgX = 0;
                        avgY = 0;
                        currIntensity = 0;
                    }
                    break;
                }
            }
            seen.add(currRandBound);
            C.remove(currRandBound);
            if (!foundNeighbor)
            {
                currRandBound = C.iterator().next();
                if (count > 0)
                {
                    avgX /= count;
                    avgY /= count;
                    currIntensity /= count;
                    approxBoundary.add(new Pixel(avgX, avgY, currIntensity));
                    count = 0;
                    avgX = 0;
                    avgY = 0;
                    currIntensity = 0;
                }
            }
        }
        if (approxBoundary.size() < connectivity && count > 0) {
            avgX /= count;
            avgY /= count;
            currIntensity /= count;
            approxBoundary.add(new Pixel(avgX, avgY, currIntensity));
        }
        calculateNewPixels(fields, weightFunc, approxBoundary, pixels);
        return pixels;
    }

    private static void calculateNewPixels(ProcessedImageFields fields, WeightFunction weightFunc, ArrayList<Pixel> approxBoundary, Pixel[][] pixels) {
        for (Pixel h : fields.getHolePixels())
        {
            float denominator = 0;
            float numerator = 0;

            for (Pixel p : approxBoundary) {
                float currWeight = weightFunc.calculateWeight(h, p);
                numerator += currWeight * p.getValue();
                denominator += currWeight;
            }
            if (denominator > 0) {
                pixels[h.getX()][h.getY()].setValue(numerator / denominator);
            }
        }
    }
}