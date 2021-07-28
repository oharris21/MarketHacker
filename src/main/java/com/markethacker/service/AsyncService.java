package com.markethacker.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markethacker.entities.HotList;
import com.markethacker.entities.StockDetailed;

@Service
public class AsyncService {
	
	@Autowired
	private CustomDataBuilder customDataBuilder; 
	
	private ExecutorService executor = Executors.newFixedThreadPool(5);
	
	public HotList invoke(List<StockDetailed> stocks) throws Throwable {
		Set<Callable<ArrayList<StockDetailed>>> taskList = new HashSet<>();
		ArrayList<ArrayList<StockDetailed>> stocksDivided = divideHotListApplicableStocks(stocks); 
		HotList hotlist = new HotList(); 
		
		for (int i = 0; i < stocksDivided.size(); i++) { 
			
			int j = i; 
			Callable<ArrayList<StockDetailed>> callableTask = () -> {
			    return customDataBuilder.hotListBuilder(stocksDivided.get(j)); 
			};
			taskList.add(callableTask); 
		}
		
		List<Future<ArrayList<StockDetailed>>> getHotList = executor.invokeAll(taskList); 
		
		ArrayList<StockDetailed> hotlistAL = new ArrayList<>(); 
		
		// this is iterating over the master list
		for (int i = 0; i < getHotList.size(); i++) {
			// get the future <ArrayList<StockDetailed>> out of the list on each iteration
			ArrayList<StockDetailed> stocksFromFuture = getHotList.get(i).get(); 
			// assign it to a new arraylist
			hotlistAL.addAll(stocksFromFuture); 
		}
		
		hotlist.setHotListStocks(hotlistAL);
		return hotlist;
	}
	
	/**
	 * Divide up the list of HotList applicable stocks. It's been a little over 100 in size and each iteration
	 * through the loop to see if the stock belongs in the HotList takes a second or two. And that's way too 
	 * long. So we need to break it up and run multiple threads, each with a smaller number of stocks to iterate. 
	 * @param stocks
	 * @return ArrayList<ArrayList<StockDetailed>>
	 */
	public ArrayList<ArrayList<StockDetailed>> divideHotListApplicableStocks(List<StockDetailed> stocks) {
		ArrayList<ArrayList<StockDetailed>> stocksDivided = new ArrayList<>(); 
		ArrayList<StockDetailed> thread1Stocks = new ArrayList<>(); 
		ArrayList<StockDetailed> thread2Stocks = new ArrayList<>(); 
		ArrayList<StockDetailed> thread3Stocks = new ArrayList<>(); 
		ArrayList<StockDetailed> thread4Stocks = new ArrayList<>(); 
		ArrayList<StockDetailed> thread5Stocks = new ArrayList<>(); 
		ArrayList<StockDetailed> thread6Stocks = new ArrayList<>(); 
		ArrayList<StockDetailed> thread7Stocks = new ArrayList<>(); 
		ArrayList<StockDetailed> thread8Stocks = new ArrayList<>(); 
		stocksDivided.add(thread1Stocks); 
		stocksDivided.add(thread2Stocks); 
		stocksDivided.add(thread3Stocks); 
		stocksDivided.add(thread4Stocks); 
		stocksDivided.add(thread5Stocks); 
		stocksDivided.add(thread6Stocks); 
		stocksDivided.add(thread7Stocks); 
		stocksDivided.add(thread8Stocks); 
		
		int counter = 0; 
		
		for (int i = 0; i < stocks.size(); i++) {
			// hard coded 8 because there are going to be 8 threads 
			if (counter < 7) {
				stocksDivided.get(counter).add(stocks.get(i));
				counter ++;
				continue; 
			}
			if (counter == 7) {
				stocksDivided.get(counter).add(stocks.get(i)); 
				counter = 0; 
				continue; 
			}
		}
		
		return stocksDivided; 
	}

}
