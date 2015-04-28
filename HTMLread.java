import java.io.IOException;
import java.io.InputStream;
import java.lang.Character;

/**
 * An implementation of HTMLreaderInterface to help with parsing HTML elements
 * 
 * @author markkingsbury
 */
public class HTMLread implements HTMLreadInterface {
	
		public boolean readUntil(InputStream inputStream, char ch1, char ch2) {
			ch1 = Character.toLowerCase(ch1);
			ch2 = Character.toLowerCase(ch2);
			char aChar;
			try {	
				while((aChar = (char) inputStream.read()) != ch2) {
					if(Character.toLowerCase(aChar) == ch1) {
						return true;
					}
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
			
		public char skipSpace(InputStream inputStream, char ch) {
			ch = Character.toLowerCase(ch);
			char aChar = (char) -1;
			try {
				while(Character.isWhitespace(aChar = (char) inputStream.read())) { 
					//just consume white space 
				}
				if(Character.toLowerCase(aChar) == ch) {
					return Character.MIN_VALUE;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return aChar;
		}
		
		public String readString(InputStream inputStream, char ch1, char ch2) {
			String result = "";
			char aChar;
			try {	
				while((aChar = (char) inputStream.read()) != ch1 && aChar != ch2) {
					if(aChar == ch2) {
						return null;
					}
					if(aChar != '\n' && aChar != '\r') {
						result += aChar;
					}
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
}
