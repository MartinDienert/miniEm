import org.json.*;

abstract class Verbraucher{
	String name;
	
	public Verbraucher(JSONObject v){
		name = v.getString("name");		
	}
	
	abstract int setLeistung(int p);
	
	public String getName(){
		return name;
	}
}

