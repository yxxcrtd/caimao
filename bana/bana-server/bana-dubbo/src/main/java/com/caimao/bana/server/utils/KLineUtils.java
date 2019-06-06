package com.caimao.bana.server.utils;

import com.caimao.bana.api.entity.ybk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class KLineUtils {
    private static final Logger logger = LoggerFactory.getLogger(KLineUtils.class);

    public static List<KLineEntity> processData(List<YBKKLineEntity> preData){
        List<KLineEntity> data = new ArrayList<>();
        if(preData != null){
            for (YBKKLineEntity ybkkLineEntity:preData){
                KLineEntity tmpEntity = new KLineEntity();
                tmpEntity.setOpen(ybkkLineEntity.getOpenPrice());
                tmpEntity.setHigh(ybkkLineEntity.getHighPrice());
                tmpEntity.setClose(ybkkLineEntity.getClosePrice());
                tmpEntity.setLow(ybkkLineEntity.getLowPrice());
                data.add(tmpEntity);
            }
        }
        return data;
    }

    public static int MACD_N1 = 12;
    public static int MACD_N2 = 26;
    public static int MACD_N = 9;

    public static YBKMACDEntity calculateMACD(List<KLineEntity> kLineData){
        YBKMACDEntity ybkmacdEntity = new YBKMACDEntity();
        if(kLineData.size() < MACD_N2 + MACD_N - 1) return ybkmacdEntity;

        List<KLineEntity> preKLineData = new ArrayList<>();
        for(int i = 1; i <= MACD_N2 + MACD_N - 1; i++){
            preKLineData.add(kLineData.get(kLineData.size() - MACD_N2 - MACD_N + i));
        }

        List<Float> DIFList = new ArrayList<>();

        for(int i = 1; i <= MACD_N; i++){
            float ema12 = calculateEMA(preKLineData, MACD_N1, i);
            float ema26 = calculateEMA(preKLineData, MACD_N2, i);
            DIFList.add(ema12 - ema26);
        }

        float DIF = DIFList.get(MACD_N - 1);
        float DEA = calculateDEA(DIFList, MACD_N);
        float MACD = (DIF - DEA) * 2;

        ybkmacdEntity.setDif(new BigDecimal(DIF * 100).longValue());
        ybkmacdEntity.setDea(new BigDecimal(DEA * 100).longValue());
        ybkmacdEntity.setMacd(new BigDecimal(MACD * 100).longValue());

        return ybkmacdEntity;
    }

    private static float calculateEMA(List<KLineEntity> kLineData, int n, int start) {
        int sum = 0;
        for (int i = 1;i<=n;i++){
            sum+=i;
        }
        double y = 0;
        try {
            for (int i = 1; i<= n; i++){
                y+= ((double)kLineData.get(start + i - 2).getClose()) / 100 * i / sum;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return (float) y;
        }
        return (float) y;
    }

    private static float calculateDEA(List<Float> list, int n){
        int sum = 0;
        for (int i = 1;i<=n;i++){
            sum+=i;
        }
        double y = 0;
        try {
            for (int i = 1; i<= n; i++){
                y+= list.get(i - 1) * i / sum;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return (float) y;
        }
        return (float) y;
    }

    public static int KDJ_N = 9;
    public static int KDJ_M1 = 3;
    public static int KDJ_M2 = 3;
    public static YBKKDJEntity calculateKDJ(List<KLineEntity> kLineData){
        YBKKDJEntity ybkkdjEntity = new YBKKDJEntity();
        if(kLineData.size() < KDJ_N + KDJ_M1 + KDJ_M2 - 1) return ybkkdjEntity;

        List<KLineEntity> preKLineData = new ArrayList<>();
        for(int i = 1; i <= KDJ_N + KDJ_M1 + KDJ_M2 - 1; i++){
            preKLineData.add(kLineData.get(kLineData.size() - KDJ_N - KDJ_M1 - KDJ_M2 + i));
        }

        List<Double> RSVList = new ArrayList<>();
        for (int i = 1; i < KDJ_M1 + KDJ_M2; i++){
            double rsv9 = calculateRSV(preKLineData, KDJ_N, i);
            RSVList.add(rsv9);
        }

        List<Double> KList = new ArrayList<>();
        for (int i = 1; i <= KDJ_M1; i++){
            double d3 = calculateK(RSVList, KDJ_M1, i);
            KList.add(d3);
        }

        double rsv = RSVList.get(KDJ_M1 + KDJ_M2 - 2);
        double k = KList.get(KDJ_M2 - 1);
        double d = calculateD(KList, KDJ_M2);
        double j = calculateJ(k, d);

        ybkkdjEntity.setRsv(new BigDecimal(rsv).longValue());
        ybkkdjEntity.setK(new BigDecimal(k * 100).longValue());
        ybkkdjEntity.setD(new BigDecimal(d * 100).longValue());
        ybkkdjEntity.setJ(new BigDecimal(j * 100).longValue());

        return ybkkdjEntity;
    }

    private static double calculateRSV(List<KLineEntity> kLineData, int n, int start){
        List<KLineEntity> preData = new ArrayList<>();
        for(int i = 1; i <= n; i++){
            preData.add(kLineData.get(i + start - 2));
        }
        double max = 0;
        double min = 0;
        for (KLineEntity kLineEntity:preData){
            if(max < (double)kLineEntity.getHigh()){
                max = (double)kLineEntity.getHigh();
            }
            if(min > (double)kLineEntity.getLow()){
                min = (double)kLineEntity.getLow();
            }
        }
        double close = preData.get(n - 1).getClose();

        if (max - min == 0) {
            max += 1;
        }
        close = close / 100;
        max = max / 100;
        min = min / 100;

        return 100 * (close - min) / (max - min);
    }

    private static double calculateK(List<Double> RSVList, int n, int start){
        List<Double> preList = new ArrayList<>();
        for (int i = 1; i <= n; i++){
            preList.add(RSVList.get(n + start - 2));
        }
        double sum = 0;
        for(Double rsv:preList){
            sum += rsv;
        }
        return sum / n;
    }

    private static double calculateD(List<Double> KList, int n){
        double sum = 0;
        for(Double d:KList){
            sum += d;
        }
        return sum / n;
    }

    private static double calculateJ(double k, double d){
        return 3 * k - 2 * d;
    }

    public static int RSI_N1 = 6;
    public static int RSI_N2 = 12;
    public static int RSI_N3 = 24;

    public static YBKRSIEntity calculateRSI(List<KLineEntity> kLineData){
        YBKRSIEntity ybkrsiEntity = new YBKRSIEntity();
        double RISN1 = calculateRS(kLineData, RSI_N1);
        double RISN2 = calculateRS(kLineData, RSI_N2);
        double RISN3 = calculateRS(kLineData, RSI_N3);

        ybkrsiEntity.setRsi1(new BigDecimal(RISN1 * 100).longValue());
        ybkrsiEntity.setRsi2(new BigDecimal(RISN2 * 100).longValue());
        ybkrsiEntity.setRsi3(new BigDecimal(RISN3 * 100).longValue());
        return ybkrsiEntity;
    }

    private static double calculateRS(List<KLineEntity> kLineData, int n){
        if(kLineData.size() < n + 1) return 0;
        List<KLineEntity> preKLineData = new ArrayList<>();
        for (int i = 1; i <= n + 1; i++){
            preKLineData.add(kLineData.get(kLineData.size() - n + i - 2));
        }

        double max = 0;
        double abs = 0;

        for (int i = n; i >= 1; i--){
            double close = preKLineData.get(i).getClose() / 100;
            double lastClose = preKLineData.get(i - 1).getClose() / 100;
            max = max + Max(close, lastClose);
            abs = abs + Math.abs(close - lastClose);
        }
        return abs > 0?100 * max / abs:0;
    }

    private static double Max(double close, double lastClose) {
        return close - lastClose < 0 ? 0 : close - lastClose;
    }
}
