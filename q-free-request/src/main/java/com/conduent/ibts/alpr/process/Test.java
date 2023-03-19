package com.conduent.ibts.alpr.process;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		
		List<String> transactionsList =  new ArrayList<>();;
		System.out.println("transactionsList.size: "+transactionsList.size());
		
		
		if (null != transactionsList && !transactionsList.isEmpty()) 
			System.out.println("hi");
		else
			System.out.println("no hi");
			
		
		/*
		Long i = new Long(0);
		
		if ( 0 == i.intValue())
				System.out.println("i: "+ i);
		else
			System.out.println("don't know");
		
		
		 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	        System.out.println();
	        System.out.println("Server sent " + chain.length + " certificate(s):");
	        System.out.println();
	        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
	        MessageDigest md5 = MessageDigest.getInstance("MD5");
	        for (int i = 0; i < chain.length; i++) {
	            X509Certificate cert = chain[i];
	            System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
	            System.out.println("   Issuer  " + cert.getIssuerDN());
	            sha1.update(cert.getEncoded());
	            System.out.println("   sha1    " + toHexString(sha1.digest()));
	            md5.update(cert.getEncoded());
	            System.out.println("   md5     " + toHexString(md5.digest()));
	            System.out.println();
	        }

	      
	            String line = reader.readLine().trim();
	            */
				
	}

}
