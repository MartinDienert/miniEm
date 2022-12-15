import java.util.*;
import org.json.*;

public class WallboxTest extends Wallbox{
	int[] status = {1,0};
		
	public WallboxTest(JSONObject w){
		super(w);
	}
	
	public int[] getStatus(){
		return status;
	}
	
	public void startWb(){
		EinAus ea = new EinAus(2);
		Timer timer = new Timer();
		timer.schedule(ea, 30000);
	}
	
	public void stopWb(){
		EinAus ea = new EinAus(1);
		Timer timer = new Timer();
		timer.schedule(ea, 20000);
	}
	
	public void setStrom(int st){
		Strom str = new Strom(st);
		Timer timer = new Timer();
		timer.schedule(str, 20000);
	}
	
	class EinAus extends TimerTask{
		int st;
		
		public EinAus(int s){
			st = s;
		}
		
		public void run(){
			status[0] = st;
			if(st == 1)
				Hauptschleife.wBLeistung = 0;
			else if(st == 2)
				Hauptschleife.wBLeistung = leist[Sonstiges.find(strom, status[1])];
		}
	}
		
	class Strom extends TimerTask{
		int str;
		
		public Strom(int s){
			str = s;
		}
		
		public void run(){
			status[1] = str;
			Hauptschleife.wBLeistung = leist[Sonstiges.find(strom, str)];
		}
	}
}

