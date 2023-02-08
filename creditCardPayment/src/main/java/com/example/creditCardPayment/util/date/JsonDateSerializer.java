/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.creditCardPayment.util.date;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

	// private static final SimpleDateFormat dateFormat = new
	// SimpleDateFormat(ApplicationConstants.applicationProperties.getProperty("format.date"));

	/**
	 *
	 * @param date     input date
	 * @param gen      JSON generator
	 * @param provider Serializer provider
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = dateFormat.format(date);
		gen.writeString(formattedDate);
	}
}
