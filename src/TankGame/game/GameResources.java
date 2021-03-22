package TankGame.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static javax.imageio.ImageIO.read;

public class GameResources {
    private static Map<String, BufferedImage> resources;

    static {
        GameResources.resources = new HashMap<>();
        //tank images
        try {
            GameResources.resources.put("lifeUp", read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("lifeUp.png"))));
            GameResources.resources.put("healthUp", read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("healthUp.png"))));
            GameResources.resources.put("lives", read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("lives.png"))));
            GameResources.resources.put("tank1", read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank1.png"))));
            GameResources.resources.put("tank2", read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank2.png"))));
            GameResources.resources.put("bullet", read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("bullet.png"))));
            GameResources.resources.put("unbreakable",read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("Wall1.gif"))));
            GameResources.resources.put("breakable",read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("Wall2.gif"))));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(-5);
        }

    }

    public static BufferedImage getResourceImage(String key) {
        return GameResources.resources.get(key);
    }
}
