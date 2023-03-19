package com.conduent.ibts.alpr.process.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dao.impl.QFreePayloadDaoImpl;
import com.conduent.ibts.alpr.process.dto.ImageDto;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.QFreeQueueResponseDto;
import com.conduent.ibts.alpr.process.exception.InvalidDataException;
import com.conduent.ibts.alpr.process.service.GenerateTransactionForQFree;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

@Service
//public class GenerateTransactionForQFreeImpl implements GenerateTransactionForQFree {
public class GenerateTransactionForQFreeImpl {
	
	private Logger logger = LoggerFactory.getLogger(GenerateTransactionForQFreeImpl.class);
	
	@Autowired
	QFreePayloadDaoImpl qFreePayloadDao;

	//@Override
	public boolean validateQueueData(QFreeQueueResponseDto queueData, MultiLogger extLog) throws InvalidDataException {
		extLog.info("entering GenerateTransactionForQFreeImpl.validateQueueData() queueData: " + queueData); 
		
		boolean validationStatus = true;

		if (null == queueData.getAgencyId() || null == queueData.getLaneTxId() || null == queueData.getLprType()
				|| null == queueData.getPlazaId()) {

			validationStatus = false;
			throw new InvalidDataException("Mandatory fields are null !");
		}
		
		extLog.info("exting GenerateTransactionForQFreeImpl.validateQueueData() queueData: " + queueData);
		return validationStatus;
	}

	//@Override
	public List<QFreeRequestVO> getTransactionsToEnqueue(QFreeBatchDto batchDto, MultiLogger extLog) {
		extLog.info("entering GenerateTransactionForQFreeImpl.getTransactionsToEnqueue() batchDto: " + batchDto+ " threadName: "+Thread.currentThread().getName()); 
		
		List<QFreeRequestVO> trnList = new CopyOnWriteArrayList<>();

		if (batchDto.getLprType().equalsIgnoreCase(Constants.QFREE_NORMAL) || batchDto.getLprType().equalsIgnoreCase(Constants.QFREE_AIR_ONLY)) 
		{
			trnList = qFreePayloadDao.getTransactionsForNormalAndAir(batchDto, extLog);
		} 
		else if (batchDto.getLprType().equalsIgnoreCase(Constants.QFREE_LPR_TYPE_MIR_ONLY)) 
		{
			trnList = qFreePayloadDao.getTransactionsForMir(batchDto);
		} 
		else if (batchDto.getLprType().equalsIgnoreCase(Constants.QFREE_DISPUTE)) 
		{
			trnList = qFreePayloadDao.getTransactionsForDispute(batchDto);
		}

		trnList.forEach(trn -> {
			if (null == trn.getAxleCount() || trn.getAxleCount() < 2)
				trn.setAxleCount(2l);

			try 
			{
				trn.setCaptureTimeUTC(stringToXMLGregorianCalendar(trn.getTxTimestamp()));
			} catch (ParseException e) 
			{
				e.printStackTrace();
			} catch (DatatypeConfigurationException e) 
			{
				e.printStackTrace();
			}

			trn.setSource("demo_source");
			trn.setPriority(Constants.PRIORITY_NORMAL);
			populateQFreeRequestVO_With_Images(trn);
		});
		
		extLog.info("exiting GenerateTransactionForQFreeImpl.getTransactionsToEnqueue() batchDto: " + batchDto+ " threadName: "+Thread.currentThread().getName()); 
		
		return trnList;
	}

	private void populateQFreeRequestVO_With_Images(QFreeRequestVO transaction) {

		boolean validImageFlag = false;

		if (null != transaction.getImageUrl1() && transaction.getImageUrl1().trim().length() > 0
				&& null != transaction.getImageIndex1() && transaction.getImageIndex1().trim().length() > 0 
				&& null != transaction.getImageFacing1()&& transaction.getImageFacing1().trim().length() > 0 
				&& transaction.getExtractLprFlag1().equals("Y")) {
			transaction.addImage(transaction.getImageIndex1(), populateImageVO(transaction.getImageFacing1(),
					transaction.getImageUrl1(), transaction.getImageIndex1()));

			validImageFlag = true;
		}

		if (null != transaction.getImageUrl2() && transaction.getImageUrl2().trim().length() > 0
				&& null != transaction.getImageIndex2() &&  transaction.getImageIndex2().trim().length() > 0
				&& null != transaction.getImageFacing2() && transaction.getImageFacing2().trim().length() > 0
				&& transaction.getExtractLprFlag2().equals("Y")) {
			transaction.addImage(transaction.getImageIndex2(), populateImageVO(transaction.getImageFacing2(),
					transaction.getImageUrl2(), transaction.getImageIndex2()));

			validImageFlag = true;
		}

		if (null != transaction.getImageUrl3() && transaction.getImageUrl3().trim().length() > 0
				&& null != transaction.getImageIndex3()  && transaction.getImageIndex3().trim().length() > 0 
				&& null != transaction.getImageFacing3() && transaction.getImageFacing3().trim().length() > 0
				&& transaction.getExtractLprFlag3().equals("Y")) {
			transaction.addImage(transaction.getImageIndex3(), populateImageVO(transaction.getImageFacing3(),
					transaction.getImageUrl3(), transaction.getImageIndex3()));

			validImageFlag = true;
		}
		if (null != transaction.getImageUrl4() && transaction.getImageUrl4().trim().length() > 0
				&& null != transaction.getImageIndex4() && transaction.getImageIndex4().trim().length() > 0 
				&& null != transaction.getImageFacing4() && transaction.getImageFacing4().trim().length() > 0
				&& transaction.getExtractLprFlag4().equals("Y")) {
			transaction.addImage(transaction.getImageIndex4(), populateImageVO(transaction.getImageFacing4(),
					transaction.getImageUrl4(), transaction.getImageIndex4()));

			validImageFlag = true;
		}
		if (null!=transaction.getImageUrl5()  && transaction.getImageUrl5().trim().length() > 0
				&& null!=transaction.getImageIndex5() && transaction.getImageIndex5().trim().length() > 0
				&& null!=transaction.getImageFacing5() && transaction.getImageFacing5().trim().length() > 0
				&& transaction.getExtractLprFlag5().equals("Y")) {
			transaction.addImage(transaction.getImageIndex5(), populateImageVO(transaction.getImageFacing5(),
					transaction.getImageUrl5(), transaction.getImageIndex5()));

			validImageFlag = true;
		}
		if (null != transaction.getImageUrl6() && transaction.getImageUrl6().trim().length() > 0
				&& null != transaction.getImageIndex6() && transaction.getImageIndex6().trim().length() > 0 
				&& null != transaction.getImageFacing6() && transaction.getImageFacing6().trim().length() > 0
				&& transaction.getExtractLprFlag6().equals("Y")) {
			transaction.addImage(transaction.getImageIndex6(), populateImageVO(transaction.getImageFacing6(),
					transaction.getImageUrl6(), transaction.getImageIndex6()));

			validImageFlag = true;
		}
		if (null != transaction.getImageUrl7() && transaction.getImageUrl7().trim().length() > 0
				&& null != transaction.getImageIndex7() && transaction.getImageIndex7().trim().length() > 0 
				&& null != transaction.getImageFacing7() && transaction.getImageFacing7().trim().length() > 0
				&& transaction.getExtractLprFlag7().equals("Y")) {
			transaction.addImage(transaction.getImageIndex7(), populateImageVO(transaction.getImageFacing7(),
					transaction.getImageUrl7(), transaction.getImageIndex7()));

			validImageFlag = true;
		}
		if (null != transaction.getImageUrl8() && transaction.getImageUrl8().trim().length() > 0
				&& null != transaction.getImageIndex8() && transaction.getImageIndex8().trim().length() > 0 
				&& null != transaction.getImageFacing8() && transaction.getImageFacing8().trim().length() > 0
				&& transaction.getExtractLprFlag8().equals("Y")) {
			transaction.addImage(transaction.getImageIndex8(), populateImageVO(transaction.getImageFacing8(),
					transaction.getImageUrl8(), transaction.getImageIndex8()));

			validImageFlag = true;
		}

		if (!validImageFlag) {
			ImageDto img = new ImageDto();
			img.setVehicleSide("not available");
			img.setHostImageId("not available");
			img.setCameraId("not available");
			img.setTarget("not available");

			if (!transaction.containsImage("0")) {
				transaction.addImage("0", img);
			}
		}

	}

	private ImageDto populateImageVO(String vehicleSide, String remoteImage, String imageIndex) {

		ImageDto img = new ImageDto();
		img.setVehicleSide(vehicleSide);
		img.setURI(remoteImage);

		// the unique id is the image index
		img.setHostImageId(imageIndex);
		img.setCameraId("cameraId_" + imageIndex);
		img.setTarget("0");

		return img;
	}

	private static XMLGregorianCalendar stringToXMLGregorianCalendar(String txTimeStamp) throws ParseException, DatatypeConfigurationException {
		Date date;
		XMLGregorianCalendar result = null;
		SimpleDateFormat simpleDateFormat;
		GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		date = simpleDateFormat.parse(txTimeStamp);

		gregorianCalendar.setTime(date);
		gregorianCalendar.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));

		result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

		return result;
	}
}
