package com.conduent.ibts.alpr.process.batch.config;

import java.sql.SQLException;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;

public class ExceptionSkipPolicy implements SkipPolicy {

	@Override
	public boolean shouldSkip(Throwable throwable, int skipCounter) throws SkipLimitExceededException {
		
		if (throwable instanceof NumberFormatException && skipCounter == 2)
			return true;  // if it is true then I want to apply the skip
		else if (throwable instanceof RuntimeException && skipCounter == 2)
			return true;  // if it is true then I want to apply the skip
		else if (throwable instanceof SQLException && skipCounter == 2)
			return true;  // if it is true then I want to apply the skip		
		else if (throwable instanceof VectorImageReviewException && skipCounter == 2)
			return true;  // if it is true then I want to apply the skip
		else
			return false; // do not apply the skip
	}
}


//SQLException.class, VectorImageReviewException.class