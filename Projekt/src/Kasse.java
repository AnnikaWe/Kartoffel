import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Kasse {
	public void zahlen(int platzNr, Controller ctrl){
		LocalDateTime jetzt = LocalDateTime.now();
		double preis = 0f;
		double eingezahlt = 0;
		double[] kosten = ctrl.getPreise();
		//Parkdauer errechnen
		int timeD = (int)ctrl.getAnkunftsZeit(platzNr).until(jetzt, ChronoUnit.DAYS);
		int timeH = (int)ctrl.getAnkunftsZeit(platzNr).until(jetzt, ChronoUnit.HOURS);
		int timeM = (int)ctrl.getAnkunftsZeit(platzNr).until(jetzt, ChronoUnit.MINUTES) + 1;
		//nur eine Stunde geparkt
		if(timeH + 1 == 1){
			preis = kosten[0];
		}
		//Kosten von 20� �berschritten
		else if(timeH * kosten [1] >= 20){
			preis = kosten[2];
		}
		//"normale" Parkpreisberechnung
		else preis = kosten[1] * timeH;
		//l�nger als 1 Tag?
		if(timeD > 0){
			System.out.println("\nIhre Parkzeit betr�gt: " + timeD + " Tage, " + timeH + " Stunden und " + timeM + " Minuten.");
		}
		else System.out.println("\nIhre Parkzeit betr�gt: " + timeH + " Stunden und " + timeM + " Minuten.");
		
		System.out.println("Die Parkgeb�hren betragen " + preis + " Euro.");
		
		//--INSERT UI--
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Dr�cken Sie auf \"abbrechen\", um den Vorgang abzubrechen.");
		//Zahlung ausstehend
		while(preis - eingezahlt > 0){	
			System.out.println("Verbleibend: " + (preis - eingezahlt));
			//Eingabe einer Zahl?
			if(keyboard.hasNextDouble()){
				double doubleIn = keyboard.nextDouble();
				//Zahl positiv?
				if(doubleIn > 0){
					eingezahlt += doubleIn;
					System.out.println("\nBezahlt: " + eingezahlt);
				}
				else{
					System.out.println("Keine g�ltige Eingabe.");
					continue;
				}
			}
			//Eingabe Abbruch?
			else if(keyboard.hasNext("abbrechen")){
				System.out.println("Vorgang abgebrochen. Die bezahlten " + eingezahlt + " Euro werden zur�ckgegeben.");
				keyboard.close();
				return;
			}
			//Eingabe M�ll?
			else{
				if(keyboard.hasNext()){
					keyboard.next();
					System.out.println("Keine g�ltige Eingabe.");
					continue;
				}
			}
		}
		keyboard.close();
		System.out.println("R�ckgeld: " + (eingezahlt - preis));
		//Gewinn eintragen
		ctrl.gewinn(preis);
		//Zahlzeit setzen
		ctrl.setZahlZeit(platzNr, jetzt);
	
	}
}
