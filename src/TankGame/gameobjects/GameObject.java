package TankGame.gameobjects;

import TankGame.game.GameSoundPlayer;

import java.awt.*;

public abstract class GameObject {

    protected int R;                //How fast object is moving
    protected float angle;          //Way object is facing
    protected Rectangle hitBox;     //Rectangle to be used as hitbox for the object
    private boolean isHit = false;  //If object is hit
    //Coordinates for GameObject positions
    protected int x;
    protected int y;
    //Coordinates for tank movements
    protected int vx;
    protected int vy;
    protected GameSoundPlayer sp;         //Sound for bullet


    public abstract void drawImage(Graphics g);

    public abstract void update();

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    protected Rectangle getHitbox() {
        return hitBox.getBounds();
    }

    protected void moveHitbox() {
        this.hitBox.setLocation(x, y);
    }

    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public void setHit(boolean value) {
        isHit = value;
    }

    public boolean getHit() {
        return isHit;
    }

    protected void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        moveHitbox();
    }

    protected void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();  //keep tank on screen
        moveHitbox();    //moves rectangle hit boxes
    }

    protected void checkBorder() {
    }
}
