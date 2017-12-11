import java.time.*;

public class Schranke {

  public void newCustomer(Controller ctrl){
    LocalTime jetzt =  LocalTime.now();
    LocalTime oeffnungszeiten[] = ctrl.getOeffnungszeiten();
    // Öfnnungszeiten eingehalten?
    if(!(jetzt.isAfter(oeffnungszeiten[0]) && jetzt.isBefore(oeffnungszeiten[1]))){
    	//Tag bestimmen
    	if(jetzt.isAfter(oeffnungszeiten[1])){
    		System.out.println("\nDas Parkhaus ist zurzeit geschlossen.\nBitte kommen Sie morgen nach " + oeffnungszeiten[0].getHour() + ":" + oeffnungszeiten[1].getMinute() + " Uhr vorbei!");
    	}
    	else System.out.println("\nDas Parkhaus ist zurzeit geschlossen.\nBitte kommen Sie nach " + oeffnungszeiten[0].getHour() + ":" + oeffnungszeiten[1].getMinute() + " Uhr vorbei!");
    	return;
    }
    //freien Parkplatz holen
    int platzNr = ctrl.getParkplatz(); 
    if(platzNr == -1){
    	System.out.println("\nDas Parkhaus ist zurzeit leider voll. Bitte versuchen Sie es später erneut.");
    	return;
    }
    //Ankunftzeit setzen
    ctrl.setAnkunftsZeit(platzNr, jetzt.atDate(LocalDate.now()));
    System.out.println("\nIhnen wurde der Parkplatz " + platzNr + " zugewiesen.\nWir wünschen Ihnen einen guten Aufenthalt!");
    
  }
  
  public void delCustomer(Controller ctrl, int platzNr){
	  LocalDateTime zahlZeit = ctrl.getZahlZeit(platzNr);
	  //noch nicht gezahlt?
	  if(zahlZeit == null){
		  System.out.println("Ihr Ticket wurde noch nicht bezahlt!");
		  return;
	  }
	  //zu viel Zeit seit Zahlung vergangen?
	  if(!zahlZeit.plusMinutes(15).isAfter(LocalDateTime.now())){
		  System.out.println("\nIhr Ticket wurde vor zu langer Zeit abgestempelt. Bitte erneuern sie den Stempel an der Kasse!");
		  return;
	  }
	  //Kunden aus Datenbank entfernen
	  ctrl.delParkplatz(platzNr);
	  System.out.println("\nAuf Wiedersehen!");
  }
}
