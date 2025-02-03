package lib;

import lib.entities.Pixel;
import lib.entities.ProcessedImageFields;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages image processing operations such as reading, validating,
 * processing, and saving images after applying the hole-filling algorithm.
 */

public class ImageLibraryManager
{
    private static final String INVALID_IMAGE_ERROR_MSG = "Error: Invalid image or mask path.";
    private static final String READ_FAILED_ERROR_MSG = "Failed to read image with error: ";
    private static final String FILLED = "_FILLED.";
    private static final String SAVING_IMAGE_MSG = "Saving image with format:";
    private static final String IMAGE_SAVED_MSG = "Image saved successfully: ";
    private static final String FAILED_TO_SAVE_IMG_MSG = "Failed to save image.";
    private static final String INVALID_FILE_FORMAT = "Invalid image format: ";
    private static final String REGEX_FOR_FORMAT = "(.*)\\.(.*)";

    /**
     * Constructs an ImageLibraryManager with specified algorithm parameters.
     */

    public  ImageLibraryManager() {}

    /**
     * Processes the input image and mask, extracting hole and boundary pixels.
     *
     * @param imagePath Path to the image file.
     * @param maskPath Path to the mask file.
     * @param connectivity The pixel connectivity type (4-connected or 8-connected).
     * @return A ProcessedImageFields object containing processed pixel data.
     */

    public ProcessedImageFields processImage(String imagePath, String maskPath, int connectivity)  {
        try
        {
            if (isValidImagePath(imagePath) || isValidImagePath(maskPath))
            {
                System.out.println(INVALID_IMAGE_ERROR_MSG);
                System.exit(1);
            }
            ImagePreProcessing preProcessing = new ImagePreProcessing(connectivity);
            return preProcessing.preProcessImage(imagePath, maskPath);
        }
        catch (IOException e)
        {
            System.out.println(READ_FAILED_ERROR_MSG + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Saves the processed image after applying the hole-filling algorithm.
     *
     * @param pixelArray The 2D array of pixels representing the filled image.
     * @param imagePath The original image path to determine output filename.
     */

    public void saveImage(Pixel[][] pixelArray, String imagePath)
    {
        Pattern p = Pattern.compile(REGEX_FOR_FORMAT);
        Matcher m = p.matcher(imagePath);

        if (!m.find())
        {
            System.out.println(INVALID_FILE_FORMAT);
            System.exit(1);
        }

        String path = m.group(1);
        String format = m.group(2);
        BufferedImage imageToSave = new BufferedImage(pixelArray[0].length, pixelArray.length, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < imageToSave.getWidth(); x++)
        {
            for (int y = 0; y < imageToSave.getHeight(); y++)
            {
                float pixelValue = pixelArray[y][x].getValue();
                Color c = new Color(pixelValue, pixelValue, pixelValue);
                imageToSave.setRGB(x, y, c.getRGB());
            }
        }
        try {
            File output = new File( path + FILLED  + format);
            System.out.println(SAVING_IMAGE_MSG + output);
            boolean success = ImageIO.write(imageToSave,format,output);
            if (success)
            {
                System.out.println(IMAGE_SAVED_MSG + output.getAbsolutePath());
            }
            else
            {
                System.out.println(FAILED_TO_SAVE_IMG_MSG);
            }
        }
        catch (IOException e)
        {
            System.err.println(READ_FAILED_ERROR_MSG + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Validates whether a given image path is valid.
     *
     * @param path The path to check.
     * @return True if the path is invalid, otherwise false.
     */

    private boolean isValidImagePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return true;
        }
        File file = new File(path);
        return !file.exists() || !file.isFile();
    }
}

