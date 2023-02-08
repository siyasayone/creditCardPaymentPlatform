/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.creditCardPayment.util.date;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonDateDeserializer extends JsonDeserializer<Date> {

	// private static final SimpleDateFormat dateFormat = new
	// SimpleDateFormat(ApplicationConstants.applicationProperties.getProperty("format.date"));

	/**
	 *
	 * @param jsonparser JSON parser
	 * @param dc         Deserialization context
	 * @return parsed date
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext dc)
			throws IOException, JsonProcessingException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String date = jsonparser.getText();
		if (!date.equalsIgnoreCase("")) {
			try {
				return dateFormat.parse(date);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		} else {
			return new Date();
		}
	}
}
