//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.image.PixelWriter;
//import javafx.scene.image.WritableImage;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//import mikera.math.PerlinNoise;
//
//public class Main extends Application {
//    @Override
//    public void start(Stage primaryStage) {
//        // Set the seed (optional)
//        long seed = 123;
//
//        // Create a PerlinNoise instance
//        PerlinNoise perlin = new PerlinNoise((int) seed);
//
//        // Define the dimensions of the image
//        int width = 1080;
//        int height = 720;
//
//        Image icon = new Image("file:Ressources/icon.png");
//        // Définir l'icône pour la fenêtre
//        primaryStage.getIcons().add(icon);
//
//        // Create an Image object to store the Perlin noise image
//        Image noiseImage = generatePerlinNoiseImage(perlin, width, height);
//
//        // Create an ImageView to display the image
//        ImageView imageView = new ImageView(noiseImage);
//
//        // Create a StackPane to hold the ImageView
//        StackPane root = new StackPane();
//        root.getChildren().add(imageView);
//
//        // Create the JavaFX Scene
//        Scene scene = new Scene(root, width, height);
//
//        // Set the stage title and scene
//        primaryStage.setTitle("Alcia: Survive !");
//
//
//        Rectangle player = new Rectangle(20, 20, Color.BLUE); // Un joueur de taille 20x20 de couleur bleue
//        player.setX(100); // Position initiale X
//        player.setY(100); // Position initiale Y
//        root.getChildren().add(player);
//
//        scene.setOnKeyPressed(e -> {
//            switch (e.getCode()) {
//                case UP:    player.setY(player.getY() - 5); break;
//                case DOWN:  player.setY(player.getY() + 5); break;
//                case LEFT:  player.setX(player.getX() - 5); break;
//                case RIGHT: player.setX(player.getX() + 5); break;
//            }
//        });
//
//
//        primaryStage.setScene(scene);
//        // Show the stage
//        primaryStage.show();
//    }
//
//    private Image generatePerlinNoiseImage(PerlinNoise perlin, int width, int height) {
//        // Create a WritableImage to store the Perlin noise image
//        WritableImage writableImage = new WritableImage(width, height);
//        PixelWriter pixelWriter = writableImage.getPixelWriter();
//
//        // Generate Perlin noise for each pixel in the image
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                // Set the coordinates with appropriate scaling
//                float scaledX = x * 0.05f; // Adjust the scale as needed
//                float scaledY = y * 0.05f; // Adjust the scale as needed
//
//                // Generate Perlin noise at the scaled coordinates
//                double noiseValue = perlin.noise2(scaledX, scaledY);
//
//                // Map the noise value to a grayscale color
//                int colorValue = (int) (Math.abs(noiseValue) * 255);
//                int rgb = (colorValue << 16) | (colorValue << 8) | colorValue;
//
//                // Set the color of the pixel in the image
//                ((PixelWriter) pixelWriter).setArgb(x, y, -16777216 | rgb); // -16777216 is for full opacity
//            }
//        }
//
//        return writableImage;
//    }
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
//
////        // define variables
////        String stickName = "walk stick";
////        // define the resources
////        Resource stick = new Resource(stickName, 11, 10);
////
////        stick.setName("stick");
////
////        int weightStick = stick.getWeightInGrams();
////        String nameStick = stick.getName();
////        System.out.println("The weight of " + nameStick +" is " + weightStick + " grams");
////
////        Resource fork = stick.Split(0);
//
///*
//        System.out.println("The name of the resource is " + fork.getName()
//                + " and the quantity is " + fork.getQuantity()
//                + " and the weight is "
//                + fork.getWeightInGrams() + " grams");
//
//        System.out.println("The name of the resource is " + stick.getName()
//                + " and the quantity is " + stick.getQuantity()
//                + " and the weight is "
//                + stick.getWeightInGrams() + " grams");
//*/
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        // // Création du personnage joueur
//        // Character player = new Character("Survivor", 100, 100, 0, null, null, null);
//
//        // // Initialisation de quelques ressources
//        // Resource water = new Resource("Water", 10);
//        // Resource food = new Resource("Food", 5);
//
//        // // Initialisation de quelques outils
//        // Tool axe = new Tool("Axe", 50, 0);
//        // Tool fishingRod = new Tool("Fishing Rod", 30, 0);
//
//        // // Initialisation de quelques dangers
//        // Danger wildAnimal = new Danger("Wild Animal", null, 70, 0);
//        // Danger storm = new Danger("Storm", null, 50, 0);
//
//        // // Logique pour démarrer le jeu
//        // // Par exemple, afficher un message de bienvenue, les instructions du jeu, etc.
//        // System.out.println("Welcome to the Survival Game!");
//        // System.out.println("Try to survive as long as you can.");
//
//        // player.collectResource(food);
//        // player.toString();

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import Player.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane gamePane = new Pane();
        Player player = new Player(gamePane);

        Scene scene = new Scene(gamePane, 300, 300);
        primaryStage.setTitle("Déplacement fluide du Joueur");
        scene.setFill(Color.DARKGRAY);
        primaryStage.setScene(scene);
        primaryStage.show();

        gamePane.requestFocus(); // Important pour capturer les événements clavier
    }

    public static void main(String[] args) {
        launch(args);
    }
}
