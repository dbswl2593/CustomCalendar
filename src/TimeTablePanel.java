import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class TimeTablePanel extends AlphaPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5642299407389505114L;
	
	public Subject timetable[] = new Subject[7];

	private AlphaPanel header;
	private JLabel headlabel;
	private JButton markplus = new JButton("+");
	private String[] dayofweek = {"¿œ", "ø˘", "»≠", "ºˆ", "∏Ò", "±›", "≈‰"};
	private ArrayList<JLabel> weekhead = new ArrayList<JLabel>();
	
	private AlphaPanel table;
	
	private final String path = "./tabledata.json";
	
	private GridBagConstraints cc = new GridBagConstraints();
	
	private Color white = new Color(255,255,255,0);
	
	private JsonObject data;
	public TimeTablePanel() {
		setLayout(null);
		header = new AlphaPanel();
		table = new AlphaPanel();
		header.setLayout(new BorderLayout());
		table.setLayout(new GridBagLayout());
		header.setBackground(white);
		table.setBackground(white);
		
		headlabel = new JLabel("Ω√∞£«•");
		headlabel.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.PLAIN, 40));
		FontMetrics fm = headlabel.getFontMetrics(headlabel.getFont());
		
		markplus.setBackground(white);
		markplus.setSize(fm.getHeight(), fm.getHeight());
		markplus.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.PLAIN, 20));
		markplus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSubject();
			}
		});
		
		header.add(headlabel, BorderLayout.WEST);
		header.add(markplus, BorderLayout.EAST);
		add(header);
		header.setVisible(true);
		add(table);
		data = readDB(path);
		
	}
	
	private void update() {
		table.setBounds(header.getBounds().x - 15, header.getBounds().height + header.getBounds().y, 
				getWidth() - 20, getHeight() - header.getBounds().y - header.getBounds().height);
		table.removeAll();
		
		cc.weightx = 1.0;
		cc.weighty = 1.0;
		cc.gridx = 0;
		cc.gridy = 0;
		cc.fill = GridBagConstraints.BOTH;
		
		for(int i=0; i<7; i++) {
			weekhead.add(new JLabel(dayofweek[i], SwingConstants.CENTER));
			weekhead.get(i).setBackground(white);
			weekhead.get(i).setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.PLAIN, 35));
			weekhead.get(i).setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100,100,100)));
			cc.gridx = i+1;
			table.add(weekhead.get(i), cc);
		}
		
		for(int i=0; i<7; i++) {
			timetable[i] = new Subject(data.get(dayofweek[i]).getAsJsonArray());
			for(int j=0; j<timetable[i].length; j++) {
				cc.gridx = i+1;
				cc.gridy = Integer.parseInt(timetable[i].schedule[j].get(2));
				JLabel subject = new JLabel();
				subject.setText("<html>"+timetable[i].schedule[j].get(0) + "<br>" + timetable[i].schedule[j].get(1) + "</html>");
				subject.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.PLAIN, 15));
				subject.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100,100,100)));
				table.add(subject, cc);
			}
		}
		
		for(int i=1; i<=10; i++) {
			JLabel leftbar = new JLabel(Integer.toString(i) + "±≥Ω√", SwingConstants.CENTER);
			leftbar.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.PLAIN, 25));
			leftbar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100,100,100)));
			cc.gridx = 0;
			cc.gridy = i;
			table.add(leftbar, cc);
		}
		
		add(table);
	}
	
	private void addSubject() {
		JTextField namefield = new JTextField(20);
		JTextField placefield = new JTextField(20);
		JComboBox<String> dayfield = new JComboBox<>(dayofweek);
		String[] temp = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		JComboBox<String> timefield = new JComboBox<>(temp);
		JPanel askingpanel = new JPanel();
		askingpanel.setLayout(new GridBagLayout());
		GridBagConstraints msgc = new GridBagConstraints();
		msgc.gridx = 0;
		msgc.gridy = 0;
		msgc.weightx = 1.0;
		msgc.weighty = 1.0;
		askingpanel.add(new JLabel("∞˙∏Ò∏Ì"), msgc);
		msgc.gridx = 1;
		askingpanel.add(namefield, msgc);
		msgc.gridx = 0;
		msgc.gridy = 1;
		askingpanel.add(new JLabel("¿Âº“"), msgc);
		msgc.gridx = 1;
		askingpanel.add(placefield, msgc);
		msgc.gridy++;
		msgc.gridx = 0;
		askingpanel.add(new JLabel("ø‰¿œ"), msgc);
		msgc.gridx++;
		askingpanel.add(dayfield, msgc);
		msgc.gridx=0;
		msgc.gridy++;
		askingpanel.add(new JLabel("±≥Ω√"), msgc);
		msgc.gridx++;
		askingpanel.add(timefield, msgc);
		
		int result = JOptionPane.showConfirmDialog(null, askingpanel, "¿œ¡§ √ﬂ∞°", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			writeTable(dayfield.getSelectedItem().toString(), namefield.getText(), placefield.getText(), timefield.getSelectedItem().toString());
			update();
		}
	}
	
	JsonObject readDB(String path) {
		JsonParser parser = new JsonParser();
		JsonObject dat;
		try {
			dat = parser.parse(new FileReader(path)).getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			dat = new JsonObject();
			for(int i=0; i<7; i++) {
				JsonArray day = new JsonArray(10);
				dat.add(dayofweek[i], day);
			}
			writeJson(dat, path);
		}
		return dat;
		
		
	}
	
	private void writeTable(String day, String name, String place, String time) {
		System.out.println(day);
		System.out.println(name);
		System.out.println(place);
		System.out.println(time);
		JsonObject newsub = new JsonObject();
		newsub.addProperty("name", name);
		newsub.addProperty("place", place);
		newsub.addProperty("time", time);
		data.get(day).getAsJsonArray().add(newsub);
		writeJson(data, path);
		update();
	}
	private void writeJson(JsonElement json, String path) {
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
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		FontMetrics fm = headlabel.getFontMetrics(headlabel.getFont());
		header.setBounds(20, 10, getWidth() - 40, fm.getHeight());
		update();
	}
}
