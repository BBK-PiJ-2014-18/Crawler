import java.net.URL;

/**
 * Example of how a future programmer would structure things so can add a search() method to existing set-up
 * 
 * @author markkingsbury
 */

public class FutureProgrammersWebCrawler extends WebCrawler implements WebCrawlerInterface {
	
	//an example method to demonstrate where a future programmer would insert their own search function
	@Override
	public boolean search(URL urlToSearch) {
		System.out.println("I am a future programmer.");
		return true;
	}
	
}
