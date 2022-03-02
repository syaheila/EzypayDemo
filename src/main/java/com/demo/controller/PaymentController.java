package com.demo.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.object.BaseResponse;
import com.demo.object.PaymentRequest;

@RestController
@RequestMapping("/subscription")
public class PaymentController {

	private final String sharedKey = "SHARED_KEY";//put in properties file

	@RequestMapping(value = "/invoice", method = RequestMethod.POST)
	public BaseResponse invoice(@RequestParam(value = "key") String key, @RequestBody PaymentRequest request) {

		BaseResponse response = new BaseResponse();
		String error_msg = "Success";
		int error_code = 0;
		
		try {
			
			if (sharedKey.equalsIgnoreCase(key)) {
				
				double amount = request.getAmount();
				String subsType = request.getSubsType();
			    String subsDay = request.getSubsDay();
			    
			    DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
			    Date startDate = sourceFormat.parse(request.getStartDate());
				Date endDate = sourceFormat.parse(request.getEndDate());
				
				//1.if it was MONTHLY, the date of the month - i.e. 20 - throw error if not number
				try {
					
					int checkNum = -1;
					
					if(subsType.equalsIgnoreCase("MONTHLY")) {
						checkNum = Integer.parseInt(subsDay);
					}
						
					if(subsType.equalsIgnoreCase("WEEKLY")){
						if(!(checkNum < 0
									&& (subsDay.equalsIgnoreCase("SUNDAY")
									|| subsDay.equalsIgnoreCase("MONDAY")
									|| subsDay.equalsIgnoreCase("TUESDAY")
									|| subsDay.equalsIgnoreCase("WEDNESDAY")
									|| subsDay.equalsIgnoreCase("THURSDAY")
									|| subsDay.equalsIgnoreCase("FRIDAY")
									|| subsDay.equalsIgnoreCase("SATURDAY")))
						){
					
							response.setStatus("Invalid subsDay for Subscription Type " + subsType + ": " + subsDay);
							response.setCode(99);
					    	return response;
						}
					}
					
			    } catch (NumberFormatException nfe) {
			    	response.setStatus("Invalid subsDay for Subscription Type " + subsType + ": " + subsDay);
					response.setCode(99);
			    	return response;
			    }
				
				//2.maximum duration of 3 months
				DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				String startFormattedDate = targetFormat.format(startDate);
				String endFormattedDate = targetFormat.format(endDate);
				
				long monthsBetween = ChronoUnit.MONTHS.between(LocalDate.parse(startFormattedDate),
					    LocalDate.parse(endFormattedDate));
				
				if(monthsBetween > 3) {
					response.setStatus("Subscription is more than 3 months");
					response.setCode(99);
			    	return response;
				}else if (monthsBetween < 0){
					response.setStatus("Invalid subscription duration");
					response.setCode(99);
			    	return response;
				}
				
				//3.get invoices according to subsType (Weekly/Monthly/Daily)
				String invoiceDates = getInvoiceDates(subsType, subsDay, startDate, endDate, sourceFormat);
				
				response.setStatus(error_msg);
				response.setCode(error_code);
				response.setAmount(amount);
				response.setSubsType(subsType);
				response.setInvoiceDates(invoiceDates);
				
			} else {
				response.setStatus("Authentication Error");
				response.setCode(102);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static String getInvoiceDates(String subsType, String subsDay, Date startDate, Date endDate, DateFormat sourceFormat) throws ParseException {
		
		String invoiceDates = "[";
		
		Date initialDate = startDate;
		
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int startDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		
		Calendar d = Calendar.getInstance();
		d.setTime(endDate);
		int endDayOfMonth = d.get(Calendar.DAY_OF_MONTH);
		
		//get weekly invoices
		if(subsType.equals("WEEKLY")) {
			
			String newDay = "";
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate d1 = LocalDate.parse(sourceFormat.format(startDate), formatter);
			LocalDate d2 = LocalDate.parse(sourceFormat.format(endDate), formatter);
			LocalDate weekDate = LocalDate.now();
			LocalDate nextWeekDate = LocalDate.now();
			
			if(subsDay.equalsIgnoreCase("SUNDAY")) {
				weekDate = d1.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
				nextWeekDate = d1.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
			}else if(subsDay.equalsIgnoreCase("MONDAY")) {
				weekDate = d1.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
				nextWeekDate = d1.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
			}else if(subsDay.equalsIgnoreCase("TUESDAY")) {
				weekDate = d1.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
				nextWeekDate = d1.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
			}else if(subsDay.equalsIgnoreCase("WEDNESDAY")) {
				weekDate = d1.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
				nextWeekDate = d1.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
			}else if(subsDay.equalsIgnoreCase("THURSDAY")) {
				weekDate = d1.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
				nextWeekDate = d1.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
			}else if(subsDay.equalsIgnoreCase("FRIDAY")) {
				weekDate = d1.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
				nextWeekDate = d1.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
			}else if(subsDay.equalsIgnoreCase("SATURDAY")) {
				weekDate = d1.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
				nextWeekDate = d1.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
			}
			
			if(!subsDay.equalsIgnoreCase(newDay)) {
				subsDay = newDay;
			}
			
			if(!weekDate.equals(nextWeekDate)) {
				weekDate = nextWeekDate;
			}
			
			String firstDayOfWeek = weekDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			initialDate = sourceFormat.parse(firstDayOfWeek);
			invoiceDates += firstDayOfWeek;
			
			//loop 7 days from start date to end date
			while (initialDate.before(endDate)) {

				//plus one day for each loop
		        Calendar calDay = Calendar.getInstance();
		        calDay.setTime(initialDate);
		        calDay.add(Calendar.DATE, 7);
		        initialDate = calDay.getTime();
		        
				invoiceDates += "," + sourceFormat.format(initialDate);
			}
			
			weekDate = initialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			if(weekDate.compareTo(d2)>0) {
				invoiceDates += "," + sourceFormat.format(endDate) + "]";
			}else {
				invoiceDates += "]";
			}
		}
		
		//get monthly invoices
		if(subsType.equals("MONTHLY")) {
			
			DateFormat monthYear = new SimpleDateFormat("MM/yyyy");
			int countDay = 1;
			
			//if the day of the month is less than the start date, then the first invoice is the start date
			if(startDayOfMonth > Integer.parseInt(subsDay)) {
				invoiceDates += sourceFormat.format(initialDate);
				countDay = 0;
			}
			
			//loop from start month to end month
			while (initialDate.before(endDate)) {
				 
			 	if(countDay != 0) {
			 		invoiceDates += subsDay + "/" + monthYear.format(initialDate);
			 		countDay++;
			 	}else {
			 		countDay++;
			 	}
		        
			 	//plus one month for each loop
		        Calendar calMonth = Calendar.getInstance();
		        calMonth.setTime(initialDate);
		        calMonth.add(Calendar.MONTH, 1);
		        initialDate = calMonth.getTime();
		        
		        if(initialDate.before(endDate)) {
		        	invoiceDates += ",";
		        }
			}
			 
			//if the day of the month is more than the end date, then the last invoice is the end date
			if(endDayOfMonth >= Integer.parseInt(subsDay) ||
					endDayOfMonth < Integer.parseInt(subsDay)) {
				invoiceDates += "," + sourceFormat.format(endDate) + "]";
			}else {
				invoiceDates += "]";
			}
		}
		
		//get daily invoices
		if(subsType.equals("DAILY")) {
			
			//loop from start month to end month
			while (initialDate.before(endDate)) {
				
				invoiceDates += sourceFormat.format(initialDate);

				//plus one day for each loop
		        Calendar calDay = Calendar.getInstance();
		        calDay.setTime(initialDate);
		        calDay.add(Calendar.DATE, 1);
		        initialDate = calDay.getTime();
		        
		        if(initialDate.before(endDate)) {
		        	invoiceDates += ",";
		        }
			}
			
			invoiceDates += "," + sourceFormat.format(endDate) + "]";
		}
		
		return invoiceDates;
	}
}
