package json转markdown表格;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description 改动记录:
 * 2021年9月24日14:02:40   yyp    改动地点为 editflag001   增加返回值可能取的标题
 * 2021年9月24日14:17:52  yyp    改动地点为  editflag002   必填信息提取出来后表格里面删除是否必填的说明,然后删除中英文括号
 * @author: yyp
 * @create: 2021-09-24
 **/
public class 转整个gitee的md文件到showdoc_backup_2021年9月24日144057 {

    //    private static Set<String> isRequired = new HashSet<>(Arrays.asList("非必传"));
    private static List<String> notRequired = Arrays.asList("非必传", "不必传输", "不必传", "可选", "选填");
    private static List<String> isRequired = Arrays.asList("必传", "必填", "必须");

    public static void main(String[] args) throws Exception {

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
        String origin = "{\n" +
                "    \"projectId\":\"\", // 项目 id [String](必传)\n" +
                "    \"spaceInstallId\":\"\", // 安装位置 [String](必传)\n" +
                "    \"platformEquId\":\"\", // 设备标准名 id（设备分类组成的一部分） [String](必传)\n" +
                "    \"equName\":\"\", // 设备名称 [String](必传)\n" +
                "    \"equCode\":\"\", // 设备编码 [String](必传)\n" +
                "    \"equStatus\":\"\", // 设备状态（run：运行; malfunction：故障; scrapped：报废; maintain：维修; disable：停用） [String](必传)\n" +
                "    \"equBrand\":\"\", // 设备品牌 [String](不必传)\n" +
                "    \"equModel\":\"\", // 设备型号 [String](不必传)\n" +
                "    \"productDate\":\"\", // 出厂日期 [date](不必传)\n" +
                "    \"useDate\":\"\", // 投用日期 [date](必传)\n" +
                "    \"designLife\":\"\", // 设计寿命 [int](必传)\n" +
                "    \"equLevel\":\"\", // 设备重要性（A：最高 B：次之 C：最低） [String](不必传)\n" +
                "    \"detailAddress\":\"\", // 详细位置 [String](不必传)\n" +
                "    \"forceInspect\":\"\", // 是否强制性检验: 强制-true,不强制-false [boolean](不必传)\n" +
                "    \"remark\":\"\", // 备注 [String](不必传)\n" +
                "    \"isIot\":\"\", // 是否是 iot 设备 [boolean](不必传)\n" +
                "    \"iotSupplierId\":\"\", // iot 设备数据采集供应商 [String](不必传)\n" +
                "    \"iotEquArg\":\"\", // iot 设备参数 [String](不必传)\n" +
                "    \"spaceServices\":[ // 服务位置 id 集合 [String[]](不必传)\n" +
                "            \"\",\n" +
                "            \"\"\n" +
                "    ],\n" +
                "    \"equFiles\":[ // 图片 id 集合 [String[]](不必传)\n" +
                "            \"\",\n" +
                "            \"\"\n" +
                "    ],\n" +
                "    \"manufacturer\":{\n" +
                "            \"objectId\":\"\",// 生产商 Id\n" +
                "            \"name\":\"\", // 生产商名称\n" +
                "            \"contact\":\"\", // 生产商联系人\n" +
                "            \"phone\":\"\", // 生产商联系电话\n" +
                "            \"address\":\"\" // 生产商地址\n" +
                "    },\n" +
                "    \"supplier\":{\n" +
                "            \"objectId\":\"\",// 供应商 Id\n" +
                "            \"name\":\"\", // 供应商名称\n" +
                "            \"contact\":\"\", // 供应商联系人\n" +
                "            \"phone\":\"\", // 供应商联系电话\n" +
                "            \"address\":\"\" // 供应商地址\n" +
                "    },\n" +
                "    \"installFactor\":{\n" +
                "            \"objectId\":\"\",// 安装单位 Id\n" +
                "            \"name\":\"\", // 安装单位名称\n" +
                "            \"contact\":\"\", // 安装单位联系人\n" +
                "            \"phone\":\"\", // 安装单位联系电话\n" +
                "            \"address\":\"\" // 安装单位地址\n" +
                "    }\n" +
                "    \"sssppp\":[\n" +
                "            {\n" +
                "                    \"objectId\":\"\",// 供应商 Id\n" +
                "                    \"name\":\"\", // 供应商名称\n" +
                "                    \"contact\":\"\", // 供应商联系人\n" +
                "                    \"phone\":\"\", // 供应商联系电话\n" +
                "                    \"address\":\"\" // 供应商地址\n" +
                "            },\n" +
                "            {\n" +
                "                    \"objectId\":\"\",// 供应商 Id\n" +
                "                    \"name\":\"\", // 供应商名称\n" +
                "                    \"contact\":\"\", // 供应商联系人\n" +
                "                    \"phone\":\"\", // 供应商联系电话\n" +
                "                    \"address\":\"\" // 供应商地址\n" +
                "            },\n" +
                "    ],\n" +
                "    \"iiifff\":[\n" +
                "            {\n" +
                "                    \"objectId\":\"\",// 安装单位 Id\n" +
                "                    \"name\":\"\", // 安装单位名称\n" +
                "                    \"contact\":\"\", // 安装单位联系人\n" +
                "                    \"phone\":\"\", // 安装单位联系电话\n" +
                "                    \"address\":\"\" // 安装单位地址\n" +
                "            },\n" +
                "                        {\n" +
                "                    \"objectId\":\"\",// 安装单位 Id\n" +
                "                    \"name\":\"\", // 安装单位名称\n" +
                "                    \"contact\":\"\", // 安装单位联系人\n" +
                "                    \"phone\":\"\", // 安装单位联系电话\n" +
                "                    \"address\":\"\" // 安装单位地址\n" +
                "            },\n" +
                "                        {\n" +
                "                    \"objectId\":\"\",// 安装单位 Id\n" +
                "                    \"name\":\"\", // 安装单位名称\n" +
                "                    \"contact\":\"\", // 安装单位联系人\n" +
                "                    \"phone\":\"\", // 安装单位联系电话\n" +
                "                    \"address\":\"\" // 安装单位地址\n" +
                "            }\n" +
                "    ]\n" +
                "}";
        origin = origin + "\n";

//        // 最后一行经常因为没有\n检测不到,加上
//        origin = origin + "\n";
////        生成[[传参]]md表格
//        toShowDocMultiLevel(origin, 0);
////      生成md[[返参]]表格
//        toShowDocResMultiLevelv2(origin, 0);

        System.out.println(toShowDocMultiLevel(origin, 0));
//        System.out.println(toWholeShowDoc(origin));

//        partsList.stream().forEach(a-> System.out.println(a+"\n\n===============================================\n\n"));
//        System.out.println("==================================");

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
        String param1 = part01.substring(part01.indexOf("`"), part01.lastIndexOf("`") + 1);
//        System.out.println(param1+"\n\n=======================================\n\n");
        String param2 = part02.substring(part02.indexOf("`"), part02.lastIndexOf("`") + 1);
//        System.out.println(param2);


        String mdTable1 = toShowDocMultiLevel(partsList.get(0), 0);
        String mdTable2 = toShowDocResMultiLevelv2(partsList.get(1), 0);
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
     * @Description json转为markdown的表格
     * 暂时处理不了多个对象的情况,还有层级太多有多个对象也麻烦
     */
    private static String toShowDocMultiLevel(String origin, int pp) {

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
        int flag = 0;
        String flagStr = "";
        forOne:
        for (int i = 0; i < sArr.length; i++) {
            // 不同层级的objectId给区分开来,同层级的不允许重复出现
//            这个是用于判断是否该写入markdown表格的的key
            String key = sArr[i] + "" + spaceArr[i].length();
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
                    isR = " | 否 ";
                    break;
                }
            }
            res.append(isR);
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
     * @Description json转为markdown的表格
     * 暂时处理不了多个对象的情况,还有层级太多有多个对象也麻烦
     */
    private static String toShowDocResMultiLevelv2(String origin, int pp) {
        int duplicateFlag = 0;
        // ,int pp
//        System.out.println("==========================尝试多级返参===========================");
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
            explain.append(matcher.group(5)).append("#");
        }
        String[] sArr = changeUnderToUpperLetter(sb.toString()).split(",");
        String[] spaceArr = sb2.toString().split(",");
        String[] explainArr = explain.toString().split("#");
        // 解决不同对象相同参数名的问题,例如
        /**
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
        int flag = 0;
        String flagStr = "";
        forOne:
        for (int i = 0; i < sArr.length; i++) {
            // 不同层级的objectId给区分开来,同层级的不允许重复出现
//            这个是用于判断是否该写入markdown表格的的key
            String key = sArr[i] + "" + spaceArr[i].length();
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

            // 前面一个的缩进比这个小,而且参数是 [  或者 { ,那就是对象或者对象数组
            if (spaceArr[i].length() >= 8 && spaceArr[i - 1].length() < spaceArr[i].length() && (explainArr[i - 1].replace(" ", "").equals("{") || explainArr[i - 1].replace(" ", "").equals("["))) {
                flagStr = sArr[i - 1];
                String repeatingKey = flagStr + key;
                if (repeatingObject.contains(repeatingKey)) {
                    duplicateFlag = 1;
                }
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
            int spaceCount = (spaceArr[i].replace("\t", "     ").length()) / 8;
            for (int j = 0; j < spaceCount; j++) {
                res.append("+");
            }
            res.append("" + sArr[i] + " ");
//            if (explainArr[i].contains("必传")) {
//                res.append(" | 是 ");
//            } else {
//                res.append(" | 否 ");
//            }
            // 找他的字段类型,偶尔会有些接口文档的json里面有一些字段类型的信息,可以取出来用,不用手动复制粘贴
            String fieldType = searchFieldType(explainArr[i]);
            res.append(fieldType);
            // 可以去掉一些空值,影响观感,有值的留着好了,做参考,  然后一些只有[  或者 {  这种就是数组或者一个对象
            String explainStr = explainArr[i].replace("\"\", //", "");
            if (explainStr.replace(" ", "").equals("{")) {
                explainStr = "是个对象";
            } else if (explainStr.replace(" ", "").equals("[")) {
                explainStr = "是个数组";
            }
            res.append(" |" + explainStr + "   |\n");
        }
//        if (repeatingObject.size() > 1) {
//            System.out.println("######################有多个对象,请手动再继续处理对象里面的内容######################");
//        }
        if (duplicateFlag == 1) {
            System.out.println("######################有多个对象,请手动再继续处理对象里面的内容######################");
        }
//        System.out.println(res.toString());
        return res.toString();
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
     * @Description 这部分是把字段转为 sql.append(" set remark = ? ");  args.add(ci.getTenantId());  这种样式
     * 参数1是Navicat数据库设计复制下来的字段(每行末尾带\n)
     * 参数2是代码中的变量名称
     */
    private static String toShowDoc(String origin, int pp) {
        String[] originArr = origin.split("\n");
        StringBuffer sb = new StringBuffer();
        StringBuilder explain = new StringBuilder();
//        String regExp = "(\\s*`[a-z]{1})(.*?`)(.*?)('.*?')(\n)";
        String regExp = "(.*?\".*?)([0-9a-zA-Z]*)(\".*?:.*?\\s*?)(.*)(\\s*?.*?)\n";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(origin);
        StringBuffer res = new StringBuffer();
        while (matcher.find()) {
            sb.append(matcher.group(2)).append(",");
            explain.append(matcher.group(4)).append("#");
        }
        String[] sArr = changeUnderToUpperLetter(sb.toString()).split(",");
        String[] explainArr = explain.toString().split("#");
        for (int i = 0; i < sArr.length; i++) {
            res.append("|");
            for (int j = 0; j < pp; j++) {
                res.append("+");
            }
            res.append("" + sArr[i] + " |否  |String |" + explainArr[i] + "   |\n");
        }
        System.out.println("======================这个是写新增的接口文档用的========================");
        System.out.println(res.toString());
        return res.toString();
    }

    /**
     * @Description 这部分是把字段转为 sql.append(" set remark = ? ");  args.add(ci.getTenantId());  这种样式
     * 参数1是Navicat数据库设计复制下来的字段(每行末尾带\n)
     * 参数2是代码中的变量名称
     */
    private static String toShowDocRes(String origin, int pp) {// ,int pp
        String[] originArr = origin.split("\n");
        StringBuffer sb = new StringBuffer();
        StringBuilder explain = new StringBuilder();
//        String regExp = "(\\s*`[a-z]{1})(.*?`)(.*?)('.*?')(\n)";
        String regExp = "(.*?\".*?)([0-9a-zA-Z]*)(\".*?:.*?\\s*?)(.*)(\\s*?.*?)\n";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(origin);
        StringBuffer res = new StringBuffer();
        while (matcher.find()) {
            sb.append(matcher.group(2)).append(",");
            explain.append(matcher.group(4)).append("#");
        }
        String[] sArr = changeUnderToUpperLetter(sb.toString()).split(",");
        String[] explainArr = explain.toString().split("#");
        for (int i = 0; i < sArr.length; i++) {
            res.append("|");
            for (int j = 0; j < pp; j++) {
                res.append("+");
            }
            res.append("" + sArr[i] + " |String |" + explainArr[i] + "   |\n");
        }
        System.out.println("======================这个是写新增的接口文档用的========================");
        System.out.println(res.toString());
        return res.toString();
    }

    /**
     * @Description 这部分是把字段转为 sql.append(" set remark = ? ");  args.add(ci.getTenantId());  这种样式
     * 参数1是Navicat数据库设计复制下来的字段(每行末尾带\n)
     * 参数2是代码中的变量名称
     */
    private static String toShowDocResMultiLevel(String origin, int pp) {// ,int pp
        System.out.println("==========================尝试多级返参===========================");
        // 检测重复
        Set<String> variableName = new HashSet<>();
        String[] originArr = origin.split("\n");
        StringBuffer sb = new StringBuffer();
        // 变量前面的退格数量
        StringBuffer sb2 = new StringBuffer();
        StringBuilder explain = new StringBuilder();
        // String regExp = "(\\s*`[a-z]{1})(.*?`)(.*?)('.*?')(\n)";
        String regExp = "(\\s*)(\".*?)([0-9a-zA-Z]*)(\".*?:.*?\\s*?)(.*)(\\s*?.*?)\n";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(origin);
        StringBuffer res = new StringBuffer();
        while (matcher.find()) {
            sb2.append(matcher.group(1)).append(",");
            sb.append(matcher.group(3)).append(",");
            explain.append(matcher.group(5)).append("#");
        }
        String[] sArr = changeUnderToUpperLetter(sb.toString()).split(",");
        String[] spaceArr = sb2.toString().split(",");
        String[] explainArr = explain.toString().split("#");
        for (int i = 0; i < sArr.length; i++) {
            if (variableName.contains(sArr[i])) {
                continue;
            } else {
                variableName.add(sArr[i]);
            }
            res.append("|");
//            int spaceCount = (spaceArr[i].length()-4)/4;
            int spaceCount = (spaceArr[i].length()) / 8;
            for (int j = 0; j < spaceCount; j++) {
                res.append("+");
            }
            res.append("" + sArr[i] + " |String |" + explainArr[i] + "   |\n");
        }
        System.out.println(res.toString());
        return res.toString();
    }

    /**
     * @Description 这部分是把字段转为 sql.append(" set remark = ? ");  args.add(ci.getTenantId());  这种样式
     * 参数1是Navicat数据库设计复制下来的字段(每行末尾带\n)
     * 参数2是代码中的变量名称
     */
    private static String toShowDocWithChild(String origin) {
        String[] originArr = origin.split("\n");
        StringBuffer sb = new StringBuffer();
        StringBuilder explain = new StringBuilder();
//        String regExp = "(\\s*`[a-z]{1})(.*?`)(.*?)('.*?')(\n)";
        String regExp = "(.*?\".*?)([a-zA-Z]*)(\".*?:.*?\\s*?)(.*)(\\s*?.*?)\n";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(origin);
        StringBuffer res = new StringBuffer();
        while (matcher.find()) {
            sb.append(matcher.group(2)).append(",");
            explain.append(matcher.group(4)).append("#");
        }
        String[] sArr = changeUnderToUpperLetter(sb.toString()).split(",");
        String[] explainArr = explain.toString().split("#");
        for (int i = 0; i < sArr.length; i++) {
            res.append("|" + sArr[i] + " |否  |String |" + explainArr[i] + "   |\n");
        }
        System.out.println("======================这个是写新增的接口文档用的========================");
        System.out.println(res.toString());
        return res.toString();
    }

    /**
     * @Description 这部分是把字段转为 sql.append(" set remark = ? ");  args.add(ci.getTenantId());  这种样式
     * 参数1是Navicat数据库设计复制下来的字段(每行末尾带\n)
     * 参数2是代码中的变量名称
     */
    private static String toShowDocWithSpecialOirgin(String origin, int pp) {
        String[] originArr = origin.split("\n");
        StringBuffer sb = new StringBuffer();
        StringBuilder explain = new StringBuilder();
//        String regExp = "(\\s*`[a-z]{1})(.*?`)(.*?)('.*?')(\n)";
        String regExp = "(.*?\".*?)([0-9a-zA-Z]*)(\".*?:.*?\\s*?)(.*)(\\s*?.*?)\n";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(origin);
        StringBuffer res = new StringBuffer();
        while (matcher.find()) {
            sb.append(matcher.group(2)).append(",");
            explain.append(matcher.group(4)).append("#");
        }
        String[] sArr = changeUnderToUpperLetter(sb.toString()).split(",");
        String[] explainArr = explain.toString().split("#");
        for (int i = 0; i < sArr.length; i++) {
            res.append("|");
            for (int j = 0; j < pp; j++) {
                res.append("+");
            }
            res.append("" + sArr[i] + " |否  |String |" + explainArr[i] + "   |\n");
        }
        System.out.println("======================这个是写新增的接口文档用的========================");
        System.out.println(res.toString());
        return res.toString();
    }

    public static String formatJson(String json) {
        StringBuffer result = new StringBuffer();
        int length = json.length();
        int number = 0;
        char key = 0;
//遍历输入字符串
        for (int i = 0; i < length; i++) {
//获取当前字符
            key = json.charAt(i);
//如果当前字符是前方括号、前花括号做如下处理
            if ((key == '[') || (key == '{')) {
                if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                    result.append('\n');
                    result.append(indent(number));
                }
//打印当前的字符
                result.append(key);
                result.append('\n');
                number++;
                result.append(indent(number));
                continue;
            }
            if ((key == ']' || (key == '}'))) {
                result.append('\n');
                number--;
                result.append(indent(number));
                result.append(key);
                if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                    result.append('\n');
                }
                continue;
            }
            if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }
            result.append(key);
        }
        return result.toString();
    }

    private static String SPACE = " ";

    private static String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }


    /**
     * // 暂未解决  // 解决不同对象相同参数名的问题,例如
     *
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

}
