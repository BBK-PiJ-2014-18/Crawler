
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WebCrawlerTest {

	
	public URL helpMakeURL(String str) {
		URL result = null;
		try {
			result = new URL(str);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String helpReadDataFileLine(int lineNumber) {	
		return "BASE = http://www.dcs.bbk.ac.uk/";
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrl() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/"));
		String actual = helpReadDataFileLine(3);
		String expected = "BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
}
