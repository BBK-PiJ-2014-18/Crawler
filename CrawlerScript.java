import java.net.MalformedURLException;
import java.net.URL;


public class CrawlerScript {

	private static final String BBC_URL = "http://www.bbc.co.uk/";
	private static final String BBK_URL = "http://www.dcs.bbk.ac.uk/";
	
	public CrawlerScript() {
		
	}
	
	
	public static void main (String[] args) {
		CrawlerScript cs = new CrawlerScript();
		cs.launch();
	}
	
	private void launch() {
		WebCrawlerInterface wc = new WebCrawler();
		URL bbcURL = null;
		try {
			bbcURL = new URL(BBC_URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wc.crawl(bbcURL, "crawlsearchresults");
		WebCrawlerInterface fpwc = new FutureProgrammersWebCrawler();
		URL bbkURL = null;
		try {
			bbkURL = new URL(BBK_URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fpwc.crawl(bbkURL, "crawlsearchresults");
	}
	
	
	
	
	
	
	
//	validate overwrite of database file with:
//		
//		if(bFile.exists()) {
//			System.out.println("Do you want to overwrite " + bFile + "? y/n");
//			String str = System.console().readLine();
//			if (str.equals("n")) {
//				System.out.println("File not copied");
//				return;
//			}
//		}	
	
	
}
