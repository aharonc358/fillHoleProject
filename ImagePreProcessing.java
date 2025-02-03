package lib;

import lib.entities.Pixel;
import lib.entities.ProcessedImageFields;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.util.HashSet;

/**
 * Handles preprocessing of an image and its corresponding mask.
 * Converts images to grayscale, identifies hole pixels, and detects boundary pixels.
 */

public class ImagePreProcessing {
    private static final float THRESHOLD_FOR_MASKING = 0.5f;
    private static final int MAX_NUM_COLOR = 255;
    private static final double RED_FACTOR = 0.299;
    private static final double GREEN_FACTOR = 0.587;
    private static final double BLUE_FACTOR = 0.114;

    private static final float HOLE_VALUE = -1f;
    private final int connectivity;
    private final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}, {-1, -1}, {-1, 1}, {1,
            -1}, {1, 1}

    };

    /**
     * Constructor for ImagePreProcessing class.
     *
     * @param connectivity Defines whether to use 4-connectivity or 8-connectivity.
     * @throws IOException If an error occurs in image processing.
     */

    public ImagePreProcessing(int connectivity) throws IOException {
        this.connectivity = connectivity;
    }

    /**
     * Processes the input image and mask to generate hole and boundary pixel data.
     *
     * @param imageToEditPath Path to the image that needs hole filling.
     * @param maskPath        Path to the mask image.
     * @return ProcessedImageFields object containing pixel data, holes, and boundary pixels.
     * @throws IOException If an error occurs while reading images.
     */

    public ProcessedImageFields preProcessImage(String imageToEditPath, String maskPath) throws
            IOException {
        HashSet<Pixel> holeSet = new HashSet<>();
        HashSet<Pixel> boundarySet = new HashSet<>();

        BufferedImage imageToEdit = ImageIO.read(new File(imageToEditPath));
        BufferedImage mask = ImageIO.read(new File(maskPath));

        int width = mask.getWidth();
        int height = mask.getHeight();

        if (width != imageToEdit.getWidth() || height != imageToEdit.getHeight()) {
            throw new IOException();
        }

        Pixel[][] pixelArr = new Pixel[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color colorOfMaskPixel = new Color(mask.getRGB(col, row));
                float intensityOfMaskPixel = convertToGrayScale(colorOfMaskPixel);
                float valueOfImagePixel = convertToGrayScale(new Color(imageToEdit.getRGB(col,
                        row)));

                if (intensityOfMaskPixel < THRESHOLD_FOR_MASKING) {
                    valueOfImagePixel = HOLE_VALUE;
                }

                pixelArr[row][col] = new Pixel(row, col, valueOfImagePixel);

                if (valueOfImagePixel == HOLE_VALUE) {
                    holeSet.add(pixelArr[row][col]);
                } else {
                    if (isBoundary(pixelArr, mask, row, col)) {
                        boundarySet.add(pixelArr[row][col]);
                    }
                }
            }
        }
        return new ProcessedImageFields(pixelArr, holeSet, boundarySet);
    }

    /**
     * Determines if a pixel is part of the boundary of the hole.
     *
     * @param pixelArray The processed image pixel array.
     * @param mask       The mask image.
     * @param rowIdx     Row index of the pixel.
     * @param colIdx     Column index of the pixel.
     * @return True if the pixel is a boundary pixel, otherwise false.
     */

    private boolean isBoundary(Pixel[][] pixelArray, BufferedImage mask, int rowIdx, int colIdx) {
        for (int connection = 0; connection < connectivity; connection++) {
            int row = rowIdx + DIRECTIONS[connection][0];
            int col = colIdx + DIRECTIONS[connection][1];
            if (row < 0 || row >= pixelArray.length || col < 0 || col >= pixelArray[0].length) {
                continue;
            }
            Color color = new Color(mask.getRGB(col, row));
            float colorVal = convertToGrayScale(color);
            if (colorVal < THRESHOLD_FOR_MASKING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts a color to grayscale using weighted RGB values.
     *
     * @param c The color to be converted.
     * @return The grayscale intensity value normalized between 0 and 1.
     */

    private float convertToGrayScale(Color c) {

        float avgForGrayScale =
                (float) (((c.getRed() * RED_FACTOR) + (c.getGreen() * GREEN_FACTOR) +
                        (c.getBlue() * BLUE_FACTOR)));
        return avgForGrayScale / MAX_NUM_COLOR;
    }
}