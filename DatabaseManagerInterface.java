import java.net.URL;

/**
 * @author markkingsbury
 *
 */
public interface DatabaseManagerInterface {
	
	/**
	 * @param startingURL
	 * @param base
	 */
	public void saveCrawlAttributes (URL startingURL, URL base);

	/**
	 * @param startingURL
	 */
	public void intitalizeTempFile(URL startingURL);
	
	/**
	 * @param priority
	 * @param urlToWrite
	 * @return
	 */
	public boolean writeURLtoTemp(int priority, URL urlToWrite);
	
	/**
	 * @param urlToWrite
	 * @param outputFileName
	 */
	public void writeToSearchResultsFile(URL urlToWrite, String outputFileName);
	
	/**
	 * @param maxDepth
	 * @return
	 */
	public URL getNextURL(int maxDepth);

	
}
