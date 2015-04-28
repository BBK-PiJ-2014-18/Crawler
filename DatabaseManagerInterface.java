import java.net.URL;

/**
 * An interface for a DatabaseManager that a WebCrawler uses to manage read/write activity.
 * 
 * @author markkingsbury
 */
public interface DatabaseManagerInterface {
	
	/**
	 * Saves details of starting point of crawl in a log file (that file is also place where any exception reports are logged)
	 * @param startingURL, the very first URL provided by programmer as a starting point for the crawl.
	 * @param base, the calculated base of the starting URL.
	 */
	public void saveCrawlAttributes (URL startingURL, URL base);

	/**
	 * Set up the temp file that will hold all indexed URLs (and the depth status of those URLs)
	 * @param startingURL, the first URL to be written to the file (with depth 0).
	 */
	public void intitalizeTempFile(URL startingURL);
	
	/**
	 * Writes a URL and its depth/priority to the temp file (is it is not already in the file)
	 * @param priority, the depth (aka priority) of the URL
	 * @param urlToWrite, the URL to be written, if it is not already in the file
	 * @return true if the URl is written, false if it is not (i.e. was already present in file).
	 */
	public boolean writeURLtoTemp(int priority, URL urlToWrite);
	
	/**
	 * Writes URLs that pass the search critera to file
	 * @param urlToWrite, the URL to be written (one that passed the search() test
	 * @param outputFileName, the name of the output file
	 */
	public void writeToSearchResultsFile(URL urlToWrite, String outputFileName);
	
	/**
	 * Finds the next URL to be crawled, a URL with the lowest depth number (greater than 0)in the temp file that also has depth less than maxDepth. 
	 * @param maxDepth, the maximum depth the crawler should go to.
	 * @return, the URL with the lowest depth number (greater than 0)in the temp file that also has depth less than maxDepth.
	 */
	public URL getNextURL(int maxDepth);

	
}
