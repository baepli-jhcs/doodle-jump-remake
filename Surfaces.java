import processing.core.PVector;

public class Surfaces {
    int rWidth;
    int rHeight;
    boolean spawned;
    private Game s;
    int speed;
    int red;
    int green = 155;
    int blue;
    boolean movement;
    int movementVelocity;
    PVector position;

    // declare location and size of surfaces
    Surfaces(Game s, float widthOffset, float heightOffset, int rWidth, int rHeight) {
        position = new PVector(widthOffset, heightOffset);
        this.s = s;
        this.rWidth = rWidth;
        this.rHeight = rHeight;
        movementVelocity = (int) s.random(-s.screenWidth / 150, s.screenWidth / 150);

    }

    // draw surfaces
    public void run() {
        s.fill(red, green, blue);
        s.rect(position.x, position.y, rWidth, rHeight, s.screenWidth / 100, s.screenWidth / 100,
                s.screenHeight / 100, s.screenHeight / 100);
        if (position.x > s.screenWidth || position.x < 0) {
            movementVelocity *= -1;

        }
    }
}
