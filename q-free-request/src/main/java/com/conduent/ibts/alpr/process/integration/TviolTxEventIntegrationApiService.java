package com.conduent.ibts.alpr.process.integration;

import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;

public interface TviolTxEventIntegrationApiService {

	boolean callingEventApi(Long laneTxId) throws VectorImageReviewException;

}
