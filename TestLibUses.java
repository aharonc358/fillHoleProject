package CommandLineUtility;

import lib.ImageLibraryManager;
import lib.algorithms.AlgorithmManager;
import lib.algorithms.WeightFunction;
import lib.entities.Pixel;
import lib.entities.ProcessedImageFields;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestLibUses {
    private static final String INPUT_IMAGE_PATH = "TestImage.jpg";
    private static final String MASK_OUTPUT_PATH = "outPutMask.jpg";
    private static final String TEST_OUTPUT_PATH = "outPutTest.jpg";
    private static final int CONNECTIVITY = 8;
    private static final int Z = 3;
    private static final float E = 0.01f;
    private static final String APPROX_ALG = "ApproximateAlgorithm";
    private static final String APPROX_PREFIX = "approx_";
    private static final String CHANGED_WEIGHT_PREFIX = "changedWeight_";
    private static final String VARS_DIDNT_CHANGE = "Invalid vals. Didnt change.";
    private static final String LAST_PREFIX = "last ";
    private static final String PASSED_MSG = "Passed!";
    private static final String ERROR_MINUS_ONE_DETECTED = "Error: -1 pixel detected";
    private static final String ERROR_ALG_DIDT_CAHNGE_NAME = "Error: Algorithm name did not " +
            "change to ";
    private static final String ERROR_WEIGHT_FUNC_DIDNT_CHANGED = "Error: Weight function did not" +
            " change";
    private static final String PNG = "png";
    private static final String MASK_SAVE_MSG = "Mask image saved at: ";
    private static final String ERROR_PROCESS = "Error processing image: ";

    public static void main(String[] args) {
        generateMaskFromImage();

        ImageLibraryManager imgManager = new ImageLibraryManager();
        AlgorithmManager algorithmManager = new AlgorithmManager(CONNECTIVITY, Z, E);

        // Run original algorithm
        ProcessedImageFields processedImageFields = imgManager.processImage(INPUT_IMAGE_PATH,
                MASK_OUTPUT_PATH, CONNECTIVITY);
        Pixel[][] filledImage = algorithmManager.runAlgorithm(processedImageFields);
        imgManager.saveImage(filledImage, TEST_OUTPUT_PATH);
        validateFilledImage(filledImage);

        // Run Approximate Algorithm

        algorithmManager.algorithmFactory(APPROX_ALG);
        validateAlgorithmName(algorithmManager);
        ProcessedImageFields processedImageFieldsApprox =
                imgManager.processImage(INPUT_IMAGE_PATH, MASK_OUTPUT_PATH, CONNECTIVITY);
        Pixel[][] filledImageApprox = algorithmManager.runAlgorithm(processedImageFieldsApprox);
        imgManager.saveImage(filledImageApprox, APPROX_PREFIX + TEST_OUTPUT_PATH);
        validateFilledImage(filledImageApprox);

        // Change Weight Function and Run Again

        updateWeightFunction(algorithmManager);
        ProcessedImageFields processedImageFieldsWeightFunc =
                imgManager.processImage(INPUT_IMAGE_PATH, MASK_OUTPUT_PATH, CONNECTIVITY);
        Pixel[][] filledImageWeightFunc =
                algorithmManager.runAlgorithm(processedImageFieldsWeightFunc);
        validateFilledImage(filledImageWeightFunc);
        imgManager.saveImage(filledImageWeightFunc, CHANGED_WEIGHT_PREFIX + TEST_OUTPUT_PATH);

//        check if possible to change e,z.

        algorithmManager.setE(0.02f);
        algorithmManager.setZ(4);
        if (E == algorithmManager.getE() || Z == algorithmManager.getZ()) {
            System.out.println(VARS_DIDNT_CHANGE);
            System.exit(1);
        }
        ProcessedImageFields procesedCheckSetValsAndRestoreSettings =
                imgManager.processImage(INPUT_IMAGE_PATH,
                        MASK_OUTPUT_PATH,
                        CONNECTIVITY);
        Pixel[][] lastout = algorithmManager.runAlgorithm(procesedCheckSetValsAndRestoreSettings);
        imgManager.saveImage(lastout, LAST_PREFIX + TEST_OUTPUT_PATH);
        validateFilledImage(lastout);
        System.out.println(PASSED_MSG);
    }

    private static void validateFilledImage(Pixel[][] filledImage) {
        for (Pixel[] row : filledImage) {
            for (Pixel pixel : row) {
                if (pixel.getValue() == -1f) {
                    System.out.println(ERROR_MINUS_ONE_DETECTED);
                    System.exit(1);
                }
            }
        }
    }

    private static void validateAlgorithmName(AlgorithmManager algorithmManager) {
        if (!algorithmManager.getAlgorithmName().equals(APPROX_ALG)) {
            System.out.println(algorithmManager.getAlgorithmName());
            System.out.println(ERROR_ALG_DIDT_CAHNGE_NAME + APPROX_ALG);
        }
    }

    private static void updateWeightFunction(AlgorithmManager algorithmManager) {
        WeightFunction originalWeightFunc = algorithmManager.getWeightFunc();
        algorithmManager.setWeightFunc((u, v) -> (float) Math.pow(((double) (u.getY() * v.getY()) + (u.getX() - v.getX() + E)), 9.0 / Z));
        WeightFunction newWeightFunc = algorithmManager.getWeightFunc();

        if (originalWeightFunc.equals(newWeightFunc)) {
            System.out.println(ERROR_WEIGHT_FUNC_DIDNT_CHANGED);
        }
    }

    private static void generateMaskFromImage() {
        try {
            BufferedImage image = ImageIO.read(new File(TestLibUses.INPUT_IMAGE_PATH));
            BufferedImage mask = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    mask.setRGB(i, j, (i >= 1000 && i <= 1300 && j >= 2000 && j <= 2300) ?
                            Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            ImageIO.write(mask, PNG, new File(TestLibUses.MASK_OUTPUT_PATH));
            System.out.println(MASK_SAVE_MSG+ TestLibUses.MASK_OUTPUT_PATH);
        } catch (IOException e) {
            System.err.println(ERROR_PROCESS + e.getMessage());
            System.exit(1);
        }
    }
}
