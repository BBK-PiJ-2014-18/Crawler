import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class WebCrawler implements WebCrawlerInterface {
	
	private DatabaseManager dm;
	private URLmanipulator um;
	private int countLinks;
	private int countDepth;
	private int maxLinks;
	private int maxDepth;
	private Set<String> protocolsToIndex;
	
	public WebCrawler() {
		this.dm = new DatabaseManager();
		this.um = new URLmanipulator();
		this.countLinks = 1;
		this.countDepth = 0;
		this.maxLinks = 50;
		this.maxDepth = 20;
		setProtocolsToIndex();
	}

	public WebCrawler(int maxLinks, int maxDepth) {
		this.dm = new DatabaseManager();
		this.um = new URLmanipulator();
		this.countLinks = 1;
		this.countDepth = 0;
		if (maxLinks < 0 || maxDepth < 0) {
			throw new IllegalArgumentException("Argument must be positive");
		}
		this.maxLinks = maxLinks;
		this.maxDepth = maxDepth;
		setProtocolsToIndex();
	}
	
	private void setProtocolsToIndex() {
		this.protocolsToIndex = new HashSet<String>();
		protocolsToIndex.add("http");
		protocolsToIndex.add("https");
		protocolsToIndex.add("file");		
	}
	
	//crawl should have database information as an argument
	public void crawl(URL currentPageURL)  {
		if(currentPageURL == null){
			throw new NullPointerException("URL may not be null");
		}
		currentPageURL = um.standardizeURL(currentPageURL);
		URL currentBase = um.makeBase(currentPageURL);
		//only do this first time
		if (countDepth == 0) {
			dm.saveCrawlAttributes(currentPageURL, currentBase);
			dm.intitalizeTempFile(currentPageURL);
		}
		
		if(countDepth <= maxDepth && countLinks <= maxLinks) {
			System.out.println("Links = " + countLinks + " Depth = " + countDepth);
			scrapePage(currentPageURL, currentBase);
			countDepth++;
		} else {
			return;
		}
		search(currentPageURL);
		URL nextURL = dm.getNextURL(maxDepth);
		if(nextURL != null) {
			crawl(nextURL);
		}
		return;
		
	}
	
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
					if(countLinks <= maxLinks) {
						boolean added = dm.writeURLtoTemp(countDepth, scrapedURL);
						if (added) {
							countLinks++;				
						}
					} else {
						return;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("File Not Found: " + pageToScrape);
		} finally {
			closeInputStream(inputStream);
		}
	}
	
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
	
	private URL filterURL(URL candidateURL) {
		String protocol = candidateURL.getProtocol();
		if(!protocolsToIndex.contains(protocol)) {
			return null;
		}
		return candidateURL;
	}
	
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