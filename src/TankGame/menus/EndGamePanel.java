package TankGame.menus;

import TankGame.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EndGamePanel extends JPanel {
    private BufferedImage menuBackground;
    private final JButton exit;
    private final Launcher lf;
    public String winning_tank;


    public void setWinningEndPanel(String tank){
        this.winning_tank = tank;

        try {
            if("tank1".equals(winning_tank)){
                menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("tank1endpanel.png"));
            }else{
                menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("tank2endpanel.png"));
            }
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
    }

    public EndGamePanel(Launcher lf) {
        this.lf = lf;
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD ,24));
        exit.setBounds(230,425,175,50);
        exit.addActionListener((actionEvent -> {
            this.lf.closeGame();
        }));

        this.add(exit);
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground,0,0,null);
    }
}
