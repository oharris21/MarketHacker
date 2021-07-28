package com.markethacker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.markethacker.entities.StockDetailed;
import com.markethacker.entities.StockOverview;
import com.markethacker.service.StockCalculator;
import com.markethacker.testbase.TestBase;

@SpringBootTest
class StockCalculatorTests {
	
	private StockCalculator stockCalculator = new StockCalculator(); 
	DecimalFormat df = new DecimalFormat("#.#");
	TestBase testBase = new TestBase(); 
	
	@Test
	void contextLoads() {}
	
	/**
	 * Is the calculatePercentages function returning the correct values based on the price in the object
	 * it's being passed? 
	 * 
	 * @throws JSONException
	 */
	@Test
	public void calculatePercentagesTest() throws JSONException {
		StockDetailed stockDetailed = new StockDetailed(); 
		
		JSONObject object = testBase.readResourceFileToJSONObject("templates/json/MockStockDetailedObject.json"); 
		stockDetailed = testBase.setStockDetailedObjectFromJSONObject(object); 
		Map<String, Double> stockPricePercentages = stockCalculator.calculatePercentages(stockDetailed.getPrice()); 
		
		assertNotNull(stockPricePercentages);
		
		assertEquals(0.21, stockPricePercentages.get("1%"));
		assertEquals(0.43, stockPricePercentages.get("2%"));
		assertEquals(0.64, stockPricePercentages.get("3%"));
		assertEquals(0.86, stockPricePercentages.get("4%"));
		assertEquals(1.07, stockPricePercentages.get("5%"));
		assertEquals(1.29, stockPricePercentages.get("6%"));
		assertEquals(1.50, stockPricePercentages.get("7%"));
		assertEquals(1.72, stockPricePercentages.get("8%"));
		assertEquals(1.93, stockPricePercentages.get("9%"));
		assertEquals(2.15, stockPricePercentages.get("10%"));
	}
	
	/**
	 * Is the fiveMinuteDipFinder function returning true for all five minute price dips and not giving us any 
	 * false negatives or positives? 
	 * @throws JSONException
	 */
	@Test
	public void fiveMinuteDipFinderTest() throws JSONException {
		List<StockOverview> historicalDataList = new ArrayList<>();  
		
		// test with fiveMinuteDipTrueObject1
		JSONArray array = testBase.readResourceFileToJSONArray("templates/json/MockFiveMinuteDipTrueObject1.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		Map<String, Object> fiveMinuteDipInfo = stockCalculator.fiveMinuteDipFinder(historicalDataList); 
		
		assertTrue(fiveMinuteDipInfo.get("fiveMinuteDip") == (Boolean) true);
		assertTrue(df.format(fiveMinuteDipInfo.get("fiveMinuteDipPercentage")).equals("0.9"));
		
		// test with fiveMinuteDipFalse1Object1
		array = testBase.readResourceFileToJSONArray("templates/json/MockFiveMinuteDipFalseObject1.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		fiveMinuteDipInfo = stockCalculator.fiveMinuteDipFinder(historicalDataList);
		
		assertTrue(fiveMinuteDipInfo.get("fiveMinuteDip") == (Boolean) false);
		assertTrue((Double) fiveMinuteDipInfo.get("fiveMinuteDipPercentage") == 0.00);
		
		// test with fiveMinuteDipFalseObject2
		array = testBase.readResourceFileToJSONArray("templates/json/MockFiveMinuteDipFalseObject2.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		fiveMinuteDipInfo = stockCalculator.fiveMinuteDipFinder(historicalDataList);
		
		assertTrue(fiveMinuteDipInfo.get("fiveMinuteDip") == (Boolean) false);
		assertTrue((Double) fiveMinuteDipInfo.get("fiveMinuteDipPercentage") == 0.00);
	}
	
	/**
	 * Is the bigMinuteDipFinder function returning true for all 5% price dips in the last five minutes and not 
	 * giving us any false negatives or positives? 
	 * @throws JSONException
	 */
	@Test
	public void bigDipFinderTrueFalseTest() throws JSONException {
		List<StockOverview> historicalDataList = new ArrayList<>();  
		Map<String, Object> bigDipInfo = new HashMap<>(); 
		
		// test with bigDipTrueObject1
		JSONArray array = testBase.readResourceFileToJSONArray("templates/json/MockBigDipTrueObject1.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		bigDipInfo = stockCalculator.bigDipFinder(historicalDataList); 
		
		assertTrue((Boolean) bigDipInfo.get("bigDipTF") == true);
		
		// test with bigDipTrueObject2
		array = testBase.readResourceFileToJSONArray("templates/json/MockBigDipTrueObject2.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		bigDipInfo = stockCalculator.bigDipFinder(historicalDataList); 
		
		assertTrue((Boolean) bigDipInfo.get("bigDipTF") == true);
		
		// test with bigDipTrueObject3
		array = testBase.readResourceFileToJSONArray("templates/json/MockBigDipTrueObject3.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		bigDipInfo = stockCalculator.bigDipFinder(historicalDataList); 
		
		assertTrue((Boolean) bigDipInfo.get("bigDipTF") == true);
		
		// test with bigDipFalseObject1
		array = testBase.readResourceFileToJSONArray("templates/json/MockBigDipFalseObject1.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		bigDipInfo = stockCalculator.bigDipFinder(historicalDataList); 
		
		assertTrue((Boolean) bigDipInfo.get("bigDipTF") == false);
	}
	
	/**
	 * Is the bigMinuteDipFinder function returning the correct percent change for the dips it finds? 
	 * @throws JSONException
	 */
	@Test
	public void bigDipFinderReturnsCorrectPercentChangeTest() throws JSONException {
		List<StockOverview> historicalDataList = new ArrayList<>();  
		Map<String, Object> bigDipInfo = new HashMap<>(); 
		
		// test with bigDipTrueObject1
		JSONArray array = testBase.readResourceFileToJSONArray("templates/json/MockBigDipTrueObject1.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		bigDipInfo = stockCalculator.bigDipFinder(historicalDataList); 
		
		assertTrue(df.format(bigDipInfo.get("bigDipPercentage")).equals("5.8"));
		
		// test with bigDipTrueObject2
		array = testBase.readResourceFileToJSONArray("templates/json/MockBigDipTrueObject2.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		bigDipInfo = stockCalculator.bigDipFinder(historicalDataList); 
		
		assertTrue(df.format(bigDipInfo.get("bigDipPercentage")).equals("9.9"));
		
		// test with bigDipTrueObject3
		array = testBase.readResourceFileToJSONArray("templates/json/MockBigDipTrueObject3.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		bigDipInfo = stockCalculator.bigDipFinder(historicalDataList); 
		
		assertTrue(df.format(bigDipInfo.get("bigDipPercentage")).equals("13.8"));
		
		// test with bigDipFalseObject1
		array = testBase.readResourceFileToJSONArray("templates/json/MockBigDipFalseObject1.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		bigDipInfo = stockCalculator.bigDipFinder(historicalDataList); 
		
		assertTrue((Double) bigDipInfo.get("bigDipPercentage") == 0.00);
	}
	
	/**
	 * Does the dipAmountCalculator give us the correct percent change when isolated by itself? 
	 * @throws JSONException
	 */
	@Test
	public void dipAmountCalculatorCalculatesCorrectPercentChangeTest() throws JSONException {
		double currentPrice = 21.41; 
		double beginningOfDipPrice = 23.87; 
		double percentChange = stockCalculator.dipAmountCalculator(currentPrice, beginningOfDipPrice);
		
		assertTrue(Double.valueOf(df.format(percentChange)) == 10.3);
		
		beginningOfDipPrice = 24.00; 
		percentChange = stockCalculator.dipAmountCalculator(currentPrice, beginningOfDipPrice);
		
		assertTrue(Double.valueOf(df.format(percentChange)) == 10.8);
		
		beginningOfDipPrice = 100.0; 
		percentChange = stockCalculator.dipAmountCalculator(currentPrice, beginningOfDipPrice);
		
		assertTrue(Double.valueOf(df.format(percentChange)) == 78.6);
	}
	
	/**
	 * Does the supportLevelCalculator give us the correct support level, supposing there is one, and also is 
	 * the strength grade correct? 
	 * @throws JSONException 
	 */
	@Test
	public void supportLevelCalculatorTest() throws JSONException {
		List<StockOverview> historicalDataList = new ArrayList<>();  
		Map<String, Object> supportLevelInfo = new HashMap<>(); 
		
		// test with MockHistoricalDataObject1.json
		JSONArray array = testBase.readResourceFileToJSONArray("templates/json/MockHistoricalDataObject1.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		supportLevelInfo = stockCalculator.supportLevelCalculator(historicalDataList);
		Double lowestPrice = (Double) supportLevelInfo.get("supportLevelPrice"); 
		Integer timesTested = (Integer) supportLevelInfo.get("supportLevelTestedCount"); 
		String grade = supportLevelInfo.get("supportLevelStrength").toString();
		
		assertNotNull(supportLevelInfo); 
		assertTrue(lowestPrice == 22.63); 
		assertTrue(timesTested == 0); 
		assertTrue(grade.equals("F")); 
		
		// test with MockHistoricalDataObject2.json
		array = testBase.readResourceFileToJSONArray("templates/json/MockHistoricalDataObject2.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		supportLevelInfo = stockCalculator.supportLevelCalculator(historicalDataList);
		lowestPrice = (Double) supportLevelInfo.get("supportLevelPrice"); 
		timesTested = (Integer) supportLevelInfo.get("supportLevelTestedCount"); 
		grade = supportLevelInfo.get("supportLevelStrength").toString();
		
		assertNotNull(supportLevelInfo); 
		assertTrue(lowestPrice == 1518.21); 
		assertTrue(timesTested == 4); 
		assertTrue(grade.equals("B")); 
		
		// test with MockHistoricalDataObject3.json
		array = testBase.readResourceFileToJSONArray("templates/json/MockHistoricalDataObject3.json");
		historicalDataList = testBase.setStockOverviewListFromJSONArray(array); 
		supportLevelInfo = stockCalculator.supportLevelCalculator(historicalDataList);
		lowestPrice = (Double) supportLevelInfo.get("supportLevelPrice"); 
		timesTested = (Integer) supportLevelInfo.get("supportLevelTestedCount"); 
		grade = supportLevelInfo.get("supportLevelStrength").toString();
		
		assertNotNull(supportLevelInfo); 
		assertTrue(lowestPrice == 130.63); 
		assertTrue(timesTested == 7); 
		assertTrue(grade.equals("A")); 
	}
	
	/**
	 * Does the calculateSupportLevelStrength function give us the correct grade for the support level? 
	 * @throws JSONException 
	 */
	@Test
	public void calculateSupportLevelStrengthTest() throws JSONException {
		String supportLevelStrengthGrade = null; 
		
		supportLevelStrengthGrade = stockCalculator.calculateSupportLevelStrength(6); 
		assertTrue(supportLevelStrengthGrade.equals("A"));
		
		supportLevelStrengthGrade = stockCalculator.calculateSupportLevelStrength(10); 
		assertTrue(supportLevelStrengthGrade.equals("A"));
		
		supportLevelStrengthGrade = stockCalculator.calculateSupportLevelStrength(4); 
		assertTrue(supportLevelStrengthGrade.equals("B"));
		
		supportLevelStrengthGrade = stockCalculator.calculateSupportLevelStrength(5); 
		assertTrue(supportLevelStrengthGrade.equals("A"));
		
		supportLevelStrengthGrade = stockCalculator.calculateSupportLevelStrength(2); 
		assertTrue(supportLevelStrengthGrade.equals("D"));
		
		supportLevelStrengthGrade = stockCalculator.calculateSupportLevelStrength(3); 
		assertTrue(supportLevelStrengthGrade.equals("C"));
		
		supportLevelStrengthGrade = stockCalculator.calculateSupportLevelStrength(0); 
		assertTrue(supportLevelStrengthGrade.equals("F"));
		
		supportLevelStrengthGrade = stockCalculator.calculateSupportLevelStrength(1); 
		assertTrue(supportLevelStrengthGrade.equals("F"));
	}
	
}
