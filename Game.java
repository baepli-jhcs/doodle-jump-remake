
// import libraries
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.sound.*;

//create main class
public class Game extends PApplet {

    // declare global variables and objects
    int state;
    boolean startMoving;
    int screenWidth = 1500;
    int screenHeight = (int) (screenWidth * 0.6);
    float velocity = screenHeight / 700;
    Menu menu;
    PowerUp powerUp;
    int frameCount;
    int score;
    int currentSurface;
    int lastSurface;

    int highScore;

    ArrayList<Surfaces> surfaces;
    ArrayList<Surfaces> tempSurfaces;
    Player player;
    PFont scoreFont;

    SoundFile surfaceSound;
    SoundFile jumpBoots;
    SoundFile trampoline;

    public void settings() {
        size(screenWidth, screenHeight);
    }

    public void setup() {
        rectMode(CENTER);
        // create fonts, create initial surfaces, player, menu, and sound
        surfaceSound = new SoundFile(this, "data/jump.wav");
        jumpBoots = new SoundFile(this, "data/springshoes.mp3");
        trampoline = new SoundFile(this, "data/trampoline.mp3");
        scoreFont = createFont("Comic Sans MS", screenWidth / 50, true);
        frameRate(60);
        powerUp = new PowerUp();
        menu = new Menu(this);
        player = new Player(this);
        surfaces = new ArrayList<Surfaces>();
        surfaces.add(new Surfaces(this, screenWidth / 2, screenHeight, screenWidth, screenHeight / 8));
        for (int i = 10; i > 1; i--) {
            surfaces.add(new Surfaces(this, (int) random(0, screenWidth),
                    i * screenHeight / 10 - screenHeight / 8, screenWidth / 15,
                    screenHeight / 40));
        }
    }

    public void draw() {
        // change rectangle mode to center, then check for state
        rectMode(CENTER);
        background(0);
        switch (state) {
            case 0:
                menu.startScreen();
                break;
            case 1:
                // run player, then run surfaces
                player.jump = false;
                float j = Math.abs(player.velocity.y);
                while (j>0 && !powerUp.bigJump) {
                    for (int i = 0; i < surfaces.size(); i++) {
                        Surfaces tempSurface = surfaces.get(i);
                        // check for collisions, start movement if hit surface higher than one, check
                        // for powerups, play sound if reached new surface
                        if (objectDetection(player.position, tempSurface.position, player.rWidth, player.rHeight,
                                tempSurface.rWidth, tempSurface.rHeight) && player.velocity.y >= 0) {
                            player.position.y = tempSurface.position.y - tempSurface.rHeight / 2 - player.rWidth / 2;
                            player.velocity.y = 0;
                            player.jump = true;
                            currentSurface = i;

                            if (i > 0) {
                                startMoving = true;
                            }
                            if (tempSurface.red == 255) {
                                powerUp.bigJump(this);

                            } else if (currentSurface != lastSurface) {
                                lastSurface = currentSurface;
                                surfaceSound.play();
                            }

                            if (tempSurface.green == 255) {
                                powerUp.jumpBoots(this);
                            }

                            if (powerUp.jumpCount == 3) {
                                powerUp.resetJump(this);
                            }

                        }
                    }
                    if (player.velocity.y < 0) {
                        player.position.y--;
                    }
                    if (player.velocity.y > 0) {
                        player.position.y++;
                    }
                    j--;
                }
                // end bigJump camera movement if starting to fall
                if (player.velocity.y > -velocity && powerUp.bigJump) {
                    powerUp.resetJump(this);
                }

                // regular movement and acceleration
                if (startMoving) {
                    for (int i = 0; i < surfaces.size(); i++) {
                        surfaces.get(i).position.y += velocity;

                    }
                    player.position.y += velocity;

                    // every fifteen frames accelerate
                    frameCount++;
                    if (frameCount == 15) {
                        velocity += ((float) screenHeight) / 4000;
                        frameCount = 0;
                    }
                    if (velocity > screenHeight / 180) {
                        velocity = screenHeight / 180;
                    }

                }

                // powerup camera tracking
                if ((powerUp.bigJump) || (powerUp.jumpBoots && player.velocity.y < 0)) {
                    powerUp.centerJump = (int) (player.position.y - screenHeight / 4) / 15;
                    for (int i = 0; i < surfaces.size(); i++) {
                        surfaces.get(i).position.y -= (player.velocity.y + powerUp.centerJump);
                    }
                    player.position.y -= powerUp.centerJump;
                }

                // random spawning of powerups and moving objects
                for (int i = 0; i < surfaces.size(); i++) {
                    surfaces.get(i).run();
                    if (surfaces.get(i).movement) {
                        surfaces.get(i).position.x += surfaces.get(i).movementVelocity;
                    }
                    if (surfaces.get(i).position.y > 2 * screenHeight / 3 && !surfaces.get(i).spawned) {
                        surfaces.add(
                                new Surfaces(this, (int) random(screenHeight / 6, screenWidth),
                                        (int) random(-screenHeight / 6, 0),
                                        screenWidth / 15,
                                        screenHeight / 40));
                        if (25 == (int) random(50)) {
                            surfaces.get(surfaces.size() - 1).red = 255;
                        } else if (25 == (int) random(50)) {
                            surfaces.get(surfaces.size() - 1).green = 255;
                            surfaces.get(surfaces.size() - 1).blue = 255;
                            surfaces.get(surfaces.size() - 1).red = 100;
                        } else if (3 == (int) random(6)) {
                            surfaces.get(surfaces.size() - 1).blue = 255;
                            surfaces.get(surfaces.size() - 1).movement = true;
                        }
                        surfaces.get(i).spawned = true;
                    }
                    // remove object if goes past window
                    if (surfaces.get(i).position.y > screenHeight + surfaces.get(i).rHeight / 2) {
                        score++;
                        surfaces.remove(i);
                        currentSurface--;
                        lastSurface--;
                    }
                }
                player.run();
                textFont(scoreFont);
                fill(255);
                text("Score: " + score, 100, 100);
                break;
            case 2:
                // end screen
                menu.endScreen();
                if (score > highScore) {
                    highScore = score;
                }
                break;
        }

    }
    // reset things

    public void playAgain() {
        score = 0;
        startMoving = false;
        powerUp.jumpBoots = false;
        player = new Player(this);
        surfaces = new ArrayList<Surfaces>();
        surfaces.add(new Surfaces(this, screenWidth / 2, screenHeight, screenWidth, screenHeight / 8));
        velocity = screenHeight / 700;
        for (int i = 10; i > 1; i--) {
            surfaces.add(new Surfaces(this, (int) random(0, screenWidth),
                    i * screenHeight / 10 - screenHeight / 8, screenWidth / 15,
                    screenHeight / 40));
        }
    }

    public void keyPressed() {
        // check for jumps
        if ((key == ' ' || key == 'w' || keyCode == UP) && player.jump) {
            player.velocity.y = -player.jumpSpeed;
            if (powerUp.jumpBoots) {
                jumpBoots.play();
                powerUp.jumpCount++;
            }
        }
        if (key == 'd' || keyCode == RIGHT) {
            player.velocity.x = screenWidth / 125;
        }
        if (key == 'a' || keyCode == LEFT) {
            player.velocity.x = -screenWidth / 125;
        }

    }

    public void keyReleased() {
        if (key == 'a' || keyCode == LEFT) {
            player.velocity.x = 0;
        }
        if (key == 'd' || keyCode == RIGHT) {
            player.velocity.x = 0;
        }
    }

    public boolean objectDetection(PVector position1, PVector position2, int widthSize1, int heightSize1,
            int widthSize2, int heightSize2) {

        return position1.x + widthSize1 / 2 >= position2.x - widthSize2 / 2
                && position1.x - widthSize1 / 2 <= position2.x + widthSize2 / 2
                && position1.y + heightSize1 / 2 >= position2.y - heightSize2 / 2
                && position1.y + heightSize1 / 2 <= position2.y + heightSize2 / 2;
    }

    // processing arguments, required for library to work
    public static void main(String[] args) {
        String[] processingArgs = { "Game" };
        Game game = new Game();
        PApplet.runSketch(processingArgs, game);
    }

    // getters and setters
}