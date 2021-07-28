package com.markethacker.testbase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.markethacker.entities.StockDetailed;
import com.markethacker.entities.StockOverview;

public class TestBase {
	
	public JSONObject readResourceFileToJSONObject(String fileName) {
		JSONObject fileConvertedToJson = null;
		ClassLoader classLoader = this.getClass().getClassLoader(); 
		File file = new File(classLoader.getResource(fileName).getFile()); 
		
		try {
			fileConvertedToJson = new JSONObject(new String(Files.readAllBytes(file.toPath())));
		} catch (IOException | JSONException io) {
			io.printStackTrace();
		}
		return fileConvertedToJson; 
	}
	
	public JSONArray readResourceFileToJSONArray(String fileName) {
		JSONArray fileConvertedToJson = null;
		ClassLoader classLoader = this.getClass().getClassLoader(); 
		File file = new File(classLoader.getResource(fileName).getFile()); 
		
		try {
			fileConvertedToJson = new JSONArray(new String(Files.readAllBytes(file.toPath())));
		} catch (IOException | JSONException io) {
			io.printStackTrace();
		}
		return fileConvertedToJson; 
	}
	
	public StockDetailed setStockDetailedObjectFromJSONObject(JSONObject jsonObject) throws JSONException {
		StockDetailed stockDetailed = new StockDetailed(); 
		
		stockDetailed.setSymbol(jsonObject.getString("symbol"));
//		stockDetailed.setCompanyName(jsonObject.getString("companyName"));
		stockDetailed.setName(jsonObject.getString("name"));
		stockDetailed.setPrice(jsonObject.getDouble("price"));
		stockDetailed.setChangesPercentage(jsonObject.getDouble("changesPercentage"));
		stockDetailed.setChange(jsonObject.getDouble("change"));
		stockDetailed.setDayLow(jsonObject.getDouble("dayLow"));
		stockDetailed.setDayHigh(jsonObject.getDouble("dayHigh"));
		stockDetailed.setYearHigh(jsonObject.getDouble("yearHigh"));
		stockDetailed.setYearLow(jsonObject.getDouble("yearLow"));
		stockDetailed.setMarketCap(jsonObject.getDouble("marketCap"));
		stockDetailed.setPriceAvg50(jsonObject.getDouble("priceAvg50"));
		stockDetailed.setPriceAvg200(jsonObject.getDouble("priceAvg200"));
		stockDetailed.setVolume(jsonObject.getDouble("volume"));
		stockDetailed.setAvgVolume(jsonObject.getDouble("avgVolume"));
		stockDetailed.setExchange(jsonObject.getString("exchange"));
		stockDetailed.setOpen(jsonObject.getDouble("open"));
		stockDetailed.setPreviousClose(jsonObject.getDouble("previousClose"));
		stockDetailed.setEps(jsonObject.getDouble("eps"));
		stockDetailed.setEarningsAnnouncement(jsonObject.getString("earningsAnnouncement"));
		stockDetailed.setSharesOutstanding(jsonObject.getLong("sharesOutstanding"));
		return stockDetailed; 
	}
	
	public List<StockOverview> setStockOverviewListFromJSONArray(JSONArray jsonArray) throws JSONException {
		List<StockOverview> historicalDataList = new ArrayList<>(); 
		
		for (int i = 0; i < jsonArray.length(); i++) {
			StockOverview stockOverview = new StockOverview();
			stockOverview.setDate(jsonArray.getJSONObject(i).getString("date"));
			stockOverview.setOpen(jsonArray.getJSONObject(i).getDouble("open"));
			stockOverview.setLow(jsonArray.getJSONObject(i).getDouble("low"));
			stockOverview.setHigh(jsonArray.getJSONObject(i).getDouble("high"));
			stockOverview.setClose(jsonArray.getJSONObject(i).getDouble("close"));
			stockOverview.setVolume(jsonArray.getJSONObject(i).getDouble("volume"));
			historicalDataList.add(stockOverview);
		}
		return historicalDataList; 
	}

}
