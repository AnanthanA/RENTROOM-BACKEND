package com.kp.demo.util;

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

public class MD5Util {

	public static String getMD5Hash(String data) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] hash = digest.digest(data.getBytes("UTF-8"));
			return bytesToHex(hash); // make it printable
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String bytesToHex(byte[] hash) {
		return DatatypeConverter.printHexBinary(hash);
	}

}
