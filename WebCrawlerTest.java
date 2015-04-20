import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class WebCrawlerTest {

	private static final String DATABASE_FILE = "./Crawler/crawloutput.txt";
	
	// RULE
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	// HELPER METHODS
	
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
	
	@Test
	public void testStartingURLMayNotBeNull() {
		WebCrawler wc = new WebCrawler();
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("URL may not be null");
		wc.crawl(null);
	}
	
	//come back to this when set up wider sequence
	@ Ignore @Test
	public void testStartingURLValidButDoesNotExist() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/monkeys.html"));
	}
	
	//tests on cleaning startingURL so saves clean for checking of duplicates

	@Test
	public void testNormaliseStartingURL() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk//about"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);		
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNormaliseStartingURLjustHost() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);		
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNormaliseStartingURLjustHostwithForwardSlashAtEnd() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);		
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMakeComplexStatingURLsaveClean() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);		
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNormaliseStartingURLwithFileNameAtEndOfURL() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk//about/map.php"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about/map.php";
		assertEquals(expected, actual);		
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMakeComplexStatingURLsaveCleanwithFileNameAtEndOfURL() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/map.php"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about/map.php";
		assertEquals(expected, actual);		
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	
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
	
	@Test
	public void testLongerURLBothStartingAndGettingBase() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/courses/msccs/entry.php"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/courses/msccs/entry.php";
		assertEquals(expected, actual);
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/courses/msccs/";
		assertEquals(expected, actual);
	}

	// tests removing a reference element in a URL (i.e. to remove a #nav or #content)
	
	@Test
	public void testRemoveReferenceFromURLnoPathOnlyHost() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/#content"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}	

	@Test
	public void testRemoveReferenceFromURLwithHostAndPatht() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/news/index.php#n708"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/news/index.php";
		assertEquals(expected, actual);
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/news/";
		assertEquals(expected, actual);
	}

	//tests keeping the "query part" of the URL	
	
	@Test
	public void testDealWithQueryPartOfURLexampleOne() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("https://www.dcs.bbk.ac.uk/courses/bbkquality.php?from=pgcourses"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = https://www.dcs.bbk.ac.uk/courses/bbkquality.php?from=pgcourses";
		assertEquals(expected, actual);
		actual = helpReadDataFileLine(3);
		expected = "START BASE = https://www.dcs.bbk.ac.uk/courses/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDealWithQueryPartOfURLexampleTwo() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://computingcareers.acm.org/?page_id=4"));
		String actual = helpReadDataFileLine(2);
		String expected = "STARTING URL = http://computingcareers.acm.org/?page_id=4";
		assertEquals(expected, actual);
		actual = helpReadDataFileLine(3);
		expected = "START BASE = http://computingcareers.acm.org/";
		assertEquals(expected, actual);
	}
	
	
}



