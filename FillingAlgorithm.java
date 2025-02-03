package lib.algorithms;

import lib.entities.Pixel;
import lib.entities.ProcessedImageFields;

/**
 * Abstract class defining the interface for hole-filling algorithms.
 * Subclasses must implement the algorithm logic for processing hole pixels.
 */

public abstract class FillingAlgorithm
{
    /**
     * Evaluates the pixels and applies the hole-filling algorithm.
     * Default implementation returns the input pixel array.
     *
     * @param fields Processed image data containing hole and boundary pixels.
     * @return A 2D array of pixels representing the filled image.
     */
    public Pixel[][] evaluatePixels(ProcessedImageFields fields,
                                    WeightFunction weightFunction){return fields.getPixelArray();}

    /**
     * Returns the name of the algorithm.
     *
     * @return A string representing the algorithm name.
     */

    public String getAlgorithmName()
    {
        return "";
    }
}
