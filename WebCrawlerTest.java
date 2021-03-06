import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;


import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * JUnit testing files for WebCrawler application CW5
 * 
 * @author markkingsbury
 */
public class WebCrawlerTest {

	private static final String TEMP_FILE = "./Data/crawltemp.txt";
	private static final String ATTRIBUTES_FILE = "./Data/crawlattributes.txt";
	
	@BeforeClass
	public static void doOnceAtStartOfTests() {
		File f = new File("./Crawler");
		f.mkdir();
		f = new File("./Crawler/TestHtml");
		f.mkdir();
	}
	
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
	
	private String helpReadAttributesFileLine(int lineNumber) {	
		File file = new File(ATTRIBUTES_FILE);
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
			System.out.println("File " + file + " does not exist");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeReader(in);
		}
		return result;
	}
	
	private String helpReadTempFileLine(int lineNumber) {	
		File file = new File(TEMP_FILE);
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
			System.out.println("File " + file + " does not exist");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeReader(in);
		}
		return result;
	}
	
	private void helpMakeHTMLTestFile(String base, String link) {
		PrintWriter out = null;
		File file = new File("./Crawler/TestHtml/test.html");
		try {
			out = new PrintWriter(file);
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println(base);
			out.println("<title>Find Links</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<p>LINKS TO FIND ARE IN THE TEXT BELOW</p>");
			out.println(link);
			out.println("<p>EOF</p>");
			out.println("</body>");
			out.println("</html>");;
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
		} finally {
			out.close();
		}
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
		WebCrawlerInterface wc = new WebCrawler(2,2);
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("URL may not be null");
		wc.crawl(null, "crawlsearchresults.txt");
	}
	
	//come back to this when set up wider sequence
	@Test
	public void testStartingURLValidButDoesNotExist() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/monkeys.html"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(5);
		String expected = "PAGE NOT FOUND: http://www.dcs.bbk.ac.uk/monkeys.html";
		assertEquals(expected,actual);		
	}
	
	//tests on cleaning startingURL so saves clean for checking of duplicates

	@Test
	public void testNormaliseStartingURL() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk//about"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNormaliseStartingURLjustHost() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNormaliseStartingURLjustHostwithForwardSlashAtEnd() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMakeComplexStatingURLsaveClean() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNormaliseStartingURLwithFileNameAtEndOfURL() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk//about/map.php"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about/map.php";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMakeComplexStatingURLsaveCleanwithFileNameAtEndOfURL() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/map.php"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about/map.php";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	//getting base ref from a starting URL
	
	@Test
	public void testCreatingBaseUrlFromStartingUrl() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlNoFinalFowardSlash() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDiffStartingURL() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.bbc.co.uk/"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.bbc.co.uk/";
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlWithIndex() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/index.php"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}

	@Test
	public void testCreatingBaseUrlFromMoreComplexStartingUrl() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/seminars/";
		assertEquals(expected, actual);
	}	
	
	@Test
	public void testCreatingBaseUrlFromMoreComplexStartingUrlNoFinalFowardSlash() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/seminars/";
		assertEquals(expected, actual);
	}	

	@Test
	public void testCreatingBaseUrlFromMoreComplexStartingUrlWithIndex() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/index-external.php"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/seminars/";
		assertEquals(expected, actual);
	}

	@Test
	public void testCreatingBaseUrlFromStartingUrlWithValidDotsInPlaceOtherThanFinalFileName() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/map.php"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlWithValidDotsInPlaceAndNoFinalFileName() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlWithValidDotsInPlaceAndNoFinalFileNameNoFinalSlash() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongerURLBothStartingAndGettingBase() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/courses/msccs/entry.php"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/courses/msccs/entry.php";
		assertEquals(expected, actual);
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/courses/msccs/";
		assertEquals(expected, actual);
	}

	// tests removing a reference element in a URL (i.e. to remove a #nav or #content)
	
	@Test
	public void testRemoveReferenceFromURLnoPathOnlyHost() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/#content"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}	

	@Test
	public void testRemoveReferenceFromURLwithHostAndPatht() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/news/index.php#n708"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/news/index.php";
		assertEquals(expected, actual);
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/news/";
		assertEquals(expected, actual);
	}

	//tests keeping the "query part" of the URL	
	
	@Test
	public void testDealWithQueryPartOfURLexampleOne() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("https://www.dcs.bbk.ac.uk/courses/bbkquality.php?from=pgcourses"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = https://www.dcs.bbk.ac.uk/courses/bbkquality.php?from=pgcourses";
		assertEquals(expected, actual);
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = https://www.dcs.bbk.ac.uk/courses/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDealWithQueryPartOfURLexampleThree() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		wc.crawl(helpMakeURL("http://jobs.bbk.ac.uk/fe/tpl_birkbeckcollege01.asp?s=4A515F4E5A565B1A&jobid=55240,0202886098&key=85175678&c=606965128771&pagestamp=sedzccxubhnvbfauxj"), "crawlsearchresults.txt");
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://jobs.bbk.ac.uk/fe/tpl_birkbeckcollege01.asp?s=4A515F4E5A565B1A&jobid=55240,0202886098&key=85175678&c=606965128771&pagestamp=sedzccxubhnvbfauxj";
		assertEquals(expected, actual);
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://jobs.bbk.ac.uk/fe/";
		assertEquals(expected, actual);
	}
	
	//SCRAPING URLS
	
	@Test
	public void testScrapeFirstURLfromFile() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("", "<a href=\"https://www.dcs.bbk.ac.uk/courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstBasePlusURLfromFile() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}

	@Test
	public void testFullTestHTMLfileWhereWeFindBase() {
		WebCrawlerInterface wc = new WebCrawler(5,5);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/UseBaseCorrect.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"file:Example/axe.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "0,\"file:Example/box.html\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstBaseWithCapsONE() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<BASE href=\"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstBaseWithCapsTWO() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<bAsE href=\"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstBaseWithSpacesONE() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<  BASE href=\"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstBaseWithSpacesTWO() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<  B A S E   h  r  e  f  =   \"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstLinkWithCapsONE() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "<A href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstLinkWithCapsTWO() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "<A hreF=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstLinkWithSpacesONE() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "< a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstLinkWithSpacesTWO() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "< a href= \"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}

	@Test
	public void testFullTestHTMLfileProblemFormatsThatHasTrickyFormatsOfLinks() {
		WebCrawlerInterface wc = new WebCrawler(15,15);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/ProblemFormats.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"file:./Crawler/TestHtml/axe.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "0,\"file:./Crawler/TestHtml/box.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(5);
		expected = "0,\"file:./Crawler/TestHtml/cat.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(6);
		expected = "0,\"file:./Crawler/TestHtml/dog.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(7);
		expected = "0,\"file:./Crawler/TestHtml/emu.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(8);
		expected = "0,\"file:./Crawler/TestHtml/fish.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(9);
		expected = "0,\"file:./Crawler/TestHtml/goat.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(10);
		expected = "0,\"file:./Crawler/TestHtml/hen.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(11);
		expected = "0,\"file:./Crawler/TestHtml/oneCAP.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(12);
		expected = "0,\"file:./Crawler/TestHtml/twoCAP.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(13);
		expected = "0,\"file:./Crawler/TestHtml/threeCAP.html\"";
		assertEquals(expected, actual);
	}

	@Test
	public void testFullTestHTMLfileProblemFormatsThatLinksSplitOverLines() {
		WebCrawlerInterface wc = new WebCrawler(15,15);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/ProblemFormatsTWO.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"file:./Crawler/TestHtml/axe.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "0,\"file:./Crawler/TestHtml/box.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(5);
		expected = "0,\"file:./Crawler/TestHtml/cat.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(6);
		expected = "0,\"file:./Crawler/TestHtml/dog.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(7);
		expected = "0,\"file:./Crawler/TestHtml/emu.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(8);
		expected = "0,\"file:./Crawler/TestHtml/fish.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(9);
		expected = "0,\"file:./Crawler/TestHtml/goat.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(10);
		expected = "0,\"file:./Crawler/TestHtml/hen.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(11);
		expected = "0,\"file:./Crawler/TestHtml/imp.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(12);
		expected = "0,\"file:./Crawler/TestHtml/jag.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(13);
		expected = "0,\"file:./Crawler/TestHtml/kite.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(14);
		//link never ends with " so readstring() to the end of file & return "", but never gets into database.
		assertNull(actual);
	}
	
	@Test
	public void testScrapeBasePlusRelativeURLThatIsEmptyStringfromFile() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk/courses\">", "<a href=\"\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeURLThatHostPlusStraightToQuestionMarkStuffNoFile() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("", "<a href=\"http://libeproject.it?lang=en%2F%3Flang%3Den&#038;paged=2\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://libeproject.it?lang=en%2F%3Flang%3Den&\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDealWithURLWithSpacesFromFile() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("", "<a href=\"http://www.dcs.bbk.ac.uk/courses/bsccomp/bsc-comp-part-time-booklet 2013-14.pdf\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://www.dcs.bbk.ac.uk/courses/bsccomp/bsc-comp-part-time-booklet%202013-14.pdf\"";
		assertEquals(expected, actual);
	}

	@Test
	public void testDealWithURLWithNumbersInIt() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("", "<a href=\"http://italk2learn.eu\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://italk2learn.eu\"";
		assertEquals(expected, actual);
	}
	

	@Test
	public void testDealWithURLWithFileNameWithNoDotONE() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("", "<a href=\"http://www.springer.com/engineering/journal/12530\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://www.springer.com/engineering/journal/12530\"";
		assertEquals(expected, actual);
	}

	@Test
	public void testDealWithURLWithFileNameWithNoDotTWO() {
		WebCrawlerInterface wc = new WebCrawler(2,2);
		helpMakeHTMLTestFile("", "<a href=\"http://www.bbk.ac.uk/news/web-pioneer-gives-2015-andrew-booth-memorial-lecture-at-birkbeck-1\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://www.bbk.ac.uk/news/web-pioneer-gives-2015-andrew-booth-memorial-lecture-at-birkbeck-1\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFullTestHTMLfileBeastsThatWeScrapeFilesInCorrectOrder() {
		WebCrawlerInterface wc = new WebCrawler(10,10);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/beasts.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"file:./Crawler/TestHtml/ant.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "0,\"file:./Crawler/TestHtml/bear.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(5);
		expected = "0,\"file:./Crawler/TestHtml/antsmall.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(6);
		expected = "0,\"file:./Crawler/TestHtml/antbig.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(7);
		expected = "0,\"file:./Crawler/TestHtml/bearsmall.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(8);
		expected = "0,\"file:./Crawler/TestHtml/bearbig.html\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testProgrammerSetsMaxLinksAndMaxDepthLinksLimitFourReachedFirst() {
		WebCrawlerInterface wc = new WebCrawler(4,500);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/beasts.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"file:./Crawler/TestHtml/ant.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "0,\"file:./Crawler/TestHtml/bear.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(5);
		expected = "0,\"file:./Crawler/TestHtml/antsmall.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(6);
		expected = "2,\"file:./Crawler/TestHtml/antbig.html\"";
		assertEquals(expected, actual);		
		actual = helpReadTempFileLine(7);
		expected = "3,\"file:./Crawler/TestHtml/bearsmall.html\"";
		assertEquals(expected, actual);	
		actual = helpReadTempFileLine(8);
		expected = "3,\"file:./Crawler/TestHtml/bearbig.html\"";
		assertEquals(expected, actual);	
		actual = helpReadTempFileLine(9);
		assertNull(expected, actual);
	}
	
	@Test
	public void testProgrammerSetsMaxLinksAndMaxDepthLinksLimitOneReachedFirst() {
		WebCrawlerInterface wc = new WebCrawler(1,500);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/beasts.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "1,\"file:./Crawler/TestHtml/ant.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "1,\"file:./Crawler/TestHtml/bear.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(5);
		assertNull(actual);
	}
	
	@Test
	public void testProgrammerSetsMaxLinksAndMaxDepthLinksLimitZeroReachedFirst() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("maxLinks must be 1 or more, maxDepth must be 0 or more");
		WebCrawlerInterface wc = new WebCrawler(0,500);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/beasts.html"), "crawlsearchresults.txt");
	}	
	
	@Test
	public void testProgrammerSetsMaxLinksAndMaxDepthLinksLimitMinusONeReachedFirst() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("maxLinks must be 1 or more, maxDepth must be 0 or more");
		WebCrawlerInterface wc = new WebCrawler(-1,500);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/beasts.html"), "crawlsearchresults.txt");
	}


	@Test
	public void testProgrammerSetsMaxLinksAndMaxDepthDepthLimitThreeReachedFirst() {
		WebCrawlerInterface wc = new WebCrawler(500,3);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/beasts.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"file:./Crawler/TestHtml/ant.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "0,\"file:./Crawler/TestHtml/bear.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(5);
		expected = "0,\"file:./Crawler/TestHtml/antsmall.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(6);
		expected = "0,\"file:./Crawler/TestHtml/antbig.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(7);
		expected = "0,\"file:./Crawler/TestHtml/bearsmall.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(8);
		expected = "0,\"file:./Crawler/TestHtml/bearbig.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(9);
		expected = "6,\"file:./Crawler/TestHtml/bearsmallpolar.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(10);
		expected = "6,\"file:./Crawler/TestHtml/bearsmallbrown.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(11);
		assertNull(actual);
	}
	
	@Test
	public void testProgrammerSetsMaxLinksAndMaxDepthDepthLimitOneReachedFirst() {
		WebCrawlerInterface wc = new WebCrawler(500,1);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/beasts.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"file:./Crawler/TestHtml/ant.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "0,\"file:./Crawler/TestHtml/bear.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(5);
		expected = "2,\"file:./Crawler/TestHtml/antsmall.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(6);
		expected = "2,\"file:./Crawler/TestHtml/antbig.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(7);
		expected = "3,\"file:./Crawler/TestHtml/bearsmall.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(8);
		expected = "3,\"file:./Crawler/TestHtml/bearbig.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(9);
		assertNull(actual);
	}
	
	@Test
	public void testProgrammerSetsMaxLinksAndMaxDepthDepthLimitZeroReachedFirst() {
		WebCrawlerInterface wc = new WebCrawler(500,0);
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/beasts.html"), "crawlsearchresults.txt");
		String actual = helpReadTempFileLine(3);
		String expected = "1,\"file:./Crawler/TestHtml/ant.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "1,\"file:./Crawler/TestHtml/bear.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(5);
		assertNull(actual);
	}
}
