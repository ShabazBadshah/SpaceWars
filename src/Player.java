import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/*This class controls the player and also draws it. It contains the player's position, health, boost
 * and boundaries. It compares the collisions in the main class by comparing the rectangle around
 * the objects with its specific rectangle
 */
public class Player {
    int velocity;
    int x; //xPosition 
    int y; //yPosition 
    int width; //width of rectangle 
    int height; //height of rectangle
    int health; //health
    int boost = 50; //Boost, changes the velocity by the boost
    Rectangle r; //Boundries for player
    Image img; //Player sprite

    public Player(int x, int y, int width, int height, String s) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = 1000;
        this.velocity = 5;
        
        //Creates the instance of the boundaries around the player
        r = new Rectangle(x, y, width, height);
        img = Toolkit.getDefaultToolkit().getImage(s);
    }
    public void setImage(String s) {
    	//Sets the image 
        img = Toolkit.getDefaultToolkit().getImage(s);
    }
    
    //Moves the player and the collision boxes to the desired direction
    public void moveLeft() {
        x -= velocity;
        r.setLocation(x,y);
    }

    public void moveRight() {
        x += velocity;
        r.setLocation(x,y);
    }

    public void moveUp() {
    	y -= velocity;
        r.setLocation(x, y);
    }

    public void moveDown() {
        y += velocity;
        r.setLocation(x, y);
    } 

    //Draws the rectangle around the player
    public void draw(Graphics g) { 
        g.fillRect(x, y, width, height);
    }
    
    //Draws the player
    public void drawImage(Graphics g) {
        g.drawImage(img, x, y, null);
    }  

}