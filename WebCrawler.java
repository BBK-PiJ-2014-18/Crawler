import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of the WebCrawlerInterface, using the default search() method specified there.
 * 
 * WebCrawler depends on an HTMLreader (to parse HTML), a URLmanipulator (to help form vaild URLs) 
 * and a DatabaseManager (to help read/write to relevant files).
 * 
 * @author markkingsbury
 */
public class WebCrawler implements WebCrawlerInterface {
	
	private static final int DEFAULT_MAX_LINKS = 20;
	private static final int DEFAULT_MAX_DEPTH = 15;
	
	private DatabaseManager dm;
	private URLmanipulator um;
	private int countLinks;
	private int countDepth;
	private int maxLinks;
	private int maxDepth;
	private Set<String> protocolsToIndex;
	
	/**
	 * Constructor that uses Default values to limit extent of crawl.
	 * Sets protocols to be indexed.
	 */
	public WebCrawler() {
		this.um = new URLmanipulator();
		this.countLinks = 1;
		this.countDepth = 0;
		this.maxLinks = DEFAULT_MAX_LINKS;
		this.maxDepth = DEFAULT_MAX_DEPTH;
		setProtocolsToIndex();
	}

	/**
	 * Constructor that allows programmer to select values to limit extent of crawl.
	 * Also sets up the protocols to be indexed.
	 * @param maxLinks, the maximum number of links to be scraped for further URLs. 
	 * @param maxDepth, is reached once all links found on pages up to that depth have been scrapped for further URLs.
	 */
	public WebCrawler(int maxLinks, int maxDepth) {
		this.um = new URLmanipulator();
		this.countLinks = 1;
		this.countDepth = 0;
		if (maxLinks < 1 || maxDepth < 0) {
			throw new IllegalArgumentException("maxLinks must be 1 or more, maxDepth must be 0 or more");
		}
		this.maxLinks = maxLinks;
		this.maxDepth = maxDepth;
		setProtocolsToIndex();
	}
	
	/**
	 * Establishes the protocols to be indexed.
	 */
	private void setProtocolsToIndex() {
		this.protocolsToIndex = new HashSet<String>();
		protocolsToIndex.add("http");
		protocolsToIndex.add("https");
		protocolsToIndex.add("file");		
	}

	public void crawl(URL currentPageURL, String outputFileName)  {
		if(currentPageURL == null){
			throw new NullPointerException("URL may not be null");
		}
		currentPageURL = um.standardizeURL(currentPageURL);
		URL currentBase = um.makeBase(currentPageURL);
		//only do this first time
		if (countDepth == 0) {
			dm = new DatabaseManager(outputFileName);
			dm.saveCrawlAttributes(currentPageURL, currentBase);
			dm.intitalizeTempFile(currentPageURL);
		}
		countDepth++;
		scrapePage(currentPageURL, currentBase);
		if(search(currentPageURL)) {
			dm.writeToSearchResultsFile(currentPageURL, outputFileName);	
		}
		URL nextURL = null;	
		if(countLinks < maxLinks) {
			nextURL = dm.getNextURL(maxDepth);
			countLinks++;
		}
		if(nextURL != null) {
			crawl(nextURL, outputFileName);
		}
	}
	
	/**
	 * Manages the scraping of a page, finding the base is present & then scraping all URLs found on the page.  These URLs are put with 
	 * their base (where relevant) & are then written to the temp data file. 
	 * @param pageToScrape, the page to scrape
	 * @param base, the base that goes with that the pageToScrape (although another base may be found on the actual page, and that one is used)
	 */
	private void scrapePage(URL pageToScrape, URL base) {
		InputStream inputStream = null;
		try {
			//replace base provided with the base in the pageToScrape (if it is there)
			inputStream = pageToScrape.openStream();
			String scrapedString = findURL(inputStream, "basehref=\"");
			if (scrapedString != null) {
				base = new URL(scrapedString);
				if (base != null) {
					base = um.standardizeURL(base);
				}
			}
			//scrape URLs on pageToScrape
			URL scrapedURL = null;
			inputStream = pageToScrape.openStream();
			while((scrapedString = findURL(inputStream,"ahref=\"")) != null) {
				scrapedURL = um.makeFullUrl(scrapedString, base);
	
				if (scrapedURL != null) {
					scrapedURL = filterURL(scrapedURL);
				}
				if (scrapedURL != null) {
					scrapedURL = um.standardizeURL(scrapedURL);
				}
				if (scrapedURL != null) {
					dm.writeURLtoTemp(countDepth, scrapedURL);
				}
			}
		} catch (IOException e) {
			um.writeToExceptionLog("PAGE NOT FOUND: " + pageToScrape.toString());			
		} finally {
			closeInputStream(inputStream);
		}
	}
	
	/**
	 * Scrapes a page looking for string that identifies relevant HTML element, and writing the following URL to  
	 * @param inputStream, the HTML to be read
	 * @param checkFor, the string that identifies the relevant HTML element.
	 * @return string representing the URL found. 
	 */
	private String findURL(InputStream inputStream, String checkFor) {
		HTMLread hr = new HTMLread();
		boolean matchPossible = false;
		while (!matchPossible) {		
			//readUntil takes us to '<' or if we hit end of file returns false
			matchPossible = hr.readUntil(inputStream, '<', (char) -1);
			//if we've reached the end of file return null
			if(!matchPossible) {
				return null;
			}
			//skipSpace takes us through the string to checkFor (e.g. "ahref=\"" or "basehref=\"") 
			int i = 0;
			while(matchPossible && i < checkFor.length()) {
				if ((hr.skipSpace(inputStream, checkFor.charAt(i))) != Character.MIN_VALUE) {
					//the next non whitespace char was not what looking for
					matchPossible = false;
				}
				i++;
			}
		}
		// return the read URL as string (will be null if hit EOF during read)
		return hr.readString(inputStream, '"', (char) -1); 
	}
	
	/**
	 * Checks that the candidateURL is of the protocol type we wish to scrape.
	 * @param candidateURL
	 * @return the candidateURL if it is of the protocol type we wish to scrape, otherwise null. 
	 */
	private URL filterURL(URL candidateURL) {
		String protocol = candidateURL.getProtocol();
		if(!protocolsToIndex.contains(protocol)) {
			return null;
		}
		return candidateURL;
	}
	
	/**
	 * Helper method to close any input stream.
	 * @param inputStream to be closed.
	 */
	private void closeInputStream(InputStream inputStream) {
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
}