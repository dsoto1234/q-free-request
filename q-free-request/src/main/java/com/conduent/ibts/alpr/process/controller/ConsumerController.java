package com.conduent.ibts.alpr.process.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conduent.ibts.alpr.process.service.impl.QFreeServiceImpl;

//@RestController
public class ConsumerController {

	/*
	 * @Autowired QFreeQueueServiceImpl qFreeQueueServiceImpl;
	 */

	@Autowired
	QFreeServiceImpl qfreeService;
	
	int cnt = 0;
	
	@GetMapping(value = "/alprApi/messageFromQueue")
	public String consumeFromQueue() throws ParseException {
		
		String msg = "";

		System.out.println("Before starting processInitiated=" + cnt);
		if (cnt == 0) {
			cnt = 1;
			try {
				/*
				 * qFreeQueueServiceImpl.getMessageFromQueue(); Thread.sleep(5000);
				 */
				//qfreeService.getTransactionsToEnqueue();
				msg = "Process Completed Successfully.";

			} /*
				 * catch (InterruptedException e) { e.printStackTrace(); msg =
				 * "Something went wrong , Please try again later.";
				 * 
				 * }
				 */catch (Exception e) {
				msg = "Something went wrong , Please try again later.";
			} finally {
				cnt = 0;

			}

		}

		else {
			msg = "Wait until current process  is completed";

		}
		return msg;
		
	}
	

}