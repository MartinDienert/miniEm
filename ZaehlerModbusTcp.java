import java.util.*;
import org.json.*;

public class ZaehlerModbusTcp extends Zaehler{
	ModbusTCP mb;
	int[] reg = new int[1];
	float[] faktor = new float[1];
	
	public ZaehlerModbusTcp(JSONObject z){
		super(z);
		JSONObject d = z.getJSONObject("daten");
		mb = new ModbusTCP(d.getString("ip"), d.getInt("port"));
		if(d.has("regP")){
			reg[0] = d.getInt("regP");
			if(d.has("faktorP"))
				faktor[0] = d.getFloat("faktorP");
			else
				faktor[0] = 1;
			if(d.has("regU")){
				reg = Arrays.copyOf(reg, reg.length + 1);
				faktor = Arrays.copyOf(faktor, faktor.length + 1);
				reg[reg.length - 1] = d.getInt("regU");
				if(d.has("faktorU"))
					faktor[faktor.length - 1] = d.getFloat("faktorU");
				else
					faktor[faktor.length - 1] = 1;
			}
		}else{
			fehler = 1;
			throw new ZaehlerModbusTcpException();
		}
	}
	 
	public int getLeistung(){
		int ret;

		ret = mb.getModbusRegisterInt(reg[0]);
		if(mb.getFehler() == 0){
			ret *= faktor[0];
			fehler = 0;
		}else
			fehler = 1;
		return ret;
	}
	
	public float getSpannung(){
		float ret;
		if(reg.length < 2)
			return 0;
		ret = mb.getModbusRegisterInt(reg[1]);
		if(mb.getFehler() == 0){
			ret *= faktor[1];
			fehler = 0;
		}else
			fehler = 1;
		return ret;
	}
}

class ZaehlerModbusTcpException extends RuntimeException{
	ZaehlerModbusTcpException(){
		super("Kein Register für die Leistung (regP) definiert.");
	}
}

