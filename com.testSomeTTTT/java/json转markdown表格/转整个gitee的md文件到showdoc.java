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


        String origin = "#### URL:http://192.168.1.201:8926/inventoryStatistic/supplier\n" +
                "#### 请求方式: POST\n" +
                "#### 负责人：蔡绍东\n" +
                "#### 参数: \n" +
                "```\n" +
                "{\n" +
                "    \"pageNum\":\"页数（int）（必须）\",\n" +
                "    \"pageSize\":\"每页数量（int）（必须）\",\n" +
                "    \"projectId\":\"项目id（string）（必须）\",\n" +
                "    \"supplier\":\"供应商id（string）（可选）\",\n" +
                "    \"inboundStartTime\":\"入库查询开始时间（string，格式：yyyy-MM-dd HH:mm:ss）（可选，不传默认查询当月）\",\n" +
                "    \"inboundEndTime\":\"入库查询结束时间（string，格式：yyyy-MM-dd HH:mm:ss）（可选）\"\n" +
                "}\n" +
                "```\n" +
                "#### 返回值：\n" +
                "```\n" +
                "{\n" +
                "    \"code\": 200,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"val\": {\n" +
                "        \"statistic\": {\n" +
                "            \"supplierCount\": \"供应商数（int）\",\n" +
                "            \"inboundPrice\": \"总金额（decimal）\"\n" +
                "        },\n" +
                "        \"supplierPage\": {\n" +
                "            \"pageNum\": 1,\n" +
                "            \"pageSize\": 10,\n" +
                "            \"orderBy\": \"\",\n" +
                "            \"countTotal\": true,\n" +
                "            \"result\": [\n" +
                "                {\n" +
                "                    \"supplier\": \"供应商id（string）\",\n" +
                "                    \"supplierNumber\": \"供应商编号（string）\",\n" +
                "                    \"supplierName\": \"供应商名称（string）\",\n" +
                "                    \"totalInboundPrice\": \"合计金额\",\n" +
                "                    \"supplierSearchDetailVOList\": [\n" +
                "                        {\n" +
                "                            \"accountNumber\": \"库存商品会计科目编号（string）\",\n" +
                "                            \"accountName\": \"库存商品会计科目名称（string）\",\n" +
                "                            \"inboundPrice\": \"金额（decimal）\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ],\n" +
                "            \"totalLength\": 1,\n" +
                "            \"offset\": 0,\n" +
                "            \"orderBySetted\": false\n" +
                "        }\n" +
                "    },\n" +
                "    \"success\": 1\n" +
                "}\n" +
                "```\n" +
                "#### 导出URL:http://192.168.1.201:8926/inventoryStatistic/supplier/export\n" +
                "#### 请求方式: POST\n" +
                "#### 负责人：蔡绍东\n" +
                "#### 参数: 同上";


        origin = origin + "\n";

        if (origin.contains(". 请求地址")){
            // (^\d*)(\.\s*)([\u4e00-\u9fa5]*)        to            - $3
            // 标题要大于两个字   ,   很见鬼,有些符号也算是中文
            String pattern = "(\\d*)(\\.\\s*)([\\u4e00-\\u9fa5]{2,})";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(origin);
            StringBuffer sb = new StringBuffer();
            while (m.find()){
                String tmp  = m.group(0);
                String key = m.group(3);
                m.appendReplacement(sb, "- "+key);
            }
            m.appendTail(sb);
//            System.out.println(sb.toString());
            origin = sb.toString();
        }

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
        origin = origin.replace(" ```", "```");

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

//        String 返回值的那段文本
        String resStart = "";
        // 传参和返参所在的部分可以用"返回示例"或者"返回值"字样来区分开
        int splitIndex = 0;
        List<String> partsList = new ArrayList<>();
        // 返回示例后面跟着的一般就是返回值json了,不能用返回参数说明什么的来分割,因为返回参数说明一般都是在返回示例下面的
        if (origin.contains("返回示例")) {
            resStart = "返回示例";
            String[] returns = origin.split("返回示例");
            Arrays.stream(returns).forEach(a -> partsList.add(a));
        } else if (origin.contains("返回值")) {
            resStart = "返回值";
            String[] returns = origin.split("返回值");
            Arrays.stream(returns).forEach(a -> partsList.add(a));
            // editflag001
        } else if (origin.contains("响应结果")) {
            resStart = "响应结果";
            String[] returns = origin.split("响应结果");
            Arrays.stream(returns).forEach(a -> partsList.add(a));
        }
        if (null == partsList || partsList.size() <= 0) {
            throw new Exception("出错了,这个可能没返回值");
        }
        String part01 = partsList.get(0);
        String part02 = partsList.get(1);
//        if (origin)
        String param1 = part01.substring(part01.indexOf("```json"), part01.lastIndexOf("```"));
//        System.out.println(param1+"\n\n=======================================\n\n");
        String param2 = part02.substring(part02.indexOf("```"), part02.lastIndexOf("`") + 1);
//        System.out.println(param2);


        String mdTable1 = toShowDocMultiLevel(partsList.get(0), 0, false);
        String mdTable2 = toShowDocMultiLevel(partsList.get(1), 0, true);
//        String mdTable2 = toShowDocResMultiLevelv2(partsList.get(1), 0);
        // 如果以后文件很大速度慢了,可以试着哪里可能有正则里面的内容就把那段用关键字剪切出来
        // 裁出一段子字符串应该是比正则的匹配要快的
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
    private static String toShowDocMultiLevel(String origin, int pp, boolean isRes) throws Exception {

        // ,int pp
//        System.out.println("==========================尝试多级传参===========================");
        // 检测重复(用于对象数组的情况)
        Set<String> variableName = new HashSet<>();
        Set<String> repeatingObject = new HashSet<>();
        String[] originArr = origin.split("\n");
        StringBuilder sb = new StringBuilder();
        // 变量前面的退格数量
        StringBuilder sb2 = new StringBuilder();
        StringBuilder explain = new StringBuilder();

        // 前面的空格到参数名位置,有个地方要用到.
        StringBuilder sb3 = new StringBuilder();

//        (\s*)(".*?)([0-9a-zA-Z]*)(".*?:.*?\s*?)(.*)(\s*?.*?)
//        (\s*)(".*?)([0-9a-zA-Z]*)(".*?:.*?\s*?)(.*)(\s*?.*?)
//        (^\x20\s*)(".*?)([0-9a-zA-Z]*)(".*?:.*?\s*?)(.*)(\s*?.*?)
        // 更改,必须以空格开头,否则会匹配到换行符   (还是用原来的,但是上下相隔4个空字符才算孩子),其实两个就够了,因为一般也就多出一个\n,或者可以折中改为3个
        String regExp = "(\\s*)(\".*?)([0-9a-zA-Z]*)(\".*?:.*?\\s*?)(.*)(\\s*?.*?)\n";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(origin);
        StringBuffer res = new StringBuffer();
        while (matcher.find()) {
            sb3.append(matcher.group(1)).append(matcher.group(2)).append(matcher.group(3)).append(",");
            sb2.append(matcher.group(1)).append(",");
            sb.append(matcher.group(3)).append(",");
            // 描述文本里面经常有逗号,不常见#号,用#号好了
            // 刚刚发现的错误,#会被当做注释符号,那还是用其他的好了.
            explain.append(matcher.group(5)).append("&");
        }
        String[] sHeadArr = sb3.toString().split(",");
        String[] sArr = changeUnderToUpperLetter(sb.toString()).split(",");
        String[] spaceArr = sb2.toString().split(",");
        String[] explainArr = explain.toString().split("&");
//        boolean isChild = false;
        // 标记上次的缩进个数  刚开始初始化应该用
        int spaceNum = 0;
        if (sArr.length>0){
            spaceNum = spaceArr[0].length();
        }
        // 一般来说缩进就是4个空格
        int tabSize = 4;
        int plusNum = 0;
        // 标记现在是否在某个孩子内
        boolean isChild = false;
        Stack<Integer> isChildStack = new Stack<>();
        int flag = 0;
        String flagStr = "";
        forOne:
        for (int i = 0; i < sArr.length; i++) {
            // 不同层级的objectId给区分开来,同层级的不允许重复出现
//            这个是用于判断是否该写入markdown表格的的key
            String key = sArr[i] + "" + spaceArr[i].length();

            // 如果这个参数是前面有个对象或者对象数组,也就是这个参数对象或者对象数组的孩子,那么
            // 用来解决多对象啊或者多对象数组的问题,考虑以后暂时先不解决,感觉一般都是同表的不同type,其实没必要重复加字段
//            if (isChild) {
//
//            }

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
            // spaceNum是全局记录用的,spaceLen是当前的
            int spaceLen = spaceArr[i].replace("\t","    ").length();
//            if (spaceArr[i].length() >= 8 && ("{".equals(explainArr[i - 1].replace(" ", "")) || "[".equals(explainArr[i - 1].replace(" ", "")))) {
            if (spaceArr[i].length() >= 4 ) {
                // 对象的孩子退四格
                // 对象数组的孩子退格   ,  因为\n的干扰,所以用当前的减去记录的值大于2(或者大于1就行,大于一可以避免有些退格符只有两个的情况
                // 但是鉴于一般都是缩进四个空格,而且万一啥时候多按了个空格也会出错,还是大于2好了,大于3也行,但是如果他少按个空格也会错,)
                // 总共可能的情况是 2,   3,4,5   而因为是否有\n的情况会向上浮动1个字符,   再加   3    4,5,6.  \n发生概率也高,因为只要不符合有参数名的,前面一行的\n会匹配到现在这行,所以最常见的情况是
//          2,4    然后加1就是    3,5      也就是 2,3,4,5最常见                  但是0和1绝对不算是缩进,     综上     spaceLen - spaceNum  > 1 来判断
                if ( spaceLen - spaceNum  >1   ){
                    // 这里说明进入了对象或者对象数组,4以上的是不会判断的,那个肯定是第一层级,然后这个tabSize初始化的时候也就是第一层级
                    isChild = true;
                    isChildStack.push(spaceLen);
                    spaceNum = spaceLen;
                }
                else if (spaceLen - spaceNum < - 1 ){
                    isChild = false;
                    // 有来有回,回来了说明已经又到同级了.  有时候回来是一次性回两层的,所以这里好像有些不对.
//                    回来的层数不一定能完全确定,所以有些不好判断()
//                    暂时有个方法,把这回的参数之间的字符串截取出来,看有几个 }   这个
                    // 如果两个对象内有两个相同参数,那么indexOf就会取到比较先的那个,这个好像不大准,取最后一个什么的也有隐患.
                    // 主要是相同变量名相同缩进的变量这个有些多
//                    int nowIndex =
                    int substrEnd = origin.lastIndexOf(sHeadArr[i]);
                    int substrStart = origin.indexOf(sHeadArr[i-1]);
                    while (substrStart < substrEnd){
                        int tmp = origin.indexOf(sHeadArr[i-1],substrStart + 1);
                        if ( tmp!=-1 && tmp < substrEnd){
                            substrStart = tmp;
                        }else {
                            break ;
                        }

                    }
                    String bt = " 为了避免符号前后没东西  " +origin.substring(substrStart, substrEnd)+" 为了避免符号前后没东西  ";
                    // 偶尔层级会很多
                    if (bt.split("\n").length>6){
                        throw new Exception("是否有对象数组且里面的对象都字段相同的例子?麻烦把对象数组里面的对象只留下一个");
                    }
                    String[] btArr = bt.split("}");
//                    if (bt)
                    int backspaceNum = btArr.length-1;
                    for (int l=0;l<backspaceNum;l++){
                        isChildStack.pop();
                    }

                    spaceNum = spaceLen;
                }
                if (isChildStack.size()>0 && i>0){
                    flagStr = sArr[i - 1];
                    String repeatingKey = flagStr + key;
                    repeatingObject.add(repeatingKey);
                }
            }
            plusNum = isChildStack.size();
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
            // 对象他的孩子是退4个空格,对象数组他的孩子是退8个空格,这里需要再次判断
            int spaceCount = (spaceArr[i].replace("\t", "    ").length()) / 8;

            for (int j = 0; j < plusNum; j++) {
                res.append("+");
            }
            res.append("" + sArr[i] + " ");
//            if (explainArr[i].contains("非必传") || explainArr[i].contains("不必传输") || explainArr[i].contains("不必传")){
//                res.append(" | 否 ");
//            }else {
//                res.append(" | 是 ");
//            }
            //
            String explainStr = explainArr[i].replace("\"\", //", "");
//            String explainStr = explainArr[i];
            String isR = " | 是 ";
            // 必填信息提取出来后表格里面删除是否必填的说明,然后删除中英文括号 editflag002
            for (String a : notRequired) {
                if (explainStr.contains(a)) {
                    explainStr = explainStr.replace("（" + a + "）", "").replace("(" + a + ")", "")
                            .replace("[" + a + "]", "").replace(a, "");
                    isR = " | 否 ";
                    break;
                }
            }
            for (String a : isRequired) {
                if (explainStr.contains(a)) {
                    explainStr = explainStr.replace("（" + a + "）", "").replace("(" + a + ")", "")
                            .replace("[" + a + "]", "").replace(a, "");
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
            String fieldTypePure = fieldType.replace(" ","").replace("|","");
            // 字段类型默认就是小写吧,一般情况下也都是小写,除非当时写接口的人切换到英文输入法再切大写弄成首字母大写,一般都是小写的.不去做其他判断了
            explainStr = explainStr.replace("[" + fieldTypePure + "]", "").replace("(" + fieldTypePure + ")", "")
                    .replace("（" + fieldTypePure + "）", "").replace(fieldTypePure, "");
            res.append(fieldType);
            // 可以去掉一些空值,影响观感,有值的留着好了,做参考,  然后一些只有[  或者 {  这种就是数组或者一个对象

            // 要么把只要有注释的把非注释内容都去掉好了
            // 这里有个风险(极少数情况出现的),有时候注释用的是# , 有时候用的是 //  然后有时候// 里面有 # , # 里面有斜杠.
            // 暂时只能给//和#分个前后顺序,然后取前面的那个

//            这个出错了,应该匹配两个//
//              #维保总完成工时（单位分钟/单）
            if (explainStr.contains("//")) {
                explainStr = explainStr.substring(explainStr.indexOf("//") + 2);
            }
            // 之前
            if (explainStr.contains("#")) {
                explainStr = explainStr.substring(explainStr.indexOf("#") + 1);
            }
            if ("{".equals(explainStr.replace(" ", ""))) {
                explainStr = "是个对象";
            } else if ("[".equals(explainStr.replace(" ", ""))) {
                explainStr = "是个数组";
            }
            res.append(" |" + explainStr + "   |\n");
        }
        if (repeatingObject.size() > 1) {
            System.out.println("######################有多个对象,请手动再继续处理对象里面的内容######################");
        }
//        System.out.println(res.toString());
        String resStr = res.toString();
        if (resStr.contains("+success")){
            // 一般都是返回高级的缩进出问题,相同层级同名字段过多会出现这个问题,但是概率小,如果出现就抛异常
            throw new Exception("缩进可能出问题了,检查一下层级(也就是字段名前面的+号个数是否正确),应该就几个,可以手动更改一下.");
        }
        return resStr;
    }

    /**
     * @Description 避免上面看起来太长, 加个查找字段类型的方法
     */
    static String searchFieldType(String explainStr) {
        // string[] 这种的判断放在  string前面,必经string[]里面也包含string这个字符串
        // 高频词汇放前面, 数组放前面(或者放在相关项目内)
        String res = " | string";
        if (explainStr.contains("string") || explainStr.contains("String")) {
            if (explainStr.contains("string[]") || explainStr.contains("String[]") || explainStr.contains("string数组") || explainStr.contains("String数组")) {
                res = " | string[] ";
            } else {
                res = " | string ";
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
