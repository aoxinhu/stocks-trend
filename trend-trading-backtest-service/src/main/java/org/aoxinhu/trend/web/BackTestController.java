package org.aoxinhu.trend.web;

import org.aoxinhu.trend.pojo.AnnualProfit;
import org.aoxinhu.trend.pojo.IndexData;
import org.aoxinhu.trend.pojo.Profit;
import org.aoxinhu.trend.pojo.Trade;
import org.aoxinhu.trend.service.BackTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
  
@RestController
public class BackTestController {
    @Autowired BackTestService backTestService;
 
    // @GetMapping("/simulate/{code}")
    // @CrossOrigin
    // public Map<String,Object> backTest(@PathVariable("code") String code) throws Exception {
    //     List<IndexData> allIndexDatas = backTestService.listIndexData(code);
    //     Map<String,Object> result = new HashMap<>();
    //     result.put("indexDatas", allIndexDatas);
    //     return result;
    // }

    @GetMapping("/simulate/{code}/{ma}/{buyThreshold}/{sellThreshold}/{serviceCharge}/{startDate}/{endDate}")
    @CrossOrigin
    public Map<String,Object> backTest(
            @PathVariable("code") String code
            ,@PathVariable("ma") int ma
            ,@PathVariable("buyThreshold") float buyThreshold
            ,@PathVariable("sellThreshold") float sellThreshold
            ,@PathVariable("serviceCharge") float serviceCharge
            ,@PathVariable("startDate") String strStartDate
            ,@PathVariable("endDate") String strEndDate
    ) throws Exception {
        
        List<IndexData> allIndexDatas = backTestService.listIndexData(code);
 
        String indexStartDate = allIndexDatas.get(0).getDate();
        String indexEndDate = allIndexDatas.get(allIndexDatas.size()-1).getDate();
        allIndexDatas = filterByDateRange(allIndexDatas,strStartDate, strEndDate);

        Map<String,Object> result = new HashMap<>();
        result.put("indexDatas", allIndexDatas);
        result.put("indexStartDate", indexStartDate);
        result.put("indexEndDate", indexEndDate);

        // int ma = 20;
        // float sellRate = 0.95f;
        // float buyRate = 1.05f;
        float sellRate = sellThreshold;
        float buyRate = buyThreshold;
        // float serviceCharge = 0f;
        Map<String,?> simulateResult= backTestService.simulate(ma,sellRate, buyRate,serviceCharge, allIndexDatas);

        @SuppressWarnings("unchecked")
        List<Profit> profits = (List<Profit>) simulateResult.get("profits");
        @SuppressWarnings("unchecked")
        List<Trade> trades = (List<Trade>) simulateResult.get("trades");
        result.put("profits", profits);
        result.put("trades", trades);

        float years = backTestService.getYear(allIndexDatas);
        float indexIncomeTotal = (allIndexDatas.get(allIndexDatas.size()-1).getClosePoint() - allIndexDatas.get(0).getClosePoint()) / allIndexDatas.get(0).getClosePoint();
        float indexIncomeAnnual = (float) Math.pow(1+indexIncomeTotal, 1/years) - 1;
        float trendIncomeTotal = (profits.get(profits.size()-1).getValue() - profits.get(0).getValue()) / profits.get(0).getValue();
        float trendIncomeAnnual = (float) Math.pow(1+trendIncomeTotal, 1/years) - 1;
        result.put("years", years);
        result.put("indexIncomeTotal", indexIncomeTotal);
        result.put("indexIncomeAnnual", indexIncomeAnnual);
        result.put("trendIncomeTotal", trendIncomeTotal);
        result.put("trendIncomeAnnual", trendIncomeAnnual);

        int winCount = (Integer) simulateResult.get("winCount");
        int lossCount = (Integer) simulateResult.get("lossCount");
        float avgWinRate = (Float) simulateResult.get("avgWinRate");
        float avgLossRate = (Float) simulateResult.get("avgLossRate");
        result.put("winCount", winCount);
        result.put("lossCount", lossCount);
        result.put("avgWinRate", avgWinRate);
        result.put("avgLossRate", avgLossRate);

        @SuppressWarnings("unchecked")
        List<AnnualProfit> annualProfits = (List<AnnualProfit>) simulateResult.get("annualProfits");
        result.put("annualProfits", annualProfits);

        return result;
    }

    private List<IndexData> filterByDateRange(List<IndexData> allIndexDatas, String strStartDate, String strEndDate) {
        if(StrUtil.isBlankOrUndefined(strStartDate) || StrUtil.isBlankOrUndefined(strEndDate) )
            return allIndexDatas;
 
        List<IndexData> result = new ArrayList<>();
        Date startDate = DateUtil.parse(strStartDate);
        Date endDate = DateUtil.parse(strEndDate);
 
        for (IndexData indexData : allIndexDatas) {
            Date date =DateUtil.parse(indexData.getDate());
            if(date.getTime()>=startDate.getTime() && date.getTime()<=endDate.getTime()) {
                result.add(indexData);
            }
        }
        return result;
    }
}