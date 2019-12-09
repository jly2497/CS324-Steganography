package main;

import javax.servlet.http.*;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

@WebServlet("/encode_hdlr")
@MultipartConfig(fileSizeThreshold=1024*1024,maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class Encode_hdlr extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public Encode_hdlr() {
		super();
	}
	
	//Handle POST request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException,FileNotFoundException {  
		
		HttpSession session = request.getSession(true);
		session.setAttribute("ErrorMessage","");
		session.setAttribute("FileName","");
		
		ServletLogger.log(this,"Handling POST request:\n" + attrToString(request));
		
		
		if (requestHandler(request)) {
			//response.setContentType("text/html");
			
			Part filePart = request.getPart("UploadFile");
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
			
			String path = System.getProperty("user.dir") + "/WebContent/web/images/tmp/tmp.png";
			ServletLogger.log(this,"Uploading image to: " + path);
			filePart.write(path);
			
			if (request.getParameter("TextOrImage").equals("text")) {
				String text = request.getParameter("TextToEnc");
				ServletLogger.log(this,"Encode Text selected, processing text: " + text);
				Steganography encoder = new Steganography();
				
				encoder.encode(System.getProperty("user.dir") + "/WebContent/web/images/tmp", 
					"tmp", "png","out", text);
				ServletLogger.log(this,"Steganography complete, exiting Encode_hdlr");
				
			} else {				//Image into image encoder
				Part hide = request.getPart("UploadToEnc");
				String hideName = Paths.get(hide.getSubmittedFileName()).getFileName().toString();
				ServletLogger.log(this,"Encode Image selected, processing image: " + hideName);
				
				String hidePath = System.getProperty("user.dir") + "/WebContent/web/images/tmp/tmp2.png";
				
				ServletLogger.log(this,"Uploading secret image to: " + hidePath);
				hide.write(hidePath);
				
				StegoCodec encoder = new StegoCodec();
				encoder.encodeImage(path, hidePath);
				ServletLogger.log(this,"Steganography complete, exiting Encode_hdlr");
			}
			session.setAttribute("ImageOutput", "Encoded");
			
			//ServletLogger.log(this,"Steganography Complete- redirecting to home.");
			//response.sendRedirect(request.getContextPath());
			//return;
			
			
			//PrintWriter out = response.getWriter();
			
			/*
			
			String docType = "<!DOCTYPE html>";
			out.print(docType 
					+ "	<html>\n"
					+ " <head><title>Encode Handler</title>\n"
					+ "		<link href='https://fonts.googleapis.com/css?family=Open+Sans&amp;display=swap' rel='stylesheet'>\n"
					+ "		<link rel='stylesheet' type='text/css' href='web/style/style.css'>\n"
					+ "	</head>\n"
					+ " <body bgcolor=\"#f0f0f0\">\n"
					+ "		<div class='imgContainer'>"
					+ "			<img src='./encoded.png' width='200'></img>"
					+ "		</div>"
					+ "		<button>Return</button>"
					+ "		<button>Replay</button>"
					+ "		<button>View Logs</button>"
					+ "		<button>Download</button>"
			);
			
			out.print("</body>\n"
					+ "</html>\n");*/
			//out.close();
			//out.flush();
			
			response.sendRedirect(request.getContextPath());
		} else {
			session.setAttribute("EncodedOutput", "");
			ServletLogger.log(this,"User input validation failed- redirecting to home.");
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		
		//String description = request.getParameter("description");
		
		
		
		
		//
		
		//InputStream fileContent = filePart.getInputStream();
		//Path target = Paths.get("/Desktop/");
		
		//File image = new File("MyFile.txt");
		//Files.copy(fileContent, target, StandardCopyOption.REPLACE_EXISTING);
		
		
		//final String absolutePath = image.getAbsolutePath();
		//log.debug(absolutePath);
		
		//Steganography encoder = new Steganography();
		
		
		
		/*if (ServletFileUpload.isMultipartContent(request)) {
			out.print(docType 
					+ "<html>\n"
					+ "  <head><title>Encode Handler</title></head>\n"
					+ "  <body bgcolor=\"#f0f0f0\">\n"
					+ "	   <p>Error, file not uploaded.</p>"
					+ "  </body>\n"
					+ "</html>\n");
			return;
		} else {*/
			//DiskFileItemFactory factory = new DiskFileItemFactory();
			//factory.setSizeThreshold(FILE_SIZE);
			//factory.setRepository(new File("/Desktop"));
			
			
	}
	
	//Get file extension
	public String getExtension(String name) {
		String[] arr = name.split("\\.",0);
		return arr[arr.length - 1];
	}
	
	//Checks if all user inputs are valid to process
	public boolean requestHandler(HttpServletRequest request) throws IOException, ServletException {
		
		HttpSession session = request.getSession(true);
		
		if (!request.getPart("UploadFile").getSubmittedFileName().equals("")) {
			if (imageHandler(request.getPart("UploadFile"),session)) {
				if (request.getParameter("TextOrImage").equals("image")) { //Handle image encode
					if (!request.getPart("UploadToEnc").getSubmittedFileName().equals("")) {
						if (imageHandler(request.getPart("UploadToEnc"),session)) {
							return true;
						}
					} else {
						session.setAttribute("ErrorMessage", "There must be a file uploaded to encode.");
						ServletLogger.log(this,"Error Message Log: There must be a file uploaded to encode.");
					}
				} else if (request.getParameter("TextOrImage").equals("text")) { //Handle text encode
					if (!request.getParameter("TextToEnc").equals("") || request.getParameter("TextToEnc").length() > 500) {
						return true;
					} else {
						session.setAttribute("ErrorMessage", "Text within the text area must be within 0 to 500 characters.");
						ServletLogger.log(this,"Error Message Log: Text within the text area must be within 0 to 500 characters.");
					}
				} else {
					session.setAttribute("ErrorMessage", "Image/Text Encode error.");
					ServletLogger.log(this,"Error Message Log: Image/Text Encode error.");
				}
			}
		} else {
			session.setAttribute("ErrorMessage", "There must be a file uploaded to perform encoding on.");
			ServletLogger.log(this,"Error Message Log: There must be a file uploaded to perform encoding on.");
		}
		return false;
	}
	
	//Checks the file type and file size of a part
	public boolean imageHandler(Part image, HttpSession session) throws IOException, ServletException {
		
		String[] split = image.getSubmittedFileName().split("\\.",0);
		String extension = split[split.length - 1];
		
		if (extension.equalsIgnoreCase("png")||extension.equalsIgnoreCase("jpg")||extension.equalsIgnoreCase("jpeg")) {
			if (image.getSize() < 1024 * 1024 * 5 && image.getSize() > 1024) {
				return true;
			} else {
				session.setAttribute("ErrorMessage","Image must be between 1KB to 5MB in size.");
			}
		} else {
			session.setAttribute("ErrorMessage","File must be an accepted image format: .png, .jpg/.jpeg.");
		}
		
		return false;
	}
	
	//Prints the form attributes
	public String attrToString(HttpServletRequest request) throws IOException, ServletException {
		
		Enumeration<String> names = request.getParameterNames();
		String out = "";
		
		out += request.getPart("UploadFile").getName() +  " : " + request.getPart("UploadFile").getSubmittedFileName() + "\n";
		out += request.getPart("UploadToEnc").getName() +  " : " + request.getPart("UploadToEnc").getSubmittedFileName() + "\n";

		while(names.hasMoreElements()) {
			String paramName = names.nextElement();
			String value = request.getParameter(paramName);
			
			if (value != null)
				out += paramName +  " : " + value + "\n";
		}
		return out;
	}
	/*
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	*/
}
