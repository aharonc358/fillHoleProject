package lib.algorithms;

import lib.entities.Pixel;
import lib.entities.ProcessedImageFields;

/**
 * Manages the execution of the hole-filling algorithm.
 * Handles weight functions, algorithm selection, and execution flow.
 */

public class AlgorithmManager
{
    private static final String APPROXIMATE_ALGORITHM = "ApproximateAlgorithm";

    private final int connectivity;
    private int z;
    private float e;

    private WeightFunction weightFunc;
    private FillingAlgorithm algorithm;

    /**
     * Constructs an AlgorithmManager and initializes the default hole-filling algorithm, with a
     * default Weight function.
     *
     * @param z The exponent parameter for the weight function.
     * @param e A small constant added to avoid division by zero.
     */

    public AlgorithmManager(int connectivity, int z, float e)
    {
        this.connectivity = connectivity;
        this.z = z;
        this.e = e;
        this.weightFunc = (Pixel u, Pixel v) -> ( 1.0f / (float) (Math.pow(defaultCalculate(u,v),
                z) + e));
        this.algorithm = new DefaultHoleFillingAlgorithm();

    }

    /**
     * Runs the selected hole-filling algorithm.
     *
     * @param fields The processed image fields containing hole and boundary pixels.
     * @return A 2D array of Pixels representing the filled image.
     */

    public Pixel[][] runAlgorithm(ProcessedImageFields fields)
    {
        return algorithm.evaluatePixels(fields, this.weightFunc);
    }

    /**
     * Retrieves the current weight function used in the algorithm.
     *
     * @return The weight function.
     */

    public WeightFunction getWeightFunc()
    {
        return this.weightFunc;
    }

    /**
     * Sets a custom weight function for the algorithm.
     *
     * @param weightFunc The new weight function to be applied.
     */

    public void setWeightFunc(WeightFunction weightFunc)
    {
        if (weightFunc == null)
        {
            System.out.println("WeightFunc is null!");
            System.exit(1);
            return;
        }
        this.weightFunc = weightFunc;
    }

    /**
     * Sets the algorithm based on the provided name.
     *
     * @param algorithmName The name of the algorithm to be used.
     */

    public void algorithmFactory(String algorithmName)
    {
        if (algorithmName.equals(APPROXIMATE_ALGORITHM))
        {
            this.algorithm = new ApproximateAlgorithm(connectivity);
        }
        else
        {
            this.algorithm = new DefaultHoleFillingAlgorithm();
        }
    }

    /**
     * Sets the exponent parameter for the weight function.
     *
     * @param z The new exponent value.
     */

    public void setZ(int z)
    {
        this.z = z;
    }

    /**
     * Sets the small constant value for the weight function.
     *
     * @param e The new small constant value.
     */

    public void setE(float e)
    {
        this.e = e;
    }

    /**
     * Computes the Euclidean distance between two pixels.
     *
     * @param u The first pixel.
     * @param v The second pixel.
     * @return The calculated distance as a float.
     */

    public float defaultCalculate(Pixel u, Pixel v) {
        float xVal = u.getX() - v.getX();
        float yVal = u.getY() - v.getY();

        return (float) (Math.sqrt(xVal * xVal + yVal * yVal));
    }

    /**
     * Gets the current algorithm the object uses.
     * @return the algorithm's name
     */

    public String getAlgorithmName()
    {
        return this.algorithm.getAlgorithmName();
    }

    /**
     * Getter for z.
     * @return z.
     */

    public int getZ() {
        return z;
    }

    /**
     * Getter for e
     * @return e.
     */

    public float getE() {
        return e;
    }
}
