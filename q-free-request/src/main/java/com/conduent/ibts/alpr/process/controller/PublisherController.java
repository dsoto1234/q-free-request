package com.conduent.ibts.alpr.process.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conduent.ibts.alpr.process.service.impl.QFreeQueueServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//@Api(value = "ALPR-QFree Queue Api", tags = { "ALPR-QFree Queue" })
//@RestController
public class PublisherController {

	//@Autowired
	QFreeQueueServiceImpl qFreeQueueServiceImpl;
	
	@ApiOperation(value = "Publish transaction related details into queue ", nickname = "ALPR_QfreeQueue", notes = "", tags = {
	"ALPR-QFree queue Operation" })
@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully publishes message in queue."),
	@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/alprApi/messageToQueue")
	public String publishMessage() {

		if (qFreeQueueServiceImpl.sendTransactionstoQFreeQueue()) {

			return "Published Message to queue successfuly!!";
		} else {
			return "Failed to publish messages in queue!!";
		}
	}
}
