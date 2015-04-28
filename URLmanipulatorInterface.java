import java.net.URL;


/**
 * An interface to specify methods that manipulate URLs to help WebCrawler.
 * 
 * @author markkingsbury
 */
public interface URLmanipulatorInterface {
	
	/**
	 * Finds the base element of a URL
	 * @param startingURL, the URL for which we wish to identify the base element.
	 * @return the base URL of the startingURL.
	 */
	public URL makeBase(URL startingURL);

	/**
	 * Makes a full URL from relative/absolute parts. 
	 *  
	 * @param scrapedString, representing a relative URL or a full URL
	 * @param base, the base URL for the page the scraped string came from.
	 * @return the full URL, either direct from the scraped URL (if it is absolute) or from the combination of the string 
	 * and the base (if the string is relative). 
	 */
	public URL makeFullUrl(String scrapedString, URL base);
	
	/**
	 * Accesses a range of functions to ensure a well structured URL
	 * @param startingURL, the URL to be standardized.
	 * @return the standardized version of the provided URL.
	 */
	public URL standardizeURL(URL startingURL);
		
	/**
	 * Helps writes any malformedURL exception to a log file.
	 * @param report, the report to be logged. 
	 */
	public void writeToExceptionLog(String report);
}
