package com.markethacker.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.markethacker.entities.StockOverview;

@Service
public class StockCalculator {
	
	DecimalFormat df = new DecimalFormat("#.##");
	
	/**
	 * Calculate the percent values of a stock price from 1% to 10% 
	 * @param stock
	 * @return
	 */
	public Map<String, Double> calculatePercentages(Double price) {
		Map<String, Double> percentages = new HashMap<>();
		percentages.put("1%", Double.valueOf(df.format(price / 100))); 
		percentages.put("2%", Double.valueOf(df.format((price / 100) * 2))); 
		percentages.put("3%", Double.valueOf(df.format((price / 100) * 3))); 
		percentages.put("4%", Double.valueOf(df.format((price / 100) * 4))); 
		percentages.put("5%", Double.valueOf(df.format((price / 100) * 5))); 
		percentages.put("6%", Double.valueOf(df.format((price / 100) * 6))); 
		percentages.put("7%", Double.valueOf(df.format((price / 100) * 7))); 
		percentages.put("8%", Double.valueOf(df.format((price / 100) * 8))); 
		percentages.put("9%", Double.valueOf(df.format((price / 100) * 9))); 
		percentages.put("10%", Double.valueOf(df.format((price / 100) * 10))); 
		return percentages; 
	}
	
	/**
	 * Has there been a price dip in the last five minutes? 
	 * @param oneMinuteStockOverviews
	 * @return
	 */
	public Map<String, Object> fiveMinuteDipFinder(List<StockOverview> oneMinuteStockOverviews) {
		Map<String, Object> fiveMinuteDipInfo = new HashMap<>(); 
		boolean fiveMinuteDip = false; 
		Double minuteOnePrice = null;
		boolean skipMinuteTwo = false; 
		boolean arriveAtMinuteThree = false; 
		Double minuteThreePrice = null; 
		boolean skipMinuteFour = false; 
		Double minuteFivePrice = null; 
		
		for (int i = 0; i < oneMinuteStockOverviews.size(); i++) {
			if (skipMinuteFour == true && oneMinuteStockOverviews.get(i).getClose() < minuteThreePrice) {
				fiveMinuteDip = true; 
				minuteFivePrice = oneMinuteStockOverviews.get(i).getClose(); 
				break; 
			}
			if (minuteThreePrice != null && skipMinuteFour == false) {
				skipMinuteFour = true; 
				continue; 
			}
			if (skipMinuteTwo == true && arriveAtMinuteThree == true) {
				if (oneMinuteStockOverviews.get(i).getClose() < minuteOnePrice) {
					minuteThreePrice = oneMinuteStockOverviews.get(i).getClose();  
					continue; 
				} else {
					break; 
				}
			}
			if (minuteOnePrice != null && skipMinuteTwo == false) {
				skipMinuteTwo = true; 
				arriveAtMinuteThree = true;
				continue; 
			}
			if (minuteOnePrice == null && skipMinuteTwo == false) {
				minuteOnePrice = oneMinuteStockOverviews.get(i).getClose(); 
				continue; 
			}
			if (minuteOnePrice == null) {
				break; 
			}
		}
		
		if (fiveMinuteDip == true) {
			fiveMinuteDipInfo.put("fiveMinuteDip", true);
			fiveMinuteDipInfo.put("fiveMinuteDipPercentage", dipAmountCalculator(minuteFivePrice, minuteOnePrice));
		} else {
			fiveMinuteDipInfo.put("fiveMinuteDip", false);
			fiveMinuteDipInfo.put("fiveMinuteDipPercentage", 0.00);
		}
		return fiveMinuteDipInfo; 
	}
	
	/**
	 * Has there been a big price dip (5% price drop or more) in the last five minutes? 
	 * @param oneMinuteStockOverviews
	 * @return
	 */
	public Map<String, Object> bigDipFinder(List<StockOverview> oneMinuteStockOverviews) {
		double currentPrice = oneMinuteStockOverviews.get(0).getClose();
		Map<String, Object> bigDipInfo = new HashMap<>(); 
		
		for (int i = 1; i < 5; i++) {
			// use calculatePercentages to get 5% of minuteFivePrice
			Map<String, Double> percentages = calculatePercentages(oneMinuteStockOverviews.get(i).getClose()); 
			double fivePercent = percentages.get("5%"); 
			
			if ((oneMinuteStockOverviews.get(i).getClose() - currentPrice) >= fivePercent) {
				bigDipInfo.put("bigDipTF", true); 
				
				// get the percent change of the dip 
				Double bigDipPercentage = dipAmountCalculator(currentPrice, oneMinuteStockOverviews.get(i).getClose()); 
				bigDipInfo.put("bigDipPercentage", bigDipPercentage); 
				break; 
			}
			
		}
		
		// bigDipTF only gets added to the map if the data meets the big dip condition. It still needs to be 
		// added eventually, even if it's false. Add it here, at the end of the function. 
		if (!bigDipInfo.containsKey("bigDipTF")) {
			bigDipInfo.put("bigDipTF", false); 
		}
		if (!bigDipInfo.containsKey("bigDipPercentage")) {
			bigDipInfo.put("bigDipPercentage", 0.00); 
		}
		return bigDipInfo; 
	}
	
	/**
	 * By what percentage has the price dipped in the last five minutes? 
	 * @param currentPrice
	 * @param beginningOfDipPrice
	 * @param minutes
	 * @return
	 */
	public Double dipAmountCalculator(double currentPrice, double beginningOfDipPrice) {
		return Double.valueOf(df.format(100 - ((currentPrice / beginningOfDipPrice) * 100))); 
	}
	
	/**
	 * Find the support level for a stock if there is one. There doesn't have to be one. Also return the strength
	 * of the support level with a grade from A-F. 
	 * 
	 * Not sure what the best thing for this to return is. Can't use a map because we need key access, not only
	 * value access. Trying an arraylist now. 
	 * @param stocks
	 * @return
	 */
	public Map<String, Object> supportLevelCalculator(List<StockOverview> oneMinuteStockOverviews) {
		Map<String, Object> supportLevelAndStrength = new HashMap<>(); 
		Double lowestPrice = null; 
		Integer supportLevelTestedCount = 0; 
		
		for (int i = 0; i < oneMinuteStockOverviews.size(); i++) {
			// this should only run when i = 0 
			if (lowestPrice == null) {
				lowestPrice = oneMinuteStockOverviews.get(i).getLow(); 
				continue; 
			}
			// once i >= 1, start comparing the lows 
			if (lowestPrice != null) {
				if (oneMinuteStockOverviews.get(i).getLow() < lowestPrice) {
					lowestPrice = oneMinuteStockOverviews.get(i).getLow(); 
				}
			}
		}
		
		boolean onePercentOrGreaterThanLowestPrice = false; 
		Double onePercentOfLow = calculatePercentages(lowestPrice).get("1%");
		Double lowPlusOnePercent = lowestPrice + onePercentOfLow; 
		
		for (int i = 0; i < oneMinuteStockOverviews.size(); i++) {
			if (oneMinuteStockOverviews.get(i).getClose() > lowPlusOnePercent) {
				onePercentOrGreaterThanLowestPrice = true; 
				continue; 
			}
			if (oneMinuteStockOverviews.get(i).getClose() <= lowPlusOnePercent 
					&& onePercentOrGreaterThanLowestPrice == true ) {
				supportLevelTestedCount ++; 
				onePercentOrGreaterThanLowestPrice = false; 
			}
		}
		
		supportLevelAndStrength.put("supportLevelPrice", lowestPrice);
		supportLevelAndStrength.put("supportLevelTestedCount", supportLevelTestedCount);
		supportLevelAndStrength.put("supportLevelStrength", calculateSupportLevelStrength(supportLevelTestedCount));
		return supportLevelAndStrength; 
	}
	
	/**
	 * What is the strength grade of the support level? 
	 * @param numberOfTimesTested
	 * @return
	 */
	public String calculateSupportLevelStrength(Integer numberOfTimesTested) {
		String grade = "F"; 
		
		if (numberOfTimesTested == 2) {
			grade = "D"; 
		}
		else if (numberOfTimesTested == 3) {
			grade = "C"; 
		}
		else if (numberOfTimesTested == 4) {
			grade = "B"; 
		}
		else if (numberOfTimesTested >= 5) {
			grade = "A"; 
		}
		
		return grade; 
	}
	
}
