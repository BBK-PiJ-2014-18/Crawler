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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class WebCrawlerTest {

	private static final String TEMP_FILE = "./Crawler/Data/crawltemp.txt";
	private static final String ATTRIBUTES_FILE = "./Crawler/Data/crawlattributes.txt";
	
	@BeforeClass
	public static void doOnceAtStartOfTests() {
		File f = new File("./Crawler/TestHtml");
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
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNormaliseStartingURLjustHost() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk"));
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNormaliseStartingURLjustHostwithForwardSlashAtEnd() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/"));
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMakeComplexStatingURLsaveClean() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/"));
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNormaliseStartingURLwithFileNameAtEndOfURL() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk//about/map.php"));
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/about/map.php";
		assertEquals(expected, actual);		
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMakeComplexStatingURLsaveCleanwithFileNameAtEndOfURL() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/map.php"));
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
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/"));
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	// favourite x test
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlNoFinalFowardSlash() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk"));
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlWithIndex() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/index.php"));
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}

	@Test
	public void testCreatingBaseUrlFromMoreComplexStartingUrl() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/"));
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/seminars/";
		assertEquals(expected, actual);
	}	
	
	@Test
	public void testCreatingBaseUrlFromMoreComplexStartingUrlNoFinalFowardSlash() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars"));
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/seminars/";
		assertEquals(expected, actual);
	}	

	@Test
	public void testCreatingBaseUrlFromMoreComplexStartingUrlWithIndex() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/index-external.php"));
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/seminars/";
		assertEquals(expected, actual);
	}

	@Test
	public void testCreatingBaseUrlFromStartingUrlWithValidDotsInPlaceOtherThanFinalFileName() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/map.php"));
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlWithValidDotsInPlaceAndNoFinalFileName() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about/"));
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreatingBaseUrlFromStartingUrlWithValidDotsInPlaceAndNoFinalFileNameNoFinalSlash() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/seminars/../about"));
		String actual = helpReadAttributesFileLine(3);
		String expected = "START BASE = http://www.dcs.bbk.ac.uk/about/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongerURLBothStartingAndGettingBase() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/courses/msccs/entry.php"));
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
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/#content"));
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = http://www.dcs.bbk.ac.uk/";
		assertEquals(expected, actual);
	}	

	@Test
	public void testRemoveReferenceFromURLwithHostAndPatht() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://www.dcs.bbk.ac.uk/news/index.php#n708"));
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
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("https://www.dcs.bbk.ac.uk/courses/bbkquality.php?from=pgcourses"));
		String actual = helpReadAttributesFileLine(2);
		String expected = "STARTING URL = https://www.dcs.bbk.ac.uk/courses/bbkquality.php?from=pgcourses";
		assertEquals(expected, actual);
		actual = helpReadAttributesFileLine(3);
		expected = "START BASE = https://www.dcs.bbk.ac.uk/courses/";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDealWithQueryPartOfURLexampleThree() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("http://jobs.bbk.ac.uk/fe/tpl_birkbeckcollege01.asp?s=4A515F4E5A565B1A&jobid=55240,0202886098&key=85175678&c=606965128771&pagestamp=sedzccxubhnvbfauxj"));
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
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("", "<a href=\"https://www.dcs.bbk.ac.uk/courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstBasePlusURLfromFile() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}

	
	@Test
	public void testFullTestHTMLfileWhereWeFindBase() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/UseBaseCorrect.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"file:Example/axe.html\"";
		assertEquals(expected, actual);
		actual = helpReadTempFileLine(4);
		expected = "0,\"file:Example/box.html\"";
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testScrapeFirstBaseWithCapsONE() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<BASE href=\"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstBaseWithCapsTWO() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<bAsE href=\"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstBaseWithSpacesONE() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<  BASE href=\"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstBaseWithSpacesTWO() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<  B A S E   h  r  e  f  =   \"https://www.dcs.bbk.ac.uk\">", "<a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstLinkWithCapsONE() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "<A href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstLinkWithCapsTWO() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "<A hreF=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeFirstLinkWithSpacesONE() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "< a href=\"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	

	@Test
	public void testScrapeFirstLinkWithSpacesTWO() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk\">", "< a href= \"courses\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}

	@Test
	public void testFullTestHTMLfileProblemFormatsThatHasTrickyFormatsOfLinks() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/ProblemFormats.html"));
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
	public void testScrapeBasePlusRelativeURLThatIsEmptyStringfromFile() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("<base href=\"https://www.dcs.bbk.ac.uk/courses\">", "<a href=\"\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"https://www.dcs.bbk.ac.uk/courses\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testScrapeURLThatHostPlusStraightToQuestionMarkStuffNoFile() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("", "<a href=\"http://libeproject.it?lang=en%2F%3Flang%3Den&#038;paged=2\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://libeproject.it?lang=en%2F%3Flang%3Den&\"";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDealWithURLWithSpacesFromFile() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("", "<a href=\"http://www.dcs.bbk.ac.uk/courses/bsccomp/bsc-comp-part-time-booklet 2013-14.pdf\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://www.dcs.bbk.ac.uk/courses/bsccomp/bsc-comp-part-time-booklet%202013-14.pdf\"";
		assertEquals(expected, actual);
	}

//		MalformedURL in makeFullUrl(). Base:http://www.bbk.ac.uk/front-page/ + String: javascript:this.print();
//		MalformedURL in makeFullUrl(). Base:http://www.bbk.ac.uk/prospective/open-evenings/open/ + String: javascript:this.print();
//		MalformedURL in makeFullUrl(). Base:http://ieeexplore.ieee.org/stamp/ + String: javascript:Modal.refresh('/xpl/mwForgotUserPasswordIntro.jsp')
//		MalformedURL in makeFullUrl(). Base:http://ieeexplore.ieee.org/stamp/ + String: javascript:Modal.refresh('/xpl/mwInstForgotUserNamePassword.jsp')
//		MalformedURL in makeFullUrl(). Base:http://ieeexplore.ieee.org/stamp/ + String: javascript:redirectToWayf()
//		MalformedURL in makeFullUrl(). Base:http://ieeexplore.ieee.org/stamp/ + String: javascript:Modal.show('/xpl/mwMemberSignIn.jsp')
//		MalformedURL in makeFullUrl(). Base:http://www.bbk.ac.uk/news/web-pioneer-gives-2015-andrew-booth-memorial-lecture-at-birkbeck-1/ + String: javascript:this.print();
//		MalformedURL in makeFullUrl(). Base:http://www.bbk.ac.uk/prospective/open-evenings/open/ + String: javascript:this.print();
//
//
//		REAL Problems:
//		====================================================================
//		quirky html link - don't understand what's intended:
//		x
//		  FNF: startingURL: http://www.dcs.bbk.ac.uk/staff/ base: http://www.dcs.bbk.ac.uk/staff/
//		  FNF: scst: &#10;                     http://www.dcs.bbk.ac.uk/staff/staffperson.php?name=andrea base: http://www.dcs.bbk.ac.uk/staff/ = result: http://www.dcs.bbk.ac.uk/staff/&/
//		x
//		====================================================================
//		dots messy (remove if still in after made?)
//		x
//		  FNF: startingURL: http://www.dcs.bbk.ac.uk/news/ base: http://www.dcs.bbk.ac.uk/news/
//		  FNF: scst: ../../r/doc/Oracle-CSI-Kernel.pdf base: http://www.dcs.bbk.ac.uk/news/ = result: http://www.dcs.bbk.ac.uk/../r/doc/Oracle-CSI-Kernel.pdf
//		x
//		x
//		  FNF: startingURL: http://www.dcs.bbk.ac.uk/news/ base: http://www.dcs.bbk.ac.uk/news/
//		  FNF: scst: ../../50years/ base: http://www.dcs.bbk.ac.uk/news/ = result: http://www.dcs.bbk.ac.uk/../50years/
//		x
//		====================================================================
//		port problem!
//		x
//		  FNF: startingURL: http://www.dcs.bbk.ac.uk/news/ base: http://www.dcs.bbk.ac.uk/news/
//		  FNF: scst: http://pamir.dia.uniroma3.it:8080/SWIM2013/Home.html base: http://www.dcs.bbk.ac.uk/news/ = result: http://pamir.dia.uniroma3.it/SWIM2013/Home.html
//		   																					  scst copy from line above: http://pamir.dia.uniroma3.it:8080/SWIM2013/Home.html
//		x
//	====================================================================
//	Don't know problem! maybe line break in their html?
//	x
//	  FNF: startingURL: http://libeproject.it/?lang=en base: http://libeproject.it/
//	  FNF: scst: http://blogs.kqed.org/mindshift/2014/07/5-essential-insights-about-mobile-learning/ base: http://libeproject.it/ 
//	   = result: http://blogs.kqed.org/mindshift/2014/07/5-essential-insights-about-mobile-learning/
//	x
//	x
//	  FNF: startingURL: http://libeproject.it/?lang=en base: http://libeproject.it/
//	  FNF: scst: http://blogs.kqed.org/mindshift/2013/09/reinventing-school-new-learning-environment-ecosystems-for-inquiry-learning/ base: http://libeproject.it/ = result: http://blogs.kqed.org/mindshift/2013/09/reinventing-school-new-learning-environment-ecosystems-for-inquiry-learning/
//	x
	
	
	
	
//	====================================================================
//	don't know problem! maybe the 2 in the host part?
//	x
//	  FNF: startingURL: http://www.dcs.bbk.ac.uk/research/projects.html base: http://www.dcs.bbk.ac.uk/research/
//	  FNF: scst: http://www.italk2learn.eu/ base: http://www.dcs.bbk.ac.uk/research/ = result: http://www.italk2learn.eu/
//	x
//		(here addding the final / doesn't seem to matter...)
//	x
//  	  FNF: startingURL: http://www.dcs.bbk.ac.uk/news/ base: http://www.dcs.bbk.ac.uk/news/
//      FNF: scst: http://italk2learn.eu base: http://www.dcs.bbk.ac.uk/news/ = result: http://italk2learn.eu/
//	x
	
	@Test
	public void testDealWithURLWithNumbersInIt() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("", "<a href=\"http://italk2learn.eu\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://italk2learn.eu\"";
		assertEquals(expected, actual);
	}
	
//SOLVED PROBLEMS	
	
	
//		====================================================================
//		I've added a / at end of result?
//		x
//		  FNF: startingURL: http://www.dcs.bbk.ac.uk/news/ base: http://www.dcs.bbk.ac.uk/news/
//		  FNF: scst: http://www.springer.com/engineering/journal/12530 base: http://www.dcs.bbk.ac.uk/news/ = result: http://www.springer.com/engineering/journal/12530/
//		x
//		x
//		  FNF: startingURL: http://www.theguardian.com/public-leaders-network/2015/apr/07/uk-public-sector-slow-internet-of-things/ base: http://www.theguardian.com/public-leaders-network/2015/apr/07/uk-public-sector-slow-internet-of-things/
//		  FNF: scst: https://profile.theguardian.com/signin base: http://www.theguardian.com/public-leaders-network/2015/apr/07/uk-public-sector-slow-internet-of-things/ = result: https://profile.theguardian.com/signin/
//		x
	

	@Test
	public void testDealWithURLWithFileNameWithNoDotONE() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("", "<a href=\"http://www.springer.com/engineering/journal/12530\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://www.springer.com/engineering/journal/12530\"";
		assertEquals(expected, actual);
	}

	@Test
	public void testDealWithURLWithFileNameWithNoDotTWO() {
		WebCrawler wc = new WebCrawler();
		helpMakeHTMLTestFile("", "<a href=\"http://www.bbk.ac.uk/news/web-pioneer-gives-2015-andrew-booth-memorial-lecture-at-birkbeck-1\">Find Me</a>");
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/test.html"));
		String actual = helpReadTempFileLine(3);
		String expected = "0,\"http://www.bbk.ac.uk/news/web-pioneer-gives-2015-andrew-booth-memorial-lecture-at-birkbeck-1\"";
		assertEquals(expected, actual);
	}
	
	

//
//		END OF PROBLEMS TO SOLVE
//
//		====================================================================
//		====================================================================
//		====================================================================	
	
	
	
//what, if anything, to do about HTML Global Attributes? Need to pick href as next after <a as can't 
//check presence of a space after <a	

	@Test
	public void testFullTestHTMLfileBeastsThatWeScrapeFilesInCorrectOrder() {
		WebCrawler wc = new WebCrawler();
		wc.crawl(helpMakeURL("file:./Crawler/TestHtml/beasts.html"));
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
}



