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
	private Point cp;
    public MotionPanel(final JFrame parent){
    	this.setSize(parent.getWidth(), parent.getHeight());
    	addMouseListener(new MouseAdapter() {
    		public void mousePressed(MouseEvent e) {
    			cp = e.getPoint();
    			getComponentAt(cp);
    		}
    	});

    	addMouseMotionListener(new MouseMotionAdapter() {
    		@Override
    		public void mouseDragged(MouseEvent e) {
    			int thisX = parent.getLocation().x;
    			int thisY = parent.getLocation().y;
    			int xMoved = e.getX() - cp.x;
    			int yMoved = e.getY() - cp.y;
    			int X = thisX + xMoved;
    			int Y = thisY + yMoved;
    			parent.setLocation(X, Y);
    		}
    	});	
    }
}
