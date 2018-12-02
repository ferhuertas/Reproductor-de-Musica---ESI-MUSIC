package dominio;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;


public class Reproductor2 extends BasicPlayer {

    private BasicPlayer player = new BasicPlayer();
    private String currentFileName;//
    private double currentVolumeValue;

   
    public Reproductor2(BasicPlayerListener listener) {
        player.addBasicPlayerListener(listener);
    }
    public void resume() {
    	try {
			player.resume();
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void play(String fileName) {

        try {

            if (currentFileName != null && currentFileName.equals(fileName) && player.getStatus() == BasicPlayer.PAUSED) {
                player.resume();
                return;
            }

            File mp3File = new File(fileName);

            currentFileName = fileName;
            player.open(mp3File);
            player.play();
            player.setGain(currentVolumeValue);

        } catch (BasicPlayerException ex) {
            Logger.getLogger(Reproductor2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        try {
            player.stop();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(Reproductor2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pause() {
        try {
            player.pause();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(Reproductor2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   public void setVolume(int currentValue, int maximumValue) {
        try {
            this.currentVolumeValue = currentValue;

            if (currentValue == 0) {
                player.setGain(0);
            } else {
                player.setGain(calcVolume(currentValue, maximumValue));
            }

        } catch (BasicPlayerException ex) {
            Logger.getLogger(Reproductor2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  private double calcVolume(int currentValue, int maximumValue) {
        currentVolumeValue = (double) currentValue / (double) maximumValue;
        return currentVolumeValue;
    }
}
