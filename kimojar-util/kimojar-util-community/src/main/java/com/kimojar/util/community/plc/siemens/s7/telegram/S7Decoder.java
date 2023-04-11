/**
 * ==============================================================================
 * PROJECT kimojar-util-community
 * PACKAGE com.kimojar.util.community.plc.siemens.s7.telegram
 * FILE S7Decoder.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2022-11-14
 * ==============================================================================
 * Copyright (C) 2022
 * KiMoJar All rights reserved
 * ==============================================================================
 * This project is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or under
 * EPL Eclipse Public License 1.0.
 * 
 * This means that you have to chose in advance which take before you import
 * the library into your project.
 * 
 * This project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE whatever license you
 * decide to adopt.
 * ==============================================================================
 */
package com.kimojar.util.community.plc.siemens.s7.telegram;

import java.util.ArrayList;
import java.util.Arrays;

import com.kimojar.util.community.data.ByteArray;
import com.kimojar.util.community.data.ByteUtil;

/**
 * @author KiMoJar
 * @date 2020-04-13
 */
public class S7Decoder {

	private S7Decoder() {
	}

	/**
	 * 解码pdu
	 * <li>epan\dissectors\packet-s7comm:dissect_s7comm</li>
	 */
	public static void decode(byte[] pdu) {
		FieldValue s7 = new FieldValue(Field.s7, pdu);
		byte[] pduHeader;
		byte[] pduPara;
		byte[] pduData;
		byte[] errorClassCode = null;
		int paraLength = ByteUtil.getWordAt(pdu, 6);
		int dataLength = ByteUtil.getWordAt(pdu, 8);
		switch(pdu[1]){
			case S7.S7_HEADER_ROSCTR_ACK:
			case S7.S7_HEADER_ROSCTR_ACKDATA:
				pduHeader = Arrays.copyOfRange(pdu, 0, S7.PduHeaderLength12);
				pduPara = Arrays.copyOfRange(pdu, S7.PduHeaderLength12, S7.PduHeaderLength12 + paraLength);
				pduData = Arrays.copyOfRange(pdu, S7.PduHeaderLength12 + paraLength, S7.PduHeaderLength12 + paraLength + dataLength);
				errorClassCode = Arrays.copyOfRange(pdu, 10, S7.PduHeaderLength12);
				break;
			default:
				pduHeader = Arrays.copyOfRange(pdu, 0, S7.PduHeaderLength10);
				pduPara = Arrays.copyOfRange(pdu, S7.PduHeaderLength10, S7.PduHeaderLength10 + paraLength);
				pduData = Arrays.copyOfRange(pdu, S7.PduHeaderLength10 + paraLength, S7.PduHeaderLength10 + paraLength + dataLength);
				break;
		}
		// s7 header
		FieldValue s7_header = s7.addChild(new FieldValue(Field.s7_header, pduHeader));
		s7_header.addChild(new FieldValue(Field.s7_header_protocolId, Arrays.copyOfRange(pdu, 0, 1)));
		s7_header.addChild(new FieldValue(Field.s7_header_rosctr, Arrays.copyOfRange(pdu, 1, 2)));
		s7_header.addChild(new FieldValue(Field.s7_header_redundancyIdentification, Arrays.copyOfRange(pdu, 2, 4)));
		s7_header.addChild(new FieldValue(Field.s7_header_pduReference, Arrays.copyOfRange(pdu, 4, 6)));
		s7_header.addChild(new FieldValue(Field.s7_header_paraLength, Arrays.copyOfRange(pdu, 6, 8)));
		s7_header.addChild(new FieldValue(Field.s7_header_dataLength, Arrays.copyOfRange(pdu, 8, 10)));
		if(errorClassCode != null)
			s7_header.addChild(new FieldValue(Field.s7_header_errorClassCode, errorClassCode));

		switch(pdu[1]){
			case S7.S7_HEADER_ROSCTR_JOB:
			case S7.S7_HEADER_ROSCTR_ACKDATA:
				decodeRequestResponse(s7, pduPara, pduData);
				break;
			case S7.S7_HEADER_ROSCTR_USERDATA:
				decodeUserData(s7, pduPara, pduData);
				break;
		}
	}

	/**
	 * <li>epan\dissectors\packet-s7comm:s7comm_decode_req_resp</li>
	 * 
	 * @param pduData
	 */
	private static void decodeRequestResponse(FieldValue s7, byte[] pduPara, byte[] pduData) {
		byte rosctr = s7.getValue()[1];
		byte function = pduPara[0];
		if(pduPara.length > 0) {
			FieldValue s7_para = s7.addChild(new FieldValue(Field.s7_parameter, pduPara));// Add parameter tree
			s7_para.addChild(new FieldValue(Field.s7_para_functionCode, Arrays.copyOfRange(pduPara, 0, 1)));
			if(rosctr == S7.S7_HEADER_ROSCTR_JOB) {
				switch(function){
					case S7.S7_PARA_FUNCODE_SERVICEREAD:
					case S7.S7_PARA_FUNCODE_SERVICEWRITE:
						s7_para.addChild(new FieldValue(Field.s7_para_itemCount, Arrays.copyOfRange(pduPara, 1, 2)));
						int itemCount = pduPara[1] & 0xFF;
						int offset = 0;
						// parse item data
						for(int i = 0; i < itemCount; i++) {
							int offset_old = offset;
							offset = decodeParaItem(s7_para, pduPara, pduData, offset);
							// if length is not a multiple of 2 and this is not the last item, then add a fill-byte
							int len = offset - offset_old;
							if((len % 2) == 1 && i < (itemCount - 1))
								offset += 1;
						}
						// in write-function there is a data part
						if(function == S7.S7_PARA_FUNCODE_SERVICEWRITE && pduData.length > 0) {
							FieldValue s7_data = s7.addChild(new FieldValue(Field.s7_data, pduData));
							decodeReponseReadData(s7_data, pduPara, pduData, itemCount);
						}
						break;
					case S7.S7_PARA_FUNCODE_SERVICESC:
						decodeSetupCommunication(s7_para, pduPara, pduData);
						break;
					case S7.S7_PARA_FUNCODE_FUNCRD:
					case S7.S7_PARA_FUNCODE_FUNCDB:
					case S7.S7_PARA_FUNCODE_FUNCDE:
					case S7.S7_PARA_FUNCODE_FUNCSU:
					case S7.S7_PARA_FUNCODE_FUNCUL:
					case S7.S7_PARA_FUNCODE_FUNCEU:
						decodePlcControlUpdownload(s7_para, pduPara, pduData);
						break;
					case S7.S7_PARA_FUNCODE_FUNCPI:
						decodePiService(s7_para, pduPara, pduData);
						break;
					case S7.S7_PARA_FUNCODE_FUNCPS:
						decodePlcControlParam(s7_para, pduPara, pduData);
						break;
					default:
						if(pduPara.length > 1)
							s7.addChild(new FieldValue(Field.s7_parameter, pduPara));
						if(pduData.length > 0)
							s7.addChild(new FieldValue(Field.s7_data, pduData));
						break;
				}
			} else if(rosctr == S7.S7_HEADER_ROSCTR_ACKDATA) {
				switch(function){
					case S7.S7_PARA_FUNCODE_SERVICEREAD:
					case S7.S7_PARA_FUNCODE_SERVICEWRITE:
						int itemCount = pduPara[1] & 0xFF;
						// This is a read-response, so the requested data may follow when address in request was ok
						s7_para.addChild(new FieldValue(Field.s7_para_itemCount, Arrays.copyOfRange(pduPara, 1, 2)));
						FieldValue s7_data = s7.addChild(new FieldValue(Field.s7_data, pduData));
						if(function == S7.S7_PARA_FUNCODE_SERVICEREAD && pduData.length > 0)
							decodeReponseReadData(s7_data, pduPara, pduData, itemCount);
						else if(function == S7.S7_PARA_FUNCODE_SERVICEWRITE && pduData.length > 0)
							decodeReponseWriteData(s7_data, pduPara, pduData, itemCount);
						break;
					case S7.S7_PARA_FUNCODE_SERVICESC:
						decodeSetupCommunication(s7_para, pduPara, pduData);
						break;
					case S7.S7_PARA_FUNCODE_FUNCRD:
					case S7.S7_PARA_FUNCODE_FUNCDB:
					case S7.S7_PARA_FUNCODE_FUNCDE:
					case S7.S7_PARA_FUNCODE_FUNCSU:
					case S7.S7_PARA_FUNCODE_FUNCUL:
					case S7.S7_PARA_FUNCODE_FUNCEU:
						decodePlcControlUpdownload(s7_para, pduPara, pduData);
						break;
					case S7.S7_PARA_FUNCODE_FUNCPI:
						if(pduPara.length >= 2) {
							// TODO
						}
						break;
					default:
						if(pduPara.length > 1)
							s7.addChild(new FieldValue(Field.s7_parameter, pduPara));
						if(pduData.length > 0)
							s7.addChild(new FieldValue(Field.s7_data, pduData));
						break;
				}
			}
		}
	}

	public static int decodeParaItem(FieldValue s7_parameter, byte[] pduPara, byte[] pduData, int offset) {
		byte s7_para_variableSpecificationType = pduPara[2];
		byte s7_para_variableSpecificationLength = pduPara[3];
		byte s7_para_variableSpecificationSyntaxId = pduPara[4];
		// decodeMap.put(Field.s7_para_variableSpecificationType, ByteUtil.getUnsignValue(pduPara[2]));
		// decodeMap.put(Field.s7_para_variableSpecificationLength, ByteUtil.getUnsignValue(pduPara[3]));
		// decodeMap.put(Field.s7_para_variableSpecificationSyntaxId, ByteUtil.getUnsignValue(pduPara[4]));
		// Step 7 Classic 300 400
		if(s7_para_variableSpecificationType == 0x12 && s7_para_variableSpecificationLength == 10
		&& s7_para_variableSpecificationSyntaxId == S7.S7_PARA_VARSPECSYNTAXID_S7ANY) {
			decodeSyntaxIdS7Any(s7_parameter, pduData, pduData);
		}
		// S7-400 special address mode (kind of cyclic read)
		else if(s7_para_variableSpecificationType == 0x12 && s7_para_variableSpecificationLength >= 7
		&& s7_para_variableSpecificationSyntaxId == S7.S7_PARA_VARSPECSYNTAXID_DBREAD) {

		}
		// TIA S7 1200 symbolic address mode
		else if(s7_para_variableSpecificationType == 0x12 && s7_para_variableSpecificationLength >= 14
		&& s7_para_variableSpecificationSyntaxId == S7.S7_PARA_VARSPECSYNTAXID_1200SYM) {

		}
		// Sinumerik NCK access
		else if(s7_para_variableSpecificationType == 0x12 && s7_para_variableSpecificationLength == 8
		&& (s7_para_variableSpecificationSyntaxId == S7.S7_PARA_VARSPECSYNTAXID_NCK
		|| s7_para_variableSpecificationSyntaxId == S7.S7_PARA_VARSPECSYNTAXID_NCKMETRIC
		|| s7_para_variableSpecificationSyntaxId == S7.S7_PARA_VARSPECSYNTAXID_NCKINCH)) {

		}
		// Accessing frequency inverter parameters (via routing)
		else if(s7_para_variableSpecificationType == 0x12 && s7_para_variableSpecificationLength == 10
		&& s7_para_variableSpecificationSyntaxId == S7.S7_PARA_VARSPECSYNTAXID_DRIVEESANY) {

		} else {
			// var spec, length and syntax id are still added to tree here
		}
		return offset;
	}

	public static void decodeSyntaxIdS7Any(FieldValue sss, byte[] pduPara, byte[] pduData) {
		// decodeMap.put(Field.s7_para_transportSize, ByteUtil.getUnsignValue(pduPara[5]));
		byte s7_para_areaType = pduPara[10];
		if(s7_para_areaType == S7.S7_PARA_AREA_DATARECORD) {
			// decodeMap.put(Field.s7_para_readAmount, ByteUtil.getWordAt(pduPara, 6));
			// decodeMap.put(Field.s7_para_readIndex, ByteUtil.getWordAt(pduPara, 8));
			// decodeMap.put(Field.s7_para_areaType, ByteUtil.getUnsignValue(pduPara[10]));
			// decodeMap.put(Field.s7_para_address, ByteUtil.getUnsignValue(pduPara[11], pduPara[12], pdu[13]));

		}
	}

	/**
	 * <li>epan\dissectors\packet-s7comm:s7comm_decode_ud</li>
	 */
	private static void decodeUserData(FieldValue s7, byte[] pduPara, byte[] pduData) {

	}

	private static void decodeReponseReadData(FieldValue s7_data, byte[] pduPara, byte[] pduData, int itemCount) {

	}

	private static void decodeReponseWriteData(FieldValue s7_data, byte[] pduPara, byte[] pduData, int itemCount) {

	}

	private static void decodeSetupCommunication(FieldValue s7_para, byte[] pduPara, byte[] pduData) {

	}

	private static void decodePlcControlUpdownload(FieldValue s7_para, byte[] pduPara, byte[] pduData) {

	}

	private static void decodePiService(FieldValue s7_para, byte[] pduPara, byte[] pduData) {

	}

	private static void decodePlcControlParam(FieldValue s7_para, byte[] pduPara, byte[] pduData) {

	}

	public enum Field {

		s7("S7 Communication"),
		s7_header("Header"),
		s7_parameter("Parameter"),
		s7_data("Data"),
		// s7 header
		s7_header_protocolId("Protocol Id"),
		s7_header_rosctr("ROSCTR"),
		s7_header_redundancyIdentification("Redundancy Identification"),
		s7_header_pduReference("PDU Reference"),
		s7_header_paraLength("Parameter Length"),
		s7_header_dataLength("Data Length"),
		s7_header_errorClass("Error Class"),
		s7_header_errorCode("Error Code"),
		s7_header_errorClassCode("Error Class & Code"),
		// s7 parameter
		s7_para_functionCode("Function Code"),
		s7_para_itemCount("Item Count"),
		s7_para_variableSpecificationType("Variable Specification Type"),
		s7_para_variableSpecificationLength("Variable Specification Length"),
		s7_para_variableSpecificationSyntaxId("Variable Specification Syntax Id"),
		s7_para_transportSize("Transport Size"),
		s7_para_readAmount("Read Amount"),
		s7_para_readIndex("Read Index"),
		s7_para_areaType("Area Type"),
		s7_para_address("Address"),
		;

		private String name;

		private Field(String name) {
			this.name = name;
		}

		public String value() {
			return this.name;
		}
	}

}

class FieldValue {

	private S7Decoder.Field field;
	private byte[] value;
	private String description;
	private ArrayList<FieldValue> children = new ArrayList<FieldValue>();

	public FieldValue(S7Decoder.Field field, byte[] bytes) {
		this.field = field;
		this.value = bytes;
		switch(field){
			case s7_header_rosctr:
				// TODO descrption = bytes对应的rosctr的描述.
				break;
			default:
				break;
		}
	}

	public void print(String parentSpace) {
		if(description == null || description.isEmpty())
			System.out.println(parentSpace + field.value() + " : " + new ByteArray(value).toByteString());
		else
			System.out.println(parentSpace + field.value() + " : " + description + ":" + new ByteArray(value).toByteString());
		for(FieldValue child : children)
			child.print(parentSpace + "   ");
	}

	public FieldValue addChild(FieldValue child) {
		children.add(child);
		return child;
	}

	public byte[] getValue() {
		return this.value;
	}
}
