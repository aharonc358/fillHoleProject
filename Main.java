package CommandLineUtility;

import lib.ImageLibraryManager;
import lib.entities.Pixel;
import lib.algorithms.AlgorithmManager;
import lib.entities.ProcessedImageFields;


/**
 * Main class for executing the Hole Filling algorithm from the command line.
 * It validates input parameters, initializes necessary components,
 * and performs the hole-filling process on an image.
 */

public class Main {
    private static final int EIGHT_CONNECTED = 8;
    private static final int FOUR_CONNECTED = 4;


    private static final int VALID_NUM_OF_ARGS = 5;
    private static final String USAGE_NUM_OF_ARGS = "Usage: java CommandLineUtility needs 5 " +
            "arguments";
    private static final String MISSING_ARGUMENT_ERROR_MSG = "Missing arguments\nArgs:" +
            " [image path] [mask path] [z] [epsilon] " +
            "[pixel connectivity: 4/8]";
    private static final String INVALID_CONNECTIVITY_TYPE = "Invalid connectivity type";
    private static final String NUMBER_FORMAT_ERROR = "ERROR: invalid number error";
    private static final String INVALID_ARGS_ERROR = "Invalid arguments";
    private static final int ZARG = 3;
    private static final int CONNECTARG = 2;
    private static final int E_ARG = 4;

    /**
     * The main entry point for the application.
     * Validates input arguments and performs the hole-filling algorithm.
     *
     * @param args Command-line arguments: [image path] [mask path] [z] [epsilon]
     *             [pixel connectivity]
     */

    public static void main(String[] args)
    {
        if (args.length != VALID_NUM_OF_ARGS) {
            System.out.println(USAGE_NUM_OF_ARGS);
            return;
        }
        try
        {
            int connectivity = Integer.parseInt(args[CONNECTARG]);
            float e = Float.parseFloat(args[E_ARG]);
            int z = Integer.parseInt(args[ZARG]);
            String mask = args[1];
            String path = args[0];
            if (connectivity != FOUR_CONNECTED && connectivity != EIGHT_CONNECTED)
            {
                throw new Exception(INVALID_CONNECTIVITY_TYPE);
            }
            ImageLibraryManager imgManager = new ImageLibraryManager();
            ProcessedImageFields processedImageFields = imgManager.processImage(path, mask,
                    connectivity);
            AlgorithmManager algorithmManager = new AlgorithmManager(connectivity, z, e);
            Pixel[][] filledImage = algorithmManager.runAlgorithm(processedImageFields);
            imgManager.saveImage(filledImage, path);
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException e)
        {
            System.out.println(MISSING_ARGUMENT_ERROR_MSG);
            System.exit(1);
        }
        catch (NumberFormatException e)
        {
            System.out.println(NUMBER_FORMAT_ERROR);
            System.exit(1);
        }
        catch (Exception e)
        {
            System.out.println(INVALID_ARGS_ERROR);
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
