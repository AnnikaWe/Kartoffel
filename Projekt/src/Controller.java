import java.util.Scanner;
import java.time.*;
import java.math.*;

@SuppressWarnings("unused")
public class Controller {

	private LocalDateTime parkPlaetze[][] = new LocalDateTime[100][2];
	private LocalTime oeffnungsZeiten[] = new LocalTime[2];
	private double preise[] = new double[3]; //i = 0: erste Stunde, i = 1: Preis pro zusätzlicher Stunde, i = 2: Maximalpreis
	private int freiePP = parkPlaetze.length; //Anzahl Parkplätze
	private double kassenstand = 0;
	private double inWoche = 0;
	private double inMonat = 0;
	private double inJahr = 0;
	private double inGes = 0;

	public static void main(String[] args) throws InterruptedException {
		
		//Objekte erstellen
		Controller ctrl = new Controller();
		Kasse kasse = new Kasse();
		Schranke schranke = new Schranke();
		
		//Öffnungszeiten und Preise festlegen
		ctrl.oeffnungsZeiten[0] = LocalTime.of(6, 00);
		ctrl.oeffnungsZeiten[1] = LocalTime.of(23, 30);
		ctrl.preise[0] = 1.5f;
		ctrl.preise[1] = 1;
		ctrl.preise[2] = 20;
		
		//--INSERT UI--
		schranke.newCustomer(ctrl);
		kasse.zahlen(0, ctrl);
		schranke.delCustomer(ctrl, 0);
		
	}

	//gibt freien Parkplatz zurück
	public int getParkplatz(){
		if(!(freiePP == 0)){
			for(int i = 0; i < parkPlaetze.length; i++){
				if(parkPlaetze[i][0] == null){
					freiePP--;
					return i;
				}
			}
		}
		return -1; //keine freien Parkplätze
	}
	//setzt Parkplatz zurück
	public void delParkplatz(int id) {
		freiePP++;
		parkPlaetze[id][0] = null;
		parkPlaetze[id][1] = null;
	}
	public void setAnkunftsZeit(int id, LocalDateTime time) {
		parkPlaetze[id][0] = time;
	}
	public LocalDateTime getAnkunftsZeit(int id) {
		return parkPlaetze[id][0];
	}
	public void setZahlZeit(int id, LocalDateTime time) {
		parkPlaetze[id][1] = time;
	}
	public LocalDateTime getZahlZeit(int id) {
		return parkPlaetze[id][1];
	}
	public LocalTime[] getOeffnungszeiten() {
		return oeffnungsZeiten;
	}
	public double[] getPreise(){
		return preise;
	}
	public void gewinn(double geld){
		kassenstand += geld;
		inWoche += geld;
		inMonat += geld;
		inJahr += geld;
		inGes += geld;
	}
}
