import java.io.IOException;
import java.io.InputStream;
import java.lang.Character;

public class HTMLread {
	
		/**
		 * 		  
		 * @param inputStream
		 * @param ch1, the char to read until
		 * @param ch2, the end of file char ?? [(char) -1]
		 * @return true if ch1 found, false if EOF reached.
		 */
		public boolean readUntil(InputStream inputStream, char ch1, char ch2) {
			char aChar;
			try {	
				while((aChar = (char) inputStream.read()) != ch2) {
					if(aChar == ch1) {
						return true;
					}
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		/**
		 * 
		 * @param inputStream
		 * @param ch, the char sought...
		 * @return the first not blank char if that is not ch; if it is ch then Character.MIN_CODE_POINT
		 */	
		public char skipSpace(InputStream inputStream, char ch) {
			//it's not cool to have to initialize aChar here... but err, this is just something... fix it.
			char aChar = '\u001a';
			try {
				while((aChar = (char) inputStream.read()) == ' ') { 
				}
				if(aChar == ch) {
					return Character.MIN_CODE_POINT;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return aChar;
		}

		/**
		 * 
		 * @param inputStream
		 * @param ch1, the char that marks the end of the URL
		 * @param ch2, the char that represents EOF
		 * @return the URL, or null if EOF is reached
		 */		
		public String readString(InputStream inputStream, char ch1, char ch2) {
			String result = "";
			char aChar;
			try {	
				while((aChar = (char) inputStream.read()) != ch1) {
					if(aChar == ch2) {
						return null;
					}	
					result += aChar;
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
}
