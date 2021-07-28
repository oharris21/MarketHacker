package com.markethacker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markethacker.entities.StockDetailed;
import com.markethacker.entities.StockOverview;
import com.markethacker.webservice.FinancialModelingPrepAPIController;

@Service
public class CustomDataBuilder {
	
	@Autowired
	private FinancialModelingPrepAPIController controller; 
	
	@Autowired
	private StockCalculator calculator; 
	
	public ArrayList<StockDetailed> hotListBuilder(List<StockDetailed> stocks) {
		ArrayList<StockDetailed> hotListStocks = new ArrayList<>(); 
		
		for (int i = 0; i < stocks.size(); i++) {
			StockDetailed stockWithCustomData = addCustomData(stocks.get(i)); 
			double supportLevelPricePlusOnePercent = stockWithCustomData.getSupportLevelPrice() + 
					calculator.calculatePercentages(stockWithCustomData.getSupportLevelPrice()).get("1%"); 
			
			// add to hotlist -> (supportLevelStrength == A or B) && (bigDip or fiveMinuteDip == true) && price >= support level 
			if ((stockWithCustomData.getSupportLevelStrength().equals("A") || stockWithCustomData.getSupportLevelStrength().equals("B")) &&
					(stockWithCustomData.getBigDip() == true || stockWithCustomData.getFiveMinuteDip() == true) &&
					stockWithCustomData.getPrice() >= stockWithCustomData.getSupportLevelPrice() &&
					stockWithCustomData.getPrice() <= supportLevelPricePlusOnePercent) {
				hotListStocks.add(stockWithCustomData); 
			}
		}
		return hotListStocks; 
	}
	
	public StockDetailed addCustomData(StockDetailed stock) {
		List<StockOverview> stockOverviews = controller.getHistoricalData(stock.getSymbol());
		
		// set big dip data
		Map<String, Object> bigDipInfo = calculator.bigDipFinder(stockOverviews); 
		stock.setBigDip((Boolean) bigDipInfo.get("bigDipTF"));
		stock.setBigDipPercentage((Double) bigDipInfo.get("bigDipPercentage"));
		
		// set support level data
		Map<String, Object> supportLevelAndStrength = calculator.supportLevelCalculator(stockOverviews);
		stock.setSupportLevelPrice((Double) supportLevelAndStrength.get("supportLevelPrice")); 
		stock.setSupportLevelStrength(supportLevelAndStrength.get("supportLevelStrength").toString()); 
		
		// set five minute dip data
		Map<String, Object> fiveMinuteDipInfo = calculator.fiveMinuteDipFinder(stockOverviews); 
		stock.setFiveMinuteDip((Boolean) fiveMinuteDipInfo.get("fiveMinuteDip"));
		stock.setFiveMinuteDipPercentage((Double) fiveMinuteDipInfo.get("fiveMinuteDipPercentage"));
		return stock; 
	}

}
