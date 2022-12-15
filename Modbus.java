import de.re.easymodbus.modbusclient.*;

public class ModbusTCP{
	int fehler = 0;
	ModbusClient modbusClient;
	
	public ModbusTCP(String ip, int port){
		modbusClient = new ModbusClient(ip, port);
	}
	
	public int getFehler(){
		return fehler;
	}
	
	public int[] getModbusRegisterInt(int reg, int anzahl){
		fehler = 0;
		int[] ret = null;

		try{
			if (!modbusClient.isConnected()) {
				modbusClient.Connect();
			}
			ret = modbusClient.ReadHoldingRegisters(reg, anzahl);
		}catch (Exception e){
			fehler = 1;
			Log.err("Modbus.getModbusRegisterInt: " + e);
		} finally {
			try {
				modbusClient.Disconnect();
			}catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public int getModbusRegisterInt(int reg){
		return getModbusRegisterInt(reg, 1)[0];
	}
}
