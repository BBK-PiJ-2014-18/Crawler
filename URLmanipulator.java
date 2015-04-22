import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URLmanipulator {
	
	private static final String LOG_FILE = "./Crawler/Data/crawllog.txt";

	public URL makeBase(URL startingURL) {
		String protocol = startingURL.getProtocol(); 	// e.g. "http"
		String host = startingURL.getHost(); 			// e.g. "www.dcs.bbk.ac.uk"
		String file = startingURL.getFile();			// e.g. "/seminars/index-external.php"
		file = fixFrontAndBackForwardSlashOnFile(file);
		//delete any file name from end of path
		file = file.substring(0, file.lastIndexOf('/') + 1);
		URL result = null;
		try {
			result = new URL(protocol, host, file);
		} catch (MalformedURLException e) {
			writeToExceptionLog("MalformedURLException in makeBase(). startingURL:" + startingURL.toString());			
			e.printStackTrace();
		}
		result = normalizeURL(result);
		return result;
	}	
	
	public URL standardizeURL(URL startingURL) {
		String protocol = startingURL.getProtocol(); 	// e.g. "http"
		String host = startingURL.getHost(); 			// e.g. "www.dcs.bbk.ac.uk"
		String file = startingURL.getFile();			// e.g. "/seminars/index-external.php"
		file = fixFrontAndBackForwardSlashOnFile(file);
		URL result = null;
		try {
			result = new URL(protocol, host, file);
		} catch (MalformedURLException e) {
			writeToExceptionLog("MalformedURLException in standardizeURL(). startingURL:" + startingURL.toString());
			return null;
		}
		result = normalizeURL(result);		
		return result;
	}
	
	private String fixFrontAndBackForwardSlashOnFile(String file) {
		//if there is no file (e.g file = "" so address is just host)
		//or file part is just query (sitting right on top of host)
		if(!file.contains("/")) {
			file = "/" + file;
		}
		//if there is no ultimate "file name" or query part add "/" (e.g. just "/seminars" becomes "/seminars/"
		if(!file.substring(file.lastIndexOf('/'), file.length()).contains(".")
				&& !file.substring(file.lastIndexOf('/'), file.length()).contains("?")) {
			file = file + '/';
		}	
		return file;
	}
	
	//remove any  duplicate "/"s that have got in
	private URL normalizeURL(URL dirtyURL) {
		URL result = null;
		try {
			URI temp = dirtyURL.toURI();
			temp = temp.normalize();
			result = temp.toURL();
			//what order should these catches be in??
		} catch (URISyntaxException ex) {
			writeToExceptionLog("URISyntaxException in normalizeURL(). Dirty URL:" + dirtyURL);		
		} catch (MalformedURLException ex) {
			writeToExceptionLog("MalformedURLException in normalizeURL(). Dirty URL:" + dirtyURL);
		}
		return result;
	}
	
	public URL makeFullUrl(String scrapedString, URL base) {
		URL result = null;
		//scraped string can be "" when relative URL is just meant to take to <base url
		if(!scrapedString.equals("") && scrapedString.charAt(0) == '/') {
			scrapedString = scrapedString.substring(1, scrapedString.length());
		}
		try {
			result = new URL(base, scrapedString);
		} catch (MalformedURLException e) {
			writeToExceptionLog("MalformedURL in makeFullUrl(). Base:" + base.toString() + " + String: " + scrapedString);
		}
		return result;
	}
	
	private void writeToExceptionLog(String report) {
		PrintWriter out = null;
		File file = new File(LOG_FILE);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			out.write(report + "\n");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file (not found) " + file + ".");
		} catch (IOException e) {
			System.out.println("Cannot write to file (IO exception) " + file + ".");
			e.printStackTrace();
		} finally {
			out.close();
		}	
	}	
}
