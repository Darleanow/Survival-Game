package Player;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

class MovementDelta {
    public double dx = 0;
    public double dy = 0;
}



/**
 * Represents a player character in a game. This class is responsible for handling
 * the player's movement, sprite rendering, and animations within the game.
 */
public class Player {
    // Constants for sprite dimensions (width and height) and file paths for sprite sheets.
    private final int SPRITE_WIDTH = 64;  // Width of the sprite.
    private final int SPRITE_HEIGHT = 64; // Height of the sprite.
    private final int SCALE = 3; // Scale the player sprite
    private final String FRONT_MOVEMENT = "file:Ressources/Player/Front Movement.png"; // File path for front movement sprite sheet.
    private final String BACK_MOVEMENT = "file:Ressources/Player/Back Movement.png";   // File path for back movement sprite sheet.
    private final String SIDE_MOVEMENT = "file:Ressources/Player/Side Movement.png";   // File path for side movement sprite sheet.

    // Variables to track the player's current state and position.
    private Boolean isMoving = false;      // Indicates whether the player is currently moving.
    private int lastOrientation = 0;       // Last orientation of the player (0: Up, 1: Right, 2: Down, -1: Left).
    private int xPosition = 0;             // X-coordinate of the player's position.
    private int yPosition = 0;             // Y-coordinate of the player's position.
    private int currentFrame = 0;          // Current frame number in the animation sequence.

    // Details of the sprite sheet used for the player's animation.
    private final int NUM_ROWS = 2;        // Number of rows in the sprite sheet.
    private final int NUM_COLS = 6;        // Number of columns in the sprite sheet.
    private Image spritesheet;             // The current sprite sheet image.
    private ImageView spriteView;          // ImageView for rendering the sprite.
    private String currentSpritesheet;     // File path of the current sprite sheet in use.

    // World view and input handling.
    private ImageView worldImageView;      // ImageView of the game world.
    private final Set<KeyCode> keysPressed = new HashSet<>(); // Set of currently pressed keys.

    /**
     * Initializes a new player object.
     * @param gamePane The game pane where the player will be displayed.
     * @param worldImageView The image view of the game world.
     */
    public Player(Pane gamePane, ImageView worldImageView) {
        this.worldImageView = worldImageView;
        setupPlayer();
        gamePane.getChildren().add(spriteView);
        handleInut(gamePane);
        handleAnimations();
    }

    /**
     * Handles the input of the player
     * @param gamePane The game pane where the player is displayed
     */
    public void handleInut(Pane gamePane)
    {
        gamePane.setOnKeyPressed(e -> keysPressed.add(e.getCode()));
        gamePane.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));
    }

    /**
     * Handles the animations of the player
     */
    public void handleAnimations()
    {
        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                move();
            }
        }.start();
    }

    /**
     * Get the current sprite view
     * @return the player sprite view
     */
    public ImageView get()
    {
        return this.spriteView;
    }

    /**
     * Initializes the player by setting up the sprite sheet, sprite view, and animation.
     * It loads the initial sprite sheet, scales it, sets up the view for rendering, and
     * initializes the animation timeline for the player sprite.
     */
    private void setupPlayer() {
        // Load and scale the sprite sheet
        spritesheet = loadAndScaleSpriteSheet("file:Ressources/Player/Front Movement.png", SPRITE_WIDTH, SPRITE_HEIGHT, SCALE);

        // Setup the ImageView for the sprite
        setupSpriteImageView();

        // Initialize and start the animation timeline
        initializeAnimationTimeline();
    }

    /**
     * Loads and scales a sprite sheet from a given file path.
     * @param filePath The path to the sprite sheet file.
     * @param spriteWidth Width of each sprite in the sheet.
     * @param spriteHeight Height of each sprite in the sheet.
     * @param scale Scaling factor for the sprite.
     * @return Scaled WritableImage of the sprite sheet.
     */
    private WritableImage loadAndScaleSpriteSheet(String filePath, int spriteWidth, int spriteHeight, int scale) {
        Image originalImage = new Image(filePath);
        int scaledWidth = (int) (originalImage.getWidth() * scale);
        int scaledHeight = (int) (originalImage.getHeight() * scale);
        WritableImage scaledImage = new WritableImage(scaledWidth, scaledHeight);
        PixelWriter pixelWriter = scaledImage.getPixelWriter();

        for (int x = 0; x < scaledWidth; x++) {
            for (int y = 0; y < scaledHeight; y++) {
                Color pixelColor = originalImage.getPixelReader().getColor(x / scale, y / scale);
                pixelWriter.setColor(x, y, pixelColor);
            }
        }
        return scaledImage;
    }

    /**
     * Sets up the ImageView for the sprite with the scaled sprite sheet.
     */
    private void setupSpriteImageView() {
        spriteView = new ImageView(spritesheet);
        spriteView.setViewport(new Rectangle2D(0, 0, SPRITE_WIDTH * SCALE, SPRITE_HEIGHT * SCALE));
        spriteView.setSmooth(false);
    }

    /**
     * Initializes and starts the animation timeline for sprite frame updates.
     */
    private void initializeAnimationTimeline() {
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(100), e -> updateAnimationFrame()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    /**
     * Updates the current frame of the animation and sets the viewport of the sprite view.
     */
    private void updateAnimationFrame() {
        currentFrame = (currentFrame + 1) % (NUM_ROWS * NUM_COLS);
        int frameX = (currentFrame % NUM_COLS) * SPRITE_WIDTH * SCALE;
        int frameY = (!isMoving) ? 0 : SPRITE_HEIGHT * SCALE;
        spriteView.setViewport(new Rectangle2D(frameX, frameY, SPRITE_WIDTH * SCALE, SPRITE_HEIGHT * SCALE));
    }


    /**
     * Updates the player's position based on input keys. This method handles the movement
     * logic, sprite orientation, and updates the sprite sheet if necessary. It also ensures
     * that the player remains centered on the screen and moves the game world background
     * accordingly.
     */
    private void move() {
        final double speed = 1; // Speed of the player
        MovementDelta delta = new MovementDelta();

        boolean wasMoving = isMoving;
        int previousOrientation = lastOrientation;

        updateMovementDirection(delta, speed);
        normalizeMovementVector(delta, speed);
        updateSpriteIfNeeded(wasMoving, previousOrientation);
        updatePositions(delta);
    }

    /**
     * Updates the movement direction based on key inputs.
     * @param delta MovementDelta object to hold changes in x and y directions.
     * @param speed The speed of the player.
     */
    private void updateMovementDirection(MovementDelta delta, double speed) {
        if (keysPressed.contains(KeyCode.UP)) {
            delta.dy -= speed;
            lastOrientation = 0; // Up
        }
        if (keysPressed.contains(KeyCode.DOWN)) {
            delta.dy += speed;
            lastOrientation = 2; // Down
        }
        if (keysPressed.contains(KeyCode.LEFT)) {
            delta.dx -= speed;
            lastOrientation = -1; // Left
        }
        if (keysPressed.contains(KeyCode.RIGHT)) {
            delta.dx += speed;
            lastOrientation = 1; // Right
        }
    }

    /**
     * Normalizes the movement vector for diagonal movements.
     * @param delta MovementDelta object containing the current movement deltas.
     * @param speed The speed of the player.
     */
    private void normalizeMovementVector(MovementDelta delta, double speed) {
        if (delta.dx != 0 && delta.dy != 0) {
            double length = Math.sqrt(delta.dx * delta.dx + delta.dy * delta.dy);
            delta.dx = delta.dx / length * speed;
            delta.dy = delta.dy / length * speed;
        }

        // Update the moving status of the player
        isMoving = delta.dx != 0 || delta.dy != 0;
    }

    /**
     * Updates the positions of the player and the world view.
     * @param delta MovementDelta object containing the current movement deltas.
     */
    private void updatePositions(MovementDelta delta) {
        double newX = 540 - spriteView.getLayoutBounds().getWidth() / 2;
        double newY = 360 - spriteView.getLayoutBounds().getHeight() / 2;

        worldImageView.setX(worldImageView.getX() - delta.dx);
        worldImageView.setY(worldImageView.getY() - delta.dy);

        spriteView.setX(newX);
        spriteView.setY(newY);
    }

    /**
     * Updates the sprite sheet if the player's movement or orientation has changed.
     * @param wasMoving Indicates if the player was previously moving.
     * @param previousOrientation The previous orientation of the player.
     */
    private void updateSpriteIfNeeded(boolean wasMoving, int previousOrientation) {
        if (wasMoving != isMoving || previousOrientation != lastOrientation) {
            updateSpriteSheet();
        }
    }

    /**
     * Updates the positions of the player and the world view.
     * @param dx Horizontal movement delta.
     * @param dy Vertical movement delta.
     */
    private void updatePositions(double dx, double dy) {
        // Calculate the new position of the player (center of the screen)
        double newX = 540 - spriteView.getLayoutBounds().getWidth() / 2;
        double newY = 360 - spriteView.getLayoutBounds().getHeight() / 2;

        // Move the background in the opposite direction
        worldImageView.setX(worldImageView.getX() - dx);
        worldImageView.setY(worldImageView.getY() - dy);

        // Keep the player at the center
        spriteView.setX(newX);
        spriteView.setY(newY);
    }


    /**
     * Updates the sprite sheet based on the player's current orientation. This method
     * decides which sprite sheet to use (front, back, left, right) depending on the
     * direction the player is facing.
     */
    private void updateSpriteSheet() {
        switch (lastOrientation) {
            case -1: // Left
            case 1:  // Right
                changeSpriteSheet(SIDE_MOVEMENT);
                break;
            case 0:  // Up
                changeSpriteSheet(BACK_MOVEMENT);
                break;
            case 2:  // Down
                changeSpriteSheet(FRONT_MOVEMENT);
                break;
        }
    }

    /**
     * Changes the sprite sheet to the specified image path. If the new sprite sheet
     * is different from the current one, it updates the sprite view to reflect the new sprite sheet.
     * @param imagePath Path to the new sprite sheet image.
     */
    private void changeSpriteSheet(String imagePath) {
        if (!imagePath.equals(currentSpritesheet)) {
            currentSpritesheet = imagePath;
            spritesheet = new Image(imagePath);
            setupSpriteView();
        }
    }

    /**
     * Sets up the sprite view with the current sprite sheet. This method scales the sprite sheet,
     * writes it into a WritableImage, and sets this image to the sprite view. It also adjusts the
     * viewport of the sprite view to display the correct portion of the sprite sheet and handles
     * horizontal flipping for leftward movement.
     */
    private void setupSpriteView() {
        int scaledWidth = SPRITE_WIDTH * SCALE * NUM_COLS; // Scaled width of the sprite sheet
        int scaledHeight = SPRITE_HEIGHT * SCALE * NUM_ROWS; // Scaled height of the sprite sheet

        // Create a writable image for the scaled sprite sheet
        WritableImage scaledImage = new WritableImage(scaledWidth, scaledHeight);
        PixelWriter pixelWriter = scaledImage.getPixelWriter();

        // Scale the sprite sheet by writing each pixel into the scaled image
        for (int x = 0; x < scaledWidth; x++) {
            for (int y = 0; y < scaledHeight; y++) {
                int originalX = x / SCALE;
                int originalY = y / SCALE;
                Color pixelColor = spritesheet.getPixelReader().getColor(originalX, originalY);
                pixelWriter.setColor(x, y, pixelColor);
            }
        }

        // Set the scaled image to the sprite view and adjust its viewport
        spriteView.setImage(scaledImage);
        spriteView.setViewport(new Rectangle2D(0, 0, SPRITE_WIDTH * SCALE, SPRITE_HEIGHT * SCALE));
        spriteView.setSmooth(false); // Disable smoothing for a pixel-art style

        // Flip the sprite horizontally for leftward orientation
        if (lastOrientation == -1 && currentSpritesheet.equals(SIDE_MOVEMENT)) {
            spriteView.setScaleX(-1); // Flip horizontally
        } else {
            spriteView.setScaleX(1); // Normal orientation
        }
    }

    /**
     * Returns the ImageView representing the player's sprite.
     * @return ImageView of the player's sprite.
     */
    public ImageView getSpriteView() {
        return spriteView;
    }

}
