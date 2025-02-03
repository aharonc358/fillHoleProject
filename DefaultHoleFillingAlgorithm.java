package lib.algorithms;

import lib.entities.Pixel;
import lib.entities.ProcessedImageFields;

import java.util.Set;

/**
 * Implements the default hole-filling algorithm.Inherits from the abstract FillingAlgorithm class.
 * Uses weighted interpolation based on boundary pixels and a weight function.
 */

public class DefaultHoleFillingAlgorithm extends FillingAlgorithm
{
    private static final String name = "HoleFillingAlgorithm";

    /**
     * Constructs a DefaultHoleFillingAlgorithm.
     */

    public DefaultHoleFillingAlgorithm()
    {}

    /**
     * Applies the hole-filling algorithm by computing new pixel values.
     *
     * @param fields The processed image fields containing hole and boundary pixels.
     * @return A 2D array of Pixels representing the filled image.
     */

    public Pixel[][] evaluatePixels(ProcessedImageFields fields, WeightFunction weightFunction)
    {
        Set<Pixel> hole = fields.getHolePixels();
        Set<Pixel> bound = fields.getBoundaryPixels();
        Pixel[][] pixels = fields.getPixelArray();
        for(Pixel u : hole)
        {
            float denominator = 0;
            float numerator = 0;
            for (Pixel v : bound)
            {
                float currWeight = weightFunction.calculateWeight(u, v);
                numerator += (currWeight * v.getValue());
                denominator += currWeight;
            }
            int uXCord = u.getX();
            int uYCord = u.getY();
            pixels[uXCord][uYCord].setValue(numerator/denominator);
        }
        return pixels;
    }

    /**
     * Returns the name of the algorithm.
     *
     * @return A string representing the algorithm name.
     */

    public String getAlgorithmName()
    {
        return name;
    }
}
