package app.seleniumap.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IconsUtil {
    private BufferedImage imageMap;
    private int iconWidth=0;
    private int iconHeight=0;
    private int scaleWidth =0;
    private int scaleHeight =0;

    public IconsUtil(BufferedImage image, int width, int height){
        this.imageMap = image;
        this.iconHeight =height;
        this.iconWidth = width;
        this.scaleHeight = height;
        this.scaleWidth = width;
    }

    public IconsUtil(String path, int width, int height) {
        imageMap = getImage(path);
        this.iconHeight = height;
        this.iconWidth = width;
        this.scaleHeight = height;
        this.scaleWidth = width;
    }

    public void setScaledSize(int width, int height) {
        this.scaleHeight = height;
        this.scaleWidth = width;
    }
    public BufferedImage getImage(String path) {
        try {
            System.out.println(path);
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    public Icon getIcon(int x, int y) {
        BufferedImage sub= imageMap.getSubimage(x,y, iconWidth, iconHeight);
        ImageIcon result = new ImageIcon(resize(sub, scaleWidth,scaleHeight));
        return result;
    }

    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
