package com.conduent.ibts.alpr.process.service.impl;

import java.time.ZoneOffset;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dto.ImageDto;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.TProcessParameterDto;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.service.BuildTransactionForQFree;
import com.conduent.ibts.alpr.process.utility.MasterCache;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

import cloud.intrada.common.DisputeCategoryEnumeration;
import cloud.intrada.common.DisputeWorkflowDetailType;
import cloud.intrada.common.IdentificationType;
import cloud.intrada.common.ImageType;
import cloud.intrada.common.MetadataItemType;
import cloud.intrada.common.ObjectFactory;
import cloud.intrada.common.OriginalIdentificationType;
import cloud.intrada.common.PriorityEnumeration;
import cloud.intrada.common.ResultEnumeration;
import cloud.intrada.common.TargetEnumeration;
import cloud.intrada.common.WorkflowEnumeration;
import cloud.intrada.passage.PassageType;

@Service
//public class BuildTransactionForQFreeImpl implements BuildTransactionForQFree {
public class BuildTransactionForQFreeImpl {

	private static final Logger logger = LoggerFactory.getLogger(BuildTransactionForQFreeImpl.class);

	@Autowired
	MasterCache masterCache;

	//@Override
	public List<PassageType> buildPassages(List<QFreeRequestVO> pssgs, QFreeBatchDto batchDto, MultiLogger extLog) throws VectorImageReviewException {
		extLog.info("entering BuildTransactionForQFreeImpl.buildPassages() batchDto: " + batchDto+ " threadName: "+Thread.currentThread().getName()+"\n");
		
		List<PassageType> passages = new CopyOnWriteArrayList<>();
		CopyOnWriteArrayList<QFreeRequestVO> passageTypeList = new CopyOnWriteArrayList<>(pssgs);

		PassageType passageType = null;
		String vehicleTypeRequired = null;
		String paramNameVehicleTypeRequired = null;

		if (passageTypeList != null) {
			synchronized (passageTypeList) {
				for (QFreeRequestVO passageTypeVO : passageTypeList) {
					passageType = new PassageType();

					passageType.setHostPassageID(null != passageTypeVO.getLaneTxId() ? passageTypeVO.getLaneTxId()+"" : "");

					passageType.setSource(null != passageTypeVO.getSource() ? passageTypeVO.getSource() : "demo_source");

					passageType.setCaptureTimeUTC(passageTypeVO.getCaptureTimeUTC());
					// passageType.setExpirationTimeUTC(passageTypeVO.getExpirationTimeUTC());
					passageType.setPriority(PriorityEnumeration.valueOf(passageTypeVO.getPriority()));
					passageType.setWorkflow(WorkflowEnumeration.fromValue(batchDto.getLprType()));
					
					extLog.info("in BuildTransactionForQFreeImpl.buildPassages() ********* passageTypeVO.getLaneTxId(): "+passageTypeVO.getLaneTxId() +
							" passageTypeVO.getAgencyId(): "+passageTypeVO.getAgencyId()+ "  batchDto.getLprType(): "+ batchDto.getLprType()+" *********");	        		   	     			 					

					try {

						if (Constants.QFREE_DISPUTE.equals(batchDto.getLprType())) {
							extLog.info("in BuildTransactionForQFreeImpl.buildPassages() enqueuing DISPUTE...");

							DisputeWorkflowDetailType disputeType = new DisputeWorkflowDetailType();
							IdentificationType idType = new IdentificationType();
							OriginalIdentificationType origIdType = new OriginalIdentificationType();

							disputeType.setDisputeCategory(new ObjectFactory().createDisputeWorkflowDetailTypeDisputeCategory(DisputeCategoryEnumeration.DMV_REJECT));
							disputeType.setSuggestedIdentification(new ObjectFactory().createDisputeWorkflowDetailTypeSuggestedIdentification(idType));

							origIdType.setJurisdiction(passageTypeVO.getManualPlateCountry() + "_" + passageTypeVO.getManualPlateState());

							if (passageTypeVO.getManualPlateNumber() == null)
								passageTypeVO.setManualPlateNumber("-");

							origIdType.setRegistration(passageTypeVO.getManualPlateNumber());
							origIdType.setType(new ObjectFactory().createOriginalIdentificationTypeType(ResultEnumeration.TRUSTWORTHY_AUTO));

							XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
									.newXMLGregorianCalendar((GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC)));

							origIdType.setOriginalCompletionTimeUTC(new ObjectFactory().createOriginalIdentificationTypeOriginalCompletionTimeUTC(xmlDate));
							disputeType.setOriginalIdentification(new ObjectFactory().createDisputeWorkflowDetailTypeOriginalIdentification(origIdType));
							disputeType.setDisputeReason(DisputeCategoryEnumeration.DMV_REJECT.value());

							passageType.setDisputeWorkflowDetails(new cloud.intrada.passage.ObjectFactory().createPassageTypeDisputeWorkflowDetails(disputeType));
						}

					} catch (Exception error) {
						extLog.error("Error in BuildTransactionForQFreeImpl.buildPassages() processing passageTypeVO.getLaneTxId(): "+passageTypeVO.getLaneTxId() + 
							" for workflowType: "+batchDto.getLprType() + " error: "+error.getMessage());
					}

					// setting up ImageType and adding to QFreeRequest

					Set<Entry<String, ImageDto>> set = passageTypeVO.getAllImages().entrySet();
					synchronized (set) {
						Iterator<Entry<String, ImageDto>> iterator = set.iterator();
						while (iterator.hasNext()) {
							// key=value separator this by Map.Entry to get key and value
							Map.Entry<String, ImageDto> entry = (Map.Entry<String, ImageDto>) iterator.next();
							String key = (String) entry.getKey();
							ImageDto imageDAO = (ImageDto) entry.getValue();
							extLog.info("<<<<<< key:{}, imageDAO: {}  ", key, imageDAO.toString());
							extLog.info("in BuildTransactionForQFreeImpl.buildPassages() <<<<<< key: "+key+"  imageDAO: "+imageDAO.toString()+" \n"); 

							passageType.getImage().add(createtImageType(imageDAO, extLog));
						}
					}

					passageType.getMetadataItem().add(createPassageMetadataItems("Agency",		passageTypeVO.getAgencyShortName(), TargetEnumeration.RESULT, extLog));
					passageType.getMetadataItem().add(createPassageMetadataItems("TollSystem",	passageTypeVO.getAgencyShortName(), TargetEnumeration.RESULT, extLog));
					passageType.getMetadataItem().add(createPassageMetadataItems("Roadway",		passageTypeVO.getPlazaName(), TargetEnumeration.RESULT, extLog));
					passageType.getMetadataItem().add(createPassageMetadataItems("Plaza",		passageTypeVO.getExtPlazaId() + "", TargetEnumeration.RESULT, extLog));
					passageType.getMetadataItem().add(createPassageMetadataItems("Lane",		passageTypeVO.getExtLaneId() + "", TargetEnumeration.RESULT, extLog));
					passageType.getMetadataItem().add(createPassageMetadataItems("AxleCount",	passageTypeVO.getAxleCount() + "", TargetEnumeration.RESULT, extLog));
					passageType.getMetadataItem().add(createPassageMetadataItems("ForceMIR", 	"false", TargetEnumeration.RESULT, extLog));
					passageType.getMetadataItem().add(createPassageMetadataItems("ActualClass",	passageTypeVO.getActualClass() + "", TargetEnumeration.RESULT, extLog));

					// ************ BEGIN VEHICLE_TYPE_REQUIRED

					if (1==passageTypeVO.getAgencyId()&&(("Y").equalsIgnoreCase(passageTypeVO.getUnmatchedEntryFlag())||"TRUE".equalsIgnoreCase(passageTypeVO.getUnmatchedEntryFlag()))) {
						paramNameVehicleTypeRequired = Constants.PARAM_NAME_VEHICLE_TYPE_REQUIRED + "_ENTRY";
					} else if (1==passageTypeVO.getAgencyId()&&(("N").equalsIgnoreCase(passageTypeVO.getUnmatchedEntryFlag())||"FALSE".equalsIgnoreCase(passageTypeVO.getUnmatchedEntryFlag()))) {
						paramNameVehicleTypeRequired = Constants.PARAM_NAME_VEHICLE_TYPE_REQUIRED + "_EXIT";
					} else
						paramNameVehicleTypeRequired = Constants.PARAM_NAME_VEHICLE_TYPE_REQUIRED;

					extLog.info("in BuildTransactionForQFreeImpl.buildPassages() paramNameVehicleTypeRequired: " + paramNameVehicleTypeRequired + "\n");

					try {
						// getfrom t_process_parameters
						String paramName = paramNameVehicleTypeRequired;
						TProcessParameterDto processParam = masterCache.getListOfTprocessParameters().stream()
								.filter(param -> paramName.equals(param.getParamName())
										&& passageTypeVO.getAgencyId()==param.getAgencyId().intValue()
										&& Constants.PARAM_GROUP_IMAGEREVIEW.equals(param.getParamGroup())
										&& Constants.PARAM_CODE_QFREE.equals(param.getParamCode()))
								.findAny().orElse(null);

						vehicleTypeRequired = processParam.getParamValue();
					} catch (Exception e) {
						throw new VectorImageReviewException("error in BuildTransactionForQFreeImpl.buildPassages() vehicleTypeRequired: "+vehicleTypeRequired
							+ " missing vehicleTypeRequired static for agencyId: "+ passageTypeVO.getAgencyId() + " on t_process_parameters table.");
					}
					extLog.info("in BuildTransactionForQFreeImpl.buildPassages() vehicleTypeRequired: "+vehicleTypeRequired );
					// ************ END VEHICLE_TYPE_REQUIRED

					if (vehicleTypeRequired.equalsIgnoreCase("FALSE") && passageTypeVO.getActualClass() <= 0) {
						passageType.getMetadataItem().add(createPassageMetadataItems("VehicleTypeRequired", "TRUE", TargetEnumeration.RESULT, extLog));
						passageTypeVO.setVehicleTypeRequired("True");
					} else {
						passageType.getMetadataItem().add(createPassageMetadataItems("VehicleTypeRequired", vehicleTypeRequired + "", TargetEnumeration.RESULT, extLog));
						passageTypeVO.setVehicleTypeRequired(vehicleTypeRequired);
					}

					passages.add(passageType);
				}
			}
		}
		
		extLog.info("exiting BuildTransactionForQFreeImpl.buildPassages() batchDto: " + batchDto+ " threadName: "+Thread.currentThread().getName()+"\n");
		return passages;
	}

	private ImageType createtImageType(ImageDto imageDAO, MultiLogger extLog) {
		extLog.info("entering BuildTransactionForQFreeImpl.createtImageType() imageDAO.getURI(): "+imageDAO.getURI()+
			" imageDAO.getHostImageId(): "+imageDAO.getHostImageId() +" imageDAO.getCameraId(): "+imageDAO.getCameraId());

		ImageType imageType = new ImageType();
		imageType.setUri(new ObjectFactory().createImageTypeUri(imageDAO.getURI()));
		imageType.setHostImageID(imageDAO.getHostImageId());
		imageType.setCameraID(imageDAO.getCameraId());

		imageType.getMetadataItem().add(createImageMetadataItemType(imageDAO, extLog));
		
		extLog.info("exiting BuildTransactionForQFreeImpl.createtImageType() image type: "+imageType.toString());
		return imageType;
	}

	private MetadataItemType createImageMetadataItemType(ImageDto imageDAO, MultiLogger extLog) {
		extLog.info("entering BuildTransactionForQFreeImpl.createImageMetadataItemType()");
		
		MetadataItemType metadataItemType = new MetadataItemType();
		metadataItemType.setKey("VehicleSide");
		metadataItemType.setValue(imageDAO.getVehicleSide());
		metadataItemType.setTarget(TargetEnumeration.RESULT);
		
		extLog.info("exiting BuildTransactionForQFreeImpl.createImageMetadataItemType() Meta data item type: "+ metadataItemType.toString());
		
		return metadataItemType;
	}

	private MetadataItemType createPassageMetadataItems(String key, String value, TargetEnumeration target, MultiLogger extLog ) {
		extLog.info("Entering BuildTransactionForQFreeImpl.createPassageMetadataItems()");
		
		MetadataItemType metadataItemType = new MetadataItemType();
		metadataItemType.setKey(key);
		metadataItemType.setValue(value);
		metadataItemType.setTarget(target);
		
		extLog.info("exiting BuildTransactionForQFreeImpl.createPassageMetadataItems() metadataItemType: "+ metadataItemType.toString());
		
		return metadataItemType;
	}

}
