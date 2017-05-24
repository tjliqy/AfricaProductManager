package com.tjliqy.util;

import com.jfinal.kit.StrKit;

import java.util.HashMap;

/**
 * Created by tjliqy on 2017/3/31.
 */
public class SearchUtil {

    StringBuilder builder;

    String tableName[] ={""};

    String tableLable[];

    String orderStr;


    public SearchUtil(String name[], String lable[]) {
        this.tableName = name;
        this.tableLable = lable;
        this.builder = new StringBuilder(" where 1=1");
    }

    public SearchUtil(String table) {
        this.tableName[0] = table;
        this.builder = new StringBuilder(" where 1=1");
    }

    public void addIntCondition(String columnName, String con) {
        if (!StrKit.isBlank(con)) {
            builder.append(" AND ").append(columnName).append(" = ").append(con);
        }
    }


    public void addStrCondition(String columnName, String con) {
        if (!StrKit.isBlank(con)) {
            builder.append(" AND ").append(columnName).append(" = '").append(con).append("'");
        }
    }

    public void addStrRangeCondition(String columnName, String minCon, String larCon) {
        if (!StrKit.isBlank(minCon) && !StrKit.isBlank(larCon)) {
            builder.append(" AND ").append(columnName).append(" BETWEEN '").append(minCon).append("' AND '").append(larCon).append("'");
        } else if (!StrKit.isBlank(minCon)) {
            builder.append(" AND ").append(columnName).append(" > '").append(minCon).append("'");
        } else if (!StrKit.isBlank(larCon)) {
            builder.append(" AND ").append(columnName).append(" < '").append(larCon).append("'");
        }
    }

    public void addIntRangeCondition(String columnName, String minCon, String larCon) {
        if (!StrKit.isBlank(minCon) && !StrKit.isBlank(larCon)) {
            builder.append(" AND ").append(columnName).append(" BETWEEN ").append(minCon).append(" AND ").append(larCon);
        } else if (!StrKit.isBlank(minCon)) {
            builder.append(" AND ").append(columnName).append(" > ").append(minCon);
        } else if (!StrKit.isBlank(larCon)) {
            builder.append(" AND ").append(columnName).append(" < ").append(larCon);
        }
    }

    public void addOrderCondition(String orderCon, boolean orderdesc) {
        if (!StrKit.isBlank(orderCon)) {
            orderStr = " order by " + orderCon;
            if (orderdesc) {
                orderStr += " desc ";
            }
        }
    }


    public void addIntCondition(String columnName, String con, int i) {
        if (!StrKit.isBlank(con)) {
            builder.append(" AND ").append(tableLable[i]).append(".").append(columnName).append(" = ").append(con);
        }
    }


    public void addStrCondition(String columnName, String con, int i) {
        if (!StrKit.isBlank(con)) {
            builder.append(" AND ").append(tableLable[i]).append(".").append(columnName).append(" = '").append(con).append("'");
        }
    }

    public void addStrRangeCondition(String columnName, String minCon, String larCon, int i) {
        if (!StrKit.isBlank(minCon) && !StrKit.isBlank(larCon)) {
            builder.append(" AND ").append(tableLable[i]).append(".").append(columnName).append(" BETWEEN '").append(minCon).append("' AND '").append(larCon).append("'");
        } else if (!StrKit.isBlank(minCon)) {
            builder.append(" AND ").append(tableLable[i]).append(".").append(columnName).append(" > '").append(minCon).append("'");
        } else if (!StrKit.isBlank(larCon)) {
            builder.append(" AND ").append(tableLable[i]).append(".").append(columnName).append(" < '").append(larCon).append("'");
        }
    }

    public void addIntRangeCondition(String columnName, String minCon, String larCon, int i) {
        if (!StrKit.isBlank(minCon) && !StrKit.isBlank(larCon)) {
            builder.append(" AND ").append(tableLable[i]).append(".").append(columnName).append(" BETWEEN ").append(minCon).append(" AND ").append(larCon);
        } else if (!StrKit.isBlank(minCon)) {
            builder.append(" AND ").append(tableLable[i]).append(".").append(columnName).append(" > ").append(minCon);
        } else if (!StrKit.isBlank(larCon)) {
            builder.append(" AND ").append(tableLable[i]).append(".").append(columnName).append(" < ").append(larCon);
        }
    }

    public void addOrderCondition(String orderCon, boolean orderdesc, int i) {
        if (!StrKit.isBlank(orderCon)) {
            orderStr = " order by " + tableLable[i] + "." + orderCon;
            if (orderdesc) {
                orderStr += " desc ";
            }
        }
    }

    public String getSql() {
        return getSelect() + getExceptSelect();
    }

    public String getSelect() {
        return "select * ";
    }

    public String getExceptSelect() {
        return "from " + tableName[0] + builder.toString() + (StrKit.isBlank(orderStr) ? "" : orderStr);
    }

    public String getMuiltExceptSelect() {
        StringBuilder res = new StringBuilder("from ");
        for (int i = 0; i < tableName.length; i++) {
            if(i != 0){
                res.append(",");
            }
            res.append(tableName[i]).append(" ").append(tableLable[i]);
        }
        res.append(" ").append(builder.toString()).append(StrKit.isBlank(orderStr) ? "" : orderStr);
        return res.toString();
    }

}
