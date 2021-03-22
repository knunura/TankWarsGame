package TankGame.game;

/**
 *
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSoundPlayer {
   private AudioInputStream soundStream; 
   private String soundFile;
   private Clip clip;
   private int type;//1 for sounds that needs to be played all the time
                    // 2 for sounds that only need to be played once
   
   
   public GameSoundPlayer(int type, String soundFile){
       this.soundFile = soundFile;
       this.type = type;
       try{
           soundStream = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource(soundFile));
           clip = AudioSystem.getClip();
           clip.open(soundStream);
       }
       catch(Exception e){
           System.out.println(e.getMessage() + "**No sound documents are found\n");
       }
       if(this.type == 1){
           Runnable myRunnable = new Runnable(){
               public void run(){
                   while(true){
                       clip.start();
                       clip.loop(clip.LOOP_CONTINUOUSLY);
                       try {
                           Thread.sleep(10000);
                       } catch (InterruptedException ex) {
                           Logger.getLogger(GameSoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
                       }
                    }
               }
           };
           Thread thread = new Thread(myRunnable);
           thread.start();
       }
       else{
           this.play();
       }
   }
   
   public void play(){
       clip.start();
   }
   public void stop(){
       clip.stop();
   }
}
