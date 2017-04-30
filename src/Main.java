/* Title: Space Wars
 * By: Shabaz Badshah
 * Date Started: Tuesday May 13, 2014
 * Date Completed Main: Wednesday May 28, 2014
 * Date Complete Final: Monday June 16, 2014
 * Classes were used after following the classes tutorial in the OUT drive and tutorial (in resource section)
 */

/*Resources Used:
 	http://www3.ntu.edu.sg/home/ehchua/programming/java/J8b_Game_2DGraphics.html
	http://software-talk.org/blog/2012/01/java-side-scroller-source/
	http://www.cokeandcode.com/info/tut2d.html
	http://www.cse.ohio-state.edu/cse1223/slides/07ArrayLists.pdf

	Video Used: https://www.youtube.com/watch?v=gHh_96Ss1AI&index=2&list=PLWms45O3n--6KCNAEETGiVTEFvnqA7qCi

	http://gamedev.stackexchange.com/questions/72355/java-2d-sidescroller-terrain
 	http://stackoverflow.com/questions/17724227/game-loop-control
	http://gamedev.stackexchange.com/questions/56956/how-do-i-make-a-game-tick-method
	http://www.java-gaming.org/index.php?topic=21372.0
	http://entropyinteractive.com/2011/02/game-engine-design-the-game-loop/
	http://www.java-gaming.org/index.php?topic=24220.0
	http://stackoverflow.com/questions/14374707/what-does-the-colon-mean-in-this-java-code
 *
 *Pictures used: 
 *http://piq.codeus.net/picture/62027/space_invaders
 */

/*BUGS and other info
 - Pictures were not showing up, kept on deleting themselves
 	- Moved to res folder, if deleted again move from res folder to bin
 - Menu would not go down to the other options
 	- Had to be implemented with integers and not booleans
 - Particles could not be implemented because no efficient and appealing solutions could be used
 	- One of the solutions worked, but the game wouldn't stop flickering (this was a wierd bug
 	  because we were using double buffering)
 - After the player's health was less than 0, the screen wouldn't update unless the user clicked
   the window to minimize it and then maximize it
 */


import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Applet implements Runnable, KeyListener{

	private static final long serialVersionUID = 1L; //Eclipse suggested to add this version ID, it does not impact the game 

	//Integers
	private Graphics graphics;
	private int shotCounter; //Amount of shots fired
	private int bulletAmount; //Amount of bullets the player has
	private int enemyAmount;  //Amount of enemies
	private int enemyKillCount = 0; //Enemies killed
	private int score = 0; //Score
	private int threadDelay = 15; //ThreadDelay of run method
	private int menuCount;//Menu Options Counter
	
	//Window Variables
	private int windowWidth = 400; //SizeX of window
	private int windowHeight = 600;//SizeY of window
	
	//Images
	private Image image;
	private Player player; //Calls player class
	private Enemy enemy; //Calls enemy class
	private Image mainMenuBG = Toolkit.getDefaultToolkit().getImage("mainMenuBG.png");
	private Image instructionBG = Toolkit.getDefaultToolkit().getImage("instructionScreen.png");
	private Image select = Toolkit.getDefaultToolkit().getImage("select.png");

	//ArrayLists
	private ArrayList <Bullets> bullets; //Array list for bullets because we don't know how many items are going to be held, they resize themselves automatically
	private ArrayList <Enemy> enemies = new ArrayList <Enemy> (); 

	//BOOLEANS
	//Player movement and control booleans
	private boolean boost = false;
	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;
	
	//Gamestates 
	public boolean running = true;
	private boolean exit = false;
	private boolean lost = false;
	private boolean play = true;
	private boolean pause;	

	//Screens
	private boolean gameScreen;
	private boolean mainMenu;
	private boolean instructionScreen;

	//Initializes arrays and the window
	public void init() {

		mainMenu = true;

		setSize(windowWidth, windowHeight);
		setFocusable (true); //Allows the user to not have to click the screen in order to start playing
		
		//Adds key listener for use of keys
		addKeyListener(this);

		//Initializes player,bullet,and enemy class	
		bullets = new ArrayList<Bullets>();
		enemies = new ArrayList<Enemy>();
	}

	public void start() {
		Thread th = new Thread(this);
		th.start();
	}

	public void run() {
		while (running) {
			repaint();
			try {
				Thread.sleep(threadDelay);
			} catch (Exception e) {
			}
		}
	}

	public void stop() {
		running = false;
	}

	public void destroy() {
		running = false;
	}

	public void paint( Graphics g) {

		if (mainMenu)
		{
			//Draws specified images and the select cursor at an option
			g.drawImage(mainMenuBG, 0, 0, this);

			if (menuCount == 0) {
				g.drawImage (select, 60, 140, this); 
			}
			if (menuCount == 1) {
				g.drawImage (select, 60, 294, this);
			}

			if (menuCount == 2) {
				g.drawImage (select, 60, 430, this); 
			}
		} else if (instructionScreen) {
			g.drawImage(instructionBG, 0, 0, this);
		}

		// If the user clicks play
		else if (play) {
			if (gameScreen)
			{			
				//Game UI
				if (player.health > 0) {
					/*Displays health bar, score, and weapon.*/
					//Health bars
					g.setColor(Color.WHITE);
					g.fillRect(10, getHeight() - 20, 100, 12);
					g.setColor(Color.RED);
					g.fillRect(10, getHeight() - 20, player.health / 10, 12);
					g.setColor(Color.BLACK);
					g.drawString("Health: " + player.health, 40, getHeight() - 10);
					g.setColor(Color.WHITE);
					//Kill Count 
					g.drawString ("Kill Count: " + enemyKillCount, getWidth() - 130, getHeight() - 23);
					//Score 
					g.drawString("Score: " + score, getWidth() - 130, getHeight() - 10);
					g.drawLine(0, getHeight() - 40, getWidth(), getHeight() - 40);
					//Shot Counter
					g.drawString("Shots Fired: " + shotCounter, getWidth() - 250, getHeight() - 10);
					//Pause instruction
					//Kill Count 
					g.drawString ("Press 'p'to pause", getWidth() - 390, getHeight() - 25);
				}

				//Draws all the bullets in the arrayList in the loop
				for (Bullets b : bullets) {
					b.paint(g);
				}

				//Draws all the enemies in the arrayList in the loop
				for (Enemy e : enemies) {
					e.drawImage(g);
				} 
			}

			//If the player loses tell the user that they have lost 
			if (lost == true) {
				g.setColor (Color.black);
				g.fillRect (0, 0, windowWidth, windowHeight);
				g.setColor (Color.white);
				g.drawString ("You have failed this Galaxy!", getWidth()/2 - 70, 215);
				g.drawString ("Press Escape to Exit", getWidth()/2 - 70, 230);
				g.drawString ("By: Shabaz Badshah", getWidth()/2 - 110, 250);
				g.drawString ("Score: " + score + " Shots Fired : " + shotCounter + " Enemies Killed: " + enemyKillCount , getWidth () / 2 - 130 , 200);
			}

			//If the player has not died yet, draw the player
			if (!lost){
				player.drawImage(g);
			}

			if (pause) {
				g.setColor(Color.WHITE); 
				g.drawString ("PAUSED",180,200);
				g.drawString ("PRESS ENTER TO RESUME", getWidth()/2 - 70,215);
			}

			//Updates the game
			update();
		}
	}

	//Double buffer method (graphics)
	public void update (Graphics g) { //Used other resources (look in resources section)
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			graphics = image.getGraphics();
		}
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight()); //Black background
		paint(graphics);
		g.drawImage(image, 0, 0, this); 
	}

	//Update method (logic)
	public void update() {

		/*The (!pause) allows for the game to be paused, it is a simple boolean that governs the pause function
		 * If the player pauses the the game, the game doesn't run
		 */
		if (!pause) {
			//Allows the player to move in the x and y directions
			if (moveUp && player.y > 0) {
				player.moveUp();
			}
			if (moveDown && player.y + player.height < getHeight() - 100) {
				player.moveDown();
			} 
			if (moveLeft && player.x > 0) {
				player.moveLeft();
			}
			if (moveRight && player.x + player.width < getWidth() - 38) {
				player.moveRight();
			}
			if (boost == true){
				player.velocity = 10;
				player.boost -= 5;
			} else {
				player.velocity = 5; 
				player.boost = 50;
			}


			//This controls the spawn rate of the enemies by comparing the random number with the spawnRate variable
			Random ra = new Random();
			int random = ra.nextInt(1000); 
			int spawnRate = 50;
			if (random < spawnRate) {
				enemyAmount = 1;
			} else {
				enemyAmount = 0;
			}
			
			//Creates an enemy with a random health amount and random speed
			if (enemyAmount == 1) {
				Random r = new Random();
				//Spawns the enemy within the boundaries of the screen
				int enemySpawnX = r.nextInt(getWidth() - 55);

				//Allows for the enemies to have random health and speeds
				int randHealth = r.nextInt(10);
				int randVelocity = r.nextInt(3) + 1;
				//Creates a new instance of the enemy 
				enemy = new Enemy(enemySpawnX, 0, 32, 32, randHealth, randVelocity, "Enemy.png");
				enemies.add(enemy);
				repaint();
				enemyAmount++;
			}

			/*Creates new instance of the bullet class and calls them twice, allows for two bullets to be shot at once
			  If the player has shot a bullet, it will create a new bullet and allow the player to fire it, There are no limits to the fire rate or
			  amount (unlimited ammo)*/

			if (bulletAmount == 1) {
				Bullets bullet2= null;
				Bullets bullet;
				bullet2 = new Bullets(player.x + 8, player.y - player.height + 2, 6, 6, Color.YELLOW);
				bullet = new Bullets(player.x + 18, player.y - player.height + 2, 6, 6, Color.YELLOW);
				//Adds the creates bullets to the arrayList
				bullets.add(bullet);
				bullets.add(bullet2);
				bulletAmount++;
				shotCounter++;
				repaint ();
			}

			/* Moves enemies toward the player. If the enemy passes the player they will be removed. If the enemy intersects with
			the player, the player will be damaged and the enemy will be removed
			if enemy intersects with bullet, the bullet will damage enemy
			health depending on what gun they have. */

			//Removes enemy if they go past player, player loses 50 health
			for (int j = 0; j < enemies.size(); j++) {
				enemies.get(j).move();

				if (enemies.get(j).y + enemies.get(j).height > getHeight() - 42) {
					enemies.remove(j);
					player.health -= 50;
					j--;
					continue;
				}

				//Removes enemy if they hit player, player loses 50 health
				if (enemies.get(j).bounds.intersects(player.r)) {
					enemies.remove(j);
					player.health -= 50;
					j--;
					continue;
				}

				/*Removes enemy if they get shot by comparing the two rectangles around the objects and checking to see if they have
				 * intersected (uses the .intersects method, imported rectangle)
				 */
				for (int i = 0; i < bullets.size(); i++) {

					//Checks for intersection
					if (enemies.get(j).bounds.intersects(bullets.get(i).bounds)) {
						enemies.get(j).health -= 100;
						//Removes the bullet from the arrayList
						bullets.remove(i);
						if (enemies.get(j).health <= 0) {
							//Removes the enemy if they die
							enemies.remove(j);
							score += 50;
							enemyKillCount++;
							//Moves to the next iteration of the loop
							continue;
						}
					}
				} 
			}
		}	

		/*Removes the bullets fired if they move off the screen (there is no use using resources for something that has been used)
		 * This for loop is also responsible for moving all the bullets
		 */
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).move();
			if (bullets.get(i).y <= 0) {
				//Removes bullets
				bullets.remove(i);
				i--;
				//Moves to the next iteration of the loop
				continue;
			}
		}

		//If the player dies
		if (player.health  <= 0) {
			//Stop running the game and set lost to true
			enemies.clear();
			repaint();
			running = false;
			lost = true; 
		}
	}

	//This method is only used for the menu screens
	public void keyTyped(KeyEvent e) {

		int key = e.getKeyCode();
		if (mainMenu) {

			if (key == KeyEvent.VK_DOWN) {
				exit = true;
				play = false;
			}

			if (key == KeyEvent.VK_UP && exit == true) {
				play = true;	
				exit = false;
			}
		}

		//Exit if the user presses escape
		if (gameScreen) {
			if (key == KeyEvent.VK_ESCAPE) {
				exit = true;
			}
		}
	}

	//Basic movement of the player and other controls
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		//These set of keys will only work if the mainMenu is true
		if (mainMenu) {	

			//The menu count corresponds to the option selected 
			if (key == KeyEvent.VK_DOWN){
				menuCount ++;

				if (menuCount >= 2) { //Does not allow the user to go lower than this value
					menuCount = 2;
				}
			}	
			if (key == KeyEvent.VK_UP) {
				menuCount--; 
				if (menuCount <= 0) { //Does not allow the user to go higher than this value 
					menuCount = 0;
				}
			}

			//If the user clicks enter and starts the game
			if (key == KeyEvent.VK_ENTER) {
				if (menuCount == 0) {
					mainMenu = false; 
					exit = false; 
					gameScreen = true; 
					//Initializes player when the use leaves the main menu
					player = new Player(getWidth() / 2, getHeight() - 100, 30, 8, "ship.png");
				}

				if (menuCount == 1) {
					instructionScreen = true; 
					mainMenu = false; 
				}

				if  (menuCount == 2) {
					System.exit(0);
				}
			}
		}

		if (instructionScreen) {
			if (key == KeyEvent.VK_ESCAPE){

				instructionScreen = false; 
				mainMenu = true; 
			}
		}

		if (gameScreen) {

			if (key == KeyEvent.VK_SHIFT){
				boost = true;
			}
			if (key == KeyEvent.VK_W) {
				moveUp = true;
			}
			if (key == KeyEvent.VK_S) {
				moveDown = true;
			} 
			if (key == KeyEvent.VK_A) { 
				moveLeft = true;
			}
			if (key == KeyEvent.VK_D) {
				moveRight = true;
			}
			if (key == KeyEvent.VK_SPACE) {
				bulletAmount = 1;
			}
			if (key == KeyEvent.VK_ESCAPE) {
				System.exit (0);
			}
			if (key == KeyEvent.VK_P) {
				pause = true;
			}
			if (key == KeyEvent.VK_ENTER) {
				pause = false;
			}
		}
	}


	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_SHIFT){
			boost = false;
		}
		if (key == KeyEvent.VK_W) {
			moveUp = false;
		}
		if (key == KeyEvent.VK_S) {
			moveDown = false;
		} 
		if (key == KeyEvent.VK_A) { 
			moveLeft = false;
		}
		if (key == KeyEvent.VK_D) {
			moveRight = false;
		}
		if (key == KeyEvent.VK_SPACE) {
			bulletAmount = 0;
		}
		if (key == KeyEvent.VK_ESCAPE) {
			exit = false;
		}
		if (key == KeyEvent.VK_P) {
			pause = true;
		}
		if (key == KeyEvent.VK_ENTER) {
			pause = false;
		}
	}
}


