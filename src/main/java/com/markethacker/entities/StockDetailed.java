package com.markethacker.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDetailed {
	
	public String symbol;//
	public String name; 
    public Double price; 
    public Double changesPercentage; 
    public Double change;
    public Double dayLow;
    public Double dayHigh;
    public Double yearHigh;
    public Double yearLow;
    public Double marketCap;
    public Double priceAvg50;
    public Double priceAvg200;
    public Double volume;//
    public Double avgVolume;
    public String exchange; 
    public Double open;//
    public Double previousClose;
    public Double eps;
    public Double pe;
    public String earningsAnnouncement;
    public Long sharesOutstanding;
    public Date  timestamp;
    public String companyName; 
    public String sector;
    public String industry; 
    public String exchangeShortName; 
    public String country; 
    public String isEtf; 
    public String isActivelyTrading; 
    
    // hotlist and personal add ons 
    public boolean bigDip; 
    public Double bigDipPercentage; 
    public Double supportLevelPrice;
    public String supportLevelStrength;
    public boolean fiveMinuteDip; 
    public Double fiveMinuteDipPercentage; 
    
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getChangesPercentage() {
		return changesPercentage;
	}
	public void setChangesPercentage(Double changesPercentage) {
		this.changesPercentage = changesPercentage;
	}
	public Double getChange() {
		return change;
	}
	public void setChange(Double change) {
		this.change = change;
	}
	public Double getDayLow() {
		return dayLow;
	}
	public void setDayLow(Double dayLow) {
		this.dayLow = dayLow;
	}
	public Double getDayHigh() {
		return dayHigh;
	}
	public void setDayHigh(Double dayHigh) {
		this.dayHigh = dayHigh;
	}
	public Double getYearHigh() {
		return yearHigh;
	}
	public void setYearHigh(Double yearHigh) {
		this.yearHigh = yearHigh;
	}
	public Double getYearLow() {
		return yearLow;
	}
	public void setYearLow(Double yearLow) {
		this.yearLow = yearLow;
	}
	public Double getMarketCap() {
		return marketCap;
	}
	public void setMarketCap(Double marketCap) {
		this.marketCap = marketCap;
	}
	public Double getPriceAvg50() {
		return priceAvg50;
	}
	public void setPriceAvg50(Double priceAvg50) {
		this.priceAvg50 = priceAvg50;
	}
	public Double getPriceAvg200() {
		return priceAvg200;
	}
	public void setPriceAvg200(Double priceAvg200) {
		this.priceAvg200 = priceAvg200;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public Double getAvgVolume() {
		return avgVolume;
	}
	public void setAvgVolume(Double avgVolume) {
		this.avgVolume = avgVolume;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public Double getOpen() {
		return open;
	}
	public void setOpen(Double open) {
		this.open = open;
	}
	public Double getPreviousClose() {
		return previousClose;
	}
	public void setPreviousClose(Double previousClose) {
		this.previousClose = previousClose;
	}
	public Double getEps() {
		return eps;
	}
	public void setEps(Double eps) {
		this.eps = eps;
	}
	public Double getPe() {
		return pe;
	}
	public void setPe(Double pe) {
		this.pe = pe;
	}
	public String getEarningsAnnouncement() {
		return earningsAnnouncement;
	}
	public void setEarningsAnnouncement(String earningsAnnouncement) {
		this.earningsAnnouncement = earningsAnnouncement;
	}
	public Long getSharesOutstanding() {
		return sharesOutstanding;
	}
	public void setSharesOutstanding(Long sharesOutstanding) {
		this.sharesOutstanding = sharesOutstanding;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getExchangeShortName() {
		return exchangeShortName;
	}
	public void setExchangeShortName(String exchangeShortName) {
		this.exchangeShortName = exchangeShortName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getIsEtf() {
		return isEtf;
	}
	public void setIsEtf(String isEtf) {
		this.isEtf = isEtf;
	}
	public String getIsActivelyTrading() {
		return isActivelyTrading;
	}
	public void setIsActivelyTrading(String isActivelyTrading) {
		this.isActivelyTrading = isActivelyTrading;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getBigDip() {
		return bigDip;
	}
	public void setBigDip(boolean bigDip) {
		this.bigDip = bigDip;
	}
	public String getSupportLevelStrength() {
		return supportLevelStrength;
	}
	public void setSupportLevelStrength(String supportLevelStrength) {
		this.supportLevelStrength = supportLevelStrength;
	}
	public Double getSupportLevelPrice() {
		return supportLevelPrice;
	}
	public void setSupportLevelPrice(Double supportLevel) {
		this.supportLevelPrice = supportLevel;
	}
	public Double getBigDipPercentage() {
		return bigDipPercentage;
	}
	public void setBigDipPercentage(Double bigDipPercentage) {
		this.bigDipPercentage = bigDipPercentage;
	}
	public boolean getFiveMinuteDip() {
		return fiveMinuteDip;
	}
	public void setFiveMinuteDip(boolean fiveMinuteDip) {
		this.fiveMinuteDip = fiveMinuteDip;
	}
	public Double getFiveMinuteDipPercentage() {
		return fiveMinuteDipPercentage;
	}
	public void setFiveMinuteDipPercentage(Double fiveMinuteDipPercentage) {
		this.fiveMinuteDipPercentage = fiveMinuteDipPercentage;
	}
}
