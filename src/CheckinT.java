package dc2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckinT {
	Map<String, Integer>[] freqs;
	int totalCheckins[];
	int userId;
	static Calendar calendar = Calendar.getInstance();
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	
	@SuppressWarnings("unchecked")
	public CheckinT(int userId){
		this.userId = userId;
		freqs = (Map<String, Integer>[]) new Map[4];
		totalCheckins = new int[4];
		for(int i = 0; i < 4; i++){
			freqs[i] = new HashMap<String, Integer>();
			totalCheckins[i] = 0;
		}
		
	}
	
	public void add(String location, String time){
		int code = getTimeCode(time);
		Map<String, Integer> history = freqs[code];
		if(history.containsKey(location)){
			Integer freq = history.get(location);
			history.put(location, new Integer(freq.intValue() + 1));
		}
		else{
			history.put(location, 1);
		}
		totalCheckins[code]++;
		
	}
	
	private int getTimeCode(String time){
		Date date = null;
		try {
			date = df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY)/6;
	}

	@Override
	public String toString() {
		return "Checkin [freqs=" + freqs + ", totalCheckins=" + totalCheckins
				+ "]";
	}
	
	
}
