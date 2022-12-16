# miniEm
ein kleiner Energiemanager (PV-Überschussladen, steuern der Heizung und Warmwasserbereitung)

**miniEm** ist ein Javaprogramm das auf der Konsole läuft. Es kann z.B. auf einem PC oder auch einem Sever gestartet werden (getestet auf XUbuntu 22.04).


Alle Dateien und Verzeichnisse in ein Verzeichnis kopieren.

Wenn nötig alte Class-Dateien löschen - `rm *.class`

Kompilieren ohne Debuginformationen - `javac -classpath .:lib/jlibmodbus.jar:lib/jssc.jar:lib/json-20211205.jar -g:none *.java`

Programm starten mit `java  -classpath .:lib/jlibmodbus.jar:lib/jssc.jar:lib/json-20211205.jar "Main"` oder

jar-Datei erzeugen - `jar cvmf META-INF/MANIFEST.MF miniEm.jar *.class`

Datei ausführen - `java -jar miniEm.jar`

Einstellungen werden in der Datei `miniEm.json` vorgenommen. Beschreibung in der `Datei Einstellungen.txt.
