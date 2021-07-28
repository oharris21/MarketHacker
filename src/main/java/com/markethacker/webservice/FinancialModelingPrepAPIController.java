package com.markethacker.webservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.markethacker.entities.StockOverview;
import com.markethacker.service.AsyncService;
import com.markethacker.service.CustomDataBuilder;
import com.markethacker.service.StockCalculator;
import com.markethacker.entities.HotList;
import com.markethacker.entities.StockDetailed;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class FinancialModelingPrepAPIController {
	// http stuff 
	private static final String URL_BASE = "https://financialmodelingprep.com/api/v3";  
	private static final String API_KEY = "apikey=6838b291d2f831c7571c1d62ae20eb96";  
	private static final String HEADER_BROWSER_INFO = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";  
	
	// paths
	private static final String HISTORICAL_DATA_URL = "/historical-chart/1min/";  
	private static final String QUOTE_URL = "/quote/"; 
	private static final String CURRENT_DAY_URL = "/technical_indicator/1min/AAL?period=10&type=ema&"; 
	private static final String SEARCH_URL = "/search?query="; 
	private static final String STOCK_SCREENER_URL = "/stock-screener?"; 
	
	// stock screener
	private static final String MARKET_CAP_MORE_THAN = "marketCapMoreThan="; 
	private static final String MARKET_CAP_LESS_THAN = "marketCapLessThan="; 
	private static final String PRICE_MORE_THAN = "priceMoreThan="; 
	private static final String PRICE_LESS_THAN = "priceLessThan="; 
	private static final String BETA_MORE_THAN = "betaMoreThan="; 
	private static final String BETA_LESS_THAN = "betaLessThan="; 
	private static final String VOLUME_MORE_THAN = "volumeMoreThan="; 
	private static final String VOLUME_LESS_THAN = "volumeLessThan="; 
	private static final String DIVIDEND_MORE_THAN = "dividendMoreThan="; 
	private static final String DIVIDEND_LESS_THAN = "dividendLessThan="; 
	private static final String SECTOR = "sector="; 
	
	@Autowired
	private RestTemplate restTemplate; 
	
	@Autowired
	private CustomDataBuilder customDataBuilder; 
	
	@Autowired
	private AsyncService asyncService; 
	
	HttpHeaders headers = new HttpHeaders();
	StockCalculator calculator = new StockCalculator(); 

	@GetMapping("/smoke-test")
	public Object smokeTest() {
		Object response = null; 
		
		try {
            headers.add("user-agent", HEADER_BROWSER_INFO);
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            response = restTemplate.exchange(URL_BASE + QUOTE_URL + "AAL?" + API_KEY, HttpMethod.GET,entity,Object.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return response; 
	}
	
	@GetMapping("/get-historical-data")
	public List<StockOverview> getHistoricalData(String symbol) {
		List<StockOverview> stocks = null; 
		
		try {
            ResponseEntity<List<StockOverview>> response =
                    restTemplate.exchange(URL_BASE + HISTORICAL_DATA_URL + symbol + "?" + API_KEY, HttpMethod.GET, null, 
                    		new ParameterizedTypeReference<List<StockOverview>>() {});
            stocks = response.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		return stocks; 
	}
	
	@GetMapping("/get-quote")
	public StockDetailed getQuote(@RequestParam("symbol") String symbol) {
		StockDetailed stock = null; 
		
		try {
            ResponseEntity<List<StockDetailed>> response =
            		restTemplate.exchange(URL_BASE + QUOTE_URL + symbol + "?" + API_KEY, HttpMethod.GET, null, 
            				new ParameterizedTypeReference<List<StockDetailed>>() {});
            stock = response.getBody().get(0);
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		stock = customDataBuilder.addCustomData(stock); 
		return stock; 
	}
	
	@GetMapping("/get-current-day")
	public List<StockOverview> getCurrentDay() {
		List<StockOverview> stocks = null; 
		
		try {
            ResponseEntity<List<StockOverview>> response =
            		restTemplate.exchange(URL_BASE + CURRENT_DAY_URL + API_KEY, HttpMethod.GET, null, 
            				new ParameterizedTypeReference<List<StockOverview>>() {});
            stocks = response.getBody();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return stocks; 
	}
	
	@GetMapping("/search")
	public List<StockOverview> search(@RequestParam("searchString") String searchString) {
		List<StockOverview> stocks = null; 

		try {
            ResponseEntity<List<StockOverview>> response =
            		restTemplate.exchange(URL_BASE + SEARCH_URL + searchString + "&" + API_KEY, HttpMethod.GET, null, 
            				new ParameterizedTypeReference<List<StockOverview>>() {});
            stocks = response.getBody();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return stocks; 
	}
	
	@GetMapping("/stock-screener")
	public List<StockDetailed> stockScreener(@RequestParam(value = "marketCapMoreThan", required = false) String marketCapMoreThan,
											 @RequestParam(value = "marketCapLessThan", required = false) String marketCapLessThan,
											 @RequestParam(value = "priceMoreThan", required = false) Double priceMoreThan, 
											 @RequestParam(value = "priceLessThan", required = false) Double priceLessThan, 
											 @RequestParam(value = "betaMoreThan", required = false) Double betaMoreThan,
											 @RequestParam(value = "betaLessThan", required = false) Double betaLessThan,
											 @RequestParam(value = "volumeMoreThan", required = false) String volumeMoreThan,
											 @RequestParam(value = "volumeLessThan", required = false) String volumeLessThan,
											 @RequestParam(value = "dividendMoreThan", required = false) Double dividendMoreThan,
											 @RequestParam(value = "dividendLessThan", required = false) Double dividendLessThan,
											 @RequestParam(value = "sector", required = false) String sector) {
		
		List<StockDetailed> stocks = null; 
		Integer volumeMoreThanAsInt = null; 
		Integer volumeLessThanAsInt = null; 
		
		if (volumeMoreThan != null) {
			volumeMoreThanAsInt = Integer.valueOf(volumeMoreThan); 
		}
		if (volumeLessThan != null) {
			volumeLessThanAsInt = Integer.valueOf(volumeLessThan); 
		}
		
		if (sector != null) {
			try {
	            ResponseEntity<List<StockDetailed>> response =
	            		restTemplate.exchange(URL_BASE + STOCK_SCREENER_URL + 
	            				MARKET_CAP_MORE_THAN + marketCapMoreThan + "&" + 
	            				MARKET_CAP_LESS_THAN + marketCapLessThan + "&" + 
	            				PRICE_MORE_THAN + priceMoreThan + "&" + 
	            				PRICE_LESS_THAN + priceLessThan + "&" + 
	            				BETA_MORE_THAN + betaMoreThan + "&" + 
	            				BETA_LESS_THAN + betaLessThan + "&" + 
	            				VOLUME_MORE_THAN + volumeMoreThanAsInt + "&" + 
	            				VOLUME_LESS_THAN + volumeLessThanAsInt + "&" + 
	            				DIVIDEND_MORE_THAN + dividendMoreThan + "&" + 
	            				DIVIDEND_LESS_THAN + dividendLessThan + "&" + 
	            				SECTOR + sector + "&" + 
	            				API_KEY, HttpMethod.GET, null, 
	            				new ParameterizedTypeReference<List<StockDetailed>>() {});
	            stocks = response.getBody();
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		} else {
			try {
	            ResponseEntity<List<StockDetailed>> response =
	            		restTemplate.exchange(URL_BASE + STOCK_SCREENER_URL + 
	            				MARKET_CAP_MORE_THAN + marketCapMoreThan + "&" + 
	            				MARKET_CAP_LESS_THAN + marketCapLessThan + "&" + 
	            				PRICE_MORE_THAN + priceMoreThan + "&" + 
	            				PRICE_LESS_THAN + priceLessThan + "&" + 
	            				BETA_MORE_THAN + betaMoreThan + "&" + 
	            				BETA_LESS_THAN + betaLessThan + "&" + 
	            				VOLUME_MORE_THAN + volumeMoreThanAsInt + "&" + 
	            				VOLUME_LESS_THAN + volumeLessThanAsInt + "&" + 
	            				DIVIDEND_MORE_THAN + dividendMoreThan + "&" + 
	            				DIVIDEND_LESS_THAN + dividendLessThan + "&" + 
	            				API_KEY, HttpMethod.GET, null, 
	            				new ParameterizedTypeReference<List<StockDetailed>>() {});
	            stocks = response.getBody();
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		}
		return stocks; 
	}
	
	@GetMapping("/hotlist")
	public HotList getHotList() {
		HotList hotList = null; 
		// call stock screener above - return list of possible HotList stocks 
		List<StockDetailed> hotListApplicableStocks = stockScreener("100000000", null, 10.0, null, null, null, "10000000", null, null, null, null); 
		
		// HL creator iterates through list of stocks and calls StockCalculator methods on each one.  
		// If the stock has a big dip and a B or + support level, add it to the hotlist
		try {
			hotList = asyncService.invoke(hotListApplicableStocks);
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		
		return hotList; 
	}	
	
}
