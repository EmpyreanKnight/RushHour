import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.io.File;

public class Speaker extends JButton implements ActionListener {
	private static final long serialVersionUID = 1L;

	private boolean playMusic;

    private Clip bgm;
    private Clip startMusic;
    private Clip endMusic;

    public Speaker() {
        try {
            File bgmFile = new File("./audio/BGM.wav");
            File startFile = new File("./audio/start.wav");
            File endFile = new File("./audio/end.wav");

            bgm = AudioSystem.getClip();
            startMusic = AudioSystem.getClip();
            endMusic = AudioSystem.getClip();

            bgm.open(AudioSystem.getAudioInputStream(bgmFile));
            startMusic.open(AudioSystem.getAudioInputStream(startFile));
            endMusic.open(AudioSystem.getAudioInputStream(endFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        addActionListener(this);
        open();
    }

	public void open() {
		setIcon(new ImageIcon("./img/buttons/speakerOn.png"));
		playMusic = true;
		bgm.loop(100);
		System.out.println("BGM ready");
	}

	public void start() {
		if (playMusic) {
			startMusic.start();
		}
	}

	public void win() {
		if (playMusic) {
			endMusic.start();
		}
	}

	public void switcher() {
		if (playMusic) {
			setIcon(new ImageIcon("./img/buttons/speakerOff.png"));
			playMusic = false;
			bgm.stop();
		} else {
			setIcon(new ImageIcon("./img/buttons/speakerOn.png"));
			playMusic = true;
			bgm.loop(100);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switcher();
	}
}
