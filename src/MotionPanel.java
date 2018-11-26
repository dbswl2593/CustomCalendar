import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

public class MotionPanel extends AlphaPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3498749399358048269L;
	private Point initialClick;
    public MotionPanel(final JFrame parent){
    	this.setSize(parent.getWidth(), parent.getHeight());
    	addMouseListener(new MouseAdapter() {
    		public void mousePressed(MouseEvent e) {
    			initialClick = e.getPoint();
    			getComponentAt(initialClick);
    		}
    	});

    	addMouseMotionListener(new MouseMotionAdapter() {
    		@Override
    		public void mouseDragged(MouseEvent e) {
    			int thisX = parent.getLocation().x;
    			int thisY = parent.getLocation().y;
    			int xMoved = e.getX() - initialClick.x;
    			int yMoved = e.getY() - initialClick.y;
    			int X = thisX + xMoved;
    			int Y = thisY + yMoved;
    			parent.setLocation(X, Y);
    		}
    	});	
    }
    @Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setComposite(AlphaComposite.Clear);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setPaint(getBackground());
		g2d.setComposite(AlphaComposite.SrcOver);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
	}
}
