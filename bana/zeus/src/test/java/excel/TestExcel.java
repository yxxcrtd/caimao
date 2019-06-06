package excel;

import com.caimao.bana.api.service.IZeusStatisticsService;
import com.caimao.zeus.util.HomsExcelUtils;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TestExcel extends BaseTest{
    @Resource
    private IZeusStatisticsService zeusStatisticsService;

    @Test
    public void testExcelReader() throws Exception{
        String filepath = "D:\\caiwu4.xls";
        File file = new File(filepath);

        List<HashMap<String, Object>> recordList = HomsExcelUtils.getExcelData(file);
        List<HashMap<String, Object>> recordBatch = new ArrayList<>();
        System.out.println("有效数据:" +  recordList.size());
        Integer error = 0;
        Integer success = 0;

        for(HashMap<String, Object> record:recordList){
            try{
                if(record.get("transAmount").equals("")) record.put("transAmount", 0);
                if(record.containsKey("transAmount")) record.put("transAmount", new BigDecimal(record.get("transAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("postAmount").equals("")) record.put("postAmount", 0);
                if(record.containsKey("postAmount")) record.put("postAmount", new BigDecimal(record.get("postAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("entrustPrice").equals("")) record.put("entrustPrice", 0);
                if(record.containsKey("entrustPrice")) record.put("entrustPrice", new BigDecimal(record.get("entrustPrice").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("entrustAmount").equals("")) record.put("entrustAmount", 0);
                if(record.containsKey("entrustAmount")) record.put("entrustAmount", new BigDecimal(record.get("entrustAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("tradeFee").equals("")) record.put("tradeFee", 0);
                if(record.containsKey("tradeFee")) record.put("tradeFee", new BigDecimal(record.get("tradeFee").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("stampDuty").equals("")) record.put("stampDuty", 0);
                if(record.containsKey("stampDuty")) record.put("stampDuty", new BigDecimal(record.get("stampDuty").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("transferFee").equals("")) record.put("transferFee", 0);
                if(record.containsKey("transferFee")) record.put("transferFee", new BigDecimal(record.get("transferFee").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("commission").equals("")) record.put("commission", 0);
                if(record.containsKey("commission")) record.put("commission", new BigDecimal(record.get("commission").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("handlingFee").equals("")) record.put("handlingFee", 0);
                if(record.containsKey("handlingFee")) record.put("handlingFee", new BigDecimal(record.get("handlingFee").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("secCharges").equals("")) record.put("secCharges", 0);
                if(record.containsKey("secCharges")) record.put("secCharges", new BigDecimal(record.get("secCharges").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("subjectTransAmount").equals("")) record.put("subjectTransAmount", 0);
                if(record.containsKey("subjectTransAmount")) record.put("subjectTransAmount", new BigDecimal(record.get("subjectTransAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("subjectPostAmount").equals("")) record.put("subjectPostAmount", 0);
                if(record.containsKey("subjectPostAmount")) record.put("subjectPostAmount", new BigDecimal(record.get("subjectPostAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("technicalServices").equals("")) record.put("technicalServices", 0);
                if(record.containsKey("technicalServices")) record.put("technicalServices", new BigDecimal(record.get("technicalServices").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(recordBatch.size() == 500){
                    zeusStatisticsService.saveHomsFinanceHistoryBatch(recordBatch);
                    recordBatch = new ArrayList<>();
                    success++;
                }
                recordBatch.add(record);
            }catch(Exception e){
                error++;
                break;
            }
        }
        if(recordBatch.size() > 0){
            zeusStatisticsService.saveHomsFinanceHistoryBatch(recordBatch);
        }
        String errorMsg = error > 0?"在第 " + success * 500 + " 条数据后面的500条内有数据格式异常，导致后面无法进行":"全部成功";

        System.out.println("Total: " + recordList.size() + "," + errorMsg);
    }

    @Test
    public void testSort(){
        List<String> fieldList = new ArrayList<>();
        fieldList.add("transDate");
        fieldList.add("transNo");
        fieldList.add("transBizType");
        fieldList.add("stockCode");
        fieldList.add("stockName");
        fieldList.add("account");
        fieldList.add("accountUnitNo");
        fieldList.add("accountUnitName");
        fieldList.add("postAmount");
        fieldList.add("entrustDirection");
        fieldList.add("entrustPrice");
        fieldList.add("entrustAmount");
        fieldList.add("tradeFee");
        fieldList.add("stampDuty");
        fieldList.add("transferFee");
        fieldList.add("commission");
        fieldList.add("handlingFee");
        fieldList.add("secCharges");
        fieldList.add("currency");
        fieldList.add("transSubject");
        fieldList.add("subjectTransAmount");
        fieldList.add("subjectPostAmount");
        fieldList.add("remark");
        fieldList.add("transAmount");
        fieldList.add("technicalServices");
        Collections.sort(fieldList);
        System.out.println(fieldList);
    }
}
