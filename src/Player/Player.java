package Player;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class Player {

    private final int spriteWidth = 64;
    private final int spriteHeight = 64;
    private int xPosition = 0;
    private int yPosition = 0;

    private Image spritesheet;
    private ImageView spriteView;
    private Timeline animation;
    private Set<KeyCode> keysPressed = new HashSet<>();

    public Player(Pane gamePane) {
        setupPlayer();
        gamePane.getChildren().add(spriteView);

        gamePane.setOnKeyPressed(e -> keysPressed.add(e.getCode()));
        gamePane.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));

        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                move();
            }
        }.start();
    }

    private void setupPlayer() {
        spritesheet = new Image("file:Ressources/Player/Front Movement.png");
        spriteView = new ImageView(spritesheet);
        spriteView.setViewport(new Rectangle2D(xPosition, yPosition, spriteWidth, spriteHeight));

        animation = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            xPosition += spriteWidth;
            if (xPosition >= spritesheet.getWidth()) {
                xPosition = 0; // Retour au début
            }
            spriteView.setViewport(new Rectangle2D(xPosition, yPosition, spriteWidth, spriteHeight));
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
    }

    private void move() {
        double dx = 0;
        double dy = 0;
        double speed = 1;

        if (keysPressed.contains(KeyCode.UP)) {
            dy -= speed;
        }
        if (keysPressed.contains(KeyCode.DOWN)) {
            dy += speed;
        }
        if (keysPressed.contains(KeyCode.LEFT)) {
            dx -= speed;
        }
        if (keysPressed.contains(KeyCode.RIGHT)) {
            dx += speed;
        }

        // Normalize the movement vector if diagonal movement is detected
        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx = dx / length * speed;
            dy = dy / length * speed;
        }

        spriteView.setX(spriteView.getX() + dx);
        spriteView.setY(spriteView.getY() + dy);
    }


    public ImageView getSpriteView() {
        return spriteView;
    }
}
