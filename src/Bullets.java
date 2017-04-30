import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/*This class controls the bullets, it sets their colours and allows them to move, it also checks for collisions
by drawing a rectangle around the image and checking for collisions against other rectangles*/

public class Bullets {

    int x; //xPosition 
	int y; //yPosition 
    int width; //width of the rectangle
    int height; //height of the rectangle
    
    //The colour of the bullets
    Color col;
    //the speed of the bullets
    int velocity;
    //Rectangle around bullets
    Rectangle bounds;

    public Bullets(int x, int y, int width, int height, Color col) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.col = col;
        this.velocity = 10;
        bounds = new Rectangle(x, y, width, height);
    }

    public void paint(Graphics g) {
        g.setColor(col);
        g.fillRect(x, y, width, height);
    }

    //Moves the bullet up the screen, also its boundaries
    public void move() {
        y -= velocity;
        bounds.setLocation(x, y);
    }
}