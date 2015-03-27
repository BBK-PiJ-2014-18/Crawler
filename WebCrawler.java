import java.io.InputStream;
import java.net.URL;

public class WebCrawler {

	private static final String STARTING_URL = "http://www.dcs.bbk.ac.uk";
	
	public static void main (String[] args) {
		WebCrawler wc = new WebCrawler();
		wc.launch();
	}
	
	public void launch() {
		try {
			crawl();
		} catch (Exception ex) {
			//do nothing
		}
	}
	
	public void crawl() throws Exception {
		// need to do the exceptions here properly
		URL startingURL = new URL(STARTING_URL);
		InputStream inputStream = startingURL.openStream();
		String str;
		while((str = findURL(inputStream)) != null) {
			System.out.println(str);
		}
		System.out.println("XX");
		inputStream.close();
	}
	
	
	private String findURL(InputStream inputStream) {
		HTMLread reader = new HTMLread();
		boolean done = false;
		while (!done) {
			//readUntil takes us to the [<] or if we hit end of file returns false
			done = reader.readUntil(inputStream, '<', (char) -1);
			if(!done) {
				return null;
			}
			//skipSpace takes us through [a href="] ... need to deal with hit EOF
			if (reader.skipSpace(inputStream, 'a') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (reader.skipSpace(inputStream, 'h') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (reader.skipSpace(inputStream, 'r') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (reader.skipSpace(inputStream, 'e') != Character.MIN_CODE_POINT) {
				done = false;
			}
			if (reader.skipSpace(inputStream, 'f') != Character.MIN_CODE_POINT) {
				done = false;
			}		
			if (reader.skipSpace(inputStream, '=') != Character.MIN_CODE_POINT) {
				done = false;
			}
			//one way to deal with EOF (skipSpace returns the first non white space char it hits..)
			//do this for each element of [a href="] ????
			//need to set up some testing as real world examples proper rare I guess...
			char aChar = reader.skipSpace(inputStream, '"');
			if (aChar != Character.MIN_CODE_POINT) {
				done = false;
			}
			if(aChar == (char) -1) {
				System.out.println("DONE DONE DONE");
				return null;
			}
		}
		// read the URL to string, we get null from readString if hit EOF during read
		String str = reader.readString(inputStream, '"', (char) -1); 	// '\u001a' > using (char) -1, fix it! 
		return str;
	}
}
