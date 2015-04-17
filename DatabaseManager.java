import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;



public class DatabaseManager {
	

	private static final String DATABASE_FILE = "./Crawler/crawloutput.txt";

	// or return boolean?
	public void saveCrawlAttributes (URL startingURL, URL base) {
		PrintWriter out = null;
		File file = new File(DATABASE_FILE);
		try {
			out = new PrintWriter(file);
			out.println("CRAWL ATTRIBUTES");
			out.println("STARTING URL = " + startingURL.toString());
			out.println("START BASE = " + base.toString());
			out.println("CRAWL TEMP TABLE");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
		} finally {
			out.close();
		}
	}
}
