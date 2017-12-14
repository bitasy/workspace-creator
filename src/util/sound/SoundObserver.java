package util.sound;

/**
 * @author Parker Pearson A sound observer is notified when a playing sound has
 *         completed playing
 */
interface SoundObserver {
	/**
	 * @param finishedSound
	 *            Notify listener that a playing sound has completed
	 */
	public void soundFinished(Sound finishedSound);
}
