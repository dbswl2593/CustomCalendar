import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
		removeAll();
		JPanel header = new JPanel(new BorderLayout());
		JLabel head = new JLabel(cal.get(Calendar.YEAR) + "³â " + (cal.get(Calendar.MONTH) + 1) + "¿ù");
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
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
