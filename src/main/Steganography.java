package main;

import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//Referenced from Steganography by William Wilson

public class Steganography {
	
    private final String outPath = System.getProperty("user.dir") + "/WebContent/web/images/tmp/out.png";
    
    public Steganography() {}
    
    public boolean encode(String path, String original, String extension, String message) {
        BufferedImage cover = null;
        
        try {
            cover = ImageIO.read(new File(path + "/" + original + "." + extension));
         } catch (IOException e) { return false; }
        
        BufferedImage secret  = new BufferedImage(cover.getWidth(), cover.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        graphics(secret,cover);
        
        secret = hideText(secret,message);

        try {
        	File outputfile = new File(outPath);
        	ImageIO.write(secret, "png",outputfile);
        } catch(IOException e) {
        	return false;
        }
        return true;
    }

    public String decode(String path, String name) {
        byte[] decode;
        
        try {
        	BufferedImage cover = null;
        	cover = ImageIO.read(new File(path + "/" + name + ".png"));
        	
        	BufferedImage secret  = new BufferedImage(cover.getWidth(), cover.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            graphics(secret,cover);
            
            WritableRaster ras   = secret.getRaster();
            DataBufferByte buf = (DataBufferByte) ras.getDataBuffer();
            
            try {
            	decode = textDec(buf.getData());
            } catch (ArrayIndexOutOfBoundsException e) { return ""; }
            String decStr = new String(decode);
            return decStr;
        } catch(IOException e) {
            return "";
        }
    }
    private void graphics(BufferedImage s, BufferedImage c) {
    	Graphics2D  g = s.createGraphics();
        g.drawRenderedImage(c, null);
        g.dispose();
    }
    private BufferedImage hideText(BufferedImage image, String text) {
        WritableRaster ras   = image.getRaster();
        DataBufferByte buf = (DataBufferByte) ras.getDataBuffer();
        
    	byte img[]  = buf.getData();;
        byte msg[] = text.getBytes();
        int ml = msg.length;
        
        byte len[]   = new byte[] {
        		((byte) ((ml & 0xFF000000) >> 24)),
        		((byte) ((ml & 0x00FF0000) >> 16)),
        		((byte) ((ml & 0x0000FF00) >> 8)),
        		((byte) ((ml & 0x000000FF)))
        };
        try {
            textEnc(img, len,  0);
            textEnc(img, msg, 32);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }
    private byte[] textEnc(byte[] image, byte[] addition, int offset) {
    	
        if(addition.length + offset > image.length)
            return (new byte[] {});

        for(int i=0; i<addition.length; ++i) {
            int add = addition[i];
            for(int bit=7; bit>=0; --bit, ++offset) {
                int b = (add >>> bit) & 1;
                image[offset] = (byte)((image[offset] & 0xFE) | b );
            }
        }
        return image;
    }

    private byte[] textDec(byte[] img) {
        int l = 0;
        int o  = 32;
        
        for(int i=0; i<32; ++i)
            l = (l << 1) | (img[i] & 1);

        byte[] res = new byte[l];

        for(int b=0; b<res.length; ++b )
            for(int i=0; i<8; ++i, ++o)
                res[b] = (byte)((res[b] << 1) | (img[o] & 1));
        return res;
    }
}

