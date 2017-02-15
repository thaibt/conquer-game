import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.border.Border;


public class StatusDisp  extends JPanel{
	//public static JTextArea out;
	public static JLabel lava;
	public static JLabel snow;
	public static JLabel lavaresources;
	public static JLabel snowresources;
	public static JLabel currentStatus;
	public static JLabel currentPlayer;
	public static JLabel console;
	public static JButton forfeit;
	//public static MessageConsole out;
	StatusDisp() throws IOException{
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setPreferredSize(new Dimension(200, 1000));
		JPanel blueSide = new JPanel();
		JPanel redSide = new JPanel();
		JPanel extra = new JPanel();

        extra.setPreferredSize(new Dimension(200, 300));
		
		BufferedImage red = ImageIO.read(getClass().getResource("art/lavabase.png"));

		JLabel rLabel = new JLabel(new ImageIcon(red));
		rLabel.setAlignmentX(CENTER_ALIGNMENT);
		lava = new JLabel();
		lava.setText("Lava Base");
		lava.setFont(new Font("Garamond", Font.PLAIN, 20));
		lava.setAlignmentX(CENTER_ALIGNMENT);
		
		BufferedImage blue = ImageIO.read(getClass().getResource("art/snowbase.png"));
		JLabel bLabel = new JLabel(new ImageIcon(blue));
		bLabel.setAlignmentX(CENTER_ALIGNMENT);
		snow = new JLabel();
		snow.setText("Snow Base");
		snow.setFont(new Font("Garamond", Font.PLAIN, 20));
		snow.setAlignmentX(CENTER_ALIGNMENT);
		
		lavaresources = new JLabel();
		lavaresources.setAlignmentX(CENTER_ALIGNMENT);
		lavaresources.setFont(new Font("Garamond", Font.PLAIN, 12));
		snowresources = new JLabel();
		snowresources.setAlignmentX(CENTER_ALIGNMENT);
		snowresources.setFont(new Font("Garamond", Font.PLAIN, 12));
		
		currentStatus = new JLabel();
		currentStatus.setAlignmentX(CENTER_ALIGNMENT);
		currentPlayer = new JLabel();
		currentPlayer.setAlignmentX(CENTER_ALIGNMENT);
		currentPlayer.setFont(new Font("Garamond", Font.BOLD, 15));
		
		console = new JLabel();
		console.setAlignmentX(CENTER_ALIGNMENT);
		


		
		blueSide.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		redSide.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		extra.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
		
		redSide.add(rLabel);
		redSide.add(lava);
		redSide.add(lavaresources);
		blueSide.add(bLabel);
		blueSide.add(snow);
		blueSide.add(snowresources);
		extra.add(currentPlayer);
		extra.add(currentStatus);
		extra.add(console);
		
		add(redSide);
		add(blueSide);
		add(extra);

		
	}
}
