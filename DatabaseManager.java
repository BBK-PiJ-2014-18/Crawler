import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseManager {
	
	private static final String TEMP_FILE = "./Crawler/Data/crawltemp.txt";
	private static final String ATTRIBUTES_FILE = "./Crawler/Data/crawlattributes.txt";
	private static final String SEARCH_RESULT_DIRECTORY = "./Crawler/Data/";
	
	public DatabaseManager(String outputFileName) {
		setUpDataFiles(outputFileName);
	}
	
	private void setUpDataFiles(String outputFileName) {
		makeDirectory("./Crawler/Data");
		deleteFileIfExists(TEMP_FILE);
		deleteFileIfExists(SEARCH_RESULT_DIRECTORY + outputFileName);
		//delete others?
	}
	
	private void deleteFileIfExists(String fileToDelete) {
		Path p = Paths.get(fileToDelete);
		try {
			Files.deleteIfExists(p);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
 	
	private void makeDirectory(String dirName) {
		File f = new File(dirName);
		f.mkdir();
	}
	
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
	
	
	public void intitalizeTempFile(URL startingURL) {
		PrintWriter out = null;
		File file = new File(TEMP_FILE);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			out.println("CRAWL TEMP DATABASE");
			out.println("0,\"" + startingURL.toString()+ "\"");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
		} catch (IOException e) {
			System.out.println("Cannot write to file (IO exception) " + file + ".");
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public boolean writeURLtoTemp(int priority, URL urlToWrite) {
		//check if urlToWrite is already in file
		File file = new File(TEMP_FILE);
		BufferedReader in = null;
		boolean urlPresent = false;
		try {
			in = new BufferedReader(new FileReader(file));
			String line;
			while((line = in.readLine()) != null) {
				if(line.contains("\"" + urlToWrite.toString() + "\"")) {
					urlPresent = true;
					break;
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File " + file + " does not exist");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeReader(in);
		}
		if(urlPresent) {
			return false;
		}
		//if it is not present, write it to file
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			out.println(priority + ",\"" + urlToWrite.toString()+ "\"");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
		} catch (IOException e) {
			System.out.println("Cannot write to file (IO exception) " + file + ".");
			e.printStackTrace();
		} finally {
			out.close();
		}
		return true;
	}
	
	public void writeToSearchResultsFile(URL urlToWrite, String outputFileName) {
		PrintWriter out = null;
		File file = new File(SEARCH_RESULT_DIRECTORY + outputFileName);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			out.write(urlToWrite.toString() + "\n");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file (not found) " + file + ".");
		} catch (IOException e) {
			System.out.println("Cannot write to file (IO exception) " + file + ".");
			e.printStackTrace();
		} finally {
			out.close();
		}	
	}	
	
	
	public URL getNextURL(int maxDepth) {
		//return the lowest priority URL or null if none <= maxDepth or all are zero
		//make the priority of the returned URL = 0;
		URL result = null;
		PrintWriter out = null;
		BufferedReader in = null;
		File aFile = new File(TEMP_FILE);
		File bFile = new File(TEMP_FILE + "1");
		try {
			out = new PrintWriter(bFile);
			in = new BufferedReader(new FileReader(aFile));
			//read the file "intro text" on first line
			String line = in.readLine();
			out.println(line);
			//work through the records
			boolean foundNextURL = false;
			while ((line = in.readLine()) != null) {
				String d = line.substring(0, line.indexOf(','));
				int depth = Integer.parseInt(d);
				if(!foundNextURL && depth > 0 && depth <= maxDepth) {
					result = new URL(line.substring(line.indexOf('"') + 1, line.length() - 1));
					foundNextURL = true;
					line = "0,\"" + result.toString() + "\"";
				}
				out.println(line);
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File " + aFile + " does not exist");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeReader(in);
			out.close();
		}
		bFile.renameTo(aFile);
		return result;
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


}
