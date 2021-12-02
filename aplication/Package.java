package aplication;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Package {
	
	private String name;
	private Date date;
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	String formattedDate;

	public Package(String name) {
		this.name = name;
		this.date = Date.from(Instant.now());
		this.formattedDate = formatter.format(date);
	}
	
	public void updateTime(){
		date = Date.from(Instant.now());
		formattedDate = formatter.format(date);
	}

	@Override
	public String toString() {
		return name + " " + formattedDate;
	}
	
	
	
}
