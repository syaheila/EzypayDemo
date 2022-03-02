package com.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

	@Autowired MockMvc mockMvc;
	
	private String input;
	
	@BeforeEach
    void setUp(){
		input = "{\r\n"
        		+ "    \"amount\":22,\r\n"
        		+ "    \"subsType\":\"WEEKLY\",\r\n"
        		+ "    \"subsDay\": \"THURSDAY\",\r\n"
        		+ "    \"startDate\":\"23/02/2022\",\r\n"
        		+ "    \"endDate\":\"01/04/2022\"\r\n"
        		+ "}";
    }
    
	@Test
    void invoice() throws Exception {
		
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/subscription/invoice?key=SHARED_KEY")
                .content(asJsonString(input))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        
        System.out.print("result: " + result);
    }
	
	public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	@Test
	void testInvoice() throws Exception{
		
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		String inpStart = "23/02/2022";
		String inpEnd = "23/04/2022";
		Date startDate = sourceFormat.parse(inpStart);
		Date endDate = sourceFormat.parse(inpEnd);
		
		assertAll(PaymentController.getInvoiceDates("WEEKLY", "THURSDAY", startDate, endDate, sourceFormat));
    }

}
