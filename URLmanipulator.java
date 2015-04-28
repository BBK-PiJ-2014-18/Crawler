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

/**
 * An instance of URLmanipulatorInterface, that helps structure and normalise URLs.
 * 
 * @author markkingsbury
 */
public class URLmanipulator implements URLmanipulatorInterface {

	private static final String ATTRIBUTES_FILE = "./Data/crawlattributes.txt";

	public URL makeBase(URL startingURL) {
		String protocol = startingURL.getProtocol(); 	// e.g. "http"
		String host = startingURL.getHost(); 			// e.g. "www.dcs.bbk.ac.uk"
		String file = startingURL.getFile();			// e.g. "/seminars/index-external.php"
		file = addFinalForwardSlash(file);
		//delete any file name from end of path
		file = file.substring(0, file.lastIndexOf('/') + 1);
		URL result = null;
		try {
			result = new URL(protocol, host, file);
		} catch (MalformedURLException e) {
			writeToExceptionLog("MalformedURLException in makeBase(). startingURL:" + startingURL.toString());			
		}
		result = normalizeURL(result);
		return result;
	}	

	/**
	 * Adds a final forward slash to the file element of a URL where needed to help create a valid base URL.
	 * @param file, the file element of a URL
	 * @return the file element that will be part of the base URL
	 */
	private String addFinalForwardSlash(String file) {
		//if there is no file (e.g file = "" so address is just host)
		if(file == "") {
			file = "/";
		}
		//if file is not just query && doesn't have a 'real' file name (rather than pathy file name) or a query at the end
		if(file.charAt(0) != '?' && !file.substring(file.lastIndexOf('/'), file.length()).contains(".")
				&& !file.substring(file.lastIndexOf('/'), file.length()).contains("?")) {
			file = file + '/';
		}	
		return file;
	}
	
	public URL makeFullUrl(String scrapedString, URL base) {
		URL result = null;
		try {
			result = new URL(base, scrapedString);
		} catch (MalformedURLException e) {
			writeToExceptionLog("MalformedURL in makeFullUrl(). Base:" + base.toString() + " + String: " + scrapedString);
		}
		return result;
	}
	
	public URL standardizeURL(URL startingURL) {
		String protocol = startingURL.getProtocol(); 	// e.g. "http"
		String host = startingURL.getHost(); 			// e.g. "www.dcs.bbk.ac.uk"
		String file = startingURL.getFile();			// e.g. "/seminars/index-external.php"
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
	

	/**
	 * Part of the URL standardization behavior (deals with duplicate '/'s in URL)
	 * @param dirtyURL, a URL that may contain duplicate '/'s
	 * @return a normalized URL without duplicate '/'s
	 */
	private URL normalizeURL(URL dirtyURL) {
		URL result = null;
		URL noSpacesURL = fixSpacesURL(dirtyURL);
		try {
			URI temp = noSpacesURL.toURI();
			temp = temp.normalize();
			result = temp.toURL();
		} catch (URISyntaxException ex) {
			writeToExceptionLog("URISyntaxException in normalizeURL(). Dirty URL:" + dirtyURL);		
		} catch (MalformedURLException ex) {
			writeToExceptionLog("MalformedURLException in normalizeURL(). Dirty URL:" + dirtyURL);
		}
		return result;
	}
	
	/**
	 * Part of the URL standardization behavior (deals with spaces in URL)
	 * @param dirtyURL, which may contain spaces
	 * @return a URL with spaces replaced by %20
	 */
	private URL fixSpacesURL(URL dirtyURL) {
		URL result = null;
		String strWithSpaces = dirtyURL.toString();
		try {
			result = new URL(strWithSpaces.replaceAll(" ", "%20"));
		} catch (MalformedURLException e) {
			writeToExceptionLog("MalformedURLException in fixSpacesURL(). Dirty URL:" + dirtyURL);
			e.printStackTrace();
		}
		return result;
	}
		
	public void writeToExceptionLog(String report) {
		PrintWriter out = null;
		File file = new File(ATTRIBUTES_FILE);
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
