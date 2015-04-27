import java.net.URL;


public interface WebCrawlerInterface {

	
	public void crawl(URL currentPageURL, String outputFileName);
	
	public default boolean search(URL urlToSearch) {
		return true;
	}
	
	
}
