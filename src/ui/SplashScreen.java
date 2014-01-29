package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * Loading animation to be displayed 
 * while data is downloaded and parsed.
 * @author Chris Allard
 *
 */
public class SplashScreen extends JWindow {
	private static final long serialVersionUID = 1L;
	private int duration;
	private String image1 = "/images/loading.gif";

	/**
	 * Constructor method.
	 * @param d duration in milliseconds.
	 */
	public SplashScreen(int d) {
		duration = d;
	}

	/**
	 * Builds and displays splash screen.
	 */
	public void showSplash() {
		setBackground(Color.white);

		JLabel label = new JLabel(createImageIcon(image1));
		add(label, BorderLayout.CENTER);
		
		pack();
		centerScreen();
		
		setVisible(true);
	}

	/**
	 * Center splash screen in middle of screen.
	 */
	private void centerScreen() {
		int width = this.getWidth();
		int height = this.getHeight();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);
	}

	/**
	 * Shows splash screen for desired duration, then hides it.
	 */
	public void showSplashAndHide() {
		showSplash();
		try {
			Thread.sleep(duration);
		} catch (Exception e) {
		}

		setVisible(false);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = SplashScreen.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}