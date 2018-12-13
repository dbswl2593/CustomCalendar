import java.util.ArrayList;

import com.google.gson.JsonArray;

public class Subject {
	@SuppressWarnings("unchecked")
	ArrayList<String> schedule[] = new ArrayList[10];
	int length;
	Subject(JsonArray input){
		for(int i=0; i<10; i++) {
			try {
			length = i;
			schedule[i] = new ArrayList<String>();
			schedule[i].add(input.get(i).getAsJsonObject().get("name").getAsString());
			schedule[i].add(input.get(i).getAsJsonObject().get("place").getAsString());
			schedule[i].add(input.get(i).getAsJsonObject().get("time").getAsString());
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
	
	public boolean add(String name, String description, String place, String time) {
		if(length == 10)return false;
		try {
			schedule[length].add(name);
			schedule[length].add(place);
			schedule[length].add(time);
			length++;
		} catch (Exception e) {return false;}
		return true;
	}
	
	@Override
	public String toString() {
		return schedule.toString();
	}
}
