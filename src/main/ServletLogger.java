package main;

import java.io.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

public class ServletLogger {
	
	public static void log(Object o, String msg) throws FileNotFoundException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		PrintWriter out = new PrintWriter(new FileOutputStream(new File("/Users/JoshuaLySoumphont/Desktop/Steganography/log.txt"), true)); 
		
		out.println(dtf.format(now) + " " + o.getClass().getSimpleName() + " -> " + msg);
		out.close();
	}
}
