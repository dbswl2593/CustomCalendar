import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class AlphaPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2009216178400208555L;

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

}
