package main;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import com.jcraft.jsch.*;
import java.io.*;
import java.util.Iterator;
import java.util.Properties;

public class SSHpluto {
	private JSch jsch;
	private Session session;
	Properties prop;
	
	
	public SSHpluto() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("sdojfh9er3904*(^&nd8w7ax2qx89"); 
		
		jsch = new JSch();
		prop = new Properties();
		
		try {
			InputStream in = new FileInputStream("config.properties");
			InputStream knownHosts = new FileInputStream("known_hosts");
			
			prop.load(in);
			String host = prop.getProperty("host");
			String username = prop.getProperty("username");
			
			try {
				String publicKey = prop.getProperty("publickey");
				jsch.setKnownHosts(knownHosts);
				
				Session session = jsch.getSession(username,host,22);
				session.setPassword(encryptor.decrypt(prop.getProperty("password")));
				session.connect();
			
				ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
				sftpChannel.connect();
			
				@SuppressWarnings("unchecked")
				Iterator<String> it = sftpChannel.ls("").iterator();
			
				while (it.hasNext()) 
					System.out.println(it.next());
			} catch (JSchException e) {
				ServletLogger.log(this,"SSH connection to " + host + " failed: " + e.toString());
				e.printStackTrace();
			} catch (SftpException e) {
				ServletLogger.log(this,"FTP connection " + host + " failed: " + e.toString());
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		SSHpluto pluto = new SSHpluto();		
	}

}
