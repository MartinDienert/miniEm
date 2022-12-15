import org.json.*;

public class WallboxHttp extends Wallbox{
	JSONObject daten;
	HttpRequest hr;
	boolean verbunden;
	
	public WallboxHttp(JSONObject w){
		super(w);
		daten = w.getJSONObject("daten");
		hr = new HttpRequest(daten.getString("ip"), daten.getInt("port"));
	}
	 
	public int[] getStatus(){
		int[] erg = new int[2];
		erg[0] = -1;
		erg[1] = 0;
		
		try{
			JSONObject status = daten.getJSONObject("status");
			String response = hr.getResponse(Main.progName, status.getString("request"), status.getString("resource"), null);
			if(response.equals(""))
				return erg;
			JSONObject res = new JSONObject(response);
			erg[0] = res.getInt(status.getString("status"));
			erg[1] = res.getInt(status.getString("strom"));
		}catch(Exception e){
			Log.err("WallboxHttp.getStatus: " + e);
		}
		return erg;
	}
	
	public void startWb(){
		JSONObject ladenStart = daten.getJSONObject("ladenStart");
		hr.getResponse(Main.progName, ladenStart.getString("request"), ladenStart.getString("resource"), ladenStart.getString("inhalt"));
	}
	
	public void stopWb(){
		JSONObject ladenStop = daten.getJSONObject("ladenStop");
		hr.getResponse(Main.progName, ladenStop.getString("request"), ladenStop.getString("resource"), ladenStop.getString("inhalt"));
	}
	
	public void setStrom(int st){
		JSONObject ladeStrom = daten.getJSONObject("ladeStrom");
		String inhalt = ladeStrom.getString("inhalt");
		inhalt = inhalt.replaceAll("%i", Integer.toString(st));
		hr.getResponse(Main.progName, ladeStrom.getString("request"), ladeStrom.getString("resource"), inhalt);	
	}
}

