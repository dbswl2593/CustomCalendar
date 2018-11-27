import java.util.ArrayList;

import com.google.gson.JsonArray;

public class Schedules {
	@SuppressWarnings("unchecked")
	ArrayList<String> schedule[] = new ArrayList[10];
	int length;
	Schedules(JsonArray input){
		for(int i=0; i<10; i++) {
			try {
			length = i;
			schedule[i] = new ArrayList<String>();
			schedule[i].add(input.get(i).getAsJsonObject().get("name").getAsString());
			schedule[i].add(input.get(i).getAsJsonObject().get("description").getAsString());
			schedule[i].add(input.get(i).getAsJsonObject().get("place").getAsString());
			schedule[i].add(input.get(i).getAsJsonObject().get("timestart").getAsString());
			schedule[i].add(input.get(i).getAsJsonObject().get("timeend").getAsString());
			} catch(NullPointerException e) {
				length--;
				break;
			} catch(IndexOutOfBoundsException e) {
				length--;
				break;
			}
		}
		length++;
	}
	
	public boolean add(String name, String description, String place, String timestart, String timeend) {
		if(length == 10)return false;
		try {
			schedule[length].add(name);
			schedule[length].add(description);
			schedule[length].add(place);
			schedule[length].add(timestart);
			schedule[length].add(timeend);
			length++;
		} catch (Exception e) {return false;}
		return true;
	}
	
	@Override
	public String toString() {
		return schedule.toString();
	}
}
