package main.sample;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Agent {
    String[] va = {"rock", "paper", "scissors"};
    BufferedImage image;

    public String getVaMove() {
        String name;
        Random r = new Random();
        name = va[r.nextInt(va.length)];
        setImage(name);
        return name;
    }

    public void setImage(String pic) {
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("VA_" + pic + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}


