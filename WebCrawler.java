import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebCrawler {

//	private static final String STARTING_URL = "file:./Crawler/TestHtml/test.html";
	
	public void crawl(URL startingURL)  {
		// need to do the exceptions here properly
		URL base = makeBase(startingURL);
		DatabaseManager dm = new DatabaseManager();
		dm.saveCrawlAttributes(startingURL, base);
		InputStream inputStream;
		try {
			inputStream = startingURL.openStream();
			String scrapedString;
			URL result;
			int count = 0;
			while((scrapedString = findURL(inputStream)) != null) {
				count ++;
				result = makeFullUrl(scrapedString, base);
//				System.out.println(count + " " + result.toString());
			}
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ZZ");
	}

	private URL makeBase(URL startingURL) {
		String protocol = startingURL.getProtocol();
		String host = startingURL.getHost();
		String path = startingURL.getPath();
		if(path.contains("/") && !path.substring(path.lastIndexOf('/'), path.length()).contains(".")) {
			path = path + '/';
		}
		if(path.contains("/")) {
			path = path.substring(0, path.lastIndexOf('/') + 1);
		} else {
			path = path + '/';
		}
		URL result = null;
		try {
			result = new URL(protocol, host, path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		result = cleanURL(result);
		return result;
	}	

	
	private URL cleanURL(URL dirtyURL) {
		URL result = null;
		try {
			URI temp = dirtyURL.toURI();
			temp = temp.normalize();
			result = temp.toURL();
			//what order should these be in??
		} catch (URISyntaxException ex) {
			ex.printStackTrace();			
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	private URL makeFullUrl(String scrapedString, URL base) {
		URL result = null;
		if(scrapedString.charAt(0) == '/') {
			scrapedString = scrapedString.substring(1, scrapedString.length());
		}
		try {
			result = new URL(base, scrapedString);
		} catch (MalformedURLException e) {
			System.out.println("MalformedURL! Base:" + base.toString() + " + String: " + scrapedString);
			e.printStackTrace();
		}
		return result;
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
}