import java.net.URL;


/**
 * Interface for a WebCrawler to scrape URLs given a starting URL and provide a default implementation of the search method of those URLs.
 * @author markkingsbury
 */
public interface WebCrawlerInterface {

	
	/**
	 * Crawl across URLs, scraping further URLs until limits are reached. Recording those (non-duplicate) URLs that meet the search criteria.
	 * @param currentPageURL, the URL to scrape.
	 * @param outputFileName, where URLs that meet the search criteria are stored.
	 */
	public void crawl(URL currentPageURL, String outputFileName);
	
	/**
	 * Default implementation of search.
	 * @param urlToSearch, the url that will be tested against the criteria
	 * @return true if the search critera are met, otherwise false.
	 */
	public default boolean search(URL urlToSearch) {
		return true;
	}
	
	
}
