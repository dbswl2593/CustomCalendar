import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class CalendarPanel extends AlphaPanel implements ActionListener {

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
	
	private JButton arrleft = new JButton("¢·");
	private JButton markplus = new JButton("+");
	private String[] dayofweek = {"", "ÀÏ", "¿ù", "È­", "¼ö", "¸ñ", "±Ý", "Åä"};
	private ArrayList<JLabel> weekhead = new ArrayList<JLabel>();
	
	private AlphaPanel header;
	private JLabel headlabel;
	
	private AlphaPanel calendar;
	private GridBagConstraints cc;
	
	private AlphaPanel schedule;
	
	private TimeTablePanel timetable = null;
	
	AlphaPanel updown = new AlphaPanel();
	AlphaPanel leftplus = new AlphaPanel();
	
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
				if(cal.get(Calendar.MONTH) == 0) {
					cal.set(Calendar.MONTH, 11);
					cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)-1);
				}
				else cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1);
				cal.set(Calendar.DATE, 1);
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				update(cal.get(Calendar.DAY_OF_WEEK));
			}
		});
		arrdown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cal.get(Calendar.MONTH) == 12) {
					cal.set(Calendar.MONTH, 0);
					cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
				}
				else cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
				cal.set(Calendar.DATE, 1);
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				update(cal.get(Calendar.DAY_OF_WEEK));
			}
		});
		arrleft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				schedule.setVisible(false);
				calendar.setVisible(true);
				leftplus.setVisible(false);
				updown.setVisible(true);
				header.add(updown, BorderLayout.EAST);
				headlabel.setText(cal.get(Calendar.YEAR) + "³â " + (cal.get(Calendar.MONTH) + 1) + "¿ù");
				cal.set(Calendar.DATE, 1);
				update(cal.get(Calendar.DAY_OF_WEEK));
			}
		});
		markplus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addschedule(cal.get(Calendar.DATE));
			}
		});
		header = new AlphaPanel();
		header.setLayout(new BorderLayout());
		headlabel = new JLabel();
		headlabel.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 40));
		headlabel.setText(cal.get(Calendar.YEAR) + "³â " + (cal.get(Calendar.MONTH) + 1) + "¿ù");
		header.add(headlabel, BorderLayout.WEST);
		updown.add(arrup);
		updown.add(arrdown);
		leftplus.add(arrleft);
		leftplus.add(markplus);
		leftplus.setVisible(false);
		header.add(updown, BorderLayout.EAST);
		header.setBackground(new Color(255,255,255,0));
		FontMetrics fm = headlabel.getFontMetrics(headlabel.getFont());
		arrup.setSize(fm.getHeight(), fm.getHeight());
		arrup.setBackground(new Color(255,255,255,0));
		arrup.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
		arrdown.setBackground(new Color(255,255,255,0));
		arrdown.setSize(fm.getHeight(), fm.getHeight());
		arrdown.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
		updown.setBackground(new Color(255,255,255,0));
		
		arrleft.setSize(fm.getHeight(), fm.getHeight());
		arrleft.setBackground(new Color(255,255,255,0));
		arrleft.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
		markplus.setBackground(new Color(255,255,255,0));
		markplus.setSize(fm.getHeight(), fm.getHeight());
		markplus.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
		leftplus.setBackground(new Color(255,255,255,0));
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
		schedule = new AlphaPanel();
		schedule.setLayout(null);
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
		
		removeAll();

		add(header);
		calendar.removeAll();
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
			JsonElement monthobj = data.get(Integer.toString(year)).getAsJsonObject().get(Integer.toString(month+1));
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
						final int day = i;
						jl.addMouseListener(new MouseListener() {
							@Override
							public void mouseReleased(MouseEvent e) {}
							@Override
							public void mousePressed(MouseEvent e) {
								if(SwingUtilities.isLeftMouseButton(e))
									updateSchedule(day);
							}
							@Override
							public void mouseExited(MouseEvent e) {}
							@Override
							public void mouseEntered(MouseEvent e) {}
							@Override
							public void mouseClicked(MouseEvent e) {}
						});
					}
				}
			}
		}
		catch (NullPointerException e) {e.printStackTrace();}
		cc.gridx %= 7;
		for(; cc.gridx < 7; cc.gridx++) {
			JLabel jl = new JLabel(" ", SwingConstants.CENTER);
			jl.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 45));
			jl.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100,100,100)));
			calendar.add(jl, cc);
		}
		add(calendar);

		calendar.repaint();
	}
	
	void updateSchedule(int day) {
		final int panelheight = (getHeight() - header.getHeight())/10;
		cal.set(Calendar.DATE, day);
		updown.setVisible(false);
		leftplus.setVisible(true);
		schedule.removeAll();
		header.add(leftplus, BorderLayout.EAST);
		headlabel.setText(cal.get(Calendar.YEAR) + "³â " + (cal.get(Calendar.MONTH) + 1) + "¿ù" + " " + day +"ÀÏ");
		schedule.setBounds(header.getBounds().x - 15, header.getBounds().height + header.getBounds().y, 
				getWidth() - 20, getHeight() - header.getBounds().y - header.getBounds().height);
		schedule.setBackground(new Color(255,255,255,0));
		
		GridBagConstraints sc = new GridBagConstraints();
		AlphaPanel[] scpanel = new AlphaPanel[10];
		for(int i=0; i<mschedules[day].length; i++) {
			scpanel[i] = new AlphaPanel();
			header.setBackground(new Color(255,255,255,0));
			JLabel name = new JLabel(mschedules[day].schedule[i].get(0));
			JLabel place = new JLabel(mschedules[day].schedule[i].get(1));
			JLabel time = new JLabel(mschedules[day].schedule[i].get(2) + " ~ " + mschedules[day].schedule[i].get(3));
			JButton delete = new JButton("D");
			name.setBackground(new Color(255,255,255,0));
			place.setBackground(new Color(255,255,255,0));
			time.setBackground(new Color(255,255,255,0));
			delete.setBackground(new Color(255,255,255,0));
			name.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 30));
			place.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 30));
			time.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 30));
			delete.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 30));
			final int index = i;
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int j = index + 1; j < mschedules[day].length; j++) {
						mschedules[day].schedule[j-1] = mschedules[day].schedule[j];
						//JsonArray newarr = new JsonArray();
						//for(int k = 0; k < mschedules[day].length; k++) {
						//	if(k!=index)newarr.add(dayarr.get(k));
						//}
						//monthobj.getAsJsonObject().remove(Integer.toString(day));
						//monthobj.getAsJsonObject().add(Integer.toString(day), newarr);
					}
					JsonElement monthobj = data.get(Integer.toString(year)).getAsJsonObject().get(Integer.toString(month+1));
					JsonArray dayarr = monthobj.getAsJsonObject().get(Integer.toString(day)).getAsJsonArray();
					dayarr.remove(index);
					writeJson(data, "./caldata.json");
					mschedules[day].length--;
					updateSchedule(day);
				}
			});
			scpanel[i].setBounds(header.getBounds().x - 15, header.getBounds().height + header.getBounds().y + panelheight*i, header.getWidth() + 15, panelheight);
			scpanel[i].setLayout(new GridBagLayout());
			sc.weightx = 1.0;
			sc.weighty = 1.0;
			sc.gridx = 0;
			sc.gridy = 0;
			sc.gridheight = 1;
			sc.gridwidth = 2;
			scpanel[i].add(name, sc);
			sc.gridx = 2;
			scpanel[i].add(place, sc);
			sc.gridx = 0;
			sc.gridy = 1;
			sc.gridwidth = 4;
			scpanel[i].add(time, sc);
			sc.gridx = 4;
			sc.gridy = 0;
			sc.gridheight = 2;
			sc.gridwidth = 1;
			scpanel[i].add(delete, sc);
			scpanel[i].setBorder(BorderFactory.createLineBorder(new Color(100,100,100)));
			scpanel[i].setBackground(new Color(255,255,255,0));
			scpanel[i].addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {
					try {
						Desktop.getDesktop().browse(new URI("https://map.naver.com/index.nhn?query="+URLEncoder.encode(place.getText(), "UTF-8")));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {}
			});
			schedule.add(scpanel[i]);
		}
		calendar.setVisible(false);
		schedule.setVisible(true);
		add(schedule);
		schedule.repaint();
	}
	
	public void addschedule(int day) {
		JTextField namefield = new JTextField(20);
		JTextField placefield = new JTextField(20);
		JTextField timestartfield = new JTextField(12);
		JTextField timeendfield = new JTextField(12);
		JPanel askingpanel = new JPanel();
		askingpanel.setLayout(new GridBagLayout());
		GridBagConstraints msgc = new GridBagConstraints();
		msgc.gridx = 0;
		msgc.gridy = 0;
		msgc.weightx = 1.0;
		msgc.weighty = 1.0;
		askingpanel.add(new JLabel("Á¦¸ñ"), msgc);
		msgc.gridx = 1;
		askingpanel.add(namefield, msgc);
		msgc.gridx = 0;
		msgc.gridy = 1;
		askingpanel.add(new JLabel("Àå¼Ò"), msgc);
		msgc.gridx = 1;
		askingpanel.add(placefield, msgc);
		msgc.gridy++;
		msgc.gridx = 0;
		askingpanel.add(new JLabel("½ÃÀÛ ½Ã°£"), msgc);
		msgc.gridx++;
		askingpanel.add(timestartfield, msgc);
		msgc.gridy++;
		msgc.gridx = 0;
		askingpanel.add(new JLabel("Á¾·á ½Ã°£"), msgc);
		msgc.gridx ++;
		askingpanel.add(timeendfield, msgc);
		
		int result = JOptionPane.showConfirmDialog(null, askingpanel, "ÀÏÁ¤ Ãß°¡", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			writeSchedule(day, namefield.getText(), placefield.getText(), timestartfield.getText(), timeendfield.getText());
			updateSchedule(day);
		}
	}
	
	public void writeSchedule(int day, String name, String place, String timestart, String timeend) {
		mschedules[day].schedule[mschedules[day].length] = new ArrayList<>();
		mschedules[day].schedule[mschedules[day].length].add(name);
		mschedules[day].schedule[mschedules[day].length].add(place);
		mschedules[day].schedule[mschedules[day].length].add(timestart);
		mschedules[day].schedule[mschedules[day].length].add(timeend);
		
		JsonElement monthobj = data.get(Integer.toString(year)).getAsJsonObject().get(Integer.toString(month+1));
		JsonArray dayarr = monthobj.getAsJsonObject().get(Integer.toString(day)).getAsJsonArray();
		JsonObject jsoncontainer = new JsonObject();
		jsoncontainer.addProperty("name", name);
		jsoncontainer.addProperty("place", place);
		jsoncontainer.addProperty("timestart", timestart);
		jsoncontainer.addProperty("timeend", timeend);
		dayarr.add(jsoncontainer);
		writeJson(data, "./caldata.json");
		mschedules[day].length += 1;
	}
	
	public void attachTimeTable(TimeTablePanel tt) {
		timetable = tt;
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		FontMetrics fm = headlabel.getFontMetrics(headlabel.getFont());
		header.setBounds(20, 10, getWidth() - 40, fm.getHeight());
		update(cal.get(Calendar.DAY_OF_WEEK));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		e.getSource();
	}

}
