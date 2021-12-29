import processing.core.PVector;

public class Player {
    int rHeight;
    int rWidth;
    float jumpSpeed;
    PVector velocity;
    PVector position;
    boolean jump;
    private Game s;


    // set player settings
    Player(Game s) {
        this.s = s;
        position = new PVector(s.screenWidth/2, s.screenHeight/7);
        velocity = new PVector(0, 0);
        rHeight = s.screenHeight / 25;
        rWidth = s.screenHeight / 25;
        jumpSpeed = s.screenHeight/33;

    }

    // draw player, then set gravity and horizontal controls
    public void run() {
        s.fill(255);
        s.rect(position.x, position.y, rHeight, rWidth);
        if (s.keyPressed == true) {

        } else {
            velocity.x = 0;
        }
        //adds position and velocity only if bigJump is inactive
        if (!s.powerUp.bigJump) {
        position.x = position.x + velocity.x;
        } else {
            position.x += velocity.x ;
        }
        velocity.y += s.screenHeight / 700;
        if (position.x > s.screenWidth) {
            position.x = 0;
        } else if (position.x < 0) {
            position.x = s.screenWidth;
        }
        if (position.y > s.screenHeight) {
            s.state = 2;
        }
    }
}
