import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/*This class controls the enemies and also draws them. It creates sets their positions, health, and speeds.
 This class also checks their collision by comparing the rectangle around them with the other 
 game objects*/
public class Enemy {

	int x; //xPosition 
	int y; //yPosition 
	int height; //height of the rectangle
	int width; //width of the rectangle
	int velocity; //Enemy Speed
	int health; //Enemy health
	Image img; //Enemy sprite
	Rectangle bounds; //Image boundries


	public Enemy(int x, int y, int height, int width, int health, int velocity, String s) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.velocity = velocity;
		this.health = health;
		img = Toolkit.getDefaultToolkit().getImage(s);
		bounds = new Rectangle(x,y,height,width);
	}

	//Gets the enemy sprite
	public void setImage(String s) {
		img = Toolkit.getDefaultToolkit().getImage(s);
	}

	//Moves the enemy and the collision rectangle down with it 
	public void move() {
		y += velocity;
		bounds.setLocation(x,y); //Moves the rectangle, testing collision
	}

	//Draws the rectangle
	public void draw(Graphics g) {
		g.fillRect(x, y, width, height);
	}

	//Draws the enemy sprite
	public void drawImage(Graphics g) {
			g.drawImage(img, x, y, null);
	}
}
