package com.markethacker;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.markethacker.webservice.FinancialModelingPrepAPIController;

@SpringBootTest
public class WebServiceTests {
	@Autowired
	private FinancialModelingPrepAPIController controller; 
	
	@Test
	void contextLoads() {}
	
	@Test
	public void smokeTest() {
		Object response = controller.smokeTest(); 
		assertNotNull(response);
	}
	
	@Test
	public void getHistoricalDataEndpointTest() {
		assertNotNull(controller.getHistoricalData("AAL"));
	}
	
	@Test
	public void getQuoteEndpointTest() {
		assertNotNull(controller.getQuote("AAL"));
	}
	
	@Test
	public void getCurrentDayEndpointTest() {
		assertNotNull(controller.getCurrentDay());
	}
	
	@Test
	public void getSearchTickerEndpointTest() {
		assertNotNull(controller.search("AAL"));
	}
	
	@Test
	public void getStockScreenerEndpointTest() {
		assertNotNull(controller.stockScreener("100000000", null, 100.00, null, null, null, null, null, null, null, null));
	}
	
	@Test
	public void getHotListEndpointTest() {
		assertNotNull(controller.getHotList());
	}
}
