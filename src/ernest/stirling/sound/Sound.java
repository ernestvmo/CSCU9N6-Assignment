package ernest.stirling.sound;

import java.io.*;
import javax.sound.sampled.*;

public class Sound extends Thread
{

	String filename; // The name of the file to play
	boolean finished; // A flag showing that the thread has finished
	boolean loops;
	float volume;

	private Clip clip;
	long pausedAt;

	/**
	 * Constructor method for the Sound object.
	 * 
	 * @param fname The name of the file associated to the Sound object.
	 */
	public Sound(String fname)
	{
		filename = fname;
	}
	
	/**
	 * Constructor method for the Sound object.
	 * 
	 * @param fname The name of the file associated to the Sound object.
	 * @param volume The volume of the Sound object.
	 */
	public Sound(String fname, float volume)
	{
		filename = fname;
		this.volume = volume;
	}

	/**
	 * run will play the actual sound but you should not call it directly. You
	 * need to call the 'start' method of your sound object (inherited from
	 * Thread, you do not need to declare your own). 'run' will eventually be
	 * called by 'start' when it has been scheduled by the process scheduler.
	 */
	public void run()
	{
		try
		{
			File file = new File(filename);
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);

			if (filename.endsWith(".mid"))
			{
				clip.open(stream);
				clip.start();

				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			else
			{
				FadeFilterStream filtered = new FadeFilterStream(stream);
				AudioInputStream f = new AudioInputStream(filtered, format,
						stream.getFrameLength());
				filtered.setVolume(volume);
				clip.open(f);
				clip.start();
			}
		}
		catch (Exception e) { } 
		finally
		{
			finished = true;
		}
	}

	/**
	 * This method is used to resume the audio clip when it was last stopped.
	 */
	public void resumeSound() 
	{
		clip.setMicrosecondPosition(pausedAt);
		clip.start();
	}

	/**
	 * A method that will record the microsecond in the clip and stop the clip.
	 */
	public void pauseSound() 
	{
		if (clip.isRunning())
		{
			pausedAt = clip.getMicrosecondPosition();
			clip.stop();
		}
	}
}
