package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

/**
 * This utility class is used as an image loader for the application.
 *
 */
public final class ImageLoader {

    private static final String IMAGES_FOLDER = "images/";

    /**
     * Enumeration for all game images.
     */
    public enum GameImage {
        /**
         * Game's icon.
         */
        ICON(IMAGES_FOLDER + "icon.png");

        private final String path;

        /**
         * Creates a new GameImage.
         * 
         * @param path
         */
        GameImage(final String path) {
            this.path = path;
        }

        /**
         * @return the path of the game image.
         */
        public String getPath() {
            return this.path;
        }
    }

    private ImageLoader() { }
    
    /**
     * Creates an ImageIcon from the specified GameImage.
     * 
     * @param img
     *          the GameImage to use
     * @return the specified ImageIcon
     */
    public static ImageIcon createImageIcon(final GameImage img) {
        final java.net.URL imgURL = MainFrame.class.getClassLoader().getResource(img.getPath());
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        throw new IllegalArgumentException("Couldn't find file: " + img.getPath());
    }

    /**
     * Creates an Image from the specified GameImage.
     * 
     * @param img
     *          the GameImage to use
     * @return the specified Image
     */
    public static Image createImage(final GameImage img) {
        return createImageIcon(img).getImage();
    }

    /**
     * Creates an Image with a certain size from the specified GameImage's path.
     * 
     * @param img
     *          the GameImage to use
     * @param width
     *          the width of the image
     * @param height
     *          the height of the image
     * @return the sized Image
     */
    public static Image createImageOfSize(final GameImage img, final int width, final int height) {
        return createImage(img).getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    /**
     * Creates a BufferedImage from the specified GameImage.
     * 
     * @param img
     *          the GameImage to use
     * @return the specified BufferedImage
     */
    public static BufferedImage createBufferedImage(final GameImage img) {
        final ImageIcon icon = createImageIcon(img);
        final BufferedImage bi = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        final Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return bi;
    }
}
