import java.util.*;
import java.text.*;
import org.json.*;

class Hauptschleife extends TimerTask{
	Zaehler zErzeugung;
	Zaehler zNetz;
	Wallbox wBox;
	static int wBLeistung = 0;	// TEST
	
	public Hauptschleife(JSONObject config){
		if(config.getJSONObject("zaehlerErzeugung").getString("type").equals("test"))
			zErzeugung = new ZaehlerTest(config.getJSONObject("zaehlerErzeugung"), config.getInt("zykluszeit"));
		else if(config.getJSONObject("zaehlerErzeugung").getString("type").equals("modbustcp"))
			zErzeugung = new ZaehlerModbusTcp(config.getJSONObject("zaehlerErzeugung"));
		else
			zErzeugung = null;										// weitere Zaehlertypen
		if(config.getJSONObject("zaehlerNetz").getString("type").equals("test"))
			zNetz = new ZaehlerTest(config.getJSONObject("zaehlerNetz"), config.getInt("zykluszeit"));
		else if(config.getJSONObject("zaehlerNetz").getString("type").equals("modbustcp"))
			zNetz = new ZaehlerModbusTcp(config.getJSONObject("zaehlerNetz"));
		else
			zNetz = null;											// weitere Zaehlertypen
		if(config.getJSONObject("wallbox").getString("type").equals("test"))
			wBox = new WallboxTest(config.getJSONObject("wallbox"));
		else if(config.getJSONObject("wallbox").getString("type").equals("http"))
			wBox = new WallboxHttp(config.getJSONObject("wallbox"));
		else
			wBox = null;											// weitere Verbraucher
	}
	
	public void run(){
		Debug.start(1);
		Sonstiges.eingabe_lesen();
		Sonstiges.ausgabe_lesen();
		if(zErzeugung != null){
			int p = zErzeugung.getLeistung();
			if(zErzeugung.getFehler() == 0){
				Log.addMW(p);
				Log.log("Erzeugung: " + (p) + " W", 2);
			}
		}
		if(zNetz != null){
			int p = zNetz.getLeistung();
			float u = zNetz.getSpannung();
			
			if(zNetz.getFehler() == 0){
				Log.addMW(p);
				Log.log("Spannung: " + new DecimalFormat("#.#").format(u) + " V, Leistung: " + (p + wBLeistung) + " W", 2);
				if(wBox != null){
					wBox.setLeistung(p + wBLeistung);
				}
			}
		}
		Log.logMW(1);
		Sonstiges.ausgabe_schreiben();
		Debug.logZeit(1);
	}
}
