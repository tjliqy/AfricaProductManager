# MarketManager——数据库大作业

2017.05

### 1. **题目要求**

在超市的数据库中存储超市的商品信息，超市每天的销售记录，进货记录。

​    商品信息包括服装类商品和食品类商品，其中服装类商品中要记录商品的名称，品牌，颜色，大小，适合人群，价格，数量。食品类商品记录商品名称，品牌，保质期截止日期，产地，价格，数量。

​    销售记录中包括销售时间，产品编号，数量，单价，总价。

​    进货记录中包括进货时间，产品编号，数量，单价，总价。

​    该系统能够实现管理员可以根据商品的任意属性的任意组合进行查询实时库存信息和商品明细，再加上时间（起始日期和截止日期）查询销售明细。可以根据指定的时间范围统计相应产品的销售信息（销售量和销售金额）。

​    能够实现模拟销售，在销售过程中如果发现某类产品的库存量低于5时，要立刻向采购部门发出采购请求。

​    采购部门在收到采购请求是应开始进货，填写进货记录。采购部门每天要统计每种产品的库存量，如果库存量小于10时，要进行采购。

​     管理部门每天，每周，每月，每季度，每年统计相应时间范围的销售情况：销售统计（每种产品的销售数量，金额，本时段内的销售总金额，每个品牌的销售总金额），将相应的统计信息存到excel表中或生成pdf文件。

​     管理部门可以将某种产品下架，下架的产品信息记录记录在下架产品表中（该表处理产品信息还要有下架时间，下架原因）

### 2. **应用简介**

- 数据库：Mysql
- 应用类型：JavaWeb
- 后端所用技术：JFinal框架 + JDBC + Apache Poi
- 前端所用技术：Vue + Bulma + echarts
- 源码地址：https://github.com/tjliqy/MarketManager

### 3.  **ER图**

![workbrench自动生成的](http://git.oschina.net/tjliqy/img4md/raw/master/img/%E6%95%B0%E6%8D%AE%E5%BA%93%E5%A4%A7%E4%BD%9C%E4%B8%9A/ER.png)

![自己画的ER图](http://git.oschina.net/tjliqy/img4md/raw/master/img/%E6%95%B0%E6%8D%AE%E5%BA%93%E5%A4%A7%E4%BD%9C%E4%B8%9A/ER2.png)

### 4. **表结构**

- goods（商品表）

|   列名    |      类型       |  外键  |   说明    |        其他        |
| :-----: | :-----------: | :--: | :-----: | :--------------: |
|   id    |    INT(11)    | NULL |         |       主键自增       |
|  name   |  VARCHAR(45)  | NULL |   商品名   |                  |
|  brand  |  VARCHAR(45)  | NULL |   生产商   |                  |
|  prize  | DECIMAL(10,2) | NULL |  出售单价   |                  |
|  type   |    INT(11)    | NULL |  商品类型   |    1为服装，2为食品     |
|   num   |    INT(11)    | NULL |  商品数量   |                  |
|  cost   | DECIMAL(10,2) | NULL |  进货单价   |                  |
| selling |    INT(1)     | NULL | 是否为在售状态 | 1为在售，0为下架（1为默认值） |
| reason  |  VARCHAR(45)  | NULL |  下架原因   |                  |

- food（食品表）

|     列名     |     类型      |    外键    |  说明  |  其他  |
| :--------: | :---------: | :------: | :--: | :--: |
|     id     |   INT(11)   | goods.id |      |      |
| shelf_life |    DATE     |   NULL   | 保质期  |      |
|   origin   | VARCHAR(45) |   NULL   |  产地  |      |

- clothes（服装表）

| 列名    | 类型          | 外键       | 说明   | 其他   |
| ----- | ----------- | -------- | ---- | ---- |
| id    | INT(11)     | goods.id |      |      |
| color | VARCHAR(45) | NULL     | 颜色   |      |
| size  | VARCHAR(45) | NULL     | 尺码   |      |
| crowd | VARCHAR(45) | NULL     | 适合人群 |      |

- log（日志表）

| 列名          | 类型           | 外键      | 说明      | 其他        |
| ----------- | ------------ | ------- | ------- | --------- |
| id          | INT(11)      | NULL    |         | 主键自增      |
| update_time | TIMESTAMP    | NULL    | 日志生成时间  | 默认当前时间    |
| good_id     | INT(11)      | good.id | 对应的商品ID |           |
| cost        | DECMAL(10,2) | NULL    | 单价      |           |
| total_cost  | DECMAL(10,2) | NULL    | 总花费     |           |
| type        | INT(1)       | NULL    | 操作类型    | 3为进货,4为出售 |
| num         | INT(11)      | NULL    | 数量      |           |

- user(用户表)

| 列名       | 类型          | 外键   | 说明   | 其他                  |
| -------- | ----------- | ---- | ---- | ------------------- |
| id       | INT(11)     | NULL |      | 主键自增                |
| account  | VARCHAR(45) | NULL | 用户名  |                     |
| password | VARCHAR(45) | NULL | 密码   |                     |
| name     | VARCHAR(45) | NULL | 用户姓名 |                     |
| position | INT(1)      | NULL | 职位   | 0为超级管理员 1为销售员 2为采购员 |

### 5. **应用截图及功能介绍**

- 登录&注册

  ![登录&注册](http://git.oschina.net/tjliqy/img4md/raw/master/img/%E6%95%B0%E6%8D%AE%E5%BA%93%E5%A4%A7%E4%BD%9C%E4%B8%9A/%E6%B3%A8%E5%86%8C.png)

- 统计（超级管理员可见）

  ![统计](http://git.oschina.net/tjliqy/img4md/raw/master/img/%E6%95%B0%E6%8D%AE%E5%BA%93%E5%A4%A7%E4%BD%9C%E4%B8%9A/%E7%BB%9F%E8%AE%A1%E5%9B%BE.png)

- 库存信息

  ![库存](http://git.oschina.net/tjliqy/img4md/raw/master/img/%E6%95%B0%E6%8D%AE%E5%BA%93%E5%A4%A7%E4%BD%9C%E4%B8%9A/%E5%BA%93%E5%AD%98.png)

- 日志（超级管理员可见）

  ![日志](http://git.oschina.net/tjliqy/img4md/raw/master/img/%E6%95%B0%E6%8D%AE%E5%BA%93%E5%A4%A7%E4%BD%9C%E4%B8%9A/%E6%97%A5%E5%BF%97.png)

- 采购界面（采购员可见）

  ![采购](http://git.oschina.net/tjliqy/img4md/raw/master/img/%E6%95%B0%E6%8D%AE%E5%BA%93%E5%A4%A7%E4%BD%9C%E4%B8%9A/%E9%87%87%E8%B4%AD.png)

### 6. 代码概览

- 建表语句

  ```mysql
  CREATE TABLE `clothes` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `color` varchar(45) DEFAULT NULL,
    `size` varchar(45) DEFAULT NULL,
    `crowd` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `clothes_ibfk_1` FOREIGN KEY (`id`) REFERENCES `goods` (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

  CREATE TABLE `food` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `shelf_life` date NOT NULL,
    `origin` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `food_ibfk_1` FOREIGN KEY (`id`) REFERENCES `goods` (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

  CREATE TABLE `goods` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    `brand` varchar(45) NOT NULL,
    `prize` int(11) NOT NULL,
    `type` int(11) NOT NULL,
    `num` int(11) NOT NULL,
    `cost` decimal(10,2) NOT NULL,
    `selling` int(1) NOT NULL DEFAULT '1',
    `reason` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

  CREATE TABLE `log` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `good_id` int(11) NOT NULL,
    `cost` decimal(10,2) NOT NULL,
    `total_cost` decimal(10,2) NOT NULL,
    `type` int(11) NOT NULL,
    `num` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `good_id_idx` (`good_id`),
    CONSTRAINT `good_id` FOREIGN KEY (`good_id`) REFERENCES `goods` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  ) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

  CREATE TABLE `user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `account` varchar(45) NOT NULL,
    `password` varchar(45) NOT NULL,
    `name` varchar(45) NOT NULL,
    `position` int(11) NOT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
  ```

  ​

- JFinal链接数据库配置文件

  ```
  #database
  jdbcUrl=jdbc:mysql://localhost/shop?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull
  user=root
  password=pppppp
  #production
  devMode=true
  ```

- 通过Generator自动生成Bean类

  ```java
  public class DBModelBuilder {

      public static void main(String[] args) {
          // base model 所使用的包名
          String baseModelPackageName = "com.tjliqy.model.baseModel";
          // base model 文件保存路径
          String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/com/tjliqy/model/baseModel";

          // model 所使用的包名 (MappingKit 默认使用的包名)
          String modelPackageName = "com.tjliqy.model";
          // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
          String modelOutputDir = baseModelOutputDir + "/..";
  ```

```
      Generator gernerator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
      // 设置数据库方言
      gernerator.setDialect(new MysqlDialect());
      // 添加不需要生成的表名
      gernerator.addExcludedTable("adv");
      // 设置是否在 Model 中生成 dao 对象
      gernerator.setGenerateDaoInModel(true);
      // 设置是否生成字典文件
      gernerator.setGenerateDataDictionary(false);
      // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
      gernerator.setRemovedTableNamePrefixes("t_");
      // 生成
      gernerator.generate();
  }

  private static DataSource getDataSource() {
      Prop p = PropKit.use("db.properties");
      DruidPlugin druidPlugin = new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
      druidPlugin.start();
      return druidPlugin.getDataSource();
  }
```

  }

```
* 配置ActiveRecord连接池插件

  ```java
      public void configPlugin(Plugins plugins) {
          //db plugin
          DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
          plugins.add(druidPlugin);

          // 配置ActiveRecord插件
          ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
          //add sql template engine
          arp.setBaseSqlTemplatePath(PathKit.getRootClassPath());

          // 所有映射在 MappingKit 中自动化搞定
          _MappingKit.mapping(arp);
          plugins.add(arp);

      }
```

- 为了方便拼接较为复杂的SQL语句，自己抽象了一个工具类SearchUtil.java（只截取部分）

  ```java
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
    	...
      public void addOrderCondition(String orderCon, boolean orderdesc) {
          if (!StrKit.isBlank(orderCon)) {
              orderStr = " order by " + orderCon;
              if (orderdesc) {
                  orderStr += " desc ";
              }
          }
      }
    	...//代码过长，只截取部分

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
  ```

  应用举例

  ```java
          SearchUtil util = new SearchUtil("goods");
          util.addStrCondition("name", getPara("name"));
  		...
          util.addIntCondition("num",getPara("cost"));
          util.addIntCondition("selling",getPara("selling",getPara("selling")));
          util.addIntRangeCondition("cost",getPara("minCost"),getPara("larCost"));
          util.addIntRangeCondition("prize",getPara("minPrize"),getPara("larPrize"));
          util.addOrderCondition(getPara("orderCon"), getParaToBoolean("desc", true));
          Page<Goods> paginate = Goods.dao.paginate(getParaToInt("page", 1), getParaToInt("size", 10), util.getSelect(), util.getExceptSelect());
  ```

- 较为复杂的SQL语句

  - 在查询每月统计信息时，需要用到date_format、count、sum函数以及Group By 语句

    ```java
            String tableNames[] = {"log","goods"};
            String tablelabels[] = {"L","G"};
            SearchUtil util = new SearchUtil(tableNames,tablelabels);
            util.addIntCondition("type",getPara("type"),1);
            util.addStrRangeCondition("update_time", getPara("minTime"), getPara("larTime"),0);

            /*月销售额及进货成本统计图*/
            List<Record> monthsSell = Db.find("SELECT L.update_time,count(L.id) log_num,sum(L.total_cost) month_cost, date_format(L.update_time,'%Y-%m') month_num " + util.getMuiltExceptSelect() + " AND L.type = 3 AND L.good_id=G.id" + " group by month_num order by month_num");
            List<Record> monthsPurchase = Db.find("SELECT L.update_time,count(L.id) log_num,sum(L.total_cost) month_cost, date_format(L.update_time,'%Y-%m') month_num " + util.getMuiltExceptSelect() + " AND L.type = 4 AND L.good_id=G.id" + " group by month_num order by month_num");
    ```

    【sql语句样例】：

    ```mysql
    SELECT L.update_time,count(L.id) log_num,sum(L.total_cost) month_cost, date_format(L.update_time,'%Y-%m') month_num FROM log L, Good G where 1=1 AND update_time > 2017-05-01 AND L.type = 4 AND L.good_id=G.id group by month_num order by month_num 你  
    ```

  - 统计商品销量

    ```mysql
    SELECT G.name, sum(L.num)totalNum, sum(L.total_cost)totalCost FROM log L, Good G where 1=1 AND update_time > 2017-05-01 AND L.good_id = G.id group by L.good_id
    ```

### 7. 心得体会

​	通过本次的实践开发，将在数据库这门课程学到的大部分只是学以致用，从建表到建立jdbc链接，再到实现mysql的增删改查，最后通过复杂的sql语句简单的查出想要的数据。在实践的过程中，不仅是在巩固学到的知识，更是在学习新的知识。