package it.danja.newsmonitor.utils;

import java.io.UnsupportedEncodingException;

public class CharsetDetector {
	
public static String fixEncoding(String latin1) {
	  try {
	   byte[] bytes = latin1.getBytes("ISO-8859-1");
	   if (!validUTF8(bytes)) {
	    return latin1;   
	    } 
	 //  log.info("ALREADY UTF-8");
	   return new String(bytes, "UTF-8");  
	  } catch (UnsupportedEncodingException e) {
	   // Impossible, throw unchecked
	   throw new IllegalStateException("No Latin1 or UTF-8: " + e.getMessage());
	  }

	 }

	 public static boolean validUTF8(byte[] input) {
	  int i = 0;
	  // Check for BOM
	  if (input.length >= 3 && (input[0] & 0xFF) == 0xEF
	    && (input[1] & 0xFF) == 0xBB & (input[2] & 0xFF) == 0xBF) {
	   i = 3;
	  }

	  int end;
	  for (int j = input.length; i < j; ++i) {
	   int octet = input[i];
	   if ((octet & 0x80) == 0) {
	    continue; // ASCII
	   }

	   // Check for UTF-8 leading byte
	   if ((octet & 0xE0) == 0xC0) {
	    end = i + 1;
	   } else if ((octet & 0xF0) == 0xE0) {
	    end = i + 2;
	   } else if ((octet & 0xF8) == 0xF0) {
	    end = i + 3;
	   } else {
	    // Java only supports BMP so 3 is max
	    return false;
	   }

	   while (i < end) {
	    i++;
	    octet = input[i];
	    if ((octet & 0xC0) != 0x80) {
	     // Not a valid trailing byte
	     return false;
	    }
	   }
	  }
	  return true;
	 }
	 
	 public static String hexadecimal(String input, String charsetName) throws UnsupportedEncodingException {
		    if (input == null) throw new NullPointerException();
		    return asHex(input.getBytes(charsetName));
		}

		private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

		public static String asHex(byte[] buf)
		{
		    char[] chars = new char[2 * buf.length];
		    for (int i = 0; i < buf.length; ++i)
		    {
		        chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
		        chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
		    }
		    return new String(chars);
		}
//
//	 public static String encode(String input) {
//		  //  input = "\u00bfMa\u00f1ana?";
//
//		    // The list of charsets to encode with
//		    String[] charsetNames = { "US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16",
//		    // "X-ROT13" // This requires META-INF/services
//		    };
//		    for (int i = 0; i < charsetNames.length; i++) {
//		      doEncode(Charset.forName(charsetNames[i]), input);
//		    }
//		    return "srgsdg";
//		  }
//	 
//		  private static void doEncode(Charset cs, String input) {
//		    ByteBuffer bb = cs.encode(input);
//		//    log.info("Charset: " + cs.name());
//		//    log.info("  Input: " + input);
//		//    log.info("Encoded: ");
//
//		    for (int i = 0; bb.hasRemaining(); i++) {
//		      int b = bb.get();
//		      int ival = ((int) b) & 0xff;
//		      char c = (char) ival;
//		      // print index number
//		      System.out.print("  " + i + ": ");
//		      // print the hex value of the byte
//		      System.out.print(Integer.toHexString(ival));
//		      log.info(" (" + c + ")");
//		    }
//		  }
}