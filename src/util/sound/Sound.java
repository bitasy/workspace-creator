package util.sound;

/**
 * @author Parker Pearson A sound simply holds the file path to sound file. A
 *         seperate sound class was created to encourage proper tracking of
 *         sound objects when sounds are removed
 */
public class Sound {
	private String soundPath;

	/**
	 * @param soundFilePath
	 *            file path to the sound file
	 */
	public Sound(String soundFilePath) {
		soundPath = soundFilePath;
	}

	/**
	 * @return soundPath
	 */
	public String getSoundPath() {
		return soundPath;
	}

}
