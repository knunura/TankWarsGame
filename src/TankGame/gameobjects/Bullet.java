package TankGame.gameobjects;

import TankGame.game.GameConstants;
import TankGame.game.GameSoundPlayer;
import TankGame.walls.BreakableWall;
import TankGame.walls.UnbreakableWall;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private BufferedImage bullet_image; //Buffered Image for bullet

    public Bullet(int x, int y, float angle, BufferedImage bullet_image) {
        this.R = 7;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.bullet_image = bullet_image;
        this.hitBox = new Rectangle(x, y, this.bullet_image.getWidth(), this.bullet_image.getHeight());
    }

    public void handleCollision(GameObject object, Tank tankObject, Tank enemyTank) {

        if (this.getHitbox().intersects(object.getHitbox())) {
            if (object instanceof BreakableWall) {
                System.out.println("Bullet hit breakable wall");
                handleHit(object, tankObject, GameConstants.BULLET_HIT_BREAK_WALL);
                sp = new GameSoundPlayer(2, "sounds/wall_break_sound.wav");

            }

            else if (object instanceof UnbreakableWall) {
                System.out.println("Bullet hit unbreakable wall");
                handleHit(object, tankObject, GameConstants.BULLET_HIT_UNBREAK_WALL);
                sp = new GameSoundPlayer(2, "sounds/wall_unbreak_sound.wav");
            }

            else if (object.getHitbox().intersects(enemyTank.getHitbox())) {
                System.out.println("Bullet hit enemy tank");
                handleHit(object, tankObject, enemyTank, GameConstants.BULLET_HIT_ENEMY_TANK);
                sp = new GameSoundPlayer(2, "sounds/tank_explosion_sound.wav");

            }
        }
    }

    private void handleHit(GameObject object, Tank tankObject, int hitType){
        if(hitType == GameConstants.BULLET_HIT_BREAK_WALL){
            object.setHit(true);
        }else if(hitType == GameConstants.BULLET_HIT_ENEMY_TANK){
            tankObject.decreaseHealth();
        }

        this.x -= vx;
        this.y -= vy;
        tankObject.setBulletHit(true); //sets bullet to hitting somthing true
    }

    private void handleHit(GameObject object, Tank tankObject, Tank enemyTank, int hitType){
        this.handleHit(object, enemyTank, hitType);
        tankObject.setBulletHit(true);
    }

    public void update() {
        this.moveForwards();
    }

    public void drawImage(Graphics g) {
        //Affine is a 3xx3 that does scale, rotate, or sheer to transform an image
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        //rotate around an angle and fixed point
        rotation.rotate(Math.toRadians(angle), this.bullet_image.getWidth() / 2.0, this.bullet_image.getHeight() / 2.0);
        //1. Draw game objects to buffered image first then to JPanel
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.bullet_image, rotation, null);

        //set red for hitbox square
        //g2d.setColor(Color.RED);
        //g2d.drawRect(x, y, this.bullet_image.getWidth(), this.bullet_image.getHeight());
    }
}
