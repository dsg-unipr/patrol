package it.unipr.ce.dsg.p2pgame.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 *
 * Use MessageDigest class, setting it to use SHA-1 algorithm, setting source data and
 * getting byte array with hash value.
 * Provide also conversion from byte array to string and from String to BigInteger.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class SHA1 {

	/**
	 * Returns hexadecimal string representation for SHA1 hash of input argument.
	 *
	 * @param data the SHA1 array of byte to convert
	 * @return the string in hex format
	 *
	 */
	public static String convertToHex(byte[] data) {

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
        	int halfbyte = (data[i] >>> 4) & 0x0F;
        	int two_halfs = 0;
        	do {
	            if ((0 <= halfbyte) && (halfbyte <= 9))
	                buf.append((char) ('0' + halfbyte));
	            else
	            	buf.append((char) ('a' + (halfbyte - 10)));
	            halfbyte = data[i] & 0x0F;
        	} while(two_halfs++ < 1);
        }
        return buf.toString();

    }

	/**
	 *
	 * Return the BigInteger representing the hexadecimal value from argument
	 *
	 * @param string the hexadecimal string to convert
	 * @return the BigInteger form input hex
	 *
	 */
	public static BigInteger convertFromStringToBig(String string) {
		if (string == null || string.compareTo("") == 0)
			return BigInteger.valueOf(0L);

		BigInteger sha1 = new BigInteger(string, 16);

		return sha1;

	}

	/**
	 *
	 * Give an array of has value from input string
	 *
	 * @param text string which calculates the hash
	 * @return the array with hash
	 * @throws NoSuchAlgorithmException on selection of SHA-1 algorithm
	 * @throws UnsupportedEncodingException on argument of message digest function
	 *
	 */
    public static byte[] calculateSHA1(String text) throws NoSuchAlgorithmException,
    	UnsupportedEncodingException  {

			MessageDigest md;
			md = MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[40];
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = md.digest();
			//return convertToHex(sha1hash);
			return sha1hash;

    }

}