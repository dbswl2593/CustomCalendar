import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

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
	int alpha = 200;
	boolean resizeFlag = false;
	JMenuItem exit;
	
	ClockPanel clock;
	CalendarPanel calendar;
	TimeTablePanel timetable;
	
	private static final long serialVersionUID = 411381519542802112L;
	public CustomCalendar() {
		super();
		tools = Toolkit.getDefaultToolkit();
		screenSize = tools.getScreenSize();
		frameSize = new Rectangle(100, 100, (int) (screenSize.getHeight() * sizeMultiplier / heightMultiplier), (int) (screenSize.getHeight() * sizeMultiplier));
		setBounds(frameSize);
		setProperties();
		MotionPanel wrapper = new MotionPanel(this);
		wrapper.setLayout(null);
		
		setSideMedu(wrapper);
		updateAlpha(wrapper);
		setClockPanel(wrapper);
		setCalendarPanel(wrapper);
		setTimeTablePanel(wrapper);
		calendar.attachTimeTable(timetable);
		add(wrapper);
		setRightClickMenu(wrapper);
		setOnClock();
	}
	
	private void setOnClock() {
		clock.start();
		clock.setVisible(true);
		calendar.setVisible(false);
		timetable.setVisible(false);
	}
	
	private void setOnCalendar() {
		clock.inturrupt();
		clock.setVisible(false);
		calendar.setVisible(true);
		timetable.setVisible(false);
		calendar.callUpdate();
	}
	
	private void setOnTimeTable() {
		clock.inturrupt();
		clock.setVisible(false);
		calendar.setVisible(false);
		timetable.setVisible(true);
	}
	
	private void setClockPanel(JPanel dest) {
		clock = new ClockPanel();
		clock.setBackground(new Color(255, 255, 255, 0));
		clock.setBounds((int) (frameSize.height*sideTabWidthMultiplier), 0, (int) (getWidth() - frameSize.height*sideTabWidthMultiplier), getHeight());
		dest.add(clock);
	}
	
	private void setCalendarPanel(JPanel dest) {
		calendar = new CalendarPanel();
		calendar.setBackground(new Color(255, 255, 255, 0));
		calendar.setBounds((int) (frameSize.height*sideTabWidthMultiplier), 0, (int) (getWidth() - frameSize.height*sideTabWidthMultiplier), getHeight());
		dest.add(calendar);
	}
	
	private void setTimeTablePanel(JPanel dest) {
		timetable = new TimeTablePanel();
		timetable.setBackground(new Color(255, 255, 255, 0));
		timetable.setBounds((int) (frameSize.height*sideTabWidthMultiplier), 0, (int) (getWidth() - frameSize.height*sideTabWidthMultiplier), getHeight());
		dest.add(timetable);
	}
	
	private void setRightClickMenu(JPanel dest) {
		JPopupMenu rightClickMenu = new JPopupMenu();
		exit = new JMenuItem(" Á¾·á");
		exit.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 18));
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
		JButton[] sideButton = new JButton[3];
		GridBagConstraints[] sideConstraints = new GridBagConstraints[3];
		sideTab.setBounds(0, (int)(frameSize.height /2 - frameSize.height*sideTabWidthMultiplier*2), (int)(frameSize.height*sideTabWidthMultiplier), (int)(frameSize.height*sideTabWidthMultiplier)*3);
		for(int i=0; i<3; i++) {
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
			sideButton[i].setBorderPainted(true);
			sideButton[i].setHorizontalAlignment(SwingConstants.CENTER);
		}
		sideButton[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!clock.isVisible())setOnClock();
			}
		});
		sideButton[0].setIcon(new ImageIcon(getScaledImage((new ImageIcon("./clock.png")).getImage(), (int)(frameSize.height*sideTabWidthMultiplier), (int)(frameSize.height*sideTabWidthMultiplier))));
		sideButton[1].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!calendar.isVisible())setOnCalendar();
			}
		});
		sideButton[1].setIcon(new ImageIcon(getScaledImage((new ImageIcon("./planner.png")).getImage(), (int)(frameSize.height*sideTabWidthMultiplier), (int)(frameSize.height*sideTabWidthMultiplier))));
		sideButton[2].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!timetable.isVisible())setOnTimeTable();
			}
		});
		sideButton[2].setIcon(new ImageIcon(getScaledImage((new ImageIcon("./timetable.png")).getImage(), (int)(frameSize.height*sideTabWidthMultiplier), (int)(frameSize.height*sideTabWidthMultiplier))));
		dest.add(sideTab);
	}
	
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	private void setProperties() {
		setTitle("CustomCalendar");
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(new Color(255,255,255,alpha));
		setVisible(true);
	}
	
	private void updateAlpha(JPanel dest) {
		dest.setBackground(new Color(255,255,255,alpha));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(exit)) {
			clock.inturrupt();
			this.dispose();
		}
	}
}
