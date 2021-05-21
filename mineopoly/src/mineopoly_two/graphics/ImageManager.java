package mineopoly_two.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ImageManager {
    private Map<String, Image> rawImages;
    private Map<String, Image> scaledImages;
    private int imagesWidth;
    private int imagesHeight;

    /**
     * Creates a new ImageManager and adds all image files in the specified directory and its subdirectories
     *
     * @param imageDirectoryPath The path to the directory containing the image files to load without scaling
     * @throws IOException if an error occurs trying to read from any of the image files
     */
    public ImageManager(String imageDirectoryPath) throws IOException {
        File imageDirectory = new File(imageDirectoryPath);
        assert imageDirectory.exists();
        assert imageDirectory.isDirectory();
        rawImages = new HashMap<>();
        scaledImages = new HashMap<>();

        // Perform a depth-first-search on the image file directory to recursively find all image files
        ImageFileFilter imageFileFilter = new ImageFileFilter();
        Stack<File> imageFileSubdirectories = new Stack<>();
        imageFileSubdirectories.push(imageDirectory);
        while (!imageFileSubdirectories.empty()) {
            File currentImageSubdirectory = imageFileSubdirectories.pop();

            for (File imageFileOrDirectory : currentImageSubdirectory.listFiles()) {
                if (imageFileOrDirectory.isDirectory()) {
                    // Subdirectory with image files found
                    imageFileSubdirectories.push(imageFileOrDirectory);
                    continue;
                }

                // Verify file is an image
                String fileName = imageFileOrDirectory.getName();
                boolean isImageFile = imageFileFilter.accept(imageFileOrDirectory, fileName);
                if (!isImageFile) {
                    continue;
                }
                byte[] imageFileContents = Files.readAllBytes(imageFileOrDirectory.toPath());
                ImageIcon loadedImage = new ImageIcon(imageFileContents);

                // Map images without the file extension, example: "diamond_item.png" -> "diamond_item"
                String imageFileName = imageFileOrDirectory.getName();
                String imageName = imageFileName.substring(0, imageFileName.indexOf('.'));
                if (rawImages.containsKey(imageName)) {
                    System.err.println("Warning: multiple images with name " + imageName);
                }
                rawImages.put(imageName, loadedImage.getImage());
            }
        }

        // Can't know what image scale is needed to start with
        this.imagesWidth = 0;
        this.imagesWidth = 0;
    }

    /**
     * This class currently assumes that all images managed have the same width and height for simplicity.
     * This method returns the shared width that all images in this ImageManager have
     *
     * @return The width of all images
     */
    public int getImageWidth() {
        return imagesWidth;
    }

    /**
     * This class currently assumes that all images managed have the same width and height for simplicity.
     * This method returns the shared height that all images in this ImageManager have
     *
     * @return The height of all images
     */
    public int getImageHeight() {
        return imagesHeight;
    }

    /**
     * Clears the cache of already scaled images and resizes them to the new dimensions from the raw image data.
     *
     * @param newWidth The new width for all scaled images in this ImageManager
     * @param newHeight The new height for all scaled images in this ImageManager
     */
    public void rescaleImages(int newWidth, int newHeight) {
        if (newWidth == this.imagesWidth && newHeight == this.imagesHeight) {
            // Images are already scaled to this size
            return;
        }

        scaledImages.clear();
        this.imagesWidth = newWidth;
        this.imagesHeight = newHeight;
    }

    private Image rescaleImage(Image imageToRescale, int newWidth, int newHeight) {
        ImageIcon rescaledImage = new ImageIcon(imageToRescale.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));
        return rescaledImage.getImage();
    }

    /**
     * Gets the scaled image corresponding to the name parameter
     *
     * @param imageName The name of the scaled image to find
     * @return The scaled image with the specified name, or null if no image by that name exists in the scaled cache
     */
    public Image getScaledImage(String imageName) {
        if (this.scaledImages.containsKey(imageName)) {
            return scaledImages.get(imageName);
        }

        // We might have the raw image and it just hasn't been scaled and cached yet
        if (this.hasImageWithName(imageName)) {
            Image rawImage = rawImages.get(imageName);
            Image rescaledImage = this.rescaleImage(rawImage, imagesWidth, imagesHeight);
            scaledImages.put(imageName, rescaledImage);
            return rescaledImage;
        }
        // No image by that name exists
        return null;
    }

    /**
     * Checks if an image with the specified name exists in this ImageManager
     *
     * @param imageName The name of the image to find
     * @return True if a raw image with the specified name exists, false otherwise
     */
    public boolean hasImageWithName(String imageName) {
        return rawImages.containsKey(imageName);
    }

    /**
     * Adds an image to the raw image data and the scaled image cache under the specified name
     *
     * @param imageName The name of the image to add
     * @param imageToAdd The ImageIcon object representing the image to add
     */
    public void addImageFromIcon(String imageName, ImageIcon imageToAdd) {
        Image rawImageToAdd = imageToAdd.getImage();
        this.rawImages.put(imageName, rawImageToAdd);

        boolean widthMatches = (imageToAdd.getIconWidth() == this.imagesWidth);
        boolean heightMatches = (imageToAdd.getIconHeight() == this.imagesHeight);
        if (widthMatches && heightMatches) {
            // Already the necessary size
            this.scaledImages.put(imageName, rawImageToAdd);
            return;
        }
        // Rescaling required
        this.scaledImages.put(imageName, rescaleImage(rawImageToAdd, this.imagesWidth, this.imagesHeight));
    }

    /**
     * Utility method for generating an (imagesWidth * imagesHeight) dimension Image of a single solid color.
     *
     * @param backgroundColor The Color object representing the solid color to fill an Image with
     * @return an ImageIcon for a single solid color
     */
    public ImageIcon generateBackgroundImageIcon(Color backgroundColor) {
        BufferedImage bufferedImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D brush = bufferedImage.createGraphics();
        brush.setColor(backgroundColor);
        brush.fillRect(0, 0, imagesWidth, imagesHeight);
        return new ImageIcon(bufferedImage);
    }
}
