import java.awt.Color;
import edu.princeton.cs.algs4.Picture;


public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture is null.");
        }
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validPixel(x, y);
        if (isBoundary(x, y)) {
            return 1000;
        }
        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        Color upper = picture.get(x, y + 1);
        Color lower = picture.get(x, y - 1);
        double energy = 0;
        energy += Math.pow(left.getRed() - right.getRed(), 2);
        energy += Math.pow(left.getGreen() - right.getGreen(), 2);
        energy += Math.pow(left.getBlue() - right.getBlue(), 2);
        energy += Math.pow(lower.getRed() - upper.getRed(), 2);
        energy += Math.pow(lower.getGreen() - upper.getGreen(), 2);
        energy += Math.pow(lower.getBlue() - upper.getBlue(), 2);
        return Math.sqrt(energy);
    }


    private boolean isBoundary(int x, int y) {
        return x == 0 || x == width - 1 || y == 0 || y == height - 1;
    }

    private void validPixel(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Invalid pixel indices.");
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // record the sum of energy along possible seams from left boundary to the pixel so far.
        double[][] energyTo = new double[width][height];
        // record the left pixel's height index along possible seams
        int[][] prevHeightIndex = new int[width][height];
        for (int h = 0; h < height; h++) {
            energyTo[0][h] = 1000;  // energyTo of leftmost pixels = 1000
        }
        for (int w = 1; w < width; w++) {
            for (int h = 0; h < height; h++) {
                energyTo[w][h] = Double.POSITIVE_INFINITY;
            }
        }

        for (int w = 0; w < width - 1; w++) {
            // relax pixels of next (right) column until one step before right boundary
            for (int h = 1; h < height - 1; h++) {
                // skip relax(for next step) of top & bottom boundary pixels
                relaxHorizontal(w, h, energyTo, prevHeightIndex);
            }
        }

        // Find the height index of the seam on right boundary
        double minSeamEnergy = Double.POSITIVE_INFINITY;
        int heightIndex = 0;
        for (int h = 1; h < height - 1; h++) {
            if (energyTo[width - 1][h] < minSeamEnergy) {
                minSeamEnergy = energyTo[width - 1][h];
                heightIndex = h;
            }
        }

        // Find the height indexes along the seam from right to left
        int[] horizontalSeam = new int[width];
        for (int w = width -1; w >= 0; w--) {
            horizontalSeam[w] = heightIndex;
            heightIndex = prevHeightIndex[w][heightIndex];
        }
        return horizontalSeam;
    }

    // relax next possible pixel along the horizontal seam (upper right /right /lower right)
    private void relaxHorizontal(int w, int h, double[][] energyTo, int[][] prevHeightIndex) {
        for (int i = -1; i <= 1; i++) {
            double energyToNextPixel = energyTo[w][h] + energy(w + 1, h + i);
            if (energyTo[w + 1][h + i] > energyToNextPixel) {
                energyTo[w + 1][h + i] = energyToNextPixel;
                prevHeightIndex[w + 1][h + i] = h;
            }
        }
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // record the sum of energy along possible seams from top boundary to the pixel so far
        double[][] energyTo = new double[width][height];
        // record the upper pixel's width index along possible seams
        int[][] prevWidthIndex = new int[width][height];
        for (int w = 0; w < width; w++) {
            energyTo[w][0] = 1000; // energyTo of topmost pixels = 1000
        }
        for (int h = 1; h < height; h++) {
            for (int w = 0; w < width; w++) {
                energyTo[w][h] = Double.POSITIVE_INFINITY;
            }
        }
        for (int h = 0; h < height - 1; h++) {
            // relax pixels of next (lower) row until one step before bottom boundary
            for (int w = 1; w < width - 1; w++) {
                // skip relax(for next step) of left & right boundary pixels
                relaxVertical(w, h, energyTo, prevWidthIndex);
            }
        }

        // Find the height index of the seam on bottom boundary
        double minSeamEnergy = Double.POSITIVE_INFINITY;
        int widthIndex = 0;
        for (int w = 1; w < width - 1; w++) {
            if (energyTo[w][height - 1] < minSeamEnergy) {
                minSeamEnergy = energyTo[w][height - 1];
                widthIndex = w;
            }
        }

        // Find the height indexes along the seam from bottom to top
        int[] verticalSeam = new int[height];
        for (int h = height -1; h >= 0; h--) {
            verticalSeam[h] = widthIndex;
            widthIndex = prevWidthIndex[widthIndex][h];
        }
        return verticalSeam;
    }

    // relax next possible pixel along the vertical seam (lower left /lower /lower right)
    private void relaxVertical(int w, int h, double[][] energyTo, int[][] prevWidthIndex) {
        for (int i = -1; i <= 1; i++) {
            double energyToNextPixel = energyTo[w][h] + energy(w + i, h + 1);
            if (energyTo[w + i][h + 1] > energyToNextPixel) {
                energyTo[w + i][h + 1] = energyToNextPixel;
                prevWidthIndex[w + i][h + 1] = w;
            }
        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validSeam(seam, width, height);
        Picture carved = new Picture(width, height - 1);
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < seam[w]; h++) {
                carved.set(w, h, picture.get(w, h));
            }
            for (int h = seam[w]; h < height() - 1; h++) {
                carved.set(w, h, picture.get(w, h + 1));
            }
        }
        picture = carved;
        height--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validSeam(seam, height, width);
        Picture carved = new Picture(width - 1, height);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < seam[h]; w++) {
                carved.set(w, h, picture.get(w, h));
            }
            for (int w = seam[h]; w < width() - 1; w++) {
                carved.set(w, h, picture.get(w + 1, h));
            }
        }
        picture = carved;
        width--;
    }

    private void validSeam(int[] seam, int length, int range) {
        if (seam == null || seam.length != length || range <= 1) {
            throw new IllegalArgumentException("Invalid seam.");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= range) {
                throw new IllegalArgumentException("seam entry out of boundary.");
            }
            if (i < seam.length - 1 && Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("Seam entries not adjacent.");
            }
        }
    }

}
