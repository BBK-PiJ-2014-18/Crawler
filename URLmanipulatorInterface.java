import java.net.URL;


/**
 * @author markkingsbury
 *
 */
public interface URLmanipulatorInterface {
	
	/**
	 * @param startingURL
	 * @return
	 */
	public URL makeBase(URL startingURL);

	/**
	 * @param scrapedString
	 * @param base
	 * @return
	 */
	public URL makeFullUrl(String scrapedString, URL base);
	
	/**
	 * @param startingURL
	 * @return
	 */
	public URL standardizeURL(URL startingURL);
		
	/**
	 * @param report
	 */
	public void writeToExceptionLog(String report);
}
