package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/decode_hdlr")
@MultipartConfig(fileSizeThreshold=1024*1024,maxFileSize=1024*1024*10, maxRequestSize=1024*1024*5*5)
public class Decode_hdlr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Decode_hdlr() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	if (request.getParameter("replay") != null) {
    		ServletLogger.log(this,"Replay clicked, replaying previous steganography: " + attrToString(request));
			HttpSession session = request.getSession(true);
			
			String rStr = (String) session.getAttribute("Replay");
			String[] rArr = rStr.split(", ",0);
			String textOrImage = rArr[1];
			
    	}
    	
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		ServletLogger.log(this,"Entering Decode_hdlr... Checking image for encrypted messages.");
		
		ServletLogger.log(this,"Handling POST request:\n" + attrToString(request));
		
		if (imageHandler(request.getPart("UploadFile"))) {
				
				Part filePart = request.getPart("UploadFile");
				
				ServletLogger.log(this,"Uploaded image validated. Processing possible hidden data...");
				String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
				String tmpPath = System.getProperty("user.dir") + "/WebContent/web/images/tmp/tmp.png";
				ServletLogger.log(this,"Handling POST AJAX request: " + fileName);
				
				filePart.write(tmpPath);
				String path = System.getProperty("user.dir") + "/WebContent/web/images/tmp/";
				
				Decoder dec = new Decoder();

				if (dec.steganographyImage(path + "tmp.png")) {
					session.setAttribute("ImageOutput","DecodedImage");
					ServletLogger.log(this,"Image decrypted.");
					response.sendRedirect(request.getContextPath());
					return;
				} else {
					ServletLogger.log(this,"No hidden image found, attempting text decode.");
					Steganography decoder = new Steganography();
					
					String message = decoder.decode(path, "tmp");
					
					if (message.equals("")) {
						ServletLogger.log(this,"No hidden text found, returning.");
						session.setAttribute("ImageOutput","None");
						response.sendRedirect(request.getContextPath());
					} else {
						ServletLogger.log(this,"Hidden text decrypted: " + message);
						session.setAttribute("ImageOutput","DecodedText");
						session.setAttribute("Message",message);
						response.sendRedirect(request.getContextPath());
					}
				}
		} else {
			session.setAttribute("ImageOutput","");
			ServletLogger.log(this,"Image failed validation, returning.");
			response.sendRedirect(request.getContextPath());
			return;
		}
	}
	private boolean imageHandler(Part image) throws IOException, ServletException {
		if (!image.getSubmittedFileName().equals("")) {
				String[] split = image.getSubmittedFileName().split("\\.",0);
				String extension = split[split.length - 1];
				
				if (extension.equalsIgnoreCase("png")||extension.equalsIgnoreCase("jpg")||extension.equalsIgnoreCase("jpeg"))
					if (image.getSize() < 1024 * 1024 * 10 && image.getSize() > 1024)
						return true;
				return false;
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
}
