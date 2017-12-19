import java.time.*;

import org.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Controller {
	
	private JSONArray parkplaetze;
	private LocalTime oeffnungsZeiten[] = new LocalTime[2];
	private double preise[] = new double[3];
	private final static int anzPP = 100;
	private int freiePP = anzPP;
	
	public static void main(String[] args) throws JSONException {
		
		//Objekte erstellen
		Controller ctrl = new Controller();
		Kasse kasse = new Kasse();
		Schranke schranke = new Schranke();
				
		//Öffnungszeiten und Preise festlegen
		ctrl.oeffnungsZeiten[0] = LocalTime.of(0, 00);
		ctrl.oeffnungsZeiten[1] = LocalTime.of(23, 59);
		ctrl.preise[0] = 2.5;	//erste Stunde
		ctrl.preise[1] = 1.5;	//stündlich ab zweiter Stunde
		ctrl.preise[2] = 50;	//Maximal
		
		ctrl.load(); //json einlesen
		for(int i = 0; i < anzPP; i++){ //Anzahl freier Parkplätze bestimmen
			if(ctrl.parkplaetze.getJSONObject(i).has("Ankunftszeit")){
				ctrl.freiePP--;
			}
		}
		
		//--INSERT UI--
		/*
		schranke.newCustomer(ctrl);
		kasse.zahlen(0, ctrl);
		schranke.delCustomer(ctrl, 0);
		*/
		
	}

	//gibt freien Parkplatz zurück
	public int getParkplatz() {
		if(freiePP != 0){
			for(int i = 0; i < anzPP; i++){
					try {
						if(parkplaetze.getJSONObject(i).has("Ankunftszeit")){ //belegt?
							continue;
						}
						freiePP--;
						return i;
					} catch (JSONException e) {}
			}
		}
		return -1; //keine freien Parkplätze
	}
	//setzt Parkplatz zurück
	public void delParkplatz(int id){
		try {
			parkplaetze.getJSONObject(id).remove("Zahlzeit");
			parkplaetze.getJSONObject(id).remove("Ankunftszeit");
			freiePP++;
		} catch (JSONException e) {}
		save();
	}
	public void setAnkunftsZeit(int id, LocalDateTime time){
		try {
			parkplaetze.getJSONObject(id).put("Ankunftszeit", time);
			save();
		} catch (JSONException e) {}
	}
	public LocalDateTime getAnkunftsZeit(int id) {
		try {
			return StrToTime(parkplaetze.getJSONObject(id).get("Ankunftszeit"));
		} catch (JSONException e) {
			return null;
		}
	}
	public void setZahlZeit(int id, LocalDateTime time){
		try {
			parkplaetze.getJSONObject(id).put("Zahlzeit", time);
			save();
		} catch (JSONException e) {}
	}
	public LocalDateTime getZahlZeit(int id) {
		try {
			return StrToTime(parkplaetze.getJSONObject(id).get("Zahlzeit"));
		} catch (JSONException e) {
			return null;
		}
	}
	public LocalTime[] getOeffnungszeiten() {
		return oeffnungsZeiten;
	}
	public double[] getPreise(){
		return preise;
	}
	public void gewinn(double geld){
		
		try {
			JSONObject geldObject = parkplaetze.getJSONObject(anzPP);
			
			if(LocalDateTime.now().getYear()== StrToTime(geldObject.get("Last")).getYear()){
				geldObject.put("Jahr", geldObject.getDouble("Jahr") + geld);
				if(LocalDateTime.now().getMonthValue()== StrToTime(geldObject.get("Last")).getMonthValue()){
					geldObject.put("Monat", geldObject.getDouble("Monat") + geld);	
					if(LocalDateTime.now().getDayOfYear()== StrToTime(geldObject.get("Last")).getDayOfYear()){
						geldObject.put("Kasse", geldObject.getDouble("Kasse") + geld);	
					}
					else{
						geldObject.put("Kasse", geld);
					}
				}
				else{
					geldObject.put("Monat", geld);
				}
			}
			else{
				geldObject.put("Jahr", geld);
			}
			
			geldObject.put("Last", LocalDateTime.now());
			geldObject.put("Gesamt", geldObject.getDouble("Gesamt") + geld);
		} catch (JSONException e) {System.out.println("nooo");}
		
	}
	private void save() {
		try {
			FileWriter writer = new FileWriter("Parkhaus.json");
			writer.write("[" + parkplaetze.join(",\n") + "]");
			writer.close();
		} catch (IOException | JSONException e) {}
	}
	private void load(){
		String s = "";
		try {
			s = new String(Files.readAllBytes(Paths.get("Parkhaus.json")));
		} catch (IOException e1) {}
		
		try {
			parkplaetze = new JSONArray(s);
		} catch (JSONException e) {
			parkplaetze = new JSONArray();
			for(int i = 0; i < anzPP;i++){
				JSONObject platz = new JSONObject();
				try {
					parkplaetze.put(i, platz);
				} catch (JSONException e1) {}
			}
			JSONObject kasse = new JSONObject();
			try {
				kasse.put("Kasse", 0);
				kasse.put("Monat", 0);
				kasse.put("Jahr", 0);
				kasse.put("Gesamt", 0);
				kasse.put("Last", LocalDateTime.now());
				parkplaetze.put(anzPP, kasse);
			} catch (JSONException f) {}
		}
		save();
	}
	
	private LocalDateTime StrToTime(Object o){
		if(o.getClass().getName() == "java.lang.String"){
			return LocalDateTime.parse((String)o);
		}
		return (LocalDateTime) o;
	}
	
}
