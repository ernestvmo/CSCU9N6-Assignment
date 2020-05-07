package ernest.stirling.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;

import ernest.stirling.sound.Sound;
import n6.stirling.game2D.Animation;
import n6.stirling.game2D.Entity;
import n6.stirling.game2D.GameCore;
import n6.stirling.game2D.Sprite;
import n6.stirling.game2D.Tile;
import n6.stirling.game2D.TileMap;

@SuppressWarnings("serial")
public class Main extends GameCore
{
	// Useful game constants
	/** The width of the game screen. */
	private static int screenWidth = 640;
	/** The height of the game screen. */
	private static int screenHeight = 384;
	/** !!! USE ONLY FOR DEBUGGING !!!
	 * This method will turn any debugging controls on.
	 */
	private boolean _debug_ = false;
	
	// Game state flags
	/** Flag for the player idling. */
	private boolean idling = true;
	/** Flag for the player moving. */
	private boolean isMoving = false;
	/** Flag for the player jumping. */
	private boolean jumping = false;
	/** Flag for the player being armed. */
	private boolean hasWeapon = false;
	/** Flag for the muted. */
	private boolean muted = false;
	/** Flag for the player attacking. */
	private boolean isAttacking = false;
	/** Flag for the cutscene playing. */
	private boolean cutScenePlaying = false;
	
	/** The force pulling the player upward when it jumps. */
	private float lift = -0.25f;
	/** The force pulling the player downward. */
	private float gravity = 0.00075f;

	/** The speed at which a player can move horizontally. */
	private float playerMovingSpeed = 0.15f;
	
	/** An offset to display the player at the right place in the screen. */
	private final int attackOffset = 32;
	
	/** The score that the player is getting fromo collecting coins, ... */
	private int score;
	/** A variable to store the score after level one was completed. */
	private int scoreAfterL1 = 0;
	/** The amount of times the player can die before the game is over. */
	private int deathCount = 3;
	/** Flag for which level is activated. */
	private int leversActivated = 1;
	
	// Game resources
	// Speeds at which to move the screen.
	private float scrollSpeedX = .5f;
	private float scrollSpeedY = .5f;
	
	// Images
	// Images for the background
	private Image bg1;
	private Image bg2;
	private Image bg3;
	private Image bg4;
	private Image bg5;
	private Image bg6;
	private Image bg7;
	
	private Image title_background;
	private Image title_screen_img;
	private Image title_play;
	private int title_play_x, title_play_y;
	private Image title_quit;
	private int title_quit_x, title_quit_y;
	private Image sound_img, sound_on,sound_off;
	private int sound_img_x, sound_img_y;
	private Image pause_resume;
	private int pause_resume_x, pause_resume_y;
	private Image pause_bg;

	private Image scroll;
	private Image heart;
	private Image icon;
	
	private Image[] numbers = {loadImage("images/title/numbers/zero.png"),
			loadImage("images/title/numbers/one.png"),
			loadImage("images/title/numbers/two.png"),
			loadImage("images/title/numbers/three.png"),
			loadImage("images/title/numbers/four.png"),
			loadImage("images/title/numbers/five.png"),
			loadImage("images/title/numbers/six.png"),
			loadImage("images/title/numbers/seven.png"),
			loadImage("images/title/numbers/eight.png"),
			loadImage("images/title/numbers/nine.png")};
	private Image winningScreenImg, losingScreenImg, yes, no;
	
	// Animation
	// Player animations
	/** Animation for the player idling without a weapon. */
	private Animation idleAnim;
	/** Animation for the player idling with a weapon. */
	private Animation idleWeaponAnim;
	/** Animation for the player walking without a weapon. */
	private Animation walkingAnim;
	/** Animation for the player walking with a weapon. */
	private Animation walkingWeaponAnim;
	/** Animation for the player attack. */
	private Animation attackAnim;
	/** Animation for the player death without a weapon. */
	private Animation deathAnim;	
	/** Animation for the player death with a weapon. */
	private Animation deathWeaponAnim;	
	
	// Enemy animations
	/** Animation for the wolf moving. */
	private Animation wolfAnim;
	/** Animation for the wolf's death. */
	private Animation wolfDeathAnim;
	/** Animation for the bat's death. */
	private Animation batAnim1, batAnim2;
	/** Animation for the bat. */
	private Animation batDeathAnim;
	
	/** Animation for the axe. */
	private Animation axeAnim;
	/** Animation for the flag. */
	private Animation flagAnim;
	
	// Entities
	/** The player entity. */
	private Entity player;
	/** Array of entities representing the enemies. */
	private Entity[] enemies;
	
	/** The amount of enemies in the game. */
	private int enemyCount = 3;
	/** The speed of the Bat entity. */
	private static float BAT_FOLLOWING_SPEED = 0.05f;
	/** The speed of the Wolf entity. */
	private static float WOLF_SPEED = 0.07f;
	
	// Standalone sprites
	/** The sprite containing the axe. */
	private Sprite axe = null;
	/** The sprite containing the flag. */
	private Sprite flag = null;
	
	// TileMaps
	/** Variable used to store the current level. */
	private TileMap level;
	/** Variable used to store the tilemap of level 1. */
	private TileMap level1 = new TileMap("level1");	
	/** Variable used to store the tilemap of level 2. */
	private TileMap level2 = new TileMap("level2");	
	
	// Sounds
	/** Player hurt sound. */
	private Sound hurt;
	/** Player death sound. */
	private Sound death;
	/** Background music. */
	private Sound background_music;
	/** Collectable sound. */
	private Sound coin_sound;
	/** Enemy death sound. */
	private Sound wolf_death, bat_death;
	/** Lever activated sound. */
	private Sound lever_sound;
	
	/** Enums used to flag the state of the game */
	private enum GameState
	{
		TITLE,
		PAUSE,
		PLAYING, 
		WIN,
		LOST
	}
	
	/** Variable used to evaluate the state of the game through enums. 
	 * @see GameState
	 */
	private GameState state;
	
	public static void main(String[] args)
	{
		Main gameCore = new Main();
		gameCore.init();
		gameCore.run(false, screenWidth, screenHeight);
	}
	
	/**
	 * Initialize the game and any of its attributes.
	 */
	private void init()
	{
		// Set the state of the game to TITLE.
		state = GameState.TITLE;
		
		// Images
		// Background
		bg1 = loadImage("maps/backgrounds/background 1.png");
		bg2 = loadImage("maps/backgrounds/background 2.png");
		bg3 = loadImage("maps/backgrounds/background 3.png");
		bg4 = loadImage("maps/backgrounds/background 4.png");
		bg5 = loadImage("maps/backgrounds/background 5.png");
		bg6 = loadImage("maps/backgrounds/background 6.png");
		bg7 = loadImage("maps/backgrounds/background 7.png");

		scroll = loadImage("images/other/scroll.png");
		heart = loadImage("images/other/heart.png");
		icon = loadImage("images/other/icon.png");
		
		title_background = loadImage("images/title/background.png");
		title_screen_img = loadImage("images/title/title_screen.png");
		title_play = loadImage("images/title/play.png");
		title_quit = loadImage("images/title/quit.png");
		pause_resume = loadImage("images/title/resume.png");
		pause_bg = loadImage("images/title/pause_menu.png");
		sound_on = loadImage("images/title/sound_on.png");
		sound_off = loadImage("images/title/sound_off.png");

		sound_img = sound_on;
		
		winningScreenImg = loadImage("images/title/winningScreen.png");
		losingScreenImg = loadImage("images/title/losingScreen.png");
		yes = loadImage("images/title/yes.png");
		no = loadImage("images/title/no.png");
		
        background_music = new Sound("sounds/background.mid");
		
		// Frame settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(icon);
		setTitle("CavEscape");
		setResizable(false);
//		setLocationRelativeTo(null);
		
		// Levels
        level1.loadMap("maps", "levels/level1.txt");
        level2.loadMap("maps", "levels/level2.txt");
        
        // Animations
        idleAnim = new Animation();
        idleAnim.loadAnimationFromSheet("images/player/Skeleton Idle - no weapon.png", 11, 1, 60);
        
        idleWeaponAnim = new Animation();
        idleWeaponAnim.loadAnimationFromSheet("images/player/Skeleton Idle - weapon.png", 11, 1, 60);
        
        walkingAnim = new Animation();
        walkingAnim.loadAnimationFromSheet("images/player/Skeleton Walk - no weapon.png", 13, 1, 60);
        
        walkingWeaponAnim = new Animation();
        walkingWeaponAnim.loadAnimationFromSheet("images/player/Skeleton Walk - weapon.png", 13, 1, 60);
        
        attackAnim = new Animation();
        attackAnim.loadAnimationFromSheet("images/player/Skeleton Attack.png", 18, 1, 90);
        
        deathAnim = new Animation();
        deathAnim.loadAnimationFromSheet("images/player/Skeleton Dead - no weapon.png", 15, 1, 60);
        
        deathWeaponAnim = new Animation();
        deathWeaponAnim.loadAnimationFromSheet("images/player/Skeleton Dead - weapon.png", 15, 1, 60);
        
//        damageAnim = new Animation();
//        damageAnim.loadAnimationFromSheet("images/player/Skeleton Hit - no weapon.png", 8, 1, 60);
        
//        damageWeaponAnim = new Animation();        
//        damageWeaponAnim.loadAnimationFromSheet("images/player/Skeleton Hit - weapon.png", 8, 1, 60);
        
        axeAnim = new Animation();
        axeAnim.loadAnimationFromSheet("images/other/axe_aura.png", 6, 1, 60);
        
        flagAnim = new Animation();
        flagAnim.loadAnimationFromSheet("images/other/flag.png", 7, 1, 70);
        
        batAnim1 = new Animation();
        batAnim1.loadAnimationFromSheet("images/enemy/bat/bat_moving.png", 5, 1, 60);
        batAnim2 = new Animation();
        batAnim2.loadAnimationFromSheet("images/enemy/bat/bat_moving.png", 5, 1, 60);

        batDeathAnim = new Animation();
        batDeathAnim.loadAnimationFromSheet("images/enemy/bat/bat_death.png", 7, 1, 80);

        wolfAnim = new Animation();
        wolfAnim.loadAnimationFromSheet("images/enemy/wolf/wolf.png", 21, 1, 60);
        
        wolfDeathAnim = new Animation();
        wolfDeathAnim.loadAnimationFromSheet("images/enemy/wolf/wolf death.png", 17, 1, 60);
        
        // Sprites
        Sprite batSprite1 = new Sprite(batAnim1);
        Sprite batSprite2 = new Sprite(batAnim2);
    	Sprite wolfSprite = new Sprite(wolfAnim);
    	Sprite playerSprite = new Sprite(idleAnim);
        axe = new Sprite(axeAnim);
        flag = new Sprite(flagAnim);

        // Create enemies
        enemies = new Entity[enemyCount];
        enemies[0] = new Entity(wolfSprite, 1);
        enemies[1] = new Entity(batSprite1, 1);
        enemies[2] = new Entity(batSprite2, 1);
        // Initialize player
        player = new Entity(playerSprite, 3);
        // Set the current level to level 1
        level = level1;
        
        initialiseGame();
        background_music.start();
	}
	
	/**
	 * This method sets the position of the enemies.
	 */
	private void setEnemyPosition()
	{
		if (level.getName().equals("level2"))
		{
			enemies[0].getSprite().setX(455);
			enemies[0].getSprite().setY(260);
			enemies[1].getSprite().setX(1200);
			enemies[1].getSprite().setY(200);
			enemies[2].getSprite().setX(1400);
			enemies[2].getSprite().setY(200);
		}
	}
	
	/**
	 * This method initializes the player and the level positions.
	 */
	private void initialiseGame()
	{
		player.setLife(3);
		switchAnim(idleAnim, player.getSprite().getAnimation(), player.getSprite());
		
		player.getSprite().setX(81);
        player.getSprite().setY(230);
        player.getSprite().setVelocityX(0);
        player.getSprite().setVelocityY(0);
        player.getSprite().show();
        
        if (level.getName().equals("level1"))
		{
			axe.setX(1650);
			axe.setY(310);
			axe.show();

			flag.setX(1950);
			flag.setY(244);
			flag.show();
		}
        else if (level.getName().equals("level2"))
		{
			setEnemyPosition();
		}
	}

	/**
	 * This method draw everything on the screen.
	 */
	public void draw(Graphics2D g)
	{
		if (state == GameState.TITLE)
		{
			g.drawImage(title_background, ((screenWidth / 2) - (title_background.getWidth(null) / 2)), 0, null);
			g.drawImage(title_screen_img, ((screenWidth / 2) - (title_screen_img.getWidth(null) / 2)), 100, null);
			title_play_x = (screenWidth / 2) - (title_play.getWidth(null) / 2);
			title_play_y = 230;
			g.drawImage(title_play, title_play_x, title_play_y, null);
			title_quit_x = (screenWidth / 2) - (title_quit.getWidth(null) / 2);
			title_quit_y = title_play_y + 50;
			g.drawImage(title_quit, title_quit_x, title_quit_y, null);
			sound_img_x = 590;
			sound_img_y = 40;
			g.drawImage(sound_img, sound_img_x, sound_img_y, null);
		}
		else if (state == GameState.PLAYING)
		{
			int xo, yo;
			xo = (screenWidth / 2) - ((player.getSprite().getAnimation().equals(attackAnim) && player.getMovingDirection() == -1) 
					? (Math.round(player.getSprite().getX() + attackOffset)) : (Math.round(player.getSprite().getX())));
			xo = Math.min(xo, 0);
			xo = Math.max(xo, screenWidth - level.getPixelWidth());

			// int yo = screenHeight / 2 - Math.round(player.getY());
			// yo = Math.min(yo, 0);
			yo = screenHeight - level.getPixelHeight();

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());

			drawBackground(g, xo, yo);

			player.getSprite().setOffsets(xo, yo);
			axe.setOffsets(xo, yo);
			axe.draw(g);

			flag.setOffsets(xo, yo);
			flag.draw(g);
			
			// Apply offsets to tile map and draw it
			level.draw(g, xo, yo);

			if (player.getMovingDirection() == -1)
				player.getSprite().flip(g);
			else
				player.getSprite().drawTransformed(g);

			if (level.equals(level2))
			{
				for (int j = 0; j < enemyCount; j++)
				{
					enemies[j].getSprite().show();
					enemies[j].getSprite().setOffsets(xo, yo);

					if (enemies[j].getMovingDirection() == 1)
						enemies[j].getSprite().drawTransformed(g);
					else
						enemies[j].getSprite().flip(g);
				}
			}
			
			if (_debug_)
			{
				g.setColor(Color.BLUE);
				g.drawRect((int) player.getSprite().getX() + xo, (int) player.getSprite().getY() + yo, player.getSprite().getWidth(), player.getSprite().getHeight());
				
				g.setColor(Color.RED);
				g.drawRect((int) enemies[0].getSprite().getX() + xo, (int) enemies[0].getSprite().getY() + yo, enemies[0].getSprite().getWidth(), enemies[0].getSprite().getHeight());
				g.drawArc((int) enemies[1].getSprite().getX() + xo - 100, (int) enemies[1].getSprite().getY() + yo - 100, enemies[1].getSprite().getWidth() + 200, enemies[1].getSprite().getHeight() + 200, 0, 360);
				g.drawArc((int) enemies[2].getSprite().getX() + xo - 100, (int) enemies[2].getSprite().getY() + yo - 100, enemies[2].getSprite().getWidth() + 200, enemies[2].getSprite().getHeight() + 200, 0, 360);
				g.drawArc((int) enemies[1].getSprite().getX() + xo, (int) enemies[1].getSprite().getY() + yo, enemies[1].getSprite().getWidth(), enemies[1].getSprite().getHeight(), 0, 360);
				g.drawArc((int) enemies[2].getSprite().getX() + xo, (int) enemies[2].getSprite().getY() + yo, enemies[2].getSprite().getWidth(), enemies[2].getSprite().getHeight(), 0, 360);

				g.setColor(Color.GREEN);
				g.drawRect((int) axe.getX() + xo, (int) axe.getY() + yo, axe.getWidth(), axe.getHeight());
				g.drawRect((int) flag.getX() + xo, (int) flag.getY() + yo, flag.getWidth(), flag.getHeight());
			}
			 
			int offsetHeart = 30;
			int y_line = 345;
			for (int i = 0; i < player.getLife(); i++)
			{
				g.drawImage(heart, offsetHeart, y_line - heart.getWidth(null)/2, null);
				offsetHeart += heart.getWidth(null) + 5;
			}
			
			switch (deathCount)
			{
				case 0 :
					g.drawImage(numbers[0], screenWidth - 80, y_line - numbers[0].getWidth(null) / 2, null);
					break;
				case 1 :
					g.drawImage(numbers[1], screenWidth - 80, y_line - numbers[1].getWidth(null) / 2, null);
					break;
				case 2 :
					g.drawImage(numbers[2], screenWidth - 80, y_line - numbers[2].getWidth(null) / 2, null);
					break;
				case 3 :
					g.drawImage(numbers[3], screenWidth - 80, y_line - numbers[3].getWidth(null) / 2, null);
					break;
			}

			g.drawImage(icon, screenWidth - 50, y_line - icon.getWidth(null) / 2, null);

			// Dialog box
			displayDialog(player.getSprite(), g);
		}
		else if(state == GameState.PAUSE)
		{
			g.setColor(new Color(34, 28, 26));
			g.fillRect(0, 0, screenWidth, screenHeight);
			
			g.drawImage(pause_bg, ((screenWidth / 2) - (pause_bg.getWidth(null) / 2)), ((screenHeight / 2) - (pause_bg.getHeight(null) / 2) + 10), null);

			pause_resume_x = ((screenWidth / 2) - (pause_resume.getWidth(null) / 2));
			pause_resume_y = 150;
			g.drawImage(pause_resume, pause_resume_x, pause_resume_y, null);
			
			sound_img_x = ((screenWidth / 2) - (64 / 2));
			sound_img_y = 200;
			g.drawImage(sound_img, sound_img_x, sound_img_y, 64, 64, null);
			
			title_quit_x = (screenWidth / 2) - (title_quit.getWidth(null) / 2);
			title_quit_y = 270;
			g.drawImage(title_quit, title_quit_x, title_quit_y, null);
		}
		else if (state == GameState.WIN)
		{
			g.setColor(new Color(34, 28, 26));
			g.fillRect(0, 0, screenWidth, screenHeight);
			int sx = (screenWidth/2 - winningScreenImg.getWidth(null)/2), sy = (screenHeight/2 - winningScreenImg.getHeight(null)/2);
			g.drawImage(winningScreenImg, sx, sy, null);
			WinningMessageGenerator.generateMessage(score, g, sx + 255, sy + 148, numbers);
		}
		else if (state == GameState.LOST)
		{
			g.setColor(new Color(34, 28, 26));
			g.fillRect(0, 0, screenWidth, screenHeight);
			g.drawImage(losingScreenImg, (screenWidth / 2 - losingScreenImg.getWidth(null) / 2), 126, null);
			g.drawImage(yes, (screenWidth / 2 - (25 + yes.getWidth(null))), 230, null);
			g.drawImage(no, (screenWidth / 2 + 25), 230, null);
		}
		

		if (_debug_)
		{
			g.setColor(Color.RED);
			g.fillRect(screenWidth / 2 - (g.getFontMetrics().stringWidth("DEBUG") / 2) - 5, 32, g.getFontMetrics().stringWidth("DEBUG")+10, 24);
			g.setColor(Color.BLACK);
			g.fillRect(screenWidth / 2 - (g.getFontMetrics().stringWidth("DEBUG") / 2) - 3, 34, g.getFontMetrics().stringWidth("DEBUG")+6, 20);
			g.setColor(Color.RED);
	        g.setFont(new Font("Consolas", Font.PLAIN, 20));
			g.drawString("DEBUG", screenWidth / 2 - (g.getFontMetrics().stringWidth("DEBUG") / 2), 50);
		}
	}
	
	/**
	 * Display the information scroll with appropriate message.
	 * 
	 * @param sprite	The player sprite.
	 * @param graphic	The Graphics2D object to draw the scroll on.
	 */
	private void displayDialog(Sprite sprite, Graphics2D graphic) 
	{
		String msg = "";
		boolean showDialog = false;
		
		if (level.getName().equals("level1"))
		{
			if (sprite.getX() == 80f && sprite.getY() == 252f)
			{
				showDialog = true;
				msg = "Use the left and rigth key to move";
			}
			else if (sprite.getY() > 120 + scroll.getHeight(null))
			{
				if (sprite.getX() >= 470f && sprite.getX() <= 820f)
				{
					showDialog = true;
					msg = "Use the Up key to jump";
				}
				else if (sprite.getX() >= 950f && sprite.getX() <= 1150f)
				{
					showDialog = true;
					msg = "Don't fall in the lava!";
				}
			}
			
			if (hasWeapon && cutScenePlaying)
			{
				showDialog = true;
				msg = "Press Space to attack!";
			}
			
			if (hasWeapon && sprite.getX() >= 1700f)
			{
				cutScenePlaying = false;
			}
		}
		
		if (player.isDead())
		{
			showDialog = true;
			msg = "You died! Press Space to Restart";
		}
		
		if (showDialog == true)
		{
			graphic.setColor(Color.black);
	        graphic.setFont(new Font("Blackadder ITC", Font.PLAIN, 32));
			graphic.drawImage(scroll, (screenWidth / 2 - scroll.getWidth(null) / 2), 60, null);
			graphic.drawString(msg, screenWidth / 2 - (graphic.getFontMetrics().stringWidth(msg) / 2), 110);
			showDialog = false;
		}
	}
	
	/**
	 * Draw the background of the game.
	 * 
	 * @param graphics 	The Graphics2D object to draw the scroll on.
	 * @param xo		The offset on the x axis.
	 * @param yo		The offset on the y axis.
	 */
	private void drawBackground(Graphics2D graphics, int xo, int yo)
	{
        graphics.drawImage(bg7, (int) (xo), (int) (yo) + 40, null);
        graphics.drawImage(bg7, (int) (xo) + bg7.getWidth(null), (int) (yo) + 40, null);
        graphics.drawImage(bg7, (int) (xo) + bg7.getWidth(null)*2, (int) (yo) + 40, null);
		
        graphics.drawImage(bg6, (int) (xo*scrollSpeedX*.65f), (int) (yo*scrollSpeedY-.2f) + 40, null);
		graphics.drawImage(bg6, (int) (xo*scrollSpeedX*.65f) + bg6.getWidth(null), (int) (yo*scrollSpeedY-.2f) + 40, null);
		
		graphics.drawImage(bg5, (int) (xo*scrollSpeedX*.55f), (int) (yo*scrollSpeedY-.2f) + 40, null);
		graphics.drawImage(bg5, (int) (xo*scrollSpeedX*.55f) + bg5.getWidth(null), (int) (yo*scrollSpeedY-.2f) + 40, null);
		
		graphics.drawImage(bg4, (int) (xo*scrollSpeedX*.45f), (int) (yo*scrollSpeedY-.2f) + 40, null);
		graphics.drawImage(bg4, (int) (xo*scrollSpeedX*.45f) + bg4.getWidth(null), (int) (yo*scrollSpeedY-.2f) + 40, null);
		
		graphics.drawImage(bg3, (int) (xo*scrollSpeedX*.35f), (int) (yo*scrollSpeedY-.2f) + 40, null);
		graphics.drawImage(bg3, (int) (xo*scrollSpeedX*.35f) + bg3.getWidth(null), (int) (yo*scrollSpeedY-.2f) + 40, null);
		
		graphics.drawImage(bg2, (int) (xo*scrollSpeedX*.25f), (int) (yo*scrollSpeedY-.2f) + 40, null);	
		graphics.drawImage(bg2, (int) (xo*scrollSpeedX*.25f) + bg2.getWidth(null), (int) (yo*scrollSpeedY-.2f) + 40, null);
		
		graphics.drawImage(bg1, (int) (xo*scrollSpeedX*.15f), (int) (yo*scrollSpeedY) + 40, null);
		graphics.drawImage(bg1, (int) (xo*scrollSpeedX*.15f) + bg1.getWidth(null), (int) (yo*scrollSpeedY) + 40, null);
	}
	
	public void update(long elapsed)
	{
		if (state == GameState.PLAYING)
		{
			if (deathCount < 0)
			{
				state = GameState.LOST;
			}
			
			if (player.getLife() <= 0 && !player.isDead())
				death(player);

			for (Entity e : enemies)
				if (e.getLife() <= 0 && !e.isDead())
				{
					death(e);
				}

			if (hasWeapon)
			{
				idleAnim = idleWeaponAnim;
				walkingAnim = walkingWeaponAnim;
				deathAnim = deathWeaponAnim;
			}

			player.getSprite().setAnimationSpeed(1.0f);

			if (idling)
			{
				switchAnim(idleAnim, player.getSprite().getAnimation(), player.getSprite());
			}

			if (jumping)
			{
				while (player.getJumps() > 0)
				{
					player.getSprite().setVelocityY(lift);
					player.setJumps(player.getJumps() - 1);
				}
			}

			if (isMoving)
			{
				switchAnim(walkingAnim, player.getSprite().getAnimation(), player.getSprite());
				player.getSprite().setVelocityX(player.getMovingDirection() * playerMovingSpeed);
			}
			else 
				player.getSprite().setVelocityX(0f);

			if (isAttacking)
			{
				attack();
			}

			player.getSprite().setVelocityY(player.getSprite().getVelocityY() + (gravity * elapsed));

			if (level.getName().equals("level2"))
			{
				if (!enemies[0].isDead())
				{
					enemies[0].getSprite().setVelocityY(enemies[0].getSprite().getVelocityY() + (gravity * elapsed));
					enemies[0].getSprite().setVelocityX(WOLF_SPEED * enemies[0].getMovingDirection());
				}
			}

			// Update the Sprites' animation and position.
			player.getSprite().update(elapsed);

			if (level.getName().equals("level2"))
			{
				enemies[0].getSprite().update(elapsed);
				enemies[1].getSprite().update(elapsed);
				enemies[2].getSprite().update(elapsed);
			}

			axe.update(elapsed);
			flag.update(elapsed);

			handleSpritesCollisions();

			// Check for any collisions that may have occurred
			handleTileMapCollisions(player);

			if (level.getName().equals("level2"))
			{
				handleTileMapCollisions(enemies[0]);
				handleTileMapCollisions(enemies[1]);
				handleTileMapCollisions(enemies[2]);
				handleEnemyDetection(enemies[1]);
				handleEnemyDetection(enemies[2]);
			}
		}
	}
	
	/**
	 * This is a method that handles the tilemap collision. 
	 * 
	 * @param entity	The entity to check the collision of.
	 */
	private void handleTileMapCollisions(Entity entity)
    {
    	// This method should check actual tile map collisions. For
    	// now it just checks if the player has gone off the bottom
    	// of the tile map.
    	
//		 int playerLeft = (int) entity.getSprite().getX();
//		 int playerRight = (int) entity.getSprite().getX() + entity.getSprite().getWidth();
//		 int playerTop = (int) entity.getSprite().getX();
//		 int playerBottom = (int) (entity.getSprite().getY() + entity.getSprite().getHeight());

		boolean collisionLeft = false;
        boolean collisionRight = false;
        boolean collisionTop = false;
        boolean collisionBottom = false;
        
        try
        {
        	Tile topLeftTile = level.getTile((int) (entity.getSprite().getX()) / level.getTileWidth(), (int) entity.getSprite().getY() / level.getTileHeight());
        	Tile topRightTile = level.getTile((int) (entity.getSprite().getX() + entity.getSprite().getImage().getWidth(null)) / level.getTileWidth(), (int) entity.getSprite().getY() / level.getTileHeight());
	        Tile bottomLeftTile = level.getTile((int) (entity.getSprite().getX()) / level.getTileWidth(), (int) (entity.getSprite().getY() + entity.getSprite().getImage().getHeight(null)) / level.getTileHeight());
	        Tile bottomRightTile = level.getTile((int) (entity.getSprite().getX() + entity.getSprite().getImage().getWidth(null)) / level.getTileWidth(), (int) (entity.getSprite().getY() + entity.getSprite().getImage().getHeight(null)) / level.getTileHeight());
        
        	collisionTop = checkCollisionTileTop(entity.getSprite(), topLeftTile, level) || checkCollisionTileTop(entity.getSprite(), topRightTile, level);
        	collisionBottom = checkCollisionTileBottom(entity.getSprite(), bottomLeftTile, level) || checkCollisionTileBottom(entity.getSprite(), bottomRightTile, level);
        	collisionRight = ((checkCollisionTileRight(entity.getSprite(), topRightTile, level)) && checkCollisionTileMiddle(entity.getSprite(), level, 'r'))
						|| (checkCollisionTileRight(entity.getSprite(), bottomRightTile, level) && checkCollisionTileMiddle(entity.getSprite(), level, 'r'))
						|| (checkCollisionTileRight(entity.getSprite(), topRightTile, level) && checkCollisionTileRight(entity.getSprite(), bottomRightTile, level));
        	collisionLeft = (checkCollisionTileLeft(entity.getSprite(), topLeftTile, level) && checkCollisionTileMiddle(entity.getSprite(), level, 'l'))
        				|| (checkCollisionTileLeft(entity.getSprite(), bottomLeftTile, level) && checkCollisionTileMiddle(entity.getSprite(), level, 'l'))
        				|| (checkCollisionTileLeft(entity.getSprite(), topLeftTile, level) && checkCollisionTileLeft(entity.getSprite(), bottomLeftTile, level));

			// System.out.println(topLeftTile);
			// System.out.println(topRightTile);
			// System.out.println(bottomLeftTile);
			// System.out.println(bottomRightTile);
			//
			// System.out.println("collisionTop: " + collisionTop);
			// System.out.println("collisionBottom: " + collisionBottom);
			// System.out.println("collisionRight: " + collisionRight);
			// System.out.println("collisionLeft: " + collisionLeft);

			// TODO: improve handling of collision
			if (collisionLeft)
			{
				entity.getSprite()
						.setX(bottomLeftTile.getXC() + level.getTileWidth());
				if (!entity.equals(player))
					entity.setMovingDirection(entity.getMovingDirection() * -1);
			}
			if (collisionRight)
			{
				entity.getSprite().setX(bottomRightTile.getXC()
						- entity.getSprite().getWidth() - 1);
				if (!entity.equals(player))
					entity.setMovingDirection(entity.getMovingDirection() * -1);
			}
			if (collisionTop)
			{
				entity.getSprite()
						.setY(topLeftTile.getYC() + level.getTileHeight());
			}
			if (collisionBottom)
			{
				jumping = false;
				entity.getSprite().setVelocityY(0f);
				entity.setJumps(1);
				entity.getSprite().setY(bottomLeftTile.getYC()
						- entity.getSprite().getHeight());
			}

			if (entity != player)
			{
				handleEnemyTurn(entity, bottomLeftTile, bottomRightTile, level);
			}

			if (!player.isDead())
			{
				if (isTileMurderous(bottomLeftTile, level) || isTileMurderous(bottomRightTile, level))
				{
					entity.getSprite().setVelocityY(lift);
					damageEntityLife(entity, -1);
				}
			}
		}
		catch (Exception e) { }
    }

	/**
	 * This method checks if there is a collision with the top of the sprite.
	 * 
	 * @param sprite The sprite to check collision of.
	 * @param tile	 The tile the sprite is colliding with.
	 * @param level  The TileMap to check the collision on.
	 * 
	 * @return {@code true} if the tile touches a tile, {@code false} otherwise.
	 */
	private boolean checkCollisionTileTop(Sprite sprite, Tile tile, TileMap level)
	{
		boolean collides = false;
		if (level.getCollectables().contains(tile.getCharacter()))
		{
			collectableCollected(tile, level, sprite);
		}
		else if (tile.getCharacter() != '.')
		{
			collides = collides || ((sprite.getY() < (tile.getYC() + level.getTileHeight())));
		}
		
		return collides;
	}
	
	/**
	 * This method checks if there is a collision with the bottom of the sprite.
	 * 
	 * @param sprite The sprite to check collision of.
	 * @param tile	 The tile the sprite is colliding with.
	 * @param level  The TileMap to check the collision on.
	 * 
	 * @return {@code true} if the tile touches a tile, {@code false} otherwise.
	 */
	private boolean checkCollisionTileBottom(Sprite p, Tile t, TileMap l)
	{
		boolean collides = false;
		if (l.getCollectables().contains(t.getCharacter()))
		{
			collectableCollected(t, l, p);
		}
		else if (t.getCharacter() != '.')
		{
			collides = collides || (((p.getY() + p.getHeight()) > t.getYC()));
		}
		
		return collides;
	}
	
	/**
	 * This method checks if there is a collision with the right of the sprite.
	 * 
	 * @param sprite The sprite to check collision of.
	 * @param tile	 The tile the sprite is colliding with.
	 * @param level  The TileMap to check the collision on.
	 * 
	 * @return {@code true} if the tile touches a tile, {@code false} otherwise.
	 */
	private boolean checkCollisionTileRight(Sprite p, Tile t, TileMap l)
	{
		boolean collides = false;
		if (l.getCollectables().contains(t.getCharacter()))
		{
			collectableCollected(t, l, p);
		}
		else if (t.getCharacter() != '.')
		{
			collides = collides || (((p.getX() + p.getWidth()) > t.getXC()));
		}
		
		return collides;
	}
	
	/**
	 * This method checks if there is a collision with the left of the sprite.
	 * 
	 * @param sprite The sprite to check collision of.
	 * @param tile	 The tile the sprite is colliding with.
	 * @param level  The TileMap to check the collision on.
	 * 
	 * @return {@code true} if the tile touches a tile, {@code false} otherwise.
	 */
	private boolean checkCollisionTileLeft(Sprite p, Tile t, TileMap l)
	{
		boolean collides = false;
		if (l.getCollectables().contains(t.getCharacter()))
		{
			collectableCollected(t, l, p);
		}
		else if (t.getCharacter() != '.')
		{
			collides = collides || ((p.getX() < (t.getXC() + l.getTileWidth())));
		}
		
		return collides;
	}
	
	/**
	 * This method checks if there is a collision with the middle (on the x axis) of the sprite.
	 * 
	 * @param sprite The sprite to check collision of.
	 * @param tile	 The tile the sprite is colliding with.
	 * @param level  The TileMap to check the collision on.
	 * 
	 * @return {@code true} if the tile touches a tile, {@code false} otherwise.
	 */
	private boolean checkCollisionTileMiddle(Sprite p, TileMap l, char movingDir)
	{
		boolean collides = false;
		Tile t = null;
		
		if (movingDir == 'r')
		{
			t = l.getTile((int) (p.getX() + p.getWidth()) / l.getTileWidth(), (int) (p.getY() + (p.getImage().getHeight(null) / 2)) / l.getTileHeight());
		}
		else
		{
			t = l.getTile((int) (p.getX()) / l.getTileWidth(), (int) (p.getY() + (p.getImage().getHeight(null) / 2)) / l.getTileHeight());
		}
		
		if (l.getCollectables().contains(t.getCharacter()))
		{
			collectableCollected(t, l, p);
		}
		else if (t.getCharacter() != '.')
		{
			if (movingDir == 'r')
			{
				collides = (((p.getX() + p.getWidth()) > t.getXC()));
			}
			else
			{
				collides = ((p.getX() < (t.getXC() + l.getTileWidth())));
			}
		}
		
		return collides;
	}
	
	/**
	 * This method handles the collision between sprites.
	 */
	private void handleSpritesCollisions()
	{
		Sprite sprite = player.getSprite();
		
		if (level.getName().equals("level1"))
		{// level 1 collision
			if (axe.getX() < (screenWidth / 2 + (sprite.getX())))
			{
				if (sprite.boundingBoxCollision(axe))
				{
					coin_sound = new Sound("sounds/coin.wav", 0.6f);
					handleSound(coin_sound);
					
					axe.setX(-50f);
					axe.setY(-50f);
					hasWeapon = true;
					cutScenePlaying = true;
					axe.hide();
				}
			}

			if (((sprite.getX() + 100f) > flag.getX()) && (flag.getX() + flag.getWidth()) > (player.getSprite().getX() - 100f))
			{
				if (sprite.boundingBoxCollision(flag))
				{
					swicthLevel(level2);
					scoreAfterL1 = score;
				}
			}
		}
		else if (level.getName().equals("level2"))
		{// level 2 collision
			if (sprite.boundingCircleRectangleCollision(enemies[0].getSprite()) && !enemies[0].isDead())
			{
				if (player.getSprite().getAnimation().equals(attackAnim))
					damageEntityLife(enemies[0], -1);
				else if (player.isDead())
				{
					// do nothing
				}
				else
				{
					damageEntityLife(player, -1);
					player.getSprite().setVelocityY(lift);
					player.getSprite().setX(player.getSprite().getX() - 5f);
					enemies[0].setMovingDirection(enemies[0].getMovingDirection() *-1);
				}
			}
			
			for (int i = 1; i < enemyCount; i++)
				if (sprite.boundingCircleRectangleCollision(enemies[i].getSprite())
						&& !enemies[i].isDead())
				{
					if (player.getSprite().getAnimation().equals(attackAnim))
						damageEntityLife(enemies[i], -1);
					else if (player.isDead())
					{
						// do nothing
					}
					else
					{
						damageEntityLife(player, -1);
					}
				}
			if (((sprite.getX() + 100f) > flag.getX()) && (flag.getX() + flag.getWidth()) > (player.getSprite().getX() - 100f))
			{
				if (sprite.boundingBoxCollision(flag))
					state = GameState.WIN;
			}
		}
	}
	
	/**
	 * This method handles the sound played by the game.
	 * 
	 * @param sound	The sound to be handled.
	 */
	private void handleSound(Sound sound)
	{
		try
		{
			if (sound.equals(background_music))
			{
				if (muted)
				{
					background_music.pauseSound();
				}
				else if (!muted)
				{
					background_music.resumeSound();
				}
			}
			else
			{
				if (!muted)
				{
					sound.start();
				}
			}
		}
		catch (Exception e)
		{}
	}
	
	/**
	 * This method will check if the player is in the range of the entity and follow it.
	 * 
	 * @param e Entity to follow the enemy.
	 */
	private void handleEnemyDetection(Entity e)
	{
		// following range in pixel;
		int range = 100;
		
		if (player.getSprite().getX() >= 960 && !e.isDead())
		{
			int dx, dy, minimumR;
			float minimum;
			
			dx = (int) (player.getSprite().getX() - e.getSprite().getX());
			dy = (int) (player.getSprite().getY() - e.getSprite().getY());
			minimum = (player.getSprite().getRadius() + e.getSprite().getRadius());
			minimumR = (int)minimum + range;
			
			if (((dx * dx) + (dy * dy)) < (minimumR * minimumR))
			{
				Point playerCenter = new Point((int) player.getSprite().getX() + (player.getSprite().getWidth() / 2), (int) player.getSprite().getY() + (player.getSprite().getHeight() / 2));
				Point batCenter = new Point((int) e.getSprite().getX() + (e.getSprite().getWidth() / 2), (int) e.getSprite().getY() + (e.getSprite().getHeight() / 2));
				
				float moveToX = playerCenter.x;
				float moveToY = playerCenter.y;
				
				float diffX = moveToX - batCenter.x;
				float diffY = moveToY - batCenter.y;
				
				float angle = (float) Math.atan2(diffY, diffX);
				
				e.getSprite().setVelocityX((float) (BAT_FOLLOWING_SPEED * Math.cos(angle)));
				e.getSprite().setVelocityY((float) (BAT_FOLLOWING_SPEED * Math.sin(angle)));
			}
			else
			{
				e.getSprite().setVelocityX(0);
				e.getSprite().setVelocityY(0);
			}
		}
	}
	
	/**
	 * Handle the movement of entities on the ground.
	 * 
	 * @param e	The entity to handle.
	 * @param bottomLeftTile	The tile at the bottom left of the entity sprite.
	 * @param bottomRightTile	The tile at the bottom right of the entity sprite.
	 * @param level	The TileMap to check the collision with.
	 */
	private void handleEnemyTurn(Entity e, Tile bottomLeftTile, Tile bottomRightTile, TileMap level)
	{
		try
		{
			Tile next_tile_left = level.getTile((bottomLeftTile.getXC() / 32) - 1, bottomLeftTile.getYC() / 32);
			Tile next_tile_right = level.getTile((bottomRightTile.getXC() / 32) + 1, bottomRightTile.getYC() / 32);
			
			if (next_tile_left.getCharacter() == '.')
			{
				if (bottomLeftTile.getCharacter() != '.')
					if (e.getSprite().getX() <= bottomLeftTile.getXC() + 3)
					{
						e.setMovingDirection(e.getMovingDirection()*-1);
					}
			}
			
			if (next_tile_right.getCharacter() == '.')
			{
				if (bottomRightTile.getCharacter() != '.')
					if ((e.getSprite().getX() + e.getSprite().getWidth()) >= (bottomRightTile.getXC() + level.getTileWidth()) - 3)
					{
						e.setMovingDirection(e.getMovingDirection()*-1);
					}
			}
		}
		catch (Exception ex)
		{
			// this try catch was added for enemies near the tilemap's edges.
			e.setMovingDirection(e.getMovingDirection()*-1);
		}
	}
	
	/**
	 * Reset the level in case of player's death.
	 * 
	 * @param level The last level the player died on.
	 */
	private void resetLevel(TileMap level)
	{
		if (level.getName().equals("level1"))
		{
			level = level1;
			score = 0;
		}
		else if (level.getName().equals("level2"))
		{
			for (Entity e : enemies)
			{
				e.setLife(1);
				e.setIsDead(false);
				e.getSprite().playAnimation();
			}
			deathAnim.reload();
			wolfDeathAnim.reload();
			batAnim1.reload();
			batAnim2.reload();
			
			enemies[0].getSprite().setAnimation(wolfAnim);
			enemies[1].getSprite().setAnimation(batAnim1);
			enemies[2].getSprite().setAnimation(batAnim2);
			
			score = scoreAfterL1;
			level = level2;
			setEnemyPosition();
		}
	}
	
	/**
	 * This method handles the attack of the player.
	 */
	private void attack()
	{
		idling = false;
		isMoving = false;
		player.getSprite().setAnimationSpeed(1.75f);
		
		if (!player.getSprite().getAnimation().equals(attackAnim))
			switchAnim(attackAnim, player.getSprite().getAnimation(), player.getSprite());
		
		if (player.getSprite().getAnimation().isAnimationFinished())
		{
			attackAnim.reload();
			isAttacking = false;
			idling = true;
		}
		
       	player.getSprite().setAnimationSpeed(1.0f);
	}
	
	/**
	 * This method handle the damage done to a specified entity.
	 * 
	 * @param entity The entity to damage.
	 * @param diff	The amount of life to remove.
	 */
	private void damageEntityLife(Entity entity, int diff)
	{
		entity.setLife(entity.getLife() + diff);
		
		if (entity.equals(player) && entity.getLife() != 0)
		{
			hurt = new Sound("sounds/oof.wav", 0.6f);
			handleSound(hurt);
		}
	}
	
	/**
	 * This method will handle the death of the specified entity.
	 * 
	 * @param e The entity to handle the death of.
	 */
	private void death(Entity e)
	{
		if (e.equals(player))
		{
			deathCount--;
			deathAnim.reload();
			deathAnim.play();
			death = new Sound("sounds/death.wav", 0.6f);
			handleSound(death);
			idling = false;
			isMoving = false;
			if (!player.getSprite().getAnimation().equals(deathAnim))
				switchAnim(deathAnim, player.getSprite().getAnimation(), player.getSprite());

			e.getSprite().getAnimation().pauseAt(14);
			player.setIsDead(true);
		}
		else if(e.equals(enemies[0]))
		{
			if (!enemies[0].getSprite().getAnimation().equals(wolfDeathAnim))
				switchAnim(wolfDeathAnim, enemies[0].getSprite().getAnimation(), enemies[0].getSprite());

			e.getSprite().getAnimation().pauseAt(16);
			score += 50;
			wolf_death = new Sound("sounds/wolf_death.wav", 0.6f);
			handleSound(wolf_death);
			
			enemies[0].setIsDead(true);
			enemies[0].getSprite().stop();
		}
		for (int i = 1; i < enemyCount; i++)
			if (e.equals(enemies[i]))
			{
				if (!enemies[i].getSprite().getAnimation().equals(batDeathAnim))
					switchAnim(batDeathAnim, enemies[i].getSprite().getAnimation(), enemies[i].getSprite());
				
				if (enemies[i].getSprite().getAnimation().isAnimationFinished())
				{
					bat_death = new Sound("sounds/bat_death.wav", 2.0f);
					handleSound(bat_death);
					enemies[i].getSprite().getAnimation().pauseAt(6);

					enemies[i].setIsDead(true);
					score += 50;
					enemies[i].kill();
					break;
				}
			}
	}
	
	/**
	 * This method will check if the tile that the player is standing on can damage the entity.
	 * 
	 * @param tile	The Tile to check the state of.
	 * @param level	The level the tile is from.
	 * 
	 * @return {@code true} if the tile is murderous, {@code false} otherwise.
	 */
	private boolean isTileMurderous(Tile tile, TileMap level)
	{
		List<Character> tiles = level.getMurderousTiles();
		boolean isMurderous = false;
		
		if (tiles.contains(tile.getCharacter()))
		{
			isMurderous = true;
		}
		
		return isMurderous;
	}
	
	/**
	 * This method will handle the player touching a collectable tile.
	 * 
	 * @param tile The tile touching the player.
	 * @param level The TleMap the player is currently on.
	 * @param sprite The sprite interacting with the tiles.
	 */
	private void collectableCollected(Tile tile, TileMap level, Sprite sprite)
	{
		if (sprite.equals(player.getSprite()))
		{
			if (tile.getCharacter() == 'c' || tile.getCharacter() == 'd')
			{
				score = (tile.getCharacter() == 'c' ? score + 10 : score + 100);
		        coin_sound = new Sound("sounds/coin.wav", 0.6f);
				handleSound(coin_sound);
				tile.setCharacter('.');
			}
			else if (tile.getCharacter() == 'e')
			{
				tile.setCharacter('h');
				level.leverActivated(leversActivated);
				lever_sound = new Sound("sounds/lever.wav", 2.0f);
				handleSound(lever_sound);
				leversActivated++;
			}
		}
	}
	
	/**
	 * This method handles the switch between each level.
	 * 
	 * @param l The TileMap to load.
	 */
	private void swicthLevel(TileMap l)
	{
		level = l;
        
        if (level.getName().equals("level2"))
		{
			player.getSprite().setX(80);
			player.getSprite().setY(250);
			flag.setY(116);
			flag.setX(1963);
			axe.setX(-50);
			axe.setY(-50);
			setEnemyPosition();
		}
	}
	
	/**
	 * This method handles the switch between animations.
	 * 
	 * @param newAnim The animation to switch to.
	 * @param oldAnim The old animation.
	 * @param sprite The sprite to update the sprite on.
	 */
	private void switchAnim(Animation newAnim, Animation oldAnim, Sprite sprite)
	{
		if (oldAnim.equals(attackAnim))
		{
			sprite.setY(sprite.getY() + 20f);
			
			if (player.getMovingDirection() == -1)
			{
				sprite.setX(sprite.getX() + attackOffset);
			}
		}
		
		if (newAnim.equals(attackAnim))
		{
			if (player.getMovingDirection() == -1)
			{
				sprite.setX(sprite.getX() - attackOffset);
			}
		}
		
		if (newAnim.equals(idleAnim) && oldAnim.equals(walkingAnim))
		{
			sprite.setY(sprite.getY() + 3f);
		}
		
		if (newAnim.equals(batDeathAnim))
		{
			newAnim.start();
			newAnim.play();
		}
		
		sprite.setAnimation(newAnim);
	}

	/**
	 * Handler for the key pressed event.
	 */
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();

		// debugging controls
		if (_debug_)
		{
			if (keyCode == KeyEvent.VK_L)
			{
				System.out.println("-removed life-");
				deathCount -= 1;
			}

			if (keyCode == KeyEvent.VK_W)
			{
				System.out.println("-score set to 596 _ set state to WIN-");
				score = 596;
				state = GameState.WIN;
			}
		}
		
		if (keyCode == KeyEvent.VK_UP && (!player.isDead()))
		{
			jumping = true;
		}
		
		if ((keyCode == KeyEvent.VK_RIGHT) && (!isAttacking) && (!player.isDead()))
		{
			isMoving = true;
			idling = false;
			player.setMovingDirection(1);
		}
		
		if ((keyCode == KeyEvent.VK_LEFT) && (!isAttacking) && (!player.isDead()))
		{
			isMoving = true;
			idling = false;
			player.setMovingDirection(-1);
		}
		
		if ((keyCode == KeyEvent.VK_SPACE) && (!isAttacking) && !(jumping || isMoving) && hasWeapon && (!player.isDead()))
		{
			isAttacking = true;
			idling = false;
		}
	}
	
	/**
	 * Handler for the key released event.
	 */
	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		
		// The following command turns on and off the debugging controls.
		if (keyCode == KeyEvent.VK_NUMPAD0) {
			_debug_ = (_debug_ ? false : true); // check the state of debug, set to false if true and set to true if false;
			System.out.println(_debug_);
		}
		
		// debugging controls
		if (_debug_)
		{
			if (keyCode == KeyEvent.VK_D && (!player.isDead()))
			{
				System.out.println("-player damaged-");
				damageEntityLife(player, -1);
			}

			if (keyCode == KeyEvent.VK_S)
			{
				System.out.println("-switched to level2-");
				hasWeapon = true;
				swicthLevel(level2);
				state = GameState.PLAYING;
				initialiseGame();
			}
		}
		
		if (keyCode == KeyEvent.VK_RIGHT && (!player.isDead()))
		{
			isMoving = false;
			idling = true;
		}

		if (keyCode == KeyEvent.VK_LEFT && (!player.isDead()))
		{
			isMoving = false;
			idling = true;
		}

		if (keyCode == KeyEvent.VK_SPACE)
		{
			if (player.isDead())
			{
				player.setIsDead(false);
				resetLevel(level);
				initialiseGame();
			}

			if (cutScenePlaying)
			{
				cutScenePlaying = false;
			}
		}

		if (keyCode == KeyEvent.VK_ESCAPE)
		{
			if (state == GameState.PLAYING && !player.isDead() /*TODO remove?*/)
				state = GameState.PAUSE;
			else if (state == GameState.PAUSE)
				state = GameState.PLAYING;
		}
	}

	/**
	 * Handler for the mouse pressed event.
	 */
	public void mousePressed(MouseEvent e)
	{
		int mouseX = e.getX();
		int mouseY = e.getY();
		
		if (state == GameState.TITLE)
		{
			if ((mouseX >= title_play_x && mouseX < title_play_x + title_play.getWidth(null)) && (mouseY >= title_play_y && mouseY < title_play_y + title_play.getHeight(null)))
			{
				state = GameState.PLAYING;
			}
			else if ((mouseX >= title_quit_x && mouseX < title_quit_x + title_play.getWidth(null)) && (mouseY >= title_quit_y && mouseY < title_quit_y + title_play.getHeight(null)))
			{
				stop();
			}
			else if ((mouseX >= sound_img_x && mouseX < sound_img_x + sound_img.getWidth(null)) && (mouseY >= sound_img_y && mouseY < sound_img_y + sound_img.getHeight(null)))
			{
				if (muted == true)
				{
					muted = false;
					sound_img = sound_on;
				}
				else
				{
					muted = true;
					sound_img = sound_off;
				}
				
				handleSound(background_music);
			}
		}
		else if (state == GameState.PAUSE)
		{
			if ((mouseX >= pause_resume_x && mouseX < pause_resume_x + pause_resume.getWidth(null)) && (mouseY >= pause_resume_y && mouseY < pause_resume_y + pause_resume.getHeight(null)))
			{
				state = GameState.PLAYING;
			}
			else if ((mouseX >= title_quit_x && mouseX < title_quit_x + title_play.getWidth(null)) && (mouseY >= title_quit_y && mouseY < title_quit_y + title_play.getHeight(null)))
			{
				stop();
			}
			else if ((mouseX >= sound_img_x && mouseX < sound_img_x + 64) && (mouseY >= sound_img_y && mouseY < sound_img_y + 64))
			{
				if (muted == true)
				{
					muted = false;
					sound_img = sound_on;
				}
				else
				{
					muted = true;
					sound_img = sound_off;
				}
				
				handleSound(background_music);
			}
		}
		else if (state == GameState.LOST)
		{
			if ((mouseX >= (screenWidth/2 - (25 + yes.getWidth(null))) && (mouseX < (screenWidth/2 - 25))) && (mouseY >= 230 && mouseY < (230 + yes.getHeight(null))))
			{
				resetLevel(level1);
				initialiseGame();
				deathCount = 3;
				state = GameState.PLAYING;
			}
			else if ((mouseX < (screenWidth/2 + (25 + no.getWidth(null))) && (mouseX > (screenWidth/2 + 25))) && (mouseY >= 230 && mouseY < (230 + no.getHeight(null))))
			{
				System.exit(0);
			}
		}
	}

}


















