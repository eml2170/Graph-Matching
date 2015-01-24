package dc2;

import java.util.HashMap;
import java.util.Map;

public class Checkin {
	Map<String, Integer> freqs;
	int totalCheckins;
	int userId;
	
	public Checkin(int userId){
		this.userId = userId;
		freqs = new HashMap<String, Integer>();
		totalCheckins = 0;
	}
	
	public void add(String location){
		if(freqs.containsKey(location)){
			Integer freq = freqs.get(location);
			freqs.put(location, new Integer(freq.intValue() + 1));
		}
		else{
			freqs.put(location, 1);
		}
		totalCheckins++;
	}

	@Override
	public String toString() {
		return "Checkin [freqs=" + freqs + ", totalCheckins=" + totalCheckins
				+ "]";
	}
	
	
}
