import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.nio.file.*;

public class Log{
	static int loglevel = 0;
	static int[] mw = new int[0];
	static String mwdatei;
	
	public static void setLoglevel(int l){
		loglevel = l;
	}
	
	public static void setMWDateiname(String s){
		mwdatei = s;
	}
	
	public static void err(String n){
		Date datum = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
		System.err.println(format.format(datum) + " Fehler: " + n);
	}
	
	public static void log(String n, int l){
		if(loglevel >= l){
			Date datum = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
			System.out.println(format.format(datum) + " " + n);
		}		
	}
	
	public static void log(String n){
		log(n, 0);
	}
	
	public static void addMW(int w){
		mw = Arrays.copyOf(mw, mw.length + 1);
		mw[mw.length - 1] = w;
	}

	public static void logMW(int l){
		if(loglevel >= l){
			try{
				Date datum = new Date();
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
				String s = "";
				if(new File(mwdatei).length() > 0)
					s += "\n";
				s += format.format(datum);
				for(int i = 0; i < mw.length; i++)
					s += ";" + Integer.toString(mw[i]);
				Files.writeString(Path.of(mwdatei), s, StandardOpenOption.APPEND);
				mw = new int[0];
			}catch(Exception e){
				Log.err("Log.logMW: " + e);
			}
		}		
	}

}
