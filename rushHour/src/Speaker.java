import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Speaker extends JButton implements ActionListener {
	private static final long serialVersionUID = 1L;

	private boolean playMusic;
	private AudioClip bgm;
	private AudioClip startMusic;
	private AudioClip endMusic;

	public Speaker() {
		URL bgmUrl = null;
		URL startUrl = null;
		URL endUrl = null;
		File bgmfile = new File("img/BGM.wav");
		File stratFile = new File("img/start.wav");
		File endFile = new File("img/end.wav");
		try {
			bgmUrl = bgmfile.toURI().toURL();
			startUrl = stratFile.toURI().toURL();
			endUrl = endFile.toURI().toURL();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		bgm = Applet.newAudioClip(bgmUrl);
		startMusic = Applet.newAudioClip(startUrl);
		endMusic = Applet.newAudioClip(endUrl);

		addActionListener(this);
		open();
	}

	public void open() {
		setIcon(new ImageIcon("img/speakerOn.png"));
		playMusic = true;
		bgm.play();
	}

	public void start() {
		if (playMusic) {
			startMusic.play();
		}
	}

	public void win() {
		if (playMusic) {
			endMusic.play();
		}
	}

	public void switcher() {
		if (playMusic) {
			setIcon(new ImageIcon("img/speakerOff.png"));
			playMusic = false;
			bgm.stop();
		} else {
			setIcon(new ImageIcon("img/speakerOn.png"));
			playMusic = true;
			bgm.play();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("111");
		switcher();
	}
}
