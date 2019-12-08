package main;

import java.io.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

public class ServletLogger {
	public static void log(Object o, String msg) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		try {
			File f = new File(System.getProperty("user.dir") + "/" + "log.txt");
			//System.out.println(f.exists() + " " + f.getAbsolutePath());
			PrintWriter out = new PrintWriter(new FileOutputStream(f, true)); 
			
			out.println(dtf.format(now) + " " + o.getClass().getSimpleName() + " -> " + msg);
			out.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
