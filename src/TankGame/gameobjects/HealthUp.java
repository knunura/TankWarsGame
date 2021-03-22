package TankGame.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HealthUp extends GameObject {
    private BufferedImage healthUp_image;

    public HealthUp(int x, int y, BufferedImage healthUp_image) {
        this.x = x;
        this.y = y;
        this.healthUp_image = healthUp_image;
        this.hitBox = new Rectangle(x,y,this.healthUp_image.getWidth(), this.healthUp_image.getHeight());
    }


    @Override
    public void update() {

    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.healthUp_image, x, y, null);
        //g2d.setColor(Color.RED);
        //g2d.drawRect(x,y,this.healthUp_image.getWidth(),this.healthUp_image.getHeight());
    }




}
