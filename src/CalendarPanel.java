import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;




public class CalendarPanel extends JPanel implements ActionListener, MouseWheelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3032615500418430799L;
	int year, month;
	Calendar cal;
	JsonObject data;
	Schedules[] mschedules = new Schedules[32];
	JButton arrup = new JButton("¡â");
	JButton arrdown = new JButton("¡ä");
	String[] dayofweek = {"", "ÀÏ", "¿ù", "È­", "¼ö", "¸ñ", "±Ý", "Åä"};
	ArrayList<JLabel> weekhead = new ArrayList<JLabel>();
	CalendarPanel(){
		setLayout(null);
		
		cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		cal.set(year, month, 1);
		data = readDB("./caldata.json");
		
		arrup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		update(cal.get(Calendar.DAY_OF_WEEK));
		
		
		
		
		
	}
	
	
	
	JsonObject readDB(String path) {
		JsonParser parser = new JsonParser();
		JsonObject dat;
		try {
			dat = parser.parse(new FileReader(path)).getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			dat = new JsonObject();
		}
		return dat;
	}
	
	void update(int start) {
		removeAll();
		JPanel header = new JPanel(new BorderLayout());
		JLabel head = new JLabel(cal.get(Calendar.YEAR) + "³â " + cal.getActualMaximum(Calendar.MONTH) + "¿ù");
		head.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 40));
		head.setVisible(true);
		header.add(head, BorderLayout.WEST);
		JPanel updown = new JPanel();
		updown.add(arrup);
		updown.add(arrdown);
		header.add(updown, BorderLayout.EAST);
		JPanel calendar = new JPanel(new GridLayout(7, 7, 0, 0));
		for(int i=1; i<8; i++) {
			weekhead.add(new JLabel(dayofweek[i]));
			weekhead.get(i-1).setBackground(new Color(255,255,255,0));
			weekhead.get(i-1).setAlignmentX(Component.CENTER_ALIGNMENT);
			weekhead.get(i-1).setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 40));
			calendar.add(weekhead.get(i-1));
		}
		for(int i=1; i<start; i++) {
			calendar.add(new JLabel("x"));
		}
		try {
			JsonElement monthobj = data.get(Integer.toString(year)).getAsJsonObject().get(Integer.toString(month));
			for(int i=1; i<32; i++) {
				if(!monthobj.isJsonNull()) {
					JsonArray dayarr = monthobj.getAsJsonObject().get(Integer.toString(i)).getAsJsonArray();
					if(!dayarr.isJsonNull()) {
						mschedules[i] = new Schedules(dayarr);
						JLabel jl = new JLabel(Integer.toString(i) + "\n" + mschedules[i].length);
						jl.setBackground(new Color(255,255,255,0));
						jl.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
					}
				}
			}
		}
		catch (NullPointerException e) {}
		FontMetrics fm = head.getFontMetrics(head.getFont());
		header.setBounds(10, 10, ((int)( fm.stringWidth(head.getText())*2.5)), fm.getHeight());
		header.setBackground(new Color(255,255,255,0));
		arrup.setSize(fm.getHeight(), fm.getHeight());
		arrup.setBackground(new Color(255,255,255,0));
		arrdown.setBackground(new Color(255,255,255,0));
		arrdown.setSize(fm.getHeight(), fm.getHeight());
		updown.setBackground(new Color(255,255,255,0));
		add(header);
		add(calendar);
		
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
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
