package application;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
* A classe Package é responsável por formatar corretamente os dados importantes para um pacote
* 
* @author Luiz Felipe Triques Moraes
* @author Renata Rona Garib
* @author Thalles Raphael Guimarães
* 
*/
public class Package {
	
	/**
 	* Nome do pacote
 	*/
	private String name;
	/**
 	* Data e horário do pacte
 	*/
	private Date date;
	/**
 	* Formatação da data e horário do pacote
 	*/
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	/**
 	* Data e horario do pacote já formatados
 	*/
	String formattedDate;

	public Package(String name) {
		this.name = name;
		this.date = Date.from(Instant.now());
		this.formattedDate = formatter.format(date);
	}
	
	/**
 	* Coloca a data e horário atual no pacote
 	*/
	public void updateTime(){
		date = Date.from(Instant.now());
		formattedDate = formatter.format(date);
	}

	@Override
	public String toString() {
		return name + " " + formattedDate;
	}

	
}