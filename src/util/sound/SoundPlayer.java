package util.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Parker Pearson Each SoundPlayer controls the playing of a single
 *         sound. SoundPlayer notifies a Sound Observer on the completion of the
 *         sound
 * 
 *         Inspired by a stack overflow example found at
 *         https://stackoverflow.com/questions/30495096/simple-java-how-to-create-a-mute-and-unmute-button-in-the-background
 */
class SoundPlayer extends Thread {

	private File soundFile;

	private Clip clip;
	private boolean shouldLoop;
	private boolean isMute;

	private int loopTimes = Clip.LOOP_CONTINUOUSLY; // default loop clips to loop continously
	private int lastFrame;
	private SoundObserver myListener;
	private Sound mySound;

	/**
	 * @param sound
	 * @param soundsPlaying
	 * @param loop
	 * @param mute
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public SoundPlayer(Sound sound, boolean loop, boolean mute, SoundObserver listener)
			throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		String filePath = sound.getSoundPath();
		soundFile = new File(filePath);
		mySound = sound;
		shouldLoop = loop;
		isMute = mute;
		myListener = listener;
		loadFile();
	}

	// private method called by the constructor to the load the sound file
	private void loadFile() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		AudioInputStream audioStream;

		audioStream = AudioSystem.getAudioInputStream(soundFile);
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		clip = (Clip) AudioSystem.getLine(info);
		clip.open(audioStream);
		clip.addLineListener(e -> {
			if (e.getType() == LineEvent.Type.STOP && clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
				myListener.soundFinished(mySound);
			}
		});

		if (isMute) {
			muteClip();
		}
	}

	// sets the clips mute control to match the set value
	private void muteClip() {
		BooleanControl muteController = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
		if (muteController != null) {
			muteController.setValue(isMute);
		}
	}

	/**
	 * If a clip is not already playing the clip will continue to play at the frame
	 * it was last paused at
	 */
	public void resumePlaying() {
		if (clip != null && !clip.isRunning()) {
			// Make sure we haven't passed the end of the file...
			if (lastFrame < clip.getFrameLength()) {
				clip.setFramePosition(lastFrame);
			} else {
				clip.setFramePosition(0);
			}
			if (shouldLoop) {
				clip.loop(loopTimes);
			} else {
				clip.start();

			}

		}
	}

	/*
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		resumePlaying();
	}

	/**
	 * Pauses the currently playing clip
	 */
	public void pause() {
		if (clip != null && clip.isRunning()) {
			lastFrame = clip.getFramePosition();
			clip.stop();
		}

	}

	/**
	 * Stops a playing clip, flushes all queued sounds for the clip, removes clip
	 * from map of currently playing sounds
	 */
	public void end() {
		clip.stop();
		clip.flush();
	}

	/**
	 * Sets whether the queue should be muted
	 * 
	 * @param mute
	 */
	public void setIsMuted(boolean mute) {
		isMute = mute;
		muteClip();
	}

	/**
	 * Sets the amount of times a clip should loop, default value for loop count is
	 * to loop non-stop
	 * 
	 * @param timesToLoop
	 */
	public void setLoopCount(int timesToLoop) {
		loopTimes = timesToLoop;
	}
}
