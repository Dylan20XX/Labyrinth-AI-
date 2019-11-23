import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// Plays the music sent to this class
public class MusicPlayer { // from YouTube by Max O'Didly

	// Variables for the music
	Clip clip;
	
	public MusicPlayer(){
		
	}

	// Methods that actually create the music
	public void loopMusic(String musicLocation) {

		try {

			// Make the method and pass the variables to the method
			File Sound = new File(musicLocation);
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(Sound);
			clip = AudioSystem.getClip();
			clip.open(audioInput);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();// start the music

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public void playMusic(String musicLocation2) {

		try {

			// Make the method and pass the variables to the method
			File Sound2 = new File(musicLocation2);
			AudioInputStream audioInput2 = AudioSystem.getAudioInputStream(Sound2);
			clip = AudioSystem.getClip();
			clip.open(audioInput2);
			clip.start();// start the music

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// Method to stop the music
	public void StopMusic() {
		clip.stop();
	}

}
