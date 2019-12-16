package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Referenced from JavaFX-Steganography by MaslovEvgeniy
 */
public class Encoder {
    private final String outPath = System.getProperty("user.dir") + "/WebContent/web/images/tmp/out.png";
	private int cBit = 7;
    private int cByte = 0;
    private BufferedImage coverImg;
    private byte[] data;

    public Encoder() {}
     
    public boolean steganographyImage(String imagePath, String hidImagePath) throws ArrayIndexOutOfBoundsException {
        try {
           this.coverImg = ImageIO.read(new File(imagePath));
        } catch (IOException e) {}
        
        byte[] imgBytes = null;
        try {
            Path path = Paths.get(hidImagePath);
            imgBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean notValid = !((imgBytes.length + 4) * 8 + 3 < coverImg.getWidth() * coverImg.getHeight() * 3);
        if (notValid)
            return false;
        BufferedImage encoded = encode(imgBytes);
        
        File outputfile = new File(outPath);
        
        try {
           ImageIO.write(encoded, "png",outputfile);
        } catch (IOException e) { return false; }
        return true;
    }

    private BufferedImage encode(byte[] msg) {
    	int ml = msg.length;
    	int index = 0, r = 0, g = 0, b = 0;
    	
        byte[] data = new byte[4 + ml];
        byte[] msgLength = new byte[] {
        		((byte) ((ml & 0xFF000000) >> 24)),
        		((byte) ((ml & 0x00FF0000) >> 16)),
        		((byte) ((ml & 0x0000FF00) >> 8)),
        		((byte) ((ml & 0x000000FF)))
        };

        System.arraycopy(msgLength, 0, data, 0, msgLength.length);
        System.arraycopy(msg, 0, data, 4, msg.length);
        this.data = data;

        int width = coverImg.getWidth();
        int height = coverImg.getHeight();
        
        Color c = new Color(coverImg.getRGB(0, 0));
        int red = c.getRed() & 0xFE;
        int green = c.getGreen() & 0xFE;
        int blue = c.getBlue() & 0xFE;
        coverImg.setRGB(0, 0, new Color(red, green, blue, c.getAlpha()).getRGB());
        
        Color color = null;
        try {
            for (index = 1; index < width * height; index++) {
                color = new Color(coverImg.getRGB(index % width, index / width));
                r = (color.getRed() & 0xFE | (nextBit()));
                g = (color.getGreen() & 0xFE | (nextBit()));
                b = (color.getBlue() & 0xFE | (nextBit()));
                coverImg.setRGB(index % width, index / width, new Color(r, g, b, color.getAlpha()).getRGB());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            cByte--;
            cBit = 7;
            b = (color.getBlue() & 0xFE | (nextBit()));
            coverImg.setRGB(index % width, index / width, new Color(r, g, b, color.getAlpha()).getRGB());
            
            this.data = null;
            return coverImg;
        }
        return null;
    }

    private int nextBit() throws ArrayIndexOutOfBoundsException {
        int r = (data[cByte] >> cBit) & 1;
        cBit--;
        if (cBit < 0) {
            cBit = 7;
            cByte++;
        }
        return r;
    }
}
