import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class CustomCalendar extends JFrame implements ActionListener{
	/**
	 * 
	 */
	Toolkit tools;
	Dimension screenSize;
	Rectangle frameSize;
	final double heightMultiplier = 1.45;
	final double sizeMultiplier = 0.6;
	final double sideTabWidthMultiplier = 0.1;
	int alpha = 170;
	boolean resizeFlag = false;
	JMenuItem exit;
	private static final long serialVersionUID = 411381519542802112L;
	public CustomCalendar() {
		super();
		tools = Toolkit.getDefaultToolkit();
		screenSize = tools.getScreenSize();
		frameSize = new Rectangle(100, 100, (int) (screenSize.getHeight() * sizeMultiplier / heightMultiplier), (int) (screenSize.getHeight() * sizeMultiplier));
		setBounds(frameSize);
		MotionPanel wrapper = new MotionPanel(this);
		FlowLayout wrapperLayout = new FlowLayout();
		wrapperLayout.setAlignment(FlowLayout.LEFT);
		wrapper.setLayout(null);
		
		setSideMedu(wrapper);
		updateAlpha(wrapper);
		//wrapper.setBorder(BorderFactory.createLineBorder(Color.black));
		add(wrapper);
		setRightClickMenu(wrapper);
		setProperties(this);
	}
	
	private void setRightClickMenu(JPanel dest) {
		JPopupMenu rightClickMenu = new JPopupMenu();
		exit = new JMenuItem("Á¾·á");
		exit.addActionListener(this);
		rightClickMenu.add(exit);
		dest.add(rightClickMenu);
		dest.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			    if(e.getModifiers() == MouseEvent.BUTTON3_MASK) {
			    	rightClickMenu.show(dest, e.getX(), e.getY());
			    }
			}

		});
	}
	
	private void setSideMedu(JPanel dest) {
		JPanel sideTab = new JPanel(new GridBagLayout());
		sideTab.setBackground(new Color(255,255,255,0));
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
			sideButton[i].setOpaque(false);
			sideButton[i].setContentAreaFilled(false);
			sideButton[i].setBorderPainted(false);
		}
		dest.add(sideTab);
	}
	
	private void setProperties(JFrame dest) {
		setTitle("CustomCalendar");
		dest.setUndecorated(true);
		dest.setResizable(false);
		dest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dest.setBackground(new Color(255,255,255,0));
		dest.setVisible(true);
	}
	
	private void updateAlpha(JPanel dest) {
		dest.setBackground(new Color(255,255,255,alpha));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(exit))this.dispose();
	}
}
