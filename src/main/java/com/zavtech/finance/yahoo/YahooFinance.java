/**
 * Copyright (C) 2014-2016 Xavier Witdouck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zavtech.finance.yahoo;

import java.time.LocalDate;
import java.util.Set;

import com.zavtech.morpheus.frame.DataFrame;

/**
 * A convenience class to expose a more specific API to request data from Yahoo Finance
 *
 * @author Xavier Witdouck
 *
 * <p><strong>This is open source software released under the <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache 2.0 License</a></strong></p>
 */
public class YahooFinance {

    /**
     * Static intializer
     */
    static {
        DataFrame.read().register(new YahooOptionQuoteSource());
        DataFrame.read().register(new YahooQuoteHistorySource());
        DataFrame.read().register(new YahooQuoteLiveSource());
        DataFrame.read().register(new YahooReturnSource());
        DataFrame.read().register(new YahooStatsSource());
    }

    /**
     * Constructor
     */
    public YahooFinance() {
        super();
    }

    /**
     * Returns a DataFrame containing equity statistics for the set of tickers specified
     * @param tickers   the set of security tickers
     * @return          the DataFrame of tickers
     */
    public DataFrame<String,YahooField> getEquityStatistics(Set<String> tickers) {
        return DataFrame.read().apply(YahooStatsOptions.class, options -> {
            options.setTickers(tickers);
        });
    }

    /**
     * Returns the option expiry dates for the underlying security ticker
     * @param ticker    the ticker for underlying security
     * @return          the set of option expiry dates
     */
    public Set<LocalDate> getOptionExpiryDates(String ticker) {
        return new YahooOptionQuoteSource().getExpiryDates(ticker);
    }

    /**
     * Returns a DataFrame with option quotes for the ticker on all expiry dates
     * @param ticker    the ticker reference for underlying security
     * @return          the DataFrame with option quotes
     */
    public DataFrame<String,YahooField> getOptionQuotes(String ticker) {
        return DataFrame.read().apply(YahooOptionQuoteOptions.class, options -> {
            options.setTicker(ticker);
        });
    }

    /**
     * Returns a DataFrame with option quotes for the ticker and expiry date specified
     * @param ticker    the ticker reference for underlying security
     * @param expiry    the expiry date expressed in ISO date formate yyyy-MM-dd
     * @return          the DataFrame with option quotes
     */
    public DataFrame<String,YahooField> getOptionQuotes(String ticker, String expiry) {
        return DataFrame.read().apply(YahooOptionQuoteOptions.class, options -> {
            options.setTicker(ticker);
            options.setExpiry(LocalDate.parse(expiry));
        });
    }

    /**
     * Returns a DataFrame with option quotes for the ticker and expiry date specified
     * @param ticker    the ticker reference for underlying security
     * @param expiry    the expiry date
     * @return          the DataFrame with option quotes
     */
    public DataFrame<String,YahooField> getOptionQuotes(String ticker, LocalDate expiry) {
        return DataFrame.read().apply(YahooOptionQuoteOptions.class, options -> {
            options.setTicker(ticker);
            options.setExpiry(expiry);
        });
    }


    /**
     * Returns a DataFrame of daily returns
     * @param start     the start date
     * @param end       the end date
     * @param tickers   the vector of tickers
     * @return          the frame of returns
     */
    public DataFrame<LocalDate,String> getDailyReturns(LocalDate start, LocalDate end, String...tickers) {
        return DataFrame.read().apply(YahooReturnOptions.class, options -> {
            options.setStart(start);
            options.setEnd(end);
            options.setType(YahooReturnOptions.Type.DAILY);
            options.setTickers(tickers);
        });
    }


    /**
     * Returns a DataFrame of cumulative returns
     * @param start     the start date
     * @param end       the end date
     * @param tickers   the vector of tickers
     * @return          the frame of returns
     */
    public DataFrame<LocalDate,String> getCumReturns(LocalDate start, LocalDate end, String...tickers) {
        return DataFrame.read().apply(YahooReturnOptions.class, options -> {
            options.setStart(start);
            options.setEnd(end);
            options.setType(YahooReturnOptions.Type.DAILY);
            options.setTickers(tickers);
        });
    }

    /**
     * Returns a DataFrame of live quotes for the tickers specified and all available fields
     * @param tickers   the set of security tickers
     * @return          the DataFrame of live quote data for all fields
     */
    public DataFrame<String,YahooField> getLiveQuotes(Set<String> tickers) {
        return DataFrame.read().apply(YahooQuoteLiveOptions.class, options -> {
           options.setTickers(tickers);
        });
    }

    /**
     * Returns a DataFrame of live quotes for the tickers and fields specified
     * @param tickers   the set of security tickers
     * @param fields    the set of fields
     * @return          the DataFrame of live quote data for all fields
     */
    public DataFrame<String,YahooField> getLiveQuotes(Set<String> tickers, YahooField... fields) {
        return DataFrame.read().apply(YahooQuoteLiveOptions.class, options -> {
            options.setTickers(tickers);
            options.setFields(fields);
        });
    }

    /**
     * Returns end of day OHLC quote bars for the security over the date range specified
     * @param ticker        the security ticker symbol
     * @param start         the start date for range, expressed as an ISO date of yyyy-MM-dd
     * @param end           the end date for range, expressed as an ISO date of yyyy-MM-dd
     * @param adjusted      true to adjust prices for splits and dividends
     * @return              the DataFrame contains OHLC end of day bars
     */
    public DataFrame<LocalDate,YahooField> getEndOfDayQuotes(String ticker, String start, String end, boolean adjusted) {
        return DataFrame.read().apply(YahooQuoteHistoryOptions.class, options -> {
            options.setTicker(ticker);
            options.setStart(LocalDate.parse(start));
            options.setEnd(LocalDate.parse(end));
            options.setAdjustForSplits(adjusted);
        });
    }

    /**
     * Returns end of day OHLC quote bars for the security over the date range specified
     * @param ticker        the security ticker symbol
     * @param start         the start date for range
     * @param end           the end date for range
     * @param adjusted      true to adjust prices for splits and dividends
     * @return              the DataFrame contains OHLC end of day bars
     */
    public DataFrame<LocalDate,YahooField> getEndOfDayQuotes(String ticker, LocalDate start, LocalDate end, boolean adjusted) {
        return DataFrame.read().apply(YahooQuoteHistoryOptions.class, options -> {
            options.setTicker(ticker);
            options.setStart(start);
            options.setEnd(end);
            options.setAdjustForSplits(adjusted);
        });
    }

}