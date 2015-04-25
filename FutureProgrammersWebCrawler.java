import java.net.URL;


public class FutureProgrammersWebCrawler extends WebCrawler implements WebCrawlerInterface {
	
	@Override
	public boolean search(URL urlToSearch) {
		System.out.println("I am a future programmer.");
		return true;
	}
	
}
