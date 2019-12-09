package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Class for hiding information using LSB-method
 */
public class StegoCodec {

    private BufferedImage currentImage;
    private byte[] data;
    private int currentBit = 7;
    private int currentByte = 0;
    private final String outPath = System.getProperty("user.dir") + "/WebContent/web/images/tmp/out.png";
    
    public static void main(String[] args) throws IOException {
      
      //StegoCodec encoder = new StegoCodec();
      //encoder.encodeImage("img1.jpg","img2.jpg");
      //encoder.decodeImage("out.png");
    }
    
    public StegoCodec() {}
    /**
     * Encode secret text message into image
     *
     * @param image   image, where text will be hidden
     * @param message text to hide
     * @return image with hidden text
     * @throws ArrayIndexOutOfBoundsException if text size is more than the amount information that image can contain
     */
     /*
    public Image encodeText(Image image, String message) throws ArrayIndexOutOfBoundsException {
        this.currentImage = SwingFXUtils.fromFXImage(image, null);
        byte[] msgBytes = null;
        try {
            msgBytes = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!checkSize(msgBytes.length))
            throw new ArrayIndexOutOfBoundsException();

        return encode(msgBytes);
    }*/

    /**
     * Encode image to image
     *
     * @param image        image, where another image will be hidden
     * @param hidImagePath path to image that will be hidden
     * @return image with hidden information
     * @throws ArrayIndexOutOfBoundsException if image can not contain
     */
     
    public void encodeImage(String imagePath, String hidImagePath) throws ArrayIndexOutOfBoundsException {
        try {
           this.currentImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {}
        
        byte[] imgBytes = null;
        try {
            Path path = Paths.get(hidImagePath);
            imgBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!checkSize(imgBytes.length))
            throw new ArrayIndexOutOfBoundsException();
        BufferedImage encoded = encode(imgBytes);
        
        File outputfile = new File(outPath);
        
        try {
           ImageIO.write(encoded, "png",outputfile);
        } catch (IOException e) {}
    }

    /**
     * Decode hidden text
     *
     * @param image image with hidden text
     * @return extracted text
     * @throws IOException if image doesn't contain hidden text
     */
     /*
    public String decodeText(Image image) throws IOException {
        this.currentImage = SwingFXUtils.fromFXImage(image, null);
        if (!decode())
            throw new IOException();
        String result = new String(data, "UTF-8");
        data = null;
        this.currentByte = 0;
        this.currentBit = 7;
        return result;
    }*/

    /**
     * Decode hidden image
     *
     * @param image image with encoded information
     * @return decoded image
     * @throws IOException if image doesn't contain hidden information
     */
    public boolean decodeImage(String imagePath) throws IOException {
        try {
           this.currentImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {}
        
        if (!decode())
            return false;
        
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(data));
            if (img == null) throw new IOException();
        } catch (IOException e) {
            throw e;
        }

        File outputfile = new File(outPath);
        ImageIO.write(img, "png", outputfile);
        return true;
    }

    /**
     * Encode information bytes into current image
     *
     * @param msg bytes of information that must be hidden
     * @return image with hidden information
     */
    private BufferedImage encode(byte[] msg) {
        byte[] data = new byte[4 + msg.length];
        byte[] msgLength = intToByteArray(msg.length);

        //merge two arrays into one
        System.arraycopy(msgLength, 0, data, 0, msgLength.length);
        System.arraycopy(msg, 0, data, 4, msg.length);
        this.data = data;

        //get current image width and height
        int w = currentImage.getWidth();
        int h = currentImage.getHeight();

        //set 000 into first pixel
        Color c = new Color(currentImage.getRGB(0, 0));
        int red = c.getRed() & 0xFE;
        int green = c.getGreen() & 0xFE;
        int blue = c.getBlue() & 0xFE;
        currentImage.setRGB(0, 0, new Color(red, green, blue, c.getAlpha()).getRGB());

        //inserting information bytes into image
        int i = 0, r = 0, g = 0, b = 0;
        Color color = null;
        try {
            for (i = 1; i < w * h; i++) {
                color = new Color(currentImage.getRGB(i % w, i / w));
                r = (color.getRed() & 0xFE | (generateNextBit()));
                g = (color.getGreen() & 0xFE | (generateNextBit()));
                b = (color.getBlue() & 0xFE | (generateNextBit()));
                currentImage.setRGB(i % w, i / w, new Color(r, g, b, color.getAlpha()).getRGB());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            currentByte--;
            currentBit = 7;
            b = (color.getBlue() & 0xFE | (generateNextBit()));
            currentImage.setRGB(i % w, i / w, new Color(r, g, b, color.getAlpha()).getRGB());
            
            
            this.data = null;
            return currentImage;
        }
        return null;
    }

    /**
     * Decode hidden information into byte array
     *
     * @return true - if information successfully decoded, false - if image doesn't contain hidden information
     */
    private boolean decode() {
        this.data = null;
        this.currentByte = 0;
        this.currentBit = 7;

        int w = currentImage.getWidth();
        int h = currentImage.getHeight();

        Color c = new Color(currentImage.getRGB(0, 0));
        int lsb = 0;
        lsb = (c.getBlue() & 1) + ((c.getGreen() & 1) << 1) + ((c.getRed() & 1) << 2);//get hidden information from 1-st pixel

        //decode length of hidden information
        int length = 0;
        this.data = new byte[4];
        int i = 1;

        try {
            for (i = 1; i < w * h; i++) {
                //extract pixel from image and set his last bits into data array
                Color color = new Color(currentImage.getRGB(i % w, i / w));
                setNextBit(color.getRed() & 1);
                setNextBit(color.getGreen() & 1);
                setNextBit(color.getBlue() & 1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {//if we extracted needed 4 bytes
            length = ByteBuffer.wrap(data).getInt();
            this.data = null;
            this.data = new byte[length];
            this.currentByte = 0;
            this.currentBit = 7;
            setNextBit(new Color(currentImage.getRGB(i % w, i / w)).getBlue() & 1);
        }

        if (lsb != 0 || (length <= 0 || length > Integer.MAX_VALUE - 4)) //check if we have encoded message
            return false;

        Color color = null;
        try {
            for (i = 12; i < w * h; i++) {
                color = new Color(currentImage.getRGB(i % w, i / w));
                setNextBit(color.getRed() & 1);
                setNextBit(color.getGreen() & 1);
                setNextBit(color.getBlue() & 1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {//if whole message have decoded
            return true;
        }
        return false;
    }

    /**
     * Checks if we can hide needed information into current image
     *
     * @param msgLength amount of information bytes
     * @return true - if information can be hidden, false - otherwise
     */
    private boolean checkSize(int msgLength) {
        return (msgLength + 4) * 8 + 3 < currentImage.getWidth() * currentImage.getHeight() * 3;
    }

    /**
     * Converts int number into array of 4 bytes
     *
     * @param number int number that must be converted
     * @return byte array with converted number
     */
    private byte[] intToByteArray(int number) {
        byte[] result = new byte[4];

        result[0] = (byte) ((number & 0xFF000000) >> 24);
        result[1] = (byte) ((number & 0x00FF0000) >> 16);
        result[2] = (byte) ((number & 0x0000FF00) >> 8);
        result[3] = (byte) ((number & 0x000000FF));

        return result;
    }

    /**
     * Generate next bit from data byte array
     *
     * @return 1 or 0, depending on which value contains in data
     * @throws ArrayIndexOutOfBoundsException if we used all data bytes
     */
    private int generateNextBit() throws ArrayIndexOutOfBoundsException {
        int r = (data[currentByte] >> currentBit) & 1;
        if (--currentBit < 0) {
            currentBit = 7;
            currentByte++;
        }
        return r;
    }

    /**
     * Sets next bit into data byte array
     *
     * @param value bit to be written into array
     * @throws ArrayIndexOutOfBoundsException if exceeded needed size for array
     */
    private void setNextBit(int value) throws ArrayIndexOutOfBoundsException {
        data[currentByte] = (byte) (data[currentByte] | value << currentBit);
        if (--currentBit < 0) {
            currentByte++;
            currentBit = 7;
        }
    }

}