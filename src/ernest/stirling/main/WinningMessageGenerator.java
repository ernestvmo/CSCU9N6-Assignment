package ernest.stirling.main;

import java.awt.Graphics2D;
import java.awt.Image;

public class WinningMessageGenerator
{
	/** This method converts a int score into a string.
	 * 
	 * @param score The score to convert to a string.
	 * 
	 * @return The converted score.
	 */
	private static String convertScore(int score)
	{
		String scoreString = "";
		
		if (score < 10)
		{
			scoreString = "000" + score;
		}
		else if (score < 100)
		{
			scoreString = "00" + score;
		}
		else if (score < 1000)
		{
			scoreString = "0" + score;
		}
		else 
		{
			scoreString = "" + score;
		}
		
		return scoreString;
	}
	
	/**
	 * Generate the message using the converted score.
	 * @see WinningMessageGenerator#convertScore(int)
	 * 
	 * @param score The string converted score.
	 * @param graphics The Graphics2D object used to draw the score.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param imgs The array of images to use to draw the score.
	 */
	public static void generateMessage(int score, Graphics2D graphics, int x, int y, Image[] imgs)
	{
		String scoreS = convertScore(score);
		
		graphics.drawImage(imgs[0], x,y, null);
		
		for (int i = 0; i < scoreS.length(); i++)
		{
			switch (scoreS.charAt(i))
			{
				case '0' : 
					graphics.drawImage(imgs[0], x, y, null);
					break;
				case '1' : 
					graphics.drawImage(imgs[1], x, y, null);
					break;
				case '2' : 
					graphics.drawImage(imgs[2], x, y, null);
					break;
				case '3' : 
					graphics.drawImage(imgs[3], x, y, null);
					break;
				case '4' : 
					graphics.drawImage(imgs[4], x, y, null);
					break;
				case '5' : 
					graphics.drawImage(imgs[5], x, y, null);
					break;
				case '6' : 
					graphics.drawImage(imgs[6], x, y, null);
					break;
				case '7' : 
					graphics.drawImage(imgs[7], x, y, null);
					break;
				case '8' : 
					graphics.drawImage(imgs[8], x, y, null);
					break;
				case '9' : 
					graphics.drawImage(imgs[9], x, y, null);
					break;
			}
			
			x += 25;
		}
			
	}
}
