import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseManager {
	
	private static final String TEMP_FILE = "./Crawler/Data/crawltemp.txt";
	private static final String ATTRIBUTES_FILE = "./Crawler/Data/crawlattributes.txt";

	
	public DatabaseManager() {
		setUpDataFiles();
	}
	
	private void setUpDataFiles() {
		makeDirectory("./Crawler/Data");
		deleteFileIfExists(TEMP_FILE);
	}
	
	private void deleteFileIfExists(String fileToDelete) {
		Path p = Paths.get(fileToDelete);
		try {
			Files.deleteIfExists(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
 	
	private void makeDirectory(String dirName) {
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
			out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			out.println("CRAWL TEMP DATABASE");
			out.println("FIX WRITE OF STARTING URL HERE");
			out.println(priority + ",\"" + urlToWrite.toString()+ "\"");
			out.println("EOF");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
		} catch (IOException e) {
			System.out.println("Cannot write to file (IO exception) " + file + ".");
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	

}
