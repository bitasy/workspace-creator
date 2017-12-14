package util.sound;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Parker Pearson A Sound Handler provides a multitude of methods to
 *         call on a group of playing sounds
 */
public interface SoundHandler {

	/**
	 * Resume playing sounds
	 * 
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	void resume() throws LineUnavailableException, IOException, UnsupportedAudioFileException;

	/**
	 * Pauses all current sounds
	 */
	void pause();

	/**
	 * Toggles whether playing sounds are muted
	 */
	void toggleMute();

	/**
	 * @param soundToRemove
	 */
	void remove(Sound soundToRemove);

	/**
	 * Removes all currently playing sounds
	 */
	void removeAllSounds();

	/**
	 * Plays a sound on repeat until it is removed or the program ends
	 * 
	 * @param soundToPlay
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 * 
	 */
	void loopSound(Sound soundToPlay) throws LineUnavailableException, IOException, UnsupportedAudioFileException;

	/**
	 * Loops through a sound the specified amount of times
	 * 
	 * @param soundToRepeat
	 * @param timesToRepeat
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	void loopSound(Sound soundToRepeat, int timesToRepeat)
			throws LineUnavailableException, IOException, UnsupportedAudioFileException;

	/**
	 * @param soundToPlay
	 *            Plays a sound once
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	void play(Sound soundToPlay) throws LineUnavailableException, IOException, UnsupportedAudioFileException;

}
