package TankGame.gameobjects;

import TankGame.game.GameConstants;
import TankGame.game.GameResources;
import TankGame.game.GameSoundPlayer;
import TankGame.game.GameWorld;
import TankGame.walls.BreakableWall;
import TankGame.walls.UnbreakableWall;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tank extends GameObject {
    //Variables for split screen x and y coordinates
    private int splitX;
    private int splitY;

    //Variables for tank's health and life management
    private int health = 100;
    private int lives = 1;

    private final float ROTATIONSPEED = 3.0f;   //How fast to rotate
    private final BufferedImage tank_image;     //Buffered Image for tank
    private final BufferedImage lives_image;    //Buffered Image for tank's lives

    //Variables for key presses
    private boolean UpPressed;                  //For moving up
    private boolean DownPressed;                //For moving down
    private boolean RightPressed;               //For moving right
    private boolean LeftPressed;                //For moving left
    private boolean ShootPressed;               //For shooting bullets

    private boolean bulletHit = false;          //Value to know if a tank is hit
    private boolean isKilled = false;           //Value to know if a tank is killed
    private ArrayList<Bullet> ammo;             //Arraylist to hold all bullets from tank
    private String name;                        //Name holder for tank created

    public Tank(int x, int y, int vx, int vy, int angle, BufferedImage img, BufferedImage limage, String name) {
        this.R = 2;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.tank_image = img;
        this.lives_image = limage;
        this.angle = angle;
        this.hitBox = new Rectangle(x,y,this.tank_image.getWidth(), this.tank_image.getHeight());
        this.ammo = new ArrayList<>();
        this.name = name;

    }

    @Override
    public void checkBorder() {    //ensures tank on screen
        if (x < 32) {
            x = 32;
            System.out.println("X collision");
        }

        if (x > GameConstants.WORLD_WIDTH - 64) {
            x = GameConstants.WORLD_WIDTH - 64;
            System.out.println("X collision");
        }

        if (y < 32) {
            y = 32;
            System.out.println("Y Collision");
        }

        if (y > GameConstants.WORLD_HEIGHT - 64) {
            y = GameConstants.WORLD_HEIGHT - 64;
            System.out.println("Y Collision");

        }
    }


    public void handleCollision(GameObject object){
        if(this.getHitbox().intersects(object.getHitbox())){
            if(object instanceof UnbreakableWall || object instanceof BreakableWall){
                if (this.UpPressed){
                    this.x -= vx;
                    this.y -= vy;
                }
                if (this.DownPressed){
                    this.x += vx;
                    this.y += vy;
                }
            }
            else if(object instanceof HealthUp){
                object.setHit(true);
                increaseHealth();
                sp = new GameSoundPlayer(2, "sounds/health_regen_sound.wav");
            }
            else if(object instanceof LifeUp){
                object.setHit(true);
                increaseLife();
                sp = new GameSoundPlayer(2, "sounds/life_increase_sound.wav");

            }
        }
    }

    private void increaseHealth(){
        if(health <= 50){
            health += 50;
        }else if(health > 50){
            health = 100;
        }
    }

    private void increaseLife(){
        lives += 1;
    }

    public void decreaseHealth(){
        health -= 25;
        System.out.println("Health = " + health + "["+name+"]");

        if(health <= 0){
            if(lives > 0) {
                lives--;
            }
            health = 100;
            System.out.println("Lives = " + lives + "["+name+"]");
            checkLife();
        }
    }

    public String getName() {
        return name;
    }

    public void checkLife(){
        if(lives == 0){
            isKilled = true;
            System.out.println("Tank Killed!!" + "["+name+"]");
        }
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void toggleShootPressed() {
        this.ShootPressed = true; }

    void unToggleShootPressed() {
        this.ShootPressed = false; }

    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }

        //decreases shoot speed based on tick
        if (this.ShootPressed && GameWorld.tick % 20 == 0) {
            Bullet bullet = new Bullet(x, y, angle, GameResources.getResourceImage("bullet"));
            this.ammo.add(bullet);
        }
        this.ammo.forEach(bullet -> bullet.update());

        checkSplit();
    }

    public void setBulletHit(boolean value){
        bulletHit = value;
    }

    public boolean getBulletHit(){
        return bulletHit;
    }

    public ArrayList<Bullet> getAmmo() {
        return ammo;
    }

    public Bullet getAmmoAt(int index){
        return ammo.get(index);
    }
    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    public int getSplitX() {
        return splitX;
    }

    public int getSplitY() {
        return splitY;
    }

    private void checkSplit(){
        splitX = x - GameConstants.GAME_SCREEN_WIDTH/4;
        splitY = y - GameConstants.GAME_SCREEN_HEIGHT/2;

        if(x < GameConstants.GAME_SCREEN_WIDTH/4){
            splitX = 0;
        }

        if(y < GameConstants.GAME_SCREEN_HEIGHT/2){
            splitY = 0;
        }

        if(x > GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/4){
            splitX = GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2;
        }

        if(y > GameConstants.WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT/2){
            splitY = GameConstants.WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT;
        }
    }

    public boolean getIsKilled(){
        return isKilled;
    }




    public void drawImage(Graphics g) {
        //Affine does scale, rotate, or sheer to transform an image
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);

        //Rotate around an angle and fixed point
        rotation.rotate(Math.toRadians(angle), this.tank_image.getWidth() / 2.0, this.tank_image.getHeight() / 2.0);

        //Draw tank objects to buffered image first then to JPanel
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.tank_image, rotation, null);

        //Draw rectangle around our tank
        //g2d.setColor(Color.RED);
        //g2d.drawRect(x,y,this.tank_image.getWidth(), this.tank_image.getHeight());


        //Draw bullets
        this.ammo.forEach(bullet -> bullet.drawImage(g));

        //Draw health and life information for individual tanks
        g2d.setColor(Color.WHITE);
        g2d.fill3DRect(this.x - 32,this.y + 32 + 16, 100,25, true);
        g2d.setColor(Color.GREEN);
        g2d.fill3DRect(this.x - 32,this.y + 32 + 16, health,25, true);
        g2d.drawImage(this.lives_image, null, this.x - 32, this.y - 32);
        g2d.drawString("    x " + this.lives, this.x - 32 + 4, this.y - 32 + 12);
    }
}
