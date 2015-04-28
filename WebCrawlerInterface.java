import java.net.URL;


/**
 * @author markkingsbury
 *
 */
public interface WebCrawlerInterface {

	
	/**
	 * @param currentPageURL
	 * @param outputFileName
	 */
	public void crawl(URL currentPageURL, String outputFileName);
	
	/**
	 * @param urlToSearch
	 * @return
	 */
	public default boolean search(URL urlToSearch) {
		return true;
	}
	
	
}
