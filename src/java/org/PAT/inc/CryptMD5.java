package org.PAT.inc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptMD5 {
	public String md5(String input){
		String result = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.reset();
			md5.update("user".getBytes());
			
			byte messageDigest[] = md5.digest();
			
			StringBuffer hexString = new StringBuffer();
			
			for (int i=0;i<messageDigest.length;i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			result = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return input; 
	}
}
