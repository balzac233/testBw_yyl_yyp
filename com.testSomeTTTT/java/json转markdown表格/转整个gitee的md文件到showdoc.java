package json转markdown表格;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description 改动记录:
 * 2021年9月24日14:02:40   yyp    改动地点为 editflag001   增加返回值可能取的标题
 * 2021年9月24日14:17:52  yyp    改动地点为  editflag002   必填信息提取出来后表格里面删除是否必填的说明,然后删除中英文括号
 * 2021年9月24日14:56:04  yyp    准备改动但是没改,一般不同对象名属性名都相同其实就是一张表里面不同type的数据罢了,没必要重复放进去
 * editflag2109241640 yyp  更改了匹配传参返参的规则
 * @author: yyp
 * @create: 2021-09-24
 **/
public class 转整个gitee的md文件到showdoc {

    //    private static Set<String> isRequired = new HashSet<>(Arrays.asList("非必传"));
    private static List<String> notRequired = Arrays.asList("非必传", "不必传输", "不必传", "可选", "选填");
    private static List<String> isRequired = Arrays.asList("必传", "必填", "必须");

    public static void main(String[] args) throws Exception {

        long nowmilli = System.currentTimeMillis();

        // 系统关键字:   [   ]   {   }   "  :   ,   \n   #  ,     注释不要换行写.推荐写在参数后面,注释可以用// 或者 # ,后面可以写一些系统关键字,因为不会被记入括号匹配
        // 参数名称一定要加引号,然后写上冒号(均为英文),后面再写参数值和注释,参数名符合命名规范,是字符数字下划线
        // 也就是 这种样式 "projectId":"proj10086", // 项目 id [String](必传)   ,末尾再来个换行(\n)
        // 可以匹配第一个逗号或者第一个左大括号或者中括号后面的就是注释
        // 多对象或者多对象数组的测试用例,暂未解决,尝试解决
        // 对象或者对象数组或者普通字符串对象的特征是,
        // 对象数组的名字例如退格是4,然后他的格式就是"iiifff":[然后直接换行,后面每个对象外框退格是12(但是单个{不会被正则匹配到),每个对象的子参数就是20了.也就是参数里最后两个对象数组
        // 对象的格式是退格例如是4,格式是"manufacturer":{,下一个匹配到的子变量应该是12的前置空格
        // 如果是普通对象,普通对象的内容没有冒号不会被匹配进去,下一个对象就是他本身也就是
        // (暂时未解决多个对象的问题,后面再说) ,
        // 如果是因为多个对象导致重复操作的麻烦上面多复制一行(也就是复制一个一级的),前面空格尽量少于8个,大于等于八个会去找父亲,这样的话第一个没父亲就会下标越界
        // ,把对象名和复制出来,否则子集找不到父亲会报错
        /**
         *   @description      //        例如
         *     "supplier":{
         *         "name":"", // 供应商名称
         *         "contact":"", // 供应商联系人
         *         "phone":"", // 供应商联系电话
         *         "address":"" // 供应商地址
         *     },
         *     前面的 "supplier":{  和   后面的  },   尽量都复制就行,然后最后出来的结果不要复制这些
         */

        // 自己往文件最上头加上 #### 标题:报单统计-问题类型分析 这块东西吧,#最好四个以内,然后1个到四个#吧.


        String origin = "#### URL:http://192.168.1.201:8050/cloudlink-building-guard/eff/specialty/responseNum\n" +
                "#### 请求方式: POST\n" +
                "#### 负责人：葛志华\n" +
                "#### 参数: \n" +
                "```\n" +
                "{\n" +
                "    \"year\": 2020,\n" +
                "    \"specialtyIds\": [\"d47426ae-31a7-4eb4-9ab7-27031d9ce445\"],\n" +
                "    \"projectId\": \"f678fb40-1206-4127-9f1a-4353a70d8e81\"\n" +
                "}\n" +
                "```\n" +
                "#### 返回值：\n" +
                "```\n" +
                "{\n" +
                "    \"code\": 200,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"val\": {\n" +
                "        \"specialtyIds\": [\"d47426ae-31a7-4eb4-9ab7-27031d9ce445\"],\n" +
                "        \"projectId\": \"f678fb40-1206-4127-9f1a-4353a70d8e81\",\n" +
                "        \"monthItemVOS\": [\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 1,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 2,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 3,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 4,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 5,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 6,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 1,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 70,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 6,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 7,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 8,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 9,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 1,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 4272,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 10,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 11,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"specialtyId\": \"d47426ae-31a7-4eb4-9ab7-27031d9ce445\",\n" +
                "                \"monthIndex\": 12,\n" +
                "                \"lastYear\": 2019,\n" +
                "                \"thisYear\": 2020,\n" +
                "                \"thisYearMonthNum\": 0,\n" +
                "                \"lastYearMonthNum\": 0,\n" +
                "                \"thisYearAcceptMonthMinutes\": 0,\n" +
                "                \"lastYearAcceptMonthMinutes\": 0,\n" +
                "                \"thisYearArriveMonthMinutes\": 0,\n" +
                "                \"lastYearArriveMonthMinutes\": 0\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"success\": 1\n" +
                "}\n" +
                "```";


        origin = origin + "\n";

//        // 最后一行经常因为没有\n检测不到,加上
//        origin = origin + "\n";
////        生成[[传参]]md表格
//        toShowDocMultiLevel(origin, 0);
////      生成md[[返参]]表格
//        toShowDocResMultiLevelv2(origin, 0);

//        System.out.println(toShowDocMultiLevel(origin,0,false));
//        System.out.println("=============================================");
//        System.out.println(toShowDocMultiLevel(origin,0,true));

//        不知道为啥有些```前面有个空格,就很难受,把空格去掉,一般空格也只有一个,不用正则好像也没问题
        origin = origin.replace(" ```","```");

        if (origin.contains("# 请求方式")) {
            System.out.println(toWholeShowDoc(origin));
            if (!origin.contains("# 返回值") && !origin.contains("# 返回示例") && !origin.contains("# 响应结果")) {
                origin = origin + " \n\n#### 返回值：\n" +
                        "```\n" +
                        "{\n" +
                        "    暂无\n" +
                        "}\n" +
                        "``` ";
            }
        } else if (origin.contains("- 请求方式")) {
            if (!origin.contains("- 响应结果")) {
                origin = origin + "\n\n- 响应结果\n" +
                        "```\n" +
                        "    暂无\n" +
                        "```";
            }
            System.out.println(toWholeShowDoc2(origin));
        } else {
            throw new Exception("检查下这个接口文档的请求方式在哪");
        }

//
//        System.out.println(System.currentTimeMillis()-nowmilli);

//        partsList.stream().forEach(a-> System.out.println(a+"\n\n===============================================\n\n"));
//        System.out.println("==================================");

    }

    /**
     * 有种是这类格式
     * - 请求地址:
     * <p>
     * ```text
     * /lyws-inspect/checkStatistics/unqualified/export
     * ```
     * 给这种也写个方法.
     */
    private static String toWholeShowDoc2(String origin) throws Exception {
        StringBuilder res = new StringBuilder("##### 简要描述\n" +
                "\n" +
                "-   TTTTT自己替换下标题TTTTT\n\n");

        // 传参和返参所在的部分可以用"返回示例"或者"返回值"字样来区分开
        int splitIndex = 0;
        List<String> partsList = new ArrayList<>();
        // 返回示例后面跟着的一般就是返回值json了,不能用返回参数说明什么的来分割,因为返回参数说明一般都是在返回示例下面的
        if (origin.contains("返回示例")) {
            String[] returns = origin.split("返回示例");
            Arrays.stream(returns).forEach(a -> partsList.add(a));
        } else if (origin.contains("返回值")) {
            String[] returns = origin.split("返回值");
            Arrays.stream(returns).forEach(a -> partsList.add(a));
            // editflag001
        } else if (origin.contains("响应结果")) {
            String[] returns = origin.split("响应结果");
            Arrays.stream(returns).forEach(a -> partsList.add(a));
        }
        if (null == partsList || partsList.size() <= 0) {
            throw new Exception("出错了,这个可能没返回值");
        }
        String part01 = partsList.get(0);
        String part02 = partsList.get(1);
        String param1 = part01.substring(part01.indexOf("```json"), part01.lastIndexOf("- 请求方式"));
//        System.out.println(param1+"\n\n=======================================\n\n");
        String param2 = part02.substring(part02.indexOf("```"), part02.lastIndexOf("`") + 1);
//        System.out.println(param2);


        String mdTable1 = toShowDocMultiLevel(partsList.get(0), 0, false);
        String mdTable2 = toShowDocMultiLevel(partsList.get(1), 0, true);
//        String mdTable2 = toShowDocResMultiLevelv2(partsList.get(1), 0);
        // 标题的正则   (.*里头是标题)
        String titleExp = "#*\\s*(标题|title)(\\s*:\\s*)(.*)(\\n)";
        // 请求url的正则 #*\s*(url|URL|Url)(\s*:\s*)(.*)(\n)
        String urlExp = "(\\s*-\\s*请求地址\\s*:\\s*)(\\n*)(```.*\\n)(.*\\n)(```\\s*?\\n)";
        // 请求方式的正则
        String postExp = "(\\x20*-\\s*请求方式\\s*:\\s*)(\\n*)(```.*\\n)(.*\\n)(```\\x20*?)";

        Pattern pattern = Pattern.compile(titleExp);
        Matcher matcher = pattern.matcher(origin);
        if (matcher.find()) {
            res.append(matcher.group(3) + "  \n");
        }
        res.append("##### 请求 URL\n" +
                "\n" +
                "- `     ");
        pattern = Pattern.compile(urlExp);
        matcher = pattern.matcher(origin);
        if (matcher.find()) {
            res.append(matcher.group(4) + "    `\n");
        }
        res.append("\n" +
                "##### 请求方式\n" +
                "\n" +
                "-   ");
        pattern = Pattern.compile(postExp);
        matcher = pattern.matcher(origin);
        if (matcher.find()) {
            res.append(matcher.group(4) + "  \n");
        }
        res.append("\n" +
                "##### 参数\n" +
                "\n" +
                "| 参数名   | 必选 | 类型   | <div style=\"min-width:400px;\" ></div>说明   |\n" +
                "| :------- | :--- | :----- | ------ |\n");
        res.append(mdTable1 + "\n");
        res.append("\n" +
                "- 参数示例\n" +
                "\n");
        res.append(param1);
        res.append("\n" +
                "##### 返回示例\n");
        res.append(param2);
        res.append("\n" +
                "##### 返回参数说明\n\n| 参数名   | 类型   | <div style=\"min-width:400px;\" ></div>说明         |\n" +
                "| :------ | :----- | --------------------------------------------------- |\n");
        res.append(mdTable2 + "\n");
        res.append("\n" +
                "##### 备注\n" +
                "\n" +
                "- 更多返回错误代码请看首页的错误代码描述");
//        System.out.println(res.toString());
        return res.toString();
    }

    //
    private static String toWholeShowDoc(String origin) throws Exception {
        StringBuilder res = new StringBuilder("##### 简要描述\n" +
                "\n" +
                "-   TTTTT自己替换下标题TTTTT\n\n");

        // 传参和返参所在的部分可以用"返回示例"或者"返回值"字样来区分开
        int splitIndex = 0;
        List<String> partsList = new ArrayList<>();
        // 返回示例后面跟着的一般就是返回值json了,不能用返回参数说明什么的来分割,因为返回参数说明一般都是在返回示例下面的
        if (origin.contains("返回示例")) {
            String[] returns = origin.split("返回示例");
            Arrays.stream(returns).forEach(a -> partsList.add(a));
        } else if (origin.contains("返回值")) {
            String[] returns = origin.split("返回值");
            Arrays.stream(returns).forEach(a -> partsList.add(a));
            // editflag001
        } else if (origin.contains("响应结果")) {
            String[] returns = origin.split("响应结果");
            Arrays.stream(returns).forEach(a -> partsList.add(a));
        }
        if (null == partsList || partsList.size() <= 0) {
            throw new Exception("出错了,这个可能没返回值");
        }
        String part01 = partsList.get(0);
        String part02 = partsList.get(1);
        if (!part01.contains("```")) {
            part01 = part01 + "```\n" +
                    "    暂无参数\n" +
                    "```\n";
        }
        // editflag2109241640
        String param1 = part01.substring(part01.indexOf("```"), part01.lastIndexOf("```") + 3);
//        System.out.println(param1+"\n\n=======================================\n\n");
        String param2 = part02.substring(part02.indexOf("```"), part02.lastIndexOf("```") + 3);
//        System.out.println(param2);


        String mdTable1 = toShowDocMultiLevel(partsList.get(0), 0, false);
        String mdTable2 = toShowDocMultiLevel(partsList.get(1), 0, true);
//        String mdTable2 = toShowDocResMultiLevelv2(partsList.get(1), 0);
        // 标题的正则   (.*里头是标题)
        String titleExp = "#*\\s*(标题|title)(\\s*:\\s*)(.*)(\\n)";
        // 请求url的正则 #*\s*(url|URL|Url)(\s*:\s*)(.*)(\n)
        String urlExp = "#*\\s*(url|URL|Url)(\\s*:\\s*)(.*)(\\n)";
        // 请求方式的正则
        String postExp = "#*\\s*(请求方式)(\\s*:\\s*)(.*)(\\n)";

        Pattern pattern = Pattern.compile(titleExp);
        Matcher matcher = pattern.matcher(origin);
        if (matcher.find()) {
            res.append(matcher.group(3) + "  \n");
        }
        res.append("##### 请求 URL\n" +
                "\n" +
                "- `     ");
        pattern = Pattern.compile(urlExp);
        matcher = pattern.matcher(origin);
        if (matcher.find()) {
            res.append(matcher.group(3) + "    `\n");
        }
        res.append("\n" +
                "##### 请求方式\n" +
                "\n" +
                "-   ");
        pattern = Pattern.compile(postExp);
        matcher = pattern.matcher(origin);
        if (matcher.find()) {
            res.append(matcher.group(3) + "  \n");
        }
        res.append("\n" +
                "##### 参数\n" +
                "\n" +
                "| 参数名   | 必选 | 类型   | <div style=\"min-width:400px;\" ></div>说明   |\n" +
                "| :------- | :--- | :----- | ------ |\n");
        res.append(mdTable1 + "\n");
        res.append("\n" +
                "- 参数示例\n" +
                "\n");
        res.append(param1);
        res.append("\n" +
                "##### 返回示例\n");
        res.append(param2);
        res.append("\n" +
                "##### 返回参数说明\n\n| 参数名   | 类型   | <div style=\"min-width:400px;\" ></div>说明         |\n" +
                "| :------ | :----- | --------------------------------------------------- |\n");
        res.append(mdTable2 + "\n");
        res.append("\n" +
                "##### 备注\n" +
                "\n" +
                "- 更多返回错误代码请看首页的错误代码描述");
//        System.out.println(res.toString());
        return res.toString();
    }

    /**
     * @Description (传参和返参也就差个是否必填, isRes等于ture就是返参, false就是传参)json转为markdown的表格
     * 暂时处理不了多个对象的情况,还有层级太多有多个对象也麻烦
     */
    private static String toShowDocMultiLevel(String origin, int pp, boolean isRes) {

        // ,int pp
//        System.out.println("==========================尝试多级传参===========================");
        // 检测重复(用于对象数组的情况)
        Set<String> variableName = new HashSet<>();
        Set<String> repeatingObject = new HashSet<>();
        String[] originArr = origin.split("\n");
        StringBuffer sb = new StringBuffer();
        // 变量前面的退格数量
        StringBuffer sb2 = new StringBuffer();
        StringBuilder explain = new StringBuilder();
        String regExp = "(\\s*)(\".*?)([0-9a-zA-Z]*)(\".*?:.*?\\s*?)(.*)(\\s*?.*?)\n";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(origin);
        StringBuffer res = new StringBuffer();
        while (matcher.find()) {
            sb2.append(matcher.group(1)).append(",");
            sb.append(matcher.group(3)).append(",");
            // 描述文本里面经常有逗号,不常见#号,用#号好了
            explain.append(matcher.group(5)).append("#");
        }
        String[] sArr = changeUnderToUpperLetter(sb.toString()).split(",");
        String[] spaceArr = sb2.toString().split(",");
        String[] explainArr = explain.toString().split("#");
        // 标记上次的缩进个数
        boolean isChild = false;
        int flag = 0;
        String flagStr = "";
        forOne:
        for (int i = 0; i < sArr.length; i++) {
            // 不同层级的objectId给区分开来,同层级的不允许重复出现
//            这个是用于判断是否该写入markdown表格的的key
            String key = sArr[i] + "" + spaceArr[i].length();

            // 如果这个参数是前面有个对象或者对象数组,也就是这个参数对象或者对象数组的孩子,那么
            if (isChild) {

            }

//            请检查
//            if (explainArr[i-1].replace(" ","").equals("{") || explainArr[i-1].replace(" ","").equals("[") ){
//                flagStr = sArr[i-1];
//                key =flagStr + key;
//                flag = spaceArr[i].length();
//            }
//            if (spaceArr[i].length() == flag){
//                key =flagStr + key;
//            }else {
//                flag = -1;
//                flagStr = "";
//            }


            if (spaceArr[i].length() >= 8 && (explainArr[i - 1].replace(" ", "").equals("{") || explainArr[i - 1].replace(" ", "").equals("["))) {
                flagStr = sArr[i - 1];
                String repeatingKey = flagStr + key;
                repeatingObject.add(repeatingKey);
            }
            if (variableName.contains(key)) {
                continue forOne;
//                if (explainArr[i-1].replace(" ","").equals("[") || explainArr[i-1].replace(" ","").equals("{")){
//                    System.out.println("一般带前面英文名儿且匹配到有变量名的,然后是{开头,一般都是不同的对象,应该要让他计入参数");
//                }else {
//                    continue forOne;
//                }
            } else {
                variableName.add(sArr[i] + "" + spaceArr[i].length());
            }
            res.append("|");
            // 一个制表符是4个空格,一般来说第一级是4个空格,第二级是12个空格,第三级是20个空格,这里我选/8,每级多个+号
//            暂时没有遇到4,8,12的情况
            int spaceCount = (spaceArr[i].replace("\t", "    ").length()) / 8;
            for (int j = 0; j < spaceCount; j++) {
                res.append("+");
            }
            res.append("" + sArr[i] + " ");
//            if (explainArr[i].contains("非必传") || explainArr[i].contains("不必传输") || explainArr[i].contains("不必传")){
//                res.append(" | 否 ");
//            }else {
//                res.append(" | 是 ");
//            }
            String explainStr = explainArr[i].replace("\"\", //", "");
//            String explainStr = explainArr[i];
            String isR = " | 是 ";
            // 必填信息提取出来后表格里面删除是否必填的说明,然后删除中英文括号 editflag002
            for (String a : notRequired) {
                if (explainStr.contains(a)) {
                    explainStr = explainStr.replace("（" + a + "）", "").replace("(" + a + ")", "").replace("[" + a + "]", "");
                    isR = " | 否 ";
                    break;
                }
            }
            for (String a : isRequired) {
                if (explainStr.contains(a)) {
                    explainStr = explainStr.replace("（" + a + "）", "").replace("(" + a + ")", "").replace("[" + a + "]", "");
                    isR = " | 是 ";
                    break;
                }
            }
            if (!isRes) {
                res.append(isR);
            }
//            if (explainArr[i].contains("(必传)") || explainArr[i].contains("[必传]") || !explainArr[i].contains("不必传") || !explainArr[i].contains("非必传") ) {
//                res.append(" | 是 ");
//            } else {
//                res.append(" | 否 ");
//            }
            // 找他的字段类型,偶尔会有些接口文档的json里面有一些字段类型的信息,可以取出来用,不用手动复制粘贴
            String fieldType = searchFieldType(explainArr[i]);
            res.append(fieldType);
            // 可以去掉一些空值,影响观感,有值的留着好了,做参考,  然后一些只有[  或者 {  这种就是数组或者一个对象

            // 要么把只要有注释的把非注释内容都去掉好了
            // 这里有个风险(极少数情况出现的),有时候注释用的是# , 有时候用的是 //  然后有时候// 里面有 # , # 里面有斜杠.
            // 暂时只能给//和#分个前后顺序,然后取前面的那个
            if (explainStr.contains("/")) {
                explainStr = explainStr.substring(explainStr.indexOf("/") + 2, explainStr.length());
            }
            if (explainStr.replace(" ", "").equals("{")) {
                explainStr = "是个对象";
            } else if (explainStr.replace(" ", "").equals("[")) {
                explainStr = "是个数组";
            }
            res.append(" |" + explainStr + "   |\n");
        }
        if (repeatingObject.size() > 1) {
            System.out.println("######################有多个对象,请手动再继续处理对象里面的内容######################");
        }
//        System.out.println(res.toString());
        return res.toString();
    }

    /**
     * @Description 避免上面看起来太长, 加个查找字段类型的方法
     */
    static String searchFieldType(String explainStr) {
        // string[] 这种的判断放在  string前面,必经string[]里面也包含string这个字符串
        // 高频词汇放前面, 数组放前面(或者放在相关项目内)
        String res = " | String";
        if (explainStr.contains("string") || explainStr.contains("String")) {
            if (explainStr.contains("string[]") || explainStr.contains("String[]") || explainStr.contains("string数组") || explainStr.contains("String数组")) {
                res = " | String[] ";
            } else {
                res = " | String ";
            }
        } else if (explainStr.contains("int") || explainStr.contains("integer") || explainStr.contains("Integer")) {
            if (explainStr.contains("int[]") || explainStr.contains("Integer[]") || explainStr.contains("integer[]")) {
                res = " | int[] ";
            } else {
                res = " | int ";
            }// 下面那仨其实都不常见,如果还有类型再添
        } else if (explainStr.contains("boolean") || explainStr.contains("Boolean")) {
            res = " | boolean ";
        } else if (explainStr.contains("long") || explainStr.contains("Long")) {
            res = " | long ";
        } else if (explainStr.contains("decimal") || explainStr.contains("Decimal")) {
            res = " | decimal ";
        }

        return res;
    }

    /**
     * @Description 下划线转驼峰式
     */
    private static String changeUnderToUpperLetter(String str) {
        String regExp = "(_)([a-z]{1})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group().replaceAll("_", "").toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * // 暂未解决  // 解决不同对象相同参数名的问题,例如
     * // 不解决了,
     * ```
     * {
     *     "projectId":"", // 项目id [String](必传)
     *     "spaceInstallId":"", // 安装位置 [String](必传)
     *     "platformEquId":"", // 设备标准名id（设备分类组成的一部分） [String](必传)
     *     "equName":"", // 设备名称 [String](必传)
     *     "equCode":"", // 设备编码  [String](必传)
     *     "equStatus":"", // 设备状态（run：运行; malfunction：故障; scrapped：报废; maintain：维修; disable：停用） [String](必传)
     *     "equBrand":"", // 设备品牌 [String](不必传)
     *     "equModel":"", // 设备型号 [String](不必传)
     *     "productDate":"", // 出厂日期 [date](不必传)
     *     "useDate":"", // 投用日期  [date](必传)
     *     "designLife":"", // 设计寿命  [int](必传)
     *     "equLevel":"", // 设备重要性（A：最高 B：次之 C：最低） [String](不必传)
     *     "detailAddress":"", // 详细位置 [String](不必传)
     *     "forceInspect":"", // 是否强制性检验: 强制-true,不强制-false  [boolean](不必传)
     *     "remark":"", // 备注 [String](不必传)
     *     "isIot":"", // 是否是iot设备 [boolean](不必传)
     *     "iotSupplierId":"", // iot设备数据采集供应商  [String](不必传)
     *     "iotEquArg":"", // iot设备参数 [String](不必传)
     *     "spaceServices":[ // 服务位置id集合 [String[]](不必传)
     *         "",
     *         ""
     *     ],
     *     "equFiles":[ // 图片id集合 [String[]](不必传)
     *         "",
     *         ""
     *     ],
     *     "manufacturer":{
     *         "objectId":"",// 生产商Id
     *         "name":"", // 生产商名称
     *         "contact":"", // 生产商联系人
     *         "phone":"", // 生产商联系电话
     *         "address":"" // 生产商地址
     *     },
     *     "supplier":{
     *         "objectId":"",// 供应商Id
     *         "name":"", // 供应商名称
     *         "contact":"", // 供应商联系人
     *         "phone":"", // 供应商联系电话
     *         "address":"" // 供应商地址
     *     },
     *     "installFactor":{
     *         "objectId":"",// 安装单位Id
     *         "name":"", // 安装单位名称
     *         "contact":"", // 安装单位联系人
     *         "phone":"", // 安装单位联系电话
     *         "address":"" // 安装单位地址
     *     }
     * }
     * ```
     */

//    例子PRO  // 不解决了,  因为一般来说对象名一样参数名字也一样其实一般就是就是一张表里面不同的类型的数据而已,但是属性名字都差不多,所以不用都写在表格内
//    {
//        "projectId":"", // 项目 id [String](必传)
//            "spaceInstallId":"", // 安装位置 [String](必传)
//            "platformEquId":"", // 设备标准名 id（设备分类组成的一部分） [String](必传)
//            "equName":"", // 设备名称 [String](必传)
//            "equCode":"", // 设备编码 [String](必传)
//            "equStatus":"", // 设备状态（run：运行; malfunction：故障; scrapped：报废; maintain：维修; disable：停用） [String](必传)
//            "equBrand":"", // 设备品牌 [String](不必传)
//            "equModel":"", // 设备型号 [String](不必传)
//            "productDate":"", // 出厂日期 [date](不必传)
//            "useDate":"", // 投用日期 [date](必传)
//            "designLife":"", // 设计寿命 [int](必传)
//            "equLevel":"", // 设备重要性（A：最高 B：次之 C：最低） [String](不必传)
//            "detailAddress":"", // 详细位置 [String](不必传)
//            "forceInspect":"", // 是否强制性检验: 强制-true,不强制-false [boolean](不必传)
//            "remark":"", // 备注 [String](不必传)
//            "isIot":"", // 是否是 iot 设备 [boolean](不必传)
//            "iotSupplierId":"", // iot 设备数据采集供应商 [String](不必传)
//            "iotEquArg":"", // iot 设备参数 [String](不必传)
//            "spaceServices":[ // 服务位置 id 集合 [String[]](不必传)
//        "",
//                ""
//    ],
//        "equFiles":[ // 图片 id 集合 [String[]](不必传)
//        "",
//                ""
//    ],
//        "manufacturer":{
//        "objectId":"",// 生产商 Id
//                "name":"", // 生产商名称
//                "contact":"", // 生产商联系人
//                "phone":"", // 生产商联系电话
//                "address":"" // 生产商地址
//    },
//        "supplier":{
//        "objectId":"",// 供应商 Id
//                "name":"", // 供应商名称
//                "contact":"", // 供应商联系人
//                "phone":"", // 供应商联系电话
//                "address":"" // 供应商地址
//    },
//        "installFactor":{
//        "objectId":"",// 安装单位 Id
//                "name":"", // 安装单位名称
//                "contact":"", // 安装单位联系人
//                "phone":"", // 安装单位联系电话
//                "address":"" // 安装单位地址
//    }
//        "sssppp":[
//        {
//            "objectId":"",// 供应商 Id
//                "name":"", // 供应商名称
//                "contact":"", // 供应商联系人
//                "phone":"", // 供应商联系电话
//                "address":"" // 供应商地址
//        },
//        {
//            "objectId":"",// 供应商 Id
//                "name":"", // 供应商名称
//                "contact":"", // 供应商联系人
//                "phone":"", // 供应商联系电话
//                "address":"" // 供应商地址
//        },
//    ],
//        "iiifff":[
//        {
//            "objectId":"",// 安装单位 Id
//                "name":"", // 安装单位名称
//                "contact":"", // 安装单位联系人
//                "phone":"", // 安装单位联系电话
//                "address":"" // 安装单位地址
//        },
//        {
//            "objectId":"",// 安装单位 Id
//                "name":"", // 安装单位名称
//                "contact":"", // 安装单位联系人
//                "phone":"", // 安装单位联系电话
//                "address":"" // 安装单位地址
//        },
//        {
//            "objectId":"",// 安装单位 Id
//                "name":"", // 安装单位名称
//                "contact":"", // 安装单位联系人
//                "phone":"", // 安装单位联系电话
//                "address":"" // 安装单位地址
//        }
//    ]
//    }

}
