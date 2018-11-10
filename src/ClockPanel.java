import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class ClockPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2143849358452364853L;
	SimpleDateFormat sdf;
	JLabel clk;
	Thread t;
	int alpha = 170;
	
	ClockPanel(){
		sdf = new SimpleDateFormat("HH:mm:ss");
		setLayout(null);
		clk = new JLabel();
		
		clk.setBackground(new Color(255,255,255,0));
		clk.setAlignmentX(Component.CENTER_ALIGNMENT);
		clk.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 70));
		add(clk);
		clk.setVisible(true);
		t = new Thread() {
			public void run() {
				try {
					while(!isInterrupted()) {
						update();
						Thread.sleep(100);
					}
					
				}
				catch(InterruptedException e) {}
			}
		};
		t.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setComposite(AlphaComposite.Clear);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setPaint(getParent().getBackground());
		g2d.setComposite(AlphaComposite.SrcOver);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
	}
	private void update() {
		String txt = sdf.format(Calendar.getInstance().getTime());
		clk.setText(txt);
		FontMetrics fm = clk.getFontMetrics(clk.getFont());
		int clkWidth = fm.stringWidth(txt);
		clk.setBounds(this.getSize().width/2 - clkWidth/2, (int)( getHeight()/2 - clk.getBounds().height), clkWidth, fm.getHeight());
		repaint();
	}
	protected void inturrupt() {
		t.interrupt();
	}
	protected void start(){
		if(!t.isAlive())t.start();
	}
}
