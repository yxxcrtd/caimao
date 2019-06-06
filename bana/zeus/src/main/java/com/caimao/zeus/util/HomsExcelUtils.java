package com.caimao.zeus.util;

import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomsExcelUtils {
    public static List<String> getFieldList(){
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
        return fieldList;
    }

    public static String getFieldByColumn(String column){
        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("发生日期", "transDate");
        fieldMap.put("流水序号", "transNo");
        fieldMap.put("发生业务", "transBizType");
        fieldMap.put("证券代码", "stockCode");
        fieldMap.put("证券名称", "stockName");
        fieldMap.put("账户", "account");
        fieldMap.put("单元编号", "accountUnitNo");
        fieldMap.put("单元名称", "accountUnitName");
        fieldMap.put("发生后余额", "postAmount");
        fieldMap.put("委托方向", "entrustDirection");
        fieldMap.put("委托价格", "entrustPrice");
        fieldMap.put("发生数量", "entrustAmount");
        fieldMap.put("交易费", "tradeFee");
        fieldMap.put("印花税", "stampDuty");
        fieldMap.put("过户费", "transferFee");
        fieldMap.put("佣金", "commission");
        fieldMap.put("经手费", "handlingFee");
        fieldMap.put("证管费", "secCharges");
        fieldMap.put("币种", "currency");
        fieldMap.put("发生科目", "transSubject");
        fieldMap.put("科目发生额", "subjectTransAmount");
        fieldMap.put("科目发生后余额", "subjectPostAmount");
        fieldMap.put("备注", "remark");
        fieldMap.put("发生金额", "transAmount");
        fieldMap.put("技术服务费", "technicalServices");

        String columnString = fieldMap.get(column);
        if(columnString != null){
            return columnString;
        }else{
            return null;
        }
    }

    public static List<HashMap<String, Object>> getExcelData(InputStream is) throws Exception{
        Workbook workbook = Workbook.getWorkbook(is);
        Sheet sheet = workbook.getSheet(0);
        Integer totalRows = sheet.getRows();
        Integer totalColumns = sheet.getColumns();

        List<HashMap<String, Object>> homsData = new ArrayList<>();

        for (int i = 1; i < totalRows - 1; i++) {
            HashMap<String, Object> record = new HashMap<>();
            for(int j = 0; j < totalColumns; j++){
                String columnName = sheet.getCell(j, 0).getContents();
                String columnKey = HomsExcelUtils.getFieldByColumn(columnName);

                if(columnKey != null){
                    record.put(columnKey, sheet.getCell(j, i).getContents().replace(",",""));
                }
            }



            homsData.add(record);
        }
        return homsData;
    }

    public static List<HashMap<String, Object>> getExcelData(File is) throws Exception{
        Workbook workbook = Workbook.getWorkbook(is);
        Sheet sheet = workbook.getSheet(0);
        Integer totalRows = sheet.getRows();
        Integer totalColumns = sheet.getColumns();

        List<HashMap<String, Object>> homsData = new ArrayList<>();
        List<String> fieldList = HomsExcelUtils.getFieldList();

        for (int i = 1; i < totalRows - 1; i++) {
            HashMap<String, Object> record = new HashMap<>();
            for(int j = 0; j < totalColumns; j++){
                String columnName = sheet.getCell(j, 0).getContents();
                String columnKey = HomsExcelUtils.getFieldByColumn(columnName);

                if(columnKey != null){
                    record.put(columnKey, sheet.getCell(j, i).getContents().replace(",",""));
                }
            }
            for (String field:fieldList){
                if(!record.containsKey(field)){
                    record.put(field, "");
                }
            }
            Object[] key_arr = record.keySet().toArray();
            Arrays.sort(key_arr);
            HashMap<String, Object> recordNew = new HashMap<>();
            for  (Object key : key_arr) {
                recordNew.put(key.toString(), record.get(key.toString()));
            }

            if(recordNew.get("transNo") == null || recordNew.get("transNo").equals("") || recordNew.get("transNo").equals("0")){
                continue;
            }

            homsData.add(recordNew);
        }
        return homsData;
    }
}
