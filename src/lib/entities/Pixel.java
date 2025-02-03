package lib.entities;

import java.util.Objects;

/**
 * Represents an individual pixel in an image.
 * Stores the pixel's x and y coordinates along with its grayscale intensity value.
 */

public class Pixel {
    private final int x;
    private final int y;
    private float value;

    /**
     * Constructs a Pixel object with specified coordinates and value.
     *
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @param value The grayscale intensity value of the pixel.
     */

    public Pixel(int x, int y, float value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    /**
     * Gets the x-coordinate of the pixel.
     *
     * @return The x-coordinate.
     */

    public int getX() { return x; }

    /**
     * Gets the y-coordinate of the pixel.
     *
     * @return The y-coordinate.
     */

    public int getY() { return y; }

    /**
     * Gets the grayscale intensity value of the pixel.
     *
     * @return The intensity value as a float.
     */

    public float getValue() { return value; }

    /**
     * Sets a new grayscale intensity value for the pixel.
     *
     * @param value The new intensity value.
     */

    public void setValue(float value)
    {
        this.value = value;
    }

    /**
     * Returns a string representation of the pixel's intensity value.
     *
     * @return The intensity value as a string.
     */

    @Override
    public String toString()
    {
        return "" + value;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pixel pixel = (Pixel) obj;
        return x == pixel.x && y == pixel.y && Float.compare(pixel.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, value);
    }
}
