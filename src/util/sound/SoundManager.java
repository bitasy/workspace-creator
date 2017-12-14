package util.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Parker Pearson SoundManager Keeps track of running sounds and allows
 *         muting as well as playing and pausing
 */
public class SoundManager implements SoundHandler, SoundObserver {
	private Map<Sound, SoundPlayer> soundsPlaying;
	private List<Sound> queuedSounds;
	private boolean isMuted;
	private boolean isRunning;

	/**
	 * @param shouldStartRunning
	 *            Set false to start the manager paused
	 */
	public SoundManager(boolean startPlaying) {
		soundsPlaying = new HashMap<Sound, SoundPlayer>();
		queuedSounds = new ArrayList<Sound>();
		isRunning = startPlaying;
	}

	@Override
	public void resume() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		isRunning = true;
		for (SoundPlayer player : soundsPlaying.values()) {
			player.resumePlaying();
		}
		playQueuedSounds();
	}

	// Plays any sound that was queued while the sound engine was paused
	private void playQueuedSounds() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		Iterator<Sound> it = queuedSounds.iterator();
		while (it.hasNext()) {
			Sound qedSound = it.next();
			play(qedSound);
			it.remove();
		}
	}

	@Override
	public void pause() {
		isRunning = false;
		for (SoundPlayer player : soundsPlaying.values()) {
			player.pause();
		}
	}

	@Override
	public void toggleMute() {
		isMuted = !isMuted;
		for (SoundPlayer player : soundsPlaying.values()) {
			player.setIsMuted(isMuted);
		}
	}

	@Override
	public void play(Sound soundToPlay) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		// Don't play sounds that are already playing
		if (!soundsPlaying.containsKey(soundToPlay)) {
			if (isRunning) {
				SoundPlayer soundPlayer = new SoundPlayer(soundToPlay, false, isMuted, this);
				soundPlayer.start();
				soundsPlaying.put(soundToPlay, soundPlayer);
			} else {
				queuedSounds.add(soundToPlay);
			}

		}
	}

	@Override
	public void remove(Sound soundToRemove) {
		SoundPlayer playerToRemove = soundsPlaying.get(soundToRemove);
		playerToRemove.end();
		if (soundsPlaying.containsKey(soundToRemove)) {
			soundsPlaying.remove(soundToRemove);
		}

	}

	@Override
	public void removeAllSounds() {
		Iterator<Map.Entry<Sound, SoundPlayer>> iter = soundsPlaying.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Sound, SoundPlayer> entry = iter.next();
			entry.getValue().end();
			iter.remove();
		}
	}

	@Override
	public void loopSound(Sound soundToPlay)
			throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		loopSound(soundToPlay, Clip.LOOP_CONTINUOUSLY);
	}

	@Override
	public void loopSound(Sound soundToRepeat, int timesToRepeat)
			throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		SoundPlayer soundPlayer = new SoundPlayer(soundToRepeat, true, isMuted, this);
		soundPlayer.setLoopCount(timesToRepeat);
		soundPlayer.start();
		soundsPlaying.put(soundToRepeat, soundPlayer);
	}

	@Override
	public void soundFinished(Sound finishedSound) {
		if (soundsPlaying.containsKey(finishedSound)) {
			soundsPlaying.remove(finishedSound);
		}
	}

}
