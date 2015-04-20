import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;

public class DatabaseManager {
	
	private static final String TEMP_FILE = "./Crawler/Data/crawltemp.txt";
	private static final String ATTRIBUTES_FILE = "./Crawler/Data/crawlattributes.txt";
	private static final String LOG_FILE = "./Crawler/Data/crawllog.txt";
	
	public void makeDirectory(String dirName) {
		File f = new File(dirName);
		f.mkdir();
	}
	
	// or return boolean?
	public void saveCrawlAttributes (URL startingURL, URL base) {
		PrintWriter out = null;
		File file = new File(ATTRIBUTES_FILE);
		try {
			out = new PrintWriter(file);
			out.println("CRAWL ATTRIBUTES");
			out.println("STARTING URL = " + startingURL.toString());
			out.println("START BASE = " + base.toString());
			out.println("EOF");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
		} finally {
			out.close();
		}
	}
	
	public void writeURLtoTemp(int priority, URL urlToWrite) {
		PrintWriter out = null;
		File file = new File(TEMP_FILE);
		try {
			out = new PrintWriter(file);
			out.println("CRAWL TEMP DATABASE");
			out.println("FIX WRITE OF STARTING URL HERE");
			out.println("\"" + priority + "\",\"" + urlToWrite.toString()+ "\"");
			out.println("EOF");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
		} finally {
			out.close();
		}
	}
	
	public void writeToExceptionLog(String report) {
		FileWriter out = null;
		File file = new File(LOG_FILE);
		try {
			out = new FileWriter(file, true);
			out.write(report + "\n");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeFileWriter(out);
		}	
	}
	
	private void closeFileWriter(FileWriter fw) {
		try {
			if (fw != null) {
				fw.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
