import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class CalendarPanel extends AlphaPanel implements ActionListener, MouseWheelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3032615500418430799L;
	private int year, month;
	private Calendar cal;
	private JsonObject data;
	private Schedules[] mschedules = new Schedules[32];
	private JButton arrup = new JButton("¡â");
	private JButton arrdown = new JButton("¡ä");
	private String[] dayofweek = {"", "ÀÏ", "¿ù", "È­", "¼ö", "¸ñ", "±Ý", "Åä"};
	private ArrayList<JLabel> weekhead = new ArrayList<JLabel>();
	
	private AlphaPanel header;
	private JLabel headlabel;
	
	private AlphaPanel calendar;
	private GridBagConstraints cc;
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
		arrdown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		header = new AlphaPanel();
		header.setLayout(new BorderLayout());
		headlabel = new JLabel();
		headlabel.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 40));
		headlabel.setText(cal.get(Calendar.YEAR) + "³â " + (cal.get(Calendar.MONTH) + 1) + "¿ù");
		header.add(headlabel, BorderLayout.WEST);
		AlphaPanel updown = new AlphaPanel();
		updown.add(arrup);
		updown.add(arrdown);
		header.add(updown, BorderLayout.EAST);
		header.setBackground(new Color(255,255,255,0));
		FontMetrics fm = headlabel.getFontMetrics(headlabel.getFont());
		header.setBounds(30, 10, ((int)( fm.stringWidth(headlabel.getText())*2.5)), fm.getHeight());
		arrup.setSize(fm.getHeight(), fm.getHeight());
		arrup.setBackground(new Color(255,255,255,0));
		arrdown.setBackground(new Color(255,255,255,0));
		arrdown.setSize(fm.getHeight(), fm.getHeight());
		updown.setBackground(new Color(255,255,255,0));
		add(header);
		
		calendar = new AlphaPanel(new GridBagLayout());
		cc = new GridBagConstraints();
		cc.weightx = 1.0;
		cc.weighty = 1.0;
		cc.gridy = 0;
		cc.fill = GridBagConstraints.BOTH;
		calendar.setBackground(new Color(255,255,255,0));
		for(int i=1; i<8; i++) {
			weekhead.add(new JLabel(dayofweek[i], SwingConstants.CENTER));
			weekhead.get(i-1).setBackground(new Color(255,255,255,0));
			weekhead.get(i-1).setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 35));
			weekhead.get(i-1).setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100,100,100)));
			cc.gridx = i-1;
			calendar.add(weekhead.get(i-1), cc);
		}
	}
	
	void writeJson(JsonElement json, String path) {
		System.out.println("Writing Data...");
		Gson gson = new Gson();
		FileOutputStream opstream;
		try {
			File out = new File(path);
			out.createNewFile();
			opstream = new FileOutputStream(path);
			OutputStreamWriter writer = new OutputStreamWriter(opstream);
			writer.write(gson.toJson(json));
			writer.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	JsonObject readDB(String path) {
		JsonParser parser = new JsonParser();
		JsonObject dat;
		try {
			dat = parser.parse(new FileReader(path)).getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			dat = new JsonObject();
			JsonObject yearobj = new JsonObject();
			for(int i=0; i<12; i++) {
				JsonObject month = new JsonObject();
				Calendar temp = Calendar.getInstance();
				temp.set(year, i, 1);
				for(int j=0; j<temp.getActualMaximum(Calendar.DATE); j++){
					JsonArray day = new JsonArray(10);
					month.add(Integer.toString(j + 1), day);
				}
				yearobj.add(Integer.toString(i + 1), month);
			}
			dat.add(Integer.toString(year), yearobj);
			writeJson(dat, path);
		}
		return dat;
	}
	
	void update(int start) {
		headlabel.setText(cal.get(Calendar.YEAR) + "³â " + (cal.get(Calendar.MONTH) + 1) + "¿ù");
		calendar.setBounds(header.getBounds().x - 15, header.getBounds().height + header.getBounds().y, 
				getWidth() - 20, getHeight() - header.getBounds().y - header.getBounds().height);
		
		cc.gridy = 1;
		for(int i = 1; i < start; i++) {
			cc.gridx = i-1;
			JLabel jl = new JLabel("", SwingConstants.CENTER);
			jl.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 45));
			jl.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100,100,100)));
			calendar.add(jl, cc);
		}
		int fir = cc.gridx;
		try {
			JsonElement monthobj = data.get(Integer.toString(year)).getAsJsonObject().get(Integer.toString(month));
			for(int i=1; i < cal.getActualMaximum(Calendar.DATE) + 1; i++) {
				if(!monthobj.isJsonNull()) {
					JsonArray dayarr = monthobj.getAsJsonObject().get(Integer.toString(i)).getAsJsonArray();
					if(!dayarr.isJsonNull()) {
						mschedules[i] = new Schedules(dayarr);
						JLabel jl = new JLabel("<html><strong>"+Integer.toString(i) + "</strong><br/><br/>[" + mschedules[i].length + "]</html>", SwingConstants.CENTER);
						jl.setBackground(new Color(255,255,255,0));
						jl.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 30));
						cc.gridx = (fir + i) % 7;
						if(cc.gridx == 6)jl.setForeground(new Color(60,60,255));
						else if(cc.gridx == 0)jl.setForeground(new Color(255,60,60));
						jl.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100,100,100)));
						calendar.add(jl, cc);
						if(cc.gridx == 6)cc.gridy++;
						//TODO: add label click listener;
					}
				}
			}
			for(; cc.gridx < 7; cc.gridx++) {
				JLabel jl = new JLabel("", SwingConstants.CENTER);
				jl.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 45));
				jl.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100,100,100)));
				calendar.add(jl, cc);
			}
		}
		catch (NullPointerException e) {}
		add(calendar);
		
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		super.setBounds(x, y, width, height);
		update(cal.get(Calendar.DAY_OF_WEEK));
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
