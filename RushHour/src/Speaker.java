import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class Speaker extends JButton implements ActionListener {
	private boolean playMusic; // status: voice on/off
    private ImageIcon voiceOffIcon;
    private ImageIcon voiceOnIcon;
    private Clip bgm;
    private Clip startMusic;
    private Clip endMusic;

    Speaker() {
        try {
            File bgmFile = new File("./audio/BGM.wav");
            File startFile = new File("./audio/start.wav");
            File endFile = new File("./audio/end.wav");

            voiceOffIcon = new ImageIcon("./img/buttons/speakerOff.png");
            voiceOnIcon = new ImageIcon("./img/buttons/speakerOn.png");

            bgm = AudioSystem.getClip();
            startMusic = AudioSystem.getClip();
            endMusic = AudioSystem.getClip();

            bgm.open(AudioSystem.getAudioInputStream(bgmFile));
            startMusic.open(AudioSystem.getAudioInputStream(startFile));
            endMusic.open(AudioSystem.getAudioInputStream(endFile));
        } catch (Exception e) {
            // show error info and exit when failed to load music
            JOptionPane.showMessageDialog(getParent(),
                    "Failed to load voice resource!");
            System.exit(-1);
        }

        addActionListener(this);
        setIcon(new ImageIcon("./img/buttons/speakerOn.png"));
        playMusic = true;
        bgm.loop(10000);
    }

    // play countdown music (when voice on)
	void startMusic() {
		if (playMusic) {
			startMusic.start();
		}
	}

    // play winning music (when voice on)
	void winningMusic() {
		if (playMusic) {
			endMusic.start();
		}
	}

	@Override
	// when button clicked, switch voice on/off
	public void actionPerformed(ActionEvent e) {
		if (playMusic) {
			setIcon(voiceOffIcon);
			playMusic = false;
			bgm.stop();
		} else {
			setIcon(voiceOnIcon);
			playMusic = true;
			bgm.loop(10000);
		}
	}
}
