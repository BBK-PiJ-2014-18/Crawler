import java.io.InputStream;


/**
 * An interface to specify methods that will help parse HTML.
 * 
 * @author markkingsbury
 */
public interface HTMLreadInterface {

	/**
	 * HTML is consumed until one of true chars are encountered, or the stream has been fully consumed. 
	 * @param inputStream, the InputStream to be consumed
	 * @param ch1, a char to read until encountered
	 * @param ch2, a char to read until encountered
	 * @return true if ch1 found, false if ch2 encountered (both ignoring case), otherwise false is returned.
	 */
	public boolean readUntil(InputStream inputStream, char ch1, char ch2);
	
	
	/**
	 * HTML is consumed until a non-white space char is encountered, if that char is ch then
	 * Character.MIN_VALUE is returned, otherwise that char is returned.
	 * @param inputStream, the InputStream to be consumed
	 * @param ch, the char to read whitespace until encountered (ignoring case)
	 * @return the first not whitespace char if that is not ch; if it is ch then Character.MIN_VALUE
	 */	
	public char skipSpace(InputStream inputStream, char ch);

	
	/**
	 * Reads the input stream until ch1 is encountered (returning what is read up to but not including ch1).
	 * If ch2 is encountered in the process then null is returned. If neither ch1 or ch2 are encountered then an
	 * empty string is returned.
	 * @param inputStream, the InputStream to be consumed.
	 * @param ch1, a char that stops the reading of a string
	 * @param ch2, a char that stops the reading of a string
	 * @return the string read if reading was stopped by ch1, or null if the terminating char was ch2 (if neither char is 
	 * encountered then an empty string "" is returned). Any line breaks encountered while reading are 
	 * not included in the returned string.
	 */		
	public String readString(InputStream inputStream, char ch1, char ch2);
	
}
