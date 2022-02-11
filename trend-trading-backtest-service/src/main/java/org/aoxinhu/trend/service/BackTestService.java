package org.aoxinhu.trend.service;

import org.aoxinhu.trend.client.IndexDataClient;
import org.aoxinhu.trend.pojo.IndexData;
import org.aoxinhu.trend.pojo.Profit;
import org.aoxinhu.trend.pojo.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
@Service
public class BackTestService {
    @Autowired IndexDataClient indexDataClient;
     
    public List<IndexData> listIndexData(String code){
        List<IndexData> result = indexDataClient.getIndexData(code);
        Collections.reverse(result);
         
        // for (IndexData indexData : result) {
        //     System.out.println(indexData.getDate());
        // }
         
        return result;
    }

    public Map<String,Object> simulate(int ma, float sellRate, float buyRate, float serviceCharge, List<IndexData> indexDatas)  {
 
        List<Profit> profits =new ArrayList<>();
        List<Trade> trades = new ArrayList<>();

        float initCash = 1000;
        float cash = initCash;
        float share = 0;
        float value = 0;
 
        float init =0;
        if(!indexDatas.isEmpty())
            init = indexDatas.get(0).getClosePoint();

        for (int i = 0; i<indexDatas.size() ; i++) {
            IndexData indexData = indexDatas.get(i);
            float closePoint = indexData.getClosePoint();
            float avg = getMA(i,ma,indexDatas);
            float max = getMax(i,ma,indexDatas);

            float increase_rate = closePoint/avg;
            float decrease_rate = closePoint/max;
 
            if(avg!=0) {
                // buy
                if(increase_rate>buyRate  ) {
                    // if havn't bought
                    if(0 == share) {
                        share = cash / closePoint;
                        cash = 0;

                        // creat new object
                        Trade trade = new Trade();
                        trade.setBuyDate(indexData.getDate());
                        trade.setBuyClosePoint(indexData.getClosePoint());
                        trade.setSellDate("n/a");
                        trade.setSellClosePoint(0);
                        trades.add(trade);
                    }
                }
                // sell
                else if(decrease_rate<sellRate ) {
                    // if havn't sold
                    if(0!= share){
                        cash = closePoint * share * (1-serviceCharge);
                        share = 0;

                        // edit previous object
                        Trade trade =trades.get(trades.size()-1);
                        trade.setSellDate(indexData.getDate());
                        trade.setSellClosePoint(indexData.getClosePoint());

                        float rate = cash / initCash;
                        trade.setRate(rate);
                    }
                }
                // do nothing
                else{
 
                }
            }
 
            if(share!=0) {
                value = closePoint * share;
            }
            else {
                value = cash;
            }
            float rate = value/initCash;
 
            Profit profit = new Profit();
            profit.setDate(indexData.getDate());
            profit.setValue(rate*init);
             
            System.out.println("profit.value:" + profit.getValue());
            profits.add(profit);
 
        }
        Map<String,Object> map = new HashMap<>();
        map.put("profits", profits);
        map.put("trades", trades);
        return map;
    }
 
    /**
     * get the MAX close point from i-1-day to i-1
     * for decrease rate
     */
    private static float getMax(int i, int day, List<IndexData> list) {
        int start = i-1-day;
        if(start<0)
            start = 0;

        int now = i-1;
 
        if(start<0)
            return 0;
 
        float max = 0;
        for (int j = start; j < now; j++) {
            IndexData bean =list.get(j);
            if(bean.getClosePoint()>max) {
                max = bean.getClosePoint();
            }
        }
        return max;
    }

    /**
     * the average close point value of last {ma} days
     * for increase rate
     */
    private static float getMA(int i, int ma, List<IndexData> list) {
        int start = i-1-ma;
        int now = i-1;
 
        if(start<0)
            return 0;
 
        float sum = 0;
        float avg = 0;
        for (int j = start; j < now; j++) {
            IndexData bean =list.get(j);
            sum += bean.getClosePoint();
        }
        avg = sum / (now - start);
        return avg;
    }
}