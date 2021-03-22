package TankGame.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LifeUp extends GameObject {
    private BufferedImage lifeUp_image;

    public LifeUp(int x, int y, BufferedImage lifeUp_image) {
        this.x = x;
        this.y = y;
        this.lifeUp_image = lifeUp_image;
        this.hitBox = new Rectangle(x,y,this.lifeUp_image.getWidth(), this.lifeUp_image.getHeight());
    }

    @Override
    public void update() {

    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.lifeUp_image, x, y, null);
        //g2d.setColor(Color.RED);
        //wg2d.drawRect(x,y,this.lifeUp_image.getWidth(),this.lifeUp_image.getHeight());
    }
}
