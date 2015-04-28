import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A script to demonstrate how WebCrawler works.
 * 
 * @author markkingsbury
 */
public class CrawlerScript {
	
	public static void main (String[] args) {
		CrawlerScript cs = new CrawlerScript();
		cs.launch();
	}
		
	private void launch() {
		System.out.println();
		System.out.println("Webcrawler for CW5 - Mark Kingsbury");
		System.out.println();
		
		URL startURL = enterStartingURL();
		String searchFileName = enterSearchFileName();
		System.out.print("Enter 'd' to use default values of limit of crawling, or v to enter your own values: ");
		String limitChoice = "";
		boolean done = false;
		while(!done) {
			limitChoice = System.console().readLine();
			if (limitChoice.equals("d") || limitChoice.equals("v") ) { 
				done = true;
			} else {
				System.out.println("Please enter d or v");
			}
		}
		done = false;
		int maxLinks = -1;
		int maxDepth = -1;
		String str;
		while(!done && limitChoice.equals("v")) {
			System.out.print("Enter max number of links to crawl: ");
			str = System.console().readLine();
			if(isNumber(str)) {
				maxLinks = Integer.parseInt(str);
				done = true;
			} else {
				System.out.println("Please enter a number");
			}
		}
		done = false;
		while(!done && limitChoice.equals("v")) {
			System.out.print("Enter max depth of crawl: ");
			str = System.console().readLine();
			if(isNumber(str)) {
				maxDepth = Integer.parseInt(str);
				done = true;
			} else {
				System.out.println("Please enter a number");
			}
		}
		WebCrawlerInterface wc;
		if(limitChoice.equals("d")) {
			wc = new WebCrawler();
		} else {
			wc = new WebCrawler(maxLinks, maxDepth);
		}
		System.out.print("Crawl has started...");
		wc.crawl(startURL,searchFileName);
		System.out.println("...crawl has finished.");
		System.out.println();
		System.out.println("Files produced are in the directory Data (which is in the Crawler directory where your class files are)");
		System.out.println("Your Search Results are in: " + searchFileName);
		System.out.println("Your Indexed Crawl Results are in: crawltemp.txt");
		System.out.println("Your record of this crawl (and any exceptions logged) are in: crawlattributes.txt");
		System.out.println();
		System.out.println("Note: If you wish to retain the Indexed Crawl Results you need to copy the file (it will be overwritten by future runs).");
		System.out.println("Note: The search function will be implemented by a Future Programmer, in this implementation it simply returns all links crawled.");
	}
	
	private URL enterStartingURL() {
		URL result = null;
		System.out.print("Enter URL of starting point for crawl (e.g. http://www.dcs.bbk.ac.uk): ");
		try {
			result = new URL (System.console().readLine());
		} catch (MalformedURLException e) {
			System.out.println("That is not a valid URL, please try again");
			result = enterStartingURL(); 
		}
		return result;
	}
	
	private boolean isNumber(String str) {
		for(int i = 0; i<str.length(); i++) {
			if(!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private String enterSearchFileName() {
		System.out.print("Enter the name of the file to save search results in: ");
		String result = System.console().readLine();
		File f = new File("./Data/" + result);
		if(f.exists()) {
			System.out.print("Do you want to overwrite " + f + "? y/n");
			String str = System.console().readLine();
			if (str.equals("n")) {
				result = enterSearchFileName();
			}
		}
		return result;
	}
	
}
