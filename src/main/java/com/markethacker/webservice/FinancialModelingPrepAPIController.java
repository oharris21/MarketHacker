package com.markethacker.webservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.markethacker.entities.StockOverview;
import com.markethacker.entities.StockDetailed;
import com.markethacker.stocklogic.StockCalculator;

@RestController
public class FinancialModelingPrepAPIController {
	// http stuff 
	private static final String URL_BASE = "https://financialmodelingprep.com/api/v3";  
	private static final String API_KEY = "apikey=6838b291d2f831c7571c1d62ae20eb96";  
	private static final String HEADER_BROWSER_INFO = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";  
	
	// path & params
	private static final String HISTORICAL_DATA_URL = "/historical-chart/1min/AAL?";  
	private static final String QUOTE_URL = "/quote/AAL?"; 
	private static final String CURRENT_DAY_URL = "/technical_indicator/1min/AAL?period=10&type=ema&"; 
	private static final String SEARCH_TICKER_URL = "/search-ticker?query=AA&"; 
	private static final String STOCK_SCREENER_URL = "/stock-screener?"; 
	private static final String MARKET_CAP_PARAM = "marketCapMoreThan=1000000000"; 
	private static final String VOLUME_PARAM = "&volumeMoreThan=10000000"; 
//	private static final String EXCHANGE_PARAM = "&exchange=NASDAQ"; 
	private static final String PRICE_PARAM = "&priceMoreThan=10&"; 
	
	@Autowired
	private RestTemplate restTemplate; 
	
	HttpHeaders headers = new HttpHeaders();
	StockCalculator calculator = new StockCalculator(); 

	@GetMapping("/smoke-test")
	public Object smokeTest() {
		Object response = null; 
		
		try {
            headers.add("user-agent", HEADER_BROWSER_INFO);
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            response = restTemplate.exchange(URL_BASE + QUOTE_URL + API_KEY, HttpMethod.GET,entity,Object.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return response; 
	}
	
	@GetMapping("/get-historical-data")
	public List<StockOverview> getHistoricalData() {
		List<StockOverview> stocks = null; 
		
		try {
            ResponseEntity<List<StockOverview>> response =
                    restTemplate.exchange(URL_BASE + HISTORICAL_DATA_URL + API_KEY, HttpMethod.GET, null, 
                    		new ParameterizedTypeReference<List<StockOverview>>() {});
            stocks = response.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		return stocks; 
	}
	
	@GetMapping("/get-quote")
	public StockDetailed getQuote() {
		StockDetailed stock = null; 
		List<StockDetailed> stocks = null; 
		
		try {
            ResponseEntity<List<StockDetailed>> response =
            		restTemplate.exchange(URL_BASE + QUOTE_URL + API_KEY, HttpMethod.GET, null, 
            				new ParameterizedTypeReference<List<StockDetailed>>() {});
            stocks = response.getBody();
            stock = stocks.get(0); 
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
	
	@GetMapping("/search-ticker")
	public List<StockOverview> search() {
		List<StockOverview> stocks = null; 
		
		try {
            ResponseEntity<List<StockOverview>> response =
            		restTemplate.exchange(URL_BASE + SEARCH_TICKER_URL + API_KEY, HttpMethod.GET, null, 
            				new ParameterizedTypeReference<List<StockOverview>>() {});
            stocks = response.getBody();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return stocks; 
	}
	
	// haven't ironed out all these http params yet
	@GetMapping("/stock-screener")
	public List<StockDetailed> stockScreener() {
		List<StockDetailed> stocks = null; 
		
		try {
            ResponseEntity<List<StockDetailed>> response =
            		restTemplate.exchange(URL_BASE + STOCK_SCREENER_URL + MARKET_CAP_PARAM + VOLUME_PARAM
            				+ PRICE_PARAM + API_KEY, HttpMethod.GET, null, 
            				new ParameterizedTypeReference<List<StockDetailed>>() {});
            stocks = response.getBody();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return stocks; 
	}
	
	// Get a single object from response instead of list of objects. 
    // stock = restTemplate.getForObject(url, Stock.class);
	
}
