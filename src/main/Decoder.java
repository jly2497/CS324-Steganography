package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

/*
 * Title: JavaFX-Steganography
 * Author: MaslovEvgeniy
 * Date: May 18, 2017
 * Availability: https://github.com/MaslovEvgeniy/JavaFX-Steganography/tree/84c1fdf3d0385b79b1b26ec4172d76fc271b74f5
 */

public class Decoder {
    private final String outPath = System.getProperty("user.dir") + "/WebContent/web/images/tmp/out.png";
    private int cBit = 7;
    private int cByte = 0;
    private BufferedImage coverImg;
    private byte[] data;
    
    public Decoder() {}
    
    public boolean steganographyImage(String imagePath) throws IOException {
        try {
           this.coverImg = ImageIO.read(new File(imagePath));
        } catch (IOException e) {}
        
        if (!decode())
            return false;
        
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(data));
            if (img == null) return false;
        } catch (IOException e) {
            throw e;
        }

        File outputfile = new File(outPath);
        ImageIO.write(img, "png", outputfile);
        return true;
    }

    private boolean decode() {
        this.data = null;
        this.cByte = 0;
        this.cBit = 7;

        int width = coverImg.getWidth();
        int height = coverImg.getHeight();

        Color c = new Color(coverImg.getRGB(0, 0));
        int lsb = 0;
        lsb = (c.getBlue() & 1) + ((c.getGreen() & 1) << 1) + ((c.getRed() & 1) << 2);

        int length = 0;
        this.data = new byte[4];
        int index = 1;

        try {
            for (index = 1; index < width * height; index++) {
                Color color = new Color(coverImg.getRGB(index % width, index / width));
                nextBit(color.getRed() & 1);
                nextBit(color.getGreen() & 1);
                nextBit(color.getBlue() & 1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            length = ByteBuffer.wrap(data).getInt();
            this.data = null;
            try {
            	this.data = new byte[length];
            } catch (NegativeArraySizeException ne) { return false; }
            this.cByte = 0;
            this.cBit = 7;
            nextBit(new Color(coverImg.getRGB(index % width, index / width)).getBlue() & 1);
        }

        if (lsb != 0 || (length <= 0 || length > Integer.MAX_VALUE - 4))
            return false;

        Color color = null;
        try {
            for (index = 12; index < width * height; index++) {
                color = new Color(coverImg.getRGB(index % width, index / width));
                nextBit(color.getRed() & 1);
                nextBit(color.getGreen() & 1);
                nextBit(color.getBlue() & 1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        return false;
    }

    private void nextBit(int value) throws ArrayIndexOutOfBoundsException {
        data[cByte] = (byte) (data[cByte] | value << cBit);
        cBit--;
        if (cBit < 0) {
            cByte++;
            cBit = 7;
        }
    }

}
