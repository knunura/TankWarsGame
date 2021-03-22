/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame.game;


import TankGame.Launcher;
import TankGame.gameobjects.*;
import TankGame.walls.BreakableWall;
import TankGame.walls.UnbreakableWall;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author anthony-pc
 */
//GameWorld, TankRotExample
//Extends JPanel for paintComponent() function. Runnable (extend Thread) allows class to be threaded object.
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;                //BufferedImage for world, used in paint COmponent
    private final Launcher lf;                  //The launcher
    private ArrayList<GameObject> gameObjects;  //Stores all of game objects (walls, tanks)
    public static long tick = 0;                //Used as counter to fake game ending for 8 secs.
    private Graphics2D g2d;
    private Graphics2D buffer;
    //Images for game objects and soundplayer
    private Tank tank1;
    private Tank tank2;
    private Image world1;
    private Image world2;
    private GameSoundPlayer sp;

    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    //called indirectly in run
    @Override
    public void run() {
        try {
            this.resetGame();
            while (true) {
                this.tick++;
                this.gameObjects.forEach(gameObject -> gameObject.update());
                this.repaint();   // redraw game. Calls paint component indirectly.
                this.handleCollisions();

                if(tank1.getIsKilled()){
                    this.lf.setEndFrame(tank2.getName());
                    System.out.println("RETURNNNNNNNN");
                    return;
                }else if(tank2.getIsKilled()){
                    this.lf.setEndFrame(tank1.getName());
                    return;
                }

                Thread.sleep(1000 / 144); //sleep for a few milliseconds (about 7 milsecs to get 1044 fps)

            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    public void handleCollisions() {
        for (int i = 0; i < gameObjects.size(); i++) {
            tank1.handleCollision(gameObjects.get(i));
            tank2.handleCollision(gameObjects.get(i));
            if (gameObjects.get(i).getHit()) {
                gameObjects.remove(i);
            }
        }

        for (int i = 0; i < tank1.getAmmo().size(); i++) {
            for (int j = 0; j < gameObjects.size(); j++) {
                tank1.getAmmoAt(i).handleCollision(gameObjects.get(j), tank1, tank2);
                if (tank1.getBulletHit()) { //remove bullets once they hit something
                    tank1.getAmmo().remove(i);
                    tank1.setBulletHit(false);
                    j = gameObjects.size() - 1;
                }
                if (gameObjects.get(j).getHit()) {   //if gameobject at (i) is breakable and is hit. remove wall.
                    System.out.println("breakable wal hit");
                    gameObjects.remove(j);
                }
            }
        }

        for (int i = 0; i < tank2.getAmmo().size(); i++) {
            for (int j = 0; j < gameObjects.size(); j++) {
                tank2.getAmmoAt(i).handleCollision(gameObjects.get(j), tank2, tank1);
                if (tank2.getBulletHit()) {
                    tank2.getAmmo().remove(i);
                    tank2.setBulletHit(false);
                    j = gameObjects.size() - 1;
                }
                if (gameObjects.get(j).getHit()) {
                    System.out.println("*Break wall*");
                    gameObjects.remove(j);
                }
            }
        }
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {

        try {
            this.world1 = ImageIO.read(this.getClass().getClassLoader().getResource("background_top.png"));
            this.world2 = ImageIO.read(this.getClass().getClassLoader().getResource("background_bottom.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.world = new BufferedImage(GameConstants.WORLD_WIDTH,
                GameConstants.WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        this.gameObjects = new ArrayList<>();

        try {
            //map text image
            InputStreamReader isr = new InputStreamReader(Launcher.class.getClassLoader().getResourceAsStream("map1"));
            BufferedReader mapReader = new BufferedReader(isr);

            String info = mapReader.readLine(); //67x67
            if (info == null) {
                throw new IOException("No data in file row");
            }
            String[] mapInfo = info.split("\t");
            int numCols = Integer.parseInt(mapInfo[0]); //67
            int numRows = Integer.parseInt(mapInfo[1]); //67

            for (int curRow = 0; curRow < numRows; curRow++) { //67 rows
                info = mapReader.readLine();
                mapInfo = info.split("\t");
                for (int curCol = 0; curCol < numCols; curCol++) { //67 col
                    switch (mapInfo[curCol]) {
                        case "2":
                            this.gameObjects.add(new BreakableWall(curCol * 32, curRow * 32, GameResources.getResourceImage("breakable")));
                            break;
                        case "3":
                            this.gameObjects.add(new HealthUp(curCol * 32, curRow * 32, GameResources.getResourceImage("healthUp")));
                            break;
                        case "4":
                            this.gameObjects.add(new LifeUp(curCol * 32, curRow * 32, GameResources.getResourceImage("lifeUp")));
                            break;
                        case "8":
                        case "9":
                            this.gameObjects.add(new UnbreakableWall(curCol * 32, curRow * 32, GameResources.getResourceImage("unbreakable")));
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        tank1 = new Tank(1056, 32, 0, 0, (short) 0, GameResources.getResourceImage("tank1"), GameResources.getResourceImage("lives"), "tank1");
        tank2 = new Tank(1056, 2080, 0, 0, (short) 0, GameResources.getResourceImage("tank2"), GameResources.getResourceImage("lives"), "tank2");
        //Set controls for tanks
        TankControl tc1 = new TankControl(tank1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SHIFT);
        TankControl tc2 = new TankControl(tank2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);

        this.gameObjects.add(tank1);
        this.gameObjects.add(tank2);

        //Add keys to key listeners for button presses and releases
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);

        //add sound to game sound player class
        sp = new GameSoundPlayer(1, "sounds/background_music.wav"); //https://soundcloud.com/ashamaluevmusic/resistance?in=ashamaluevmusic/sets/war-music
    }


    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.tank1.setX(1056);
        this.tank1.setY(32);
        this.tank2.setX(1056);
        this.tank2.setY(2080);

    }

    @Override
    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        buffer = world.createGraphics(); //Makes a buffered value of world graphics because drawImage() takes graphics as parameter

        //Draw whole map from two halves of a map
        buffer.drawImage(world1, 0, 0, GameConstants.WORLD_WIDTH, 1074, null);
        buffer.drawImage(world2, 0, 1074, GameConstants.WORLD_WIDTH, 1074, null);

        //Draw every game object
        this.gameObjects.forEach(gameObject -> gameObject.drawImage(buffer));

        //Draw split screens
        BufferedImage leftHalf = world.getSubimage(tank1.getSplitX(), tank1.getSplitY(), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rightHalf = world.getSubimage(tank2.getSplitX(), tank2.getSplitY(), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        g2d.drawImage(leftHalf, 0, 0, null);
        g2d.drawImage(rightHalf, GameConstants.GAME_SCREEN_WIDTH / 2 + 4, 0, null);

        //Draw minimap
        BufferedImage minimap = world.getSubimage(0, 0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
        g2d.scale(.10, .10);
        g2d.drawImage(minimap, GameConstants.GAME_SCREEN_WIDTH * 4, 0, null);
    }
}
