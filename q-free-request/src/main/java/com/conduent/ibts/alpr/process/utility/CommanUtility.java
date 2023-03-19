package com.conduent.ibts.alpr.process.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class CommanUtility {

	public int getRandomNumber(int n) {
		int m = (int) Math.pow(10, (n - 1));
		return m + new Random().nextInt(9 * m);
	}
	
	public String convertedDate(Date input) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");  
		return dateFormat.format(input.getTime()).toUpperCase();
	}
}
