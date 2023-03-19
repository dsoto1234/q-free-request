package com.conduent.ibts.alpr.process.constants;

public class Constants {

	private Constants() {
	}

	public static final String ACK_STATUS = "ACK_STATUS";
	public static final String AGENCY_ID = "AGENCY_ID";
	public static final String CREATED_BY = "CREATED_BY";
	public static final String CREATE_TS = "CREATE_TS";
	public static final String IMAGE_RECEIVE_DATE = "IMAGE_RECEIVE_DATE";
	public static final String IMAGE_TX_ID = "IMAGE_TX_ID";
	public static final String ISS_PASSAGE_ID = "ISS_PASSAGE_ID";
	public static final String IS_ENTRY = "IS_ENTRY";
	public static final String LANE_TX_ID = "LANE_TX_ID";
	public static final String LPR_SOURCE = "LPR_SOURCE";
	public static final String LPR_STATUS = "LPR_STATUS";
	public static final String LPR_SUBTYPE = "LPR_SUBTYPE";
	public static final String LPR_TYPE = "LPR_TYPE";
	public static final String ORT_FLAG = "ORT_FLAG";
	public static final String OUTPUT_FILE_ID = "OUTPUT_FILE_ID";
	public static final String OUTPUT_FILE_TYPE = "OUTPUT_FILE_TYPE";
	public static final String PLAZA_ID = "PLAZA_ID";
	public static final String UNMATCHED_ENTRY_FLAG = "UNMATCHED_ENTRY_FLAG";
	public static final String UPDATED_BY = "UPDATED_BY";
	public static final String UPDATE_TS = "UPDATE_TS";
	public static final String MSG_KEY = "MSG_KEY";
	public static final String IN_PROGRESS = "IN_PROGRESS";
	public static final String ENQUEUED = "ENQUEUED";
	public static final String ACTUAL_CLASS = "ACTUAL_CLASS";
	public static final String VEHICLE_TYPE_REQUIRE = "VEHICLE_TYPE_REQUIRE";
	public static final String N = "N";
	public static final String Y = "Y";


	// lpr status
	public static final String ENQUEUE_ACK_RECEIVED = "ENQUEUE_ACK_RECEIVED";
	public static final String TO_BE_ENQUEUED = "TO_BE_ENQUEUED";

	// lpr types
	public static final String QFREE_NORMAL = "NORMAL";
	public static final String QFREE_AIR_ONLY = "AIR_ONLY";
	public static final String QFREE_LPR_TYPE_MIR_ONLY = "MIR_ONLY";
	public static final String QFREE_DISPUTE = "DISPUTE";

	public static final String PRIORITY_NORMAL = "NORMAL";

	// t-process-parameters
	public static final String PARAM_NAME_VEHICLE_TYPE_REQUIRED = "VEHICLE_TYPE_REQUIRED";
	public static final String PARAM_NAME_STATS_MAX_RECORD_COUNT = "STATS_MAX_RECORD_COUNT";
	public static final String PARAM_NAME_Q_FREE_USER_NAME = "Q_FREE_USER_NAME";
	public static final String PARAM_NAME_Q_FREE_PASSWORD = "Q_FREE_PASSWORD";
	public static final String PARAM_NAME_THREAD_CONCURRENCY_LIMIT = "THREAD_CONCURRENCY_LIMIT";
	public static final String PARAM_NAME_RETRY_ATTEMPS = "RETRY_ATTEMPS";
	public static final String PARAM_NAME_WAIT_TIME_IN_MILLISECONDS = "WAIT_TIME_IN_MILLISECONDS";
	public static final String PARAM_GROUP_IMAGEREVIEW = "IMAGEREVIEW";
	public static final String PARAM_CODE_QFREE = "QFREE";
	public static final String PARAM_GROUP = "PARAM_GROUP";

	public static final long TVIOLTX_STATUS_179 = 179;
	public static final long TVIOLTX_STATUS_153 = 153;
	public static final String TVIOLTX_OUTPUT_FILE_TYPE_TR = "TR";

	// qFree acknowledgements
	public static final String ACK_FROM_NY = "NY";
	public static final String ACK_TO_NY = "NY";
	public static final String ACK_FROM_QFREE = "QFREE";
	public static final String ACK_TO_QFREE = "QFREE";
	// processing results
	public static final String ACKNOWLEDGEMENT_STATUS_OK = "OK";
	public static final String ACKNOWLEDGEMENT_STATUS_REJECT = "REJECT";
	public static final String ACKNOWLEDGEMENT_STATUS_RETRY = "RETRY";
	public static int RETRY_ATTEMPS = 0;
	public static long WAIT_TIME_IN_MILLISECONDS = 0;

	// t-viol-xfer-statistics
	public static final String TX_DATE = "TX_DATE";
	public static final String SECTION_ID = "SECTION_ID";
	public static final String AET_FLAG = "AET_FLAG";
	public static final String VAR_RECORD_COUNT = "VAR_RECORD_COUNT";
	public static final String IS_UNMATCHED = "IS_UNMATCHED";
	public static final String VIOL_XFER_FILE_ID = "VIOL_XFER_FILE_ID";
	public static final String FILE_TYPE = "FILE_TYPE";
	public static final String RECORD_COUNT = "RECORD_COUNT";
	public static final String PROCESS_FLAG = "PROCESS_FLAG";
	public static final String IS_DOUBLE_BLIND = "IS_DOUBLE_BLIND";
	public static final String REVIEWED_COUNT = "REVIEWED_COUNT";
	public static final String IS_REREVIEW = "IS_REREVIEW";

	// T_VIOL_IMAGE_TX
	public static final String FILE_TYPE_TR = "TR";
	public static final String QFREE_PROCESS_FLAG_REREVIEW = "11";
	public static final String QFREE_PROCESS_FLAG_DEFAULT = "10";
	public static final String TR_FILE_ID = "TR_FILE_ID";

	// T_UNMATCHED_ENTRY_TX
	public static final String TX_STATUS = "TX_STATUS";
	public static final String EVENT_TYPE = "EVENT_TYPE";
	public static final String IMAGE_BATCH_ID = "IMAGE_BATCH_ID";

	// t_Codes
	public static final String CODE_TYPE_EVENT_TYPE = "EVENT_TYPE";
	public static final String CODE_TYPE_TX_STATUS = "TX_STATUS";
	public static final String EXISTING_TX_STATUS = "EXISTING_TX_STATUS";
	public static final String CODE_VALUE_IMGREVIEW = "IMGREVIEW";
	public static final String CODE_VALUE_DMVREREVIEW = "DMVREREVIEW";
	public static final String CODE_VALUE_IMGREREVIEW = "IMGREREVIEW";
	public static final String CODE_VALUE_LPRREQUESTED = "LPRREQUESTED";

	// t-viol-tx
	public static final String IMAGE_BATCH_SEQ_NUMBER = "IMAGE_BATCH_SEQ_NUMBER";
	public static final String QFREE_TX_STATUS = "QFREE_TX_STATUS";
	
	//T_LPR_ACK_INFO
	public static final String HOST_PASSAGE_ID = "HOST_PASSAGE_ID";
	public static final String ACK_TYPE = "ACK_TYPE";
	public static final String ACK_DESCRIPTION = "ACK_DESCRIPTION";
	public static final String NUMBER_OF_ATTEMPTS = "NUMBER_OF_ATTEMPTS";
	public static final String ACK_FROM = "ACK_FROM";
	public static final String ACK_TO = "ACK_TO";
	public static final String THREAD_ID = "THREAD_ID";
	
	//Q_FREE_REQUEST_BATCH
	public static final String RUN_ID = "RUN_ID";
	public static final String EXECUTION_STATUS = "EXECUTION_STATUS";
	public static final String START_TIME = "START_TIME";
	public static final String END_TIME = "END_TIME";
	public static final String BATCH_SIZE = "BATCH_SIZE";
	public static final String TOTAL_THREADS = "TOTAL_THREADS";
	public static final String THREAD_INDEX = "THREAD_INDEX";
	

	
	
	
	
	
	
	

}
