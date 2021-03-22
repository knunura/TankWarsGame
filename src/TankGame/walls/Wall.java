package TankGame.walls;

import TankGame.gameobjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Wall extends GameObject {
    protected BufferedImage wall_image;

    public void update(){

    }

    public void drawImage(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.wall_image, x, y, null);

        //g2d.setColor(Color.RED);
        //g2d.drawRect(x,y,this.wall_image.getWidth(), this.wall_image.getHeight());
    }
}
