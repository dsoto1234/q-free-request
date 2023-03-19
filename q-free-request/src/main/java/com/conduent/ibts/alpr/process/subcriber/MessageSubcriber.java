package com.conduent.ibts.alpr.process.subcriber;

import java.io.IOException;
import java.util.List;

import com.conduent.ibts.alpr.process.dto.QFreeQueueResponseDto;

public interface MessageSubcriber {

	public List<QFreeQueueResponseDto> getMessage(int limit) throws IOException ;
}