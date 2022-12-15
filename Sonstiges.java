import java.io.*;
import java.nio.file.*;
import org.json.*;

public class Sonstiges{
	public static String eingabedatei;
	public static String ausgabedatei;
	public static String eingabe;
	public static String ausgabe;
	
	public static int find(int[] a, int w){
		int i = 0;
		while(i < a.length && a[i] <= w)
			i++;
		if(i > 0)
			i--;
		return i;
	}
	
	public static int[] getIntArray(JSONArray a){
		int[] ar = new int[a.length()];
		for(int i = 0; i < a.length(); i++)
			ar[i] = a.getInt(i);
		return ar;
	}
	
	public static void eingabe_lesen(){
		try{
			eingabe = Files.readString(Path.of(eingabedatei));
		}catch(Exception e){
			Log.err("Sonstiges.eingabe_lesen: " + e);
			eingabe = "";
		}
	}
	
	public static void ausgabe_lesen(){
		try{
			ausgabe = Files.readString(Path.of(ausgabedatei));
		}catch(Exception e){
			Log.err("Sonstiges.ausgabe_lesenn: " + e);
		}
	}
	
	public static void ausgabe_schreiben(){
		try{
			Files.writeString(Path.of(ausgabedatei), ausgabe);
		}catch(Exception e){
			Log.err("Sonstiges.ausgabe_schreiben: " + e);
		}
	}
	
	public static int[] eintrag_lesen(String[] s){
		int[] erg = new int[s.length];
		
		for(int i = 0; i < s.length; i++){
			int p1 = eingabe.indexOf(s[i] + ":");
			if(p1 > -1){
				p1 += s[i].length() + 1;
				int p2 = eingabe.indexOf("\n", p1);
				if(p2 == -1)
					p2 = eingabe.length();
				erg[i] = Integer.parseInt(eingabe.substring(p1, p2));
			}else
				erg[i] = -1;
		}
		return erg;
	}
	
	public static void eintrag_schreiben(String[] s, String[] w){
		for(int i = 0; i < s.length; i++){
			int p1 = ausgabe.indexOf(s[i] + ":");
			if(p1 == -1){
				if(ausgabe.length() > 0 && !ausgabe.endsWith("\n"))
					ausgabe += "\n";
				ausgabe += s[i] + ":" + w[i];
			}else{
				p1 += s[i].length() + 1;
				int p2 = ausgabe.indexOf("\n", p1);
				if(p2 == -1)
					p2 = ausgabe.length();
				String s1 = ausgabe.substring(0, p1);
				String s2 = ausgabe.substring(p2);
				ausgabe = s1 + w[i] + s2;
			}
		}
	}
	
	public static void eintrag_schreiben(String[] s, int[] w){
		String[] sa = new String[w.length];
		for(int i = 0; i < w.length; i++){
			sa[i] = Integer.toString(w[i]);
		}
		eintrag_schreiben(s, sa);
	}
}
