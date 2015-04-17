import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WebCrawlerTest {

	private static final String DATABASE_FILE = "./Crawler/crawloutput.txt";
	
	private URL helpMakeURL(String str) {
		URL result = null;
		try {
			result = new URL(str);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private String helpReadDataFileLine(int lineNumber) {	
		File file = new File(DATABASE_FILE);
		BufferedReader in = null;
		String result = null;
		try {
			in = new BufferedReader(new FileReader(file));
			String line;
			int count = 0;
			while((line = in.readLine()) != null) {
				count ++;
				if(count == lineNumber) {
					result = line;
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File " + file + "does not exist");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeReader(in);
		}
		return result;
	}
	
	private void closeReader(Reader reader) {
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	

	// TESTS START HERE
	
	//getting base ref from a starting URL
	
	@Test
	public void testCreatingBaseUrlFromStartingUrl() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/"));
		String actual = helpReadDataFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlNoFinalFowardSlash() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk"));
		String actual = helpReadDataFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlWithIndex() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/index.php"));
		String actual = helpReadDataFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}

	@Test
	public void testCreatingBaseUrlFromMoreComplexStartingUrl() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/"));
		String actual = helpReadDataFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/seminars/";
		assertEquals(expected, actual);
	}	
	
	@Test
	public void testCreatingBaseUrlFromMoreComplexStartingUrlNoFinalFowardSlash() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars"));
		String actual = helpReadDataFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/seminars/";
		assertEquals(expected, actual);
	}	

	@Test
	public void testCreatingBaseUrlFromMoreComplexStartingUrlWithIndex() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/index-external.php"));
		String actual = helpReadDataFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/seminars/";
		assertEquals(expected, actual);
	}

	@Test
	public void testCreatingBaseUrlFromStartingUrlWithValidDotsInPlaceOtherThanFinalFileName() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/map.php"));
		String actual = helpReadDataFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlWithValidDotsInPlaceAndNoFinalFileName() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/"));
		String actual = helpReadDataFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlWithValidDotsInPlaceAndNoFinalFileNameNoFinalSlash() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about"));
		String actual = helpReadDataFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
}
