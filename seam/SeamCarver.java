import edu.princeton.cs.algs4.Picture;


public class SeamCarver {
    private Picture picture;
    private double[][] energy;
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
        energy = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture pictureNow = new Picture(width, height);  // resize picture
        double[][] energyNow = new double[width][height]; // resize energy cache
        for (int x = 0; x < width; x++) {
            System.arraycopy(energy[x], 0, energyNow[x], 0, height);
            for (int y = 0; y < height; y++) {
                pictureNow.setRGB(x, y, picture.getRGB(x, y));
            }
        }
        picture = pictureNow;
        energy = energyNow;
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
        int left = picture.get(x - 1, y).getRGB();
        int right = picture.get(x + 1, y).getRGB();
        int upper = picture.get(x, y + 1).getRGB();
        int lower = picture.get(x, y - 1).getRGB();
        double e = 0;
        e += Math.pow((left >> 16 & 0xFF) - (right >> 16 & 0xFF), 2);  // red
        e += Math.pow((left >> 8 & 0xFF) - (right >> 8 & 0xFF), 2);    // green
        e += Math.pow((left & 0xFF) - (right & 0xFF), 2);              // blue
        e += Math.pow((lower >> 16 & 0xFF) - (upper >> 16 & 0xFF), 2);
        e += Math.pow((lower >> 8 & 0xFF) - (upper >> 8 & 0xFF), 2);
        e += Math.pow((lower & 0xFF) - (upper & 0xFF), 2);
        return Math.sqrt(e);
    }


    private boolean isBoundary(int x, int y) {
        return x == 0 || x == width - 1 || y == 0 || y == height - 1;
    }

    // throw an IllegalArgumentException for invalid pixel index
    private void validPixel(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Invalid pixel indices.");
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // record the sum of energy along possible seams from left boundary to the pixel so far.
        double[][] energyTo = new double[width][height];
        // record the left pixel's row index along possible seams
        int[][] prevRowIndex = new int[width][height];
        for (int y = 0; y < height; y++) {
            energyTo[0][y] = 1000;  // energyTo of leftmost pixels = 1000
        }
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energyTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 0; x < width - 1; x++) {
            // relax pixels of next (right) column until one step before right boundary
            for (int y = 1; y < height - 1; y++) {
                // skip relax(for next step) of top & bottom boundary pixels
                relaxHorizontal(x, y, energyTo, prevRowIndex);
            }
        }

        // Find the row index of the seam on right boundary
        double minSeamEnergy = Double.POSITIVE_INFINITY;
        int rowIndex = 0;
        for (int y = 1; y < height - 1; y++) {
            if (energyTo[width - 1][y] < minSeamEnergy) {
                minSeamEnergy = energyTo[width - 1][y];
                rowIndex = y;
            }
        }

        // Find the row indexes along the seam from right to left
        int[] horizontalSeam = new int[width];
        for (int x = width - 1; x >= 0; x--) {
            horizontalSeam[x] = rowIndex;
            rowIndex = prevRowIndex[x][rowIndex];
        }
        return horizontalSeam;
    }

    // relax next possible pixel along the horizontal seam (upper right /right /lower right)
    private void relaxHorizontal(int x, int y, double[][] energyTo, int[][] prevRowIndex) {
        for (int i = -1; i <= 1; i++) {
            double energyToNextPixel = energyTo[x][y] + energy[x + 1][y + i];
            if (energyTo[x + 1][y + i] > energyToNextPixel) {
                energyTo[x + 1][y + i] = energyToNextPixel;
                prevRowIndex[x + 1][y + i] = y;
            }
        }
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // record the sum of energy along possible seams from top boundary to the pixel so far
        double[][] energyTo = new double[width][height];
        // record the upper pixel's width index along possible seams
        int[][] prevColIndex = new int[width][height];
        for (int x = 0; x < width; x++) {
            energyTo[x][0] = 1000; // energyTo of topmost pixels = 1000
        }
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                energyTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }
        for (int y = 0; y < height - 1; y++) {
            // relax pixels of next (lower) row until one step before bottom boundary
            for (int x = 1; x < width - 1; x++) {
                // skip relax(for next step) of left & right boundary pixels
                relaxVertical(x, y, energyTo, prevColIndex);
            }
        }

        // Find the height index of the seam on bottom boundary
        double minSeamEnergy = Double.POSITIVE_INFINITY;
        int colIndex = 0;
        for (int x = 1; x < width - 1; x++) {
            if (energyTo[x][height - 1] < minSeamEnergy) {
                minSeamEnergy = energyTo[x][height - 1];
                colIndex = x;
            }
        }

        // Find the height indexes along the seam from bottom to top
        int[] verticalSeam = new int[height];
        for (int y = height -1; y >= 0; y--) {
            verticalSeam[y] = colIndex;
            colIndex = prevColIndex[colIndex][y];
        }
        return verticalSeam;
    }

    // relax next possible pixel along the vertical seam (lower left /lower /lower right)
    private void relaxVertical(int x, int y, double[][] energyTo, int[][] prevColIndex) {
        for (int i = -1; i <= 1; i++) {
            double energyToNextPixel = energyTo[x][y] + energy[x + i][y + 1];
            if (energyTo[x + i][y + 1] > energyToNextPixel) {
                energyTo[x + i][y + 1] = energyToNextPixel;
                prevColIndex[x + i][y + 1] = x;
            }
        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validSeam(seam, width, height);
        height--;
        for (int x = 0; x < width; x++) {
            for (int y = seam[x]; y < height; y++) {
                picture.setRGB(x, y, picture.getRGB(x, y + 1));
                energy[x][y] = energy[x][y + 1];
            }
        }
        // recalculate energy of pixels along the seam
        for (int x = 0; x < width; x++) {
            if (seam[x] > 0) {
                energy[x][seam[x] - 1] = energy(x, seam[x] - 1);
            }
            if (seam[x] < height) {
                energy[x][seam[x]] = energy(x, seam[x]);
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validSeam(seam, height, width);
        width--;
        for (int y = 0; y < height; y++) {
            for (int x = seam[y]; x < width; x++) {
                picture.setRGB(x, y, picture.getRGB(x + 1, y));
                energy[x][y] = energy[x + 1][y];
            }
        }
        // recalculate energy of pixels along the seam
        for (int y = 0; y < height; y++) {
            if (seam[y] > 0) {
                energy[seam[y] - 1][y] = energy(seam[y] - 1, y);
            }
            if (seam[y] < width) {
                energy[seam[y]][y] = energy(seam[y], y);
            }
        }
    }

    // throw an IllegalArgumentException for invalid seam
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
