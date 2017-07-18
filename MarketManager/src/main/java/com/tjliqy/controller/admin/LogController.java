package com.tjliqy.controller.admin;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.tjliqy.interceptors.JsonResponse;
import com.tjliqy.model.Log;
import com.tjliqy.util.SearchUtil;
import com.tjliqy.util.StatisticsUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

/**
 * Created by tjliqy on 2017/5/7.
 */
public class LogController extends Controller {

    public void index() {
        SearchUtil util = new SearchUtil("log");
        util.addIntCondition("good_id", getPara("goodId"));
        util.addIntCondition("type", getPara("type"));
        util.addStrRangeCondition("update_time", getPara("minTime"), getPara("larTime"));
        util.addIntRangeCondition("cost", getPara("minPrize"), getPara("larPrize"));
        util.addOrderCondition(getPara("orderCon", "update_time"), getParaToBoolean("desc", true));
        Page<Log> paginate = Log.dao.paginate(getParaToInt("page", 1), getParaToInt("size", 10), util.getSelect(), util.getExceptSelect());

        setAttr("data", paginate);
    }

    public void list() {
        String tableNames[] = {"log", "goods"};
        String tablelabels[] = {"L", "G"};
        SearchUtil util = new SearchUtil(tableNames, tablelabels);
        util.addIntCondition("type", getPara("type"), 1);
        util.addStrRangeCondition("update_time", getPara("minTime"), getPara("larTime"), 0);

        /*月销售额及进货成本统计图*/
        List<Record> monthsSell = Db.find("SELECT count(L.id) log_num,sum(L.total_cost) month_cost, date_format(L.update_time,'%Y-%m') month_num " + util.getMuiltExceptSelect() + " AND L.type = 3 AND L.good_id=G.id" + " group by month_num order by month_num");
        List<Record> monthsPurchase = Db.find("SELECT count(L.id) log_num,sum(L.total_cost) month_cost, date_format(L.update_time,'%Y-%m') month_num " + util.getMuiltExceptSelect() + " AND L.type = 4 AND L.good_id=G.id" + " group by month_num order by month_num");


        List<String> monthList1 = StatisticsUtil.getMonthList(monthsSell, getPara("minTime"), getPara("larTime"));
        List<String> monthList2 = StatisticsUtil.getMonthList(monthsPurchase, getPara("minTime"), getPara("larTime"));
        List<String> monthList = monthList1.size() > monthList2.size() ? monthList1 : monthList2;

        List<BigDecimal> sellMonthList = StatisticsUtil.getMoneyValueList(monthsSell, monthList);
        List<BigDecimal> purchaseMonthList = StatisticsUtil.getMoneyValueList(monthsPurchase, monthList);


        setAttr("monthSell", sellMonthList);
        setAttr("monthPurchase", purchaseMonthList);
        setAttr("monthList", monthList);


        /*商品销量及总销售额*/
        util.addIntCondition("type", "3", 0);
        List<Record> goodSta = Db.find("SELECT G.name, sum(L.num)totalNum, sum(L.total_cost)totalCost " + util.getMuiltExceptSelect() + " AND L.good_id = G.id group by L.good_id");

        List<String> goodNames = new ArrayList<>();
        List<BigDecimal> goodNums = new ArrayList<>();
        List<BigDecimal> goodCost = new ArrayList<>();

        for (Record r : goodSta) {
            goodNames.add((String) r.getColumns().get("name"));
            goodNums.add((BigDecimal) r.getColumns().get("totalNum"));
            goodCost.add((BigDecimal) r.getColumns().get("totalCost"));
        }
        setAttr("goodNames", goodNames);
        setAttr("goodNums", goodNums);
        setAttr("goodCost", goodCost);
    }


    @Clear(JsonResponse.class)
    public void excel() {

        int rowNum = 0;
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("月商品销量及销售额");
        HSSFRow row1 = sheet.createRow(rowNum++);
        HSSFCell cell = row1.createCell(0);
        cell.setCellValue("月商品销量及销售额");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        String tableNames[] = {"log", "goods"};
        String tablelabels[] = {"L", "G"};
        SearchUtil util = new SearchUtil(tableNames, tablelabels);
        util.addIntCondition("type", getPara("type"), 1);
        util.addStrRangeCondition("update_time", getPara("minTime"), getPara("larTime"), 0);

        /*月销售额及进货成本统计图*/
        List<Record> monthsSell = Db.find("SELECT count(L.id) log_num,sum(L.total_cost) month_cost, date_format(L.update_time,'%Y-%m') month_num " + util.getMuiltExceptSelect() + " AND L.type = 3 AND L.good_id=G.id" + " group by month_num order by month_num");
        List<Record> monthsPurchase = Db.find("SELECT count(L.id) log_num,sum(L.total_cost) month_cost, date_format(L.update_time,'%Y-%m') month_num " + util.getMuiltExceptSelect() + " AND L.type = 4 AND L.good_id=G.id" + " group by month_num order by month_num");

        List<String> monthList1 = StatisticsUtil.getMonthList(monthsSell, getPara("minTime"), getPara("larTime"));
        List<String> monthList2 = StatisticsUtil.getMonthList(monthsPurchase, getPara("minTime"), getPara("larTime"));
        List<String> monthList = monthList1.size() > monthList2.size() ? monthList1 : monthList2;

        List<BigDecimal> sellMonthList = StatisticsUtil.getMoneyValueList(monthsSell, monthList);
        List<BigDecimal> purchaseMonthList = StatisticsUtil.getMoneyValueList(monthsPurchase, monthList);

        HSSFRow row = sheet.createRow(rowNum++);
        //创建单元格并设置单元格内容
        row.createCell(0).setCellValue("月份");
        row.createCell(1).setCellValue("销售额（元）");
        row.createCell(2).setCellValue("成本总额（元）");

        for (int i = 0; i < monthList.size(); i++) {
            row = sheet.createRow(rowNum++);
            //创建单元格并设置单元格内容
            row.createCell(0).setCellValue(monthList.get(i));
            row.createCell(1).setCellValue(String.valueOf(sellMonthList.get(i)));
            row.createCell(2).setCellValue(String.valueOf(purchaseMonthList.get(i)));
        }

        /*商品销量及总销售额*/
        util.addIntCondition("type", "3", 0);
        List<Record> goodSta = Db.find("SELECT G.*, sum(L.num)totalNum, sum(L.total_cost)totalCost " + util.getMuiltExceptSelect() + " AND L.good_id = G.id group by L.good_id");

        rowNum = 0;
        sheet = wb.createSheet("商品统计");
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("商品统计");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        row = sheet.createRow(rowNum++);

        int cellNum = 0;
        //创建单元格并设置单元格内容
        row.createCell(cellNum++).setCellValue("配件名称");
        row.createCell(cellNum++).setCellValue("配件代码");
        row.createCell(cellNum++).setCellValue("单位");
        row.createCell(cellNum++).setCellValue("单价");
        row.createCell(cellNum++).setCellValue("剩余数量");
        row.createCell(cellNum++).setCellValue("配件类别");
        //设置月份标题
        row1 = sheet.createRow(rowNum);

        for (String month : monthList) {
            row.createCell(cellNum++).setCellValue(month);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, cellNum - 1, cellNum));
            row1.createCell(cellNum - 1).setCellValue("月销量");
            row1.createCell(cellNum++).setCellValue("月销售额");
        }
        row.createCell(cellNum++).setCellValue("销量（件）");
        row.createCell(cellNum++).setCellValue("销售额（元）");
        rowNum++;

        //设置值
        for (Record r : goodSta) {
            cellNum = 0;
            row = sheet.createRow(rowNum++);
            //创建单元格并设置单元格内容
            row.createCell(cellNum++).setCellValue((String) r.getColumns().get("name"));
            row.createCell(cellNum++).setCellValue(String.valueOf(r.getColumns().get("number")));
            row.createCell(cellNum++).setCellValue(String.valueOf(r.getColumns().get("unit")));
            row.createCell(cellNum++).setCellValue(String.valueOf(r.getColumns().get("prize")));
            row.createCell(cellNum++).setCellValue(String.valueOf(r.getColumns().get("quantity")));
            row.createCell(cellNum++).setCellValue(String.valueOf(r.getColumns().get("type")));

            util.addIntCondition("id", String.valueOf(r.getColumns().get("id")), 1);
            List<Record> sellLogs = Db.find("SELECT count(L.id) log_num,sum(L.total_cost) month_cost,sum(L.num) month_quantity, date_format(L.update_time,'%Y-%m') month_num " + util.getMuiltExceptSelect() + " AND L.type = 3 AND L.good_id = G.id group by month_num order by month_num");
            sellMonthList = StatisticsUtil.getMoneyValueList(sellLogs, monthList);
            List<BigDecimal> quantityMonthList = StatisticsUtil.getNumValueList(sellLogs,monthList);
            for (int i = 0; i < monthList.size(); i++) {
                row.createCell(cellNum++).setCellValue(String.valueOf(quantityMonthList.get(i)));

                row.createCell(cellNum++).setCellValue(String.valueOf(sellMonthList.get(i)));
            }

            row.createCell(cellNum++).setCellValue(String.valueOf((BigDecimal) r.getColumns().get("totalNum")));
            row.createCell(cellNum++).setCellValue(String.valueOf((BigDecimal) r.getColumns().get("totalCost")));
        }

        try {
            File file = File.createTempFile("record", ".xls");
            wb.write(file);
            renderFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
