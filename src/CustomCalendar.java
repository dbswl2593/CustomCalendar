import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CustomCalendar extends JFrame{
	/**
	 * 
	 */
	Toolkit tools;
	Dimension screenSize;
	Rectangle frameSize;
	final double heightMultiplier = 1.45;
	final double sizeMultiplier = 0.6;
	final double sideTabWidthMultiplier = 0.1;
	boolean resizeFlag = false;
	private static final long serialVersionUID = 411381519542802112L;
	public CustomCalendar() {
		super();
		tools = Toolkit.getDefaultToolkit();
		screenSize = tools.getScreenSize();
		frameSize = new Rectangle(100, 100, (int) (screenSize.getHeight() * sizeMultiplier / heightMultiplier), (int) (screenSize.getHeight() * sizeMultiplier));
		setBounds(frameSize);
		setTitle("CustomCalendar");
		MotionPanel wrapper = new MotionPanel(this);
		FlowLayout wrapperLayout = new FlowLayout();
		wrapperLayout.setAlignment(FlowLayout.LEFT);
		wrapper.setLayout(null);
		JPanel sideTab = new JPanel(new GridBagLayout());
		JPanel right = new JPanel();
		JButton[] sideButton = new JButton[4];
		GridBagConstraints[] sideConstraints = new GridBagConstraints[4];
		sideTab.setBounds(0, (int)(frameSize.height /2 - frameSize.height*sideTabWidthMultiplier*2), (int)(frameSize.height*sideTabWidthMultiplier), (int)(frameSize.height*sideTabWidthMultiplier)*4);
		for(int i=0; i<4; i++) {
			sideButton[i] = new JButton(Integer.toString(i));
			sideConstraints[i] = new GridBagConstraints();
			sideConstraints[i].gridx = 0;
			sideConstraints[i].gridy = i;
			sideConstraints[i].fill = GridBagConstraints.BOTH;
			sideConstraints[i].weightx = 1;
			sideConstraints[i].weighty = 1;
			sideTab.add(sideButton[i], sideConstraints[i]);
		}
		wrapper.add(sideTab);
		wrapper.add(right);
		add(wrapper);
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wrapper.setBackground(new Color(255,255,255,150));
		wrapper.setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(new Color(255,255,255,150));
		setVisible(true);
	}

}
