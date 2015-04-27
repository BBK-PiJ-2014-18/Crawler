import java.net.URL;


public interface WebCrawlerInterface {

	
	public void crawl(URL currentPageURL);
	
	public default boolean search(URL urlToSearch) {
		return true;
	}
	
	
}
