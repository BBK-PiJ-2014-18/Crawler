import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WebCrawler {
	
	private DatabaseManager dm;
	private URLmanipulator um;
	private int countLinks;
	private int countDepth;
	private int maxLinks;
	private int maxDepth;
	
	public WebCrawler() {
		this.dm = new DatabaseManager();
		this.um = new URLmanipulator();
		this.countLinks = 0;
		this.countDepth = 0;
		this.maxLinks = 10000;
		this.maxDepth = 1000;
	}

	//crawl should have database information as an argument
	public void crawl(URL startingURL)  {
		if(startingURL == null){
			throw new NullPointerException("URL may not be null");
		}
		startingURL = um.standardizeURL(startingURL);
		URL base = um.makeBase(startingURL);
		//only do this first time
		if (countDepth == 0) {
			dm.saveCrawlAttributes(startingURL, base);
			dm.intitalizeTempFile(startingURL);
		}
		countDepth++;
		if(countDepth <= maxDepth && countLinks <= maxLinks) {
			System.out.println("Links = " + countLinks + " Depth = " + countDepth);
			scrapePage(startingURL, base);
		} else {
			return;
		}

		//INSERT: searchPage here

		URL nextURL = dm.getNextURL(maxDepth);
		if(nextURL != null) {
			crawl(nextURL);
		} else {
			return;
		}
	}

	private void scrapePage(URL startingURL, URL base) {
		InputStream inputStream;
		// need to do the exceptions are done right - check PiJ notes examples
		try {
			inputStream = startingURL.openStream();
			String scrapedString;
			while((scrapedString = findBaseURL(inputStream)) != null) {
				base = new URL(scrapedString);
				if (base != null) {
					base = um.standardizeURL(base);
				}
			}
			inputStream.close();
		} catch (IOException e) {
			System.out.println("File Not Found: " + startingURL);
		}
		URL result = null;
		// need to do the exceptions are done right - check PiJ notes examples
		try {
			inputStream = startingURL.openStream();
			String scrapedString;
			while((scrapedString = findURL(inputStream)) != null) {
				result = um.makeFullUrl(scrapedString, base);
				if (result != null) {
					result = filterURL(result);
				}
				if (result != null) {
					result = um.standardizeURL(result);
				}
				if (result != null) {
					if(countLinks <= maxLinks) {
						dm.writeURLtoTemp(countDepth, result);
						countLinks++;
					} else {
						return;
					}
				}
			}
			inputStream.close();
		} catch (IOException e) {
			System.out.println("File Not Found: " + startingURL);
		}
		System.out.println("ZZ");
	}
	

	private String findBaseURL(InputStream inputStream) {
		HTMLread reader = new HTMLread();
		boolean done = false;
		while (!done) {		
			//readUntil takes us to a [<] or if we hit end of file returns false
			done = reader.readUntil(inputStream, '<', (char) -1);
			//if we've reached the end of file return null
			if(!done) {
				return null;
			}
			
			//can rework this to iterate thru a string "ahref=" doing one char at a time
			//and reuse method for "basehref=" and for the search method
			
			//skipSpace takes us through [a href="] ... need to deal with hit EOF	
			if (reader.skipSpace(inputStream, 'b') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'a') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 's') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'e') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'h') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'r') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'e') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'f') != Character.MIN_CODE_POINT) {
				done = false;
			}		
			if (done && reader.skipSpace(inputStream, '=') != Character.MIN_CODE_POINT) {
				done = false;
			}
			//one way to deal with EOF (skipSpace returns the first non white space char it hits..)
			//do this for each element of [a href="] ????
			//need to set up some testing as real world examples proper rare I guess...
			char aChar = reader.skipSpace(inputStream, '"');
			if (done && aChar != Character.MIN_CODE_POINT) {
				done = false;
			}
			if(aChar == (char) -1) {
				System.out.println("EOF IN MIDDLE OF URL");
				return null;
			}
		}
		// read the URL to string, we get null from readString if hit EOF during read
		String str = reader.readString(inputStream, '"', (char) -1); 	// '\u001a' > using (char) -1, fix it! 
		return str;
	}
	
	private String findURL(InputStream inputStream) {
		HTMLread reader = new HTMLread();
		boolean done = false;
		while (!done) {		
			//readUntil takes us to a [<] or if we hit end of file returns false
			done = reader.readUntil(inputStream, '<', (char) -1);
			//if we've reached the end of file return null
			if(!done) {
				return null;
			}
			//skipSpace takes us through [a href="] ... need to deal with hit EOF	
			if (reader.skipSpace(inputStream, 'a') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'h') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'r') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'e') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (done && reader.skipSpace(inputStream, 'f') != Character.MIN_CODE_POINT) {
				done = false;
			}		
			if (done && reader.skipSpace(inputStream, '=') != Character.MIN_CODE_POINT) {
				done = false;
			}
			//one way to deal with EOF (skipSpace returns the first non white space char it hits..)
			//do this for each element of [a href="] ????
			//need to set up some testing as real world examples proper rare I guess...
			char aChar = reader.skipSpace(inputStream, '"');
			if (done && aChar != Character.MIN_CODE_POINT) {
				done = false;
			}
			if(aChar == (char) -1) {
				System.out.println("EOF IN MIDDLE OF URL");
				return null;
			}
		}
		// read the URL to string, we get null from readString if hit EOF during read
		String str = reader.readString(inputStream, '"', (char) -1); 	// '\u001a' > using (char) -1, fix it! 
		return str;
	}	
	
	// need to set up constructor to have a set of allowed protocols, 
	// this just placeholder so testing doesn't fail
	private URL filterURL(URL candidateURL) {
		String protocol = candidateURL.getProtocol();
		if(protocol.equals("mailto")) {
			return null;
		}
		return candidateURL;
	}
}