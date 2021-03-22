package TankGame.walls;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnbreakableWall extends Wall {

    public UnbreakableWall(int x, int y, BufferedImage wall_image) {
        this.x = x;
        this.y = y;
        this.wall_image = wall_image;
        this.hitBox = new Rectangle(x,y,this.wall_image.getWidth(), this.wall_image.getHeight());
    }
}
