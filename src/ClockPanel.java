import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class ClockPanel extends AlphaPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2143849358452364853L;
	private SimpleDateFormat sdf;
	private JLabel clk;
	private Thread t;
	private boolean interrupt;
	
	ClockPanel(){
		sdf = new SimpleDateFormat("HH:mm:ss");
		setLayout(null);
		clk = new JLabel();
		
		clk.setBackground(new Color(255,255,255,0));
		clk.setAlignmentX(Component.CENTER_ALIGNMENT);
		clk.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 70));
		add(clk);
		clk.setVisible(true);
		start();
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
		interrupt = false;
	}
	protected void start(){
		t = new Thread() {
			public void run() {
				interrupt = true;
				try {
					while(interrupt) {
						update();
						Thread.sleep(500);
					}
				}
				catch(InterruptedException e) {}
			}
		};
		t.start();
	}
}
