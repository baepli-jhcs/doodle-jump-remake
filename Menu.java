import processing.core.PConstants;
import processing.core.PFont;

public class Menu {
    private Game s;
    int textWidthOffset;
    int widthButtonOffset;
    int heightButtonOffset;
    int widthSize;
    int heightSize;
    int curveSize;
    PFont large;
    PFont small;
    PFont options;

    // create menu
    Menu(Game s) {
        this.s = s;
        s.textAlign(PConstants.CENTER);
        large = s.createFont("Microsoft YaHei Bold", s.screenWidth / 25, true);
        small = s.createFont("Comic Sans MS", s.screenWidth / 80, true);
        options = s.createFont("Microsoft YaHei Bold", s.screenWidth / 60, true);
        textWidthOffset = s.screenWidth / 2;
        widthButtonOffset = s.screenWidth / 2;
        heightButtonOffset = (int) (0.60 * s.screenHeight);
        widthSize = (int) (0.2 * s.screenWidth);
        heightSize = (int) (0.1 * s.screenHeight);
        curveSize = (int) (0.005 * s.screenWidth);
    }

    // start screen
    public void startScreen() {
        s.fill(255);
        s.textFont(large);
        s.text("Welcome to Square Jump!", textWidthOffset, 2 * s.screenHeight / 5);
        s.fill(255, 255, 0);
        if (createButton(widthButtonOffset, heightButtonOffset, widthSize, heightSize, curveSize)) {
            s.state = 1;
        }
        s.fill(0, 0, 255);
        s.textFont(small);
        s.text("Click here to begin!", widthButtonOffset, heightButtonOffset);
    }

    // button function
    private boolean createButton(int widthOffset, int heightOffset, int widthSize, int heightSize, int curve) {
        if (s.mouseX > (widthOffset - widthSize / 2)
                && s.mouseX < (widthOffset + widthSize / 2)
                && s.mouseY > (heightOffset - heightSize / 2)
                && s.mouseY < (heightOffset + heightSize / 2)) {
            s.fill(255, 255, 255);
            s.rect(widthOffset, heightOffset, widthSize, heightSize, curve, curve, curve, curve);
            return s.mousePressed;

        } else {
            s.fill(255, 255, 0);
            s.rect(widthOffset, heightOffset, widthSize, heightSize, curve, curve, curve, curve);
        }
        return false;
    }

    // end screen
    public void endScreen() {
        s.fill(255);
        s.textFont(large);
        s.text("Failed! Your Score Is: " + s.score, textWidthOffset, s.screenHeight / 3);
        s.textFont(options);
        s.text("Your High Score Is: " + s.highScore, s.screenWidth / 2, s.screenHeight * 3 / 7);
        s.fill(255, 255, 0);
        if (createButton(s.screenWidth / 2, heightButtonOffset, widthSize, heightSize, 0)) {
            s.playAgain();
            s.state = 1;
        }
        s.fill(0, 0, 255);
        s.textFont(small);
        s.text("Click here to play again!", s.screenWidth / 2, heightButtonOffset);

    }
}
