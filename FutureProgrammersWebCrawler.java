import java.net.URL;

/**
 * @author markkingsbury
 *
 */

public class FutureProgrammersWebCrawler extends WebCrawler implements WebCrawlerInterface {
	
	//an example method to demonstrate where a future programmer would insert their own search function
	@Override
	public boolean search(URL urlToSearch) {
		System.out.println("I am a future programmer.");
		return true;
	}
	
}
