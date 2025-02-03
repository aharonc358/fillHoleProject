package lib.entities;

import java.util.HashSet;

/**
 * Stores processed image data, including the pixel array, hole pixels, and boundary pixels.
 * This class is used to encapsulate all necessary data after image preprocessing.
 */

public class ProcessedImageFields {
    private final Pixel[][] pixelArray;
    private final HashSet<Pixel> holePixels;
    private final HashSet<Pixel> boundaryPixels;

    /**
     * Constructs a ProcessedImageFields object.
     *
     * @param pixelArray The 2D array of pixels representing the image.
     * @param holePixels A set containing all pixels identified as part of a hole.
     * @param boundaryPixels A set containing all pixels identified as boundary pixels.
     */

    public ProcessedImageFields(Pixel[][] pixelArray, HashSet<Pixel> holePixels,
                                HashSet<Pixel> boundaryPixels) {
        this.pixelArray = pixelArray;
        this.holePixels = holePixels;
        this.boundaryPixels = boundaryPixels;
    }

    /**
     * Returns the 2D pixel array representing the processed image.
     *
     * @return A 2D array of Pixel objects.
     */

    public Pixel[][] getPixelArray() { return pixelArray; }

    /**
     * Returns the set of hole pixels in the image.
     *
     * @return A set containing Pixel objects representing hole pixels.
     */

    public HashSet<Pixel> getHolePixels() { return holePixels; }

    /**
     * Returns the set of boundary pixels in the image.
     *
     * @return A set containing Pixel objects representing boundary pixels.
     */

    public HashSet<Pixel> getBoundaryPixels() { return boundaryPixels; }
}
