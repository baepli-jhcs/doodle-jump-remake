public class PowerUp {
    int centerJump;
    boolean bigJump;
    boolean jumpBoots;
    int jumpCount;

    void bigJump(Game s) {
        s.trampoline.play();
        bigJump = true;
        s.startMoving = false;
        s.player.velocity.y = -s.screenHeight / 13;
    }

    void jumpBoots(Game s) {
        jumpCount = 0;
        jumpBoots = true;
        s.player.jumpSpeed = s.screenHeight / 23;
    }

    void resetJump(Game s) {
        bigJump = false;
        s.startMoving = true;
        jumpBoots = false;
        s.player.jumpSpeed = s.screenHeight / 33;
        jumpCount = 0;
    }
}
