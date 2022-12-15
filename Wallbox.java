import org.json.*;

abstract class Wallbox extends Verbraucher{
	int leist_start;
	int leist_stop;
	int leist_offset;
	int[] strom;
	int[] leist;
	Eieruhr startUhr;
	Eieruhr stopUhr;
	Eieruhr plusUhr;
	Eieruhr minusUhr;
	Wartezeit wartezeit;
	int modus = 1;				// 0 - Aus
								// 1 - PV-Überschuss
								// 2 - PV-Minimal laden
								// 3 - Maximal laden

	int[] status = {-1, 0};		// Status 0: -1 - keine Http Verbindung
								//            0 - Fahrzeug nicht verbunden
								//            1 - Warte auf ladefreigabe
								//            2 - Ladebereit
								//            3 - Lädt
								//            4 - Fehler
								// Status 1:  maximal erlaubter Ladestrom
	
	public Wallbox(JSONObject w){
		super(w);
		leist_start = w.getInt("startleistung");
		leist_stop = w.getInt("stopleistung");
		leist_offset = w.getInt("offsetleistung");
		strom = Sonstiges.getIntArray(w.getJSONArray("strom"));
		leist = Sonstiges.getIntArray(w.getJSONArray("leistung"));
		startUhr = new Eieruhr(getName() + "-Startuhr", w.getInt("startzeit"), new WbCbStart());
		stopUhr = new Eieruhr(getName() + "-Stopuhr", w.getInt("stopzeit"), new WbCbStop());
		plusUhr = new Eieruhr(getName() + "-Plusuhr", w.getInt("pluszeit"), new WbCbStrom());
		minusUhr = new Eieruhr(getName() + "-Minusuhr", w.getInt("minuszeit"), new WbCbStrom());
		wartezeit = new Wartezeit(getName() + "-Wartezeit", (w.has("wartezeit"))? w.getInt("wartezeit"): 0, (w.has("wartetimeout"))? w.getInt("wartetimeout"): 10);
	}
	
	void alleUhrenaus(){
		startUhr.stop();
		stopUhr.stop();
		plusUhr.stop();
		minusUhr.stop();
	}
	
	void wb_ein(){
		if(status[0] == 1){
			setStrom(strom[0]);
			Log.log(getName() + " - Strom auf kleinsten Wert gestellt: " + strom[0] + " mA, (" + leist[0] + " W)", 2);
			startWb();
			Log.log(getName() + " - Laden gestartet.", 2);
			wartezeit.start(2, -1);
		}
	}
	
	void wb_aus(){
		if(status[0] == 2 || status[0] == 3){				// laden stoppen
			stopWb();
			Log.log(getName() + " - Laden gestopt.", 2);
			wartezeit.start(1, -1);
		}
	}
	
	void wb_strom(int i){
		setStrom(strom[i]);
		Log.log(getName() + " - neuer Strom eingestellt : " + strom[i] + " mA, (" + leist[i] + " W)", 2);
		wartezeit.start(-1, strom[i]);
	}
	
	public int setLeistung(int p){
		modus = Sonstiges.eintrag_lesen(new String[]{"modus"})[0];
		status = getStatus();								// Status der Box abrufen
		Log.log(getName() + "-Status der Box: " + status[0] + ", Strom: " + status[1] + " mA", 5);
		Sonstiges.eintrag_schreiben(new String[]{"modus"}, new int[]{modus});
		Sonstiges.eintrag_schreiben(new String[]{"status", "strom"}, status);
		if(status[0] == -1 || status[0] == 0 || status[0] == 4)	// kein Auto angeschlossen oder Fehler
			return 0;
		if(wartezeit.laeuft(status)){						// während die Wartezeit läuft nichts tun
			return -1;
		}
		if(modus < 1){										// Laden aus
			alleUhrenaus();
			wb_aus();
			return 0;
		}
		if(modus == 3){										// Laden Maximalstrom
			alleUhrenaus();
			wb_ein();
			wb_strom(strom.length - 1);
			return leist[leist.length - 1];
		}
		if(status[0] == 1){									// start laden
			if(modus == 2)
				wb_ein();
			else if(-p >= leist_start)
				startUhr.run();
			else
				startUhr.stop();
			return leist[0];
		}
		if(modus == 1 && (status[0] == 2 || status[0] == 3)){	// laden stoppen
			if(p > leist_stop){
				stopUhr.run();
			}else
				stopUhr.stop();
		}
		int iBox = Sonstiges.find(strom, status[1]);		// Index nach Strom der Box
		p = leist[iBox] - p - leist_offset;
		Log.log("Leistung ges. mit Offset: " + p + " W", 5);
		int iNeu = Sonstiges.find(leist, p);	// Index nach akt. Leistung 
		Log.log("Index Box: " + iBox + ", Index Neu: " + iNeu, 2);
		if(iBox == iNeu){									// keine Leistungsänderung -> nichts tun
			plusUhr.stop();
			minusUhr.stop();
			return 0;
		}
		if(iBox < iNeu){									// Leistungserhöhung
			minusUhr.stop();
			plusUhr.run(iNeu);
		}else if(iBox > iNeu){								// Leistungsverringerung
			plusUhr.stop();
			minusUhr.run(iNeu);
		}
		return leist[iNeu];
	}
	
	abstract int[] getStatus();
	
	abstract void startWb();
	
	abstract void stopWb();
	
	abstract void setStrom(int st);
	
	class WbCbStart implements WallboxCallback{
		public void start(int i){
		}
		public void abgelaufen(int i){
			wb_ein();
		}
	}
	
	class WbCbStop implements WallboxCallback{
		public void start(int i){
		}
		public void abgelaufen(int i){
			wb_aus();
		}
	}
	
	class WbCbStrom implements WallboxCallback{
		public void start(int i){
			Log.log(getName() + " - neue Leistung: " + leist[i] + " W, Strom: " + strom[i] + " mA", 2);
		}
		public void abgelaufen(int i){
			wb_strom(i);
		}
	}
}

