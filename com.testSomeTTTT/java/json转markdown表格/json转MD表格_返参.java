package json转markdown表格;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description
 * @author: yyp
 * @create: 2021-09-24
 **/
public class json转MD表格_返参 {

    //    private static Set<String> isRequired = new HashSet<>(Arrays.asList("非必传"));
    private static List<String> isRequired = Arrays.asList("非必传","不必传输","不必传","可选","选填");

    public static void main(String[] args) {

        // 系统关键字:   [   ]   {   }   "  :   ,   \n   #  ,     注释不要换行写.推荐写在参数后面,注释可以用// 或者 # ,后面可以写一些系统关键字,因为不会被记入括号匹配
        // 参数名称一定要加引号,然后写上冒号(均为英文),后面再写参数值和注释
        // 也就是 这种样式 "projectId":"proj10086", // 项目 id [String](必传)   ,末尾再来个换行(\n)
        // 可以匹配第一个逗号或者第一个左大括号或者中括号后面的就是注释
        // 多对象或者多对象数组的测试用例,暂未解决,尝试解决
        // 对象或者对象数组或者普通字符串对象的特征是,
        // 对象数组的名字例如退格是4,然后他的格式就是"iiifff":[然后直接换行,后面每个对象外框退格是12(但是单个{不会被正则匹配到),每个对象的子参数就是20了.也就是参数里最后两个对象数组
        // 对象的格式是退格例如是4,格式是"manufacturer":{,下一个匹配到的子变量应该是12的前置空格
        // 如果是普通对象,普通对象的内容没有冒号不会被匹配进去,下一个对象就是他本身也就是
//        String origin = "```\n" +
//                "{\n" +
//                "    \"projectId\":\"\", // 项目 id [String](必传)\n" +
//                "    \"spaceInstallId\":\"\", // 安装位置 [String](必传)\n" +
//                "    \"platformEquId\":\"\", // 设备标准名 id（设备分类组成的一部分） [String](必传)\n" +
//                "    \"equName\":\"\", // 设备名称 [String](必传)\n" +
//                "    \"equCode\":\"\", // 设备编码 [String](必传)\n" +
//                "    \"equStatus\":\"\", // 设备状态（run：运行; malfunction：故障; scrapped：报废; maintain：维修; disable：停用） [String](必传)\n" +
//                "    \"equBrand\":\"\", // 设备品牌 [String](不必传)\n" +
//                "    \"equModel\":\"\", // 设备型号 [String](不必传)\n" +
//                "    \"productDate\":\"\", // 出厂日期 [date](不必传)\n" +
//                "    \"useDate\":\"\", // 投用日期 [date](必传)\n" +
//                "    \"designLife\":\"\", // 设计寿命 [int](必传)\n" +
//                "    \"equLevel\":\"\", // 设备重要性（A：最高 B：次之 C：最低） [String](不必传)\n" +
//                "    \"detailAddress\":\"\", // 详细位置 [String](不必传)\n" +
//                "    \"forceInspect\":\"\", // 是否强制性检验: 强制-true,不强制-false [boolean](不必传)\n" +
//                "    \"remark\":\"\", // 备注 [String](不必传)\n" +
//                "    \"isIot\":\"\", // 是否是 iot 设备 [boolean](不必传)\n" +
//                "    \"iotSupplierId\":\"\", // iot 设备数据采集供应商 [String](不必传)\n" +
//                "    \"iotEquArg\":\"\", // iot 设备参数 [String](不必传)\n" +
//                "    \"spaceServices\":[ // 服务位置 id 集合 [String[]](不必传)\n" +
//                "            \"\",\n" +
//                "            \"\"\n" +
//                "    ],\n" +
//                "    \"equFiles\":[ // 图片 id 集合 [String[]](不必传)\n" +
//                "            \"\",\n" +
//                "            \"\"\n" +
//                "    ],\n" +
//                "    \"manufacturer\":{\n" +
//                "            \"objectId\":\"\",// 生产商 Id\n" +
//                "            \"name\":\"\", // 生产商名称\n" +
//                "            \"contact\":\"\", // 生产商联系人\n" +
//                "            \"phone\":\"\", // 生产商联系电话\n" +
//                "            \"address\":\"\" // 生产商地址\n" +
//                "    },\n" +
//                "    \"supplier\":{\n" +
//                "            \"objectId\":\"\",// 供应商 Id\n" +
//                "            \"name\":\"\", // 供应商名称\n" +
//                "            \"contact\":\"\", // 供应商联系人\n" +
//                "            \"phone\":\"\", // 供应商联系电话\n" +
//                "            \"address\":\"\" // 供应商地址\n" +
//                "    },\n" +
//                "    \"installFactor\":{\n" +
//                "            \"objectId\":\"\",// 安装单位 Id\n" +
//                "            \"name\":\"\", // 安装单位名称\n" +
//                "            \"contact\":\"\", // 安装单位联系人\n" +
//                "            \"phone\":\"\", // 安装单位联系电话\n" +
//                "            \"address\":\"\" // 安装单位地址\n" +
//                "    }\n" +
//                "    \"sssppp\":[\n" +
//                "            {\n" +
//                "                    \"objectId\":\"\",// 供应商 Id\n" +
//                "                    \"name\":\"\", // 供应商名称\n" +
//                "                    \"contact\":\"\", // 供应商联系人\n" +
//                "                    \"phone\":\"\", // 供应商联系电话\n" +
//                "                    \"address\":\"\" // 供应商地址\n" +
//                "            },\n" +
//                "            {\n" +
//                "                    \"objectId\":\"\",// 供应商 Id\n" +
//                "                    \"name\":\"\", // 供应商名称\n" +
//                "                    \"contact\":\"\", // 供应商联系人\n" +
//                "                    \"phone\":\"\", // 供应商联系电话\n" +
//                "                    \"address\":\"\" // 供应商地址\n" +
//                "            },\n" +
//                "    ],\n" +
//                "    \"iiifff\":[\n" +
//                "            {\n" +
//                "                    \"objectId\":\"\",// 安装单位 Id\n" +
//                "                    \"name\":\"\", // 安装单位名称\n" +
//                "                    \"contact\":\"\", // 安装单位联系人\n" +
//                "                    \"phone\":\"\", // 安装单位联系电话\n" +
//                "                    \"address\":\"\" // 安装单位地址\n" +
//                "            },\n" +
//                "                        {\n" +
//                "                    \"objectId\":\"\",// 安装单位 Id\n" +
//                "                    \"name\":\"\", // 安装单位名称\n" +
//                "                    \"contact\":\"\", // 安装单位联系人\n" +
//                "                    \"phone\":\"\", // 安装单位联系电话\n" +
//                "                    \"address\":\"\" // 安装单位地址\n" +
//                "            },\n" +
//                "                        {\n" +
//                "                    \"objectId\":\"\",// 安装单位 Id\n" +
//                "                    \"name\":\"\", // 安装单位名称\n" +
//                "                    \"contact\":\"\", // 安装单位联系人\n" +
//                "                    \"phone\":\"\", // 安装单位联系电话\n" +
//                "                    \"address\":\"\" // 安装单位地址\n" +
//                "            }\n" +
//                "    ]\n" +
//                "}\n" +
//                "```\n";

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
        String origin = "    \"finishType\":\"完工类型（1：完工，2：完成）（int）（必须）\",\n" +
                "    \"projectId\":\"项目id（string）（必须）\",\n" +
                "    \"startTime\":\"开始时间（string，格式：yyyy-MM-dd HH:mm:ss）（可选，不传默认查询当月）\",\n" +
                "    \"endTime\":\"结束时间（string，格式：yyyy-MM-dd HH:mm:ss）（可选）\",\n" +
                "    \"serviceId\":\"服务类型id（string）（可选）\"";

        // 最后一行经常因为没有\n检测不到,加上
        origin = origin + "\n";

        toShowDocMultiLevel(origin, 0);
//        System.out.println("===============上面的是传参,下面的是返参====================");
//        toShowDocRes(origin, 1);
//        System.out.println("==========================尝试多级返参===========================");
        toShowDocResMultiLevelv2(origin, 0);
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
     * @Description json转为markdown的表格
     * 暂时处理不了多个对象的情况,还有层级太多有多个对象也麻烦
     */
    private static String toShowDocMultiLevel(String origin, int pp) {// ,int pp
        System.out.println("==========================尝试多级传参===========================");
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
        forOne:for (int i = 0; i < sArr.length; i++) {
            // 不同层级的objectId给区分开来,同层级的不允许重复出现
//            这个是用于判断是否该写入markdown表格的的key
            String key = sArr[i]+""+spaceArr[i].length();
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

            if (spaceArr[i].length()>=8 && (explainArr[i - 1].replace(" ", "").equals("{") || explainArr[i - 1].replace(" ", "").equals("["))) {
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
                variableName.add(sArr[i]+""+spaceArr[i].length());
            }
            res.append("|");
            // 一个制表符是4个空格,一般来说第一级是4个空格,第二级是12个空格,第三级是20个空格,这里我选/8,每级多个+号
//            暂时没有遇到4,8,12的情况
            int spaceCount = (spaceArr[i].replace("\t","    ").length()) / 8;
            for (int j = 0; j < spaceCount; j++) {
                res.append("+");
            }
            res.append("" + sArr[i] + " ");
//            if (explainArr[i].contains("非必传") || explainArr[i].contains("不必传输") || explainArr[i].contains("不必传")){
//                res.append(" | 否 ");
//            }else {
//                res.append(" | 是 ");
//            }
            String explainStr = explainArr[i].replace("\"\", //","");
//            String explainStr = explainArr[i];
            String isR = " | 是 ";
            for (String a:isRequired){
                if (explainStr.contains(a)){
                    isR = " | 否 ";
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
            if (explainStr.contains("/")){
                explainStr = explainStr.substring(explainStr.indexOf("/")+2,explainStr.length());
            }
            if (explainStr.replace(" ","").equals("{") ){
                explainStr = "是个对象";
            }else if (explainStr.replace(" ","").equals("[")){
                explainStr = "是个数组";
            }
            res.append(" |" + explainStr + "   |\n");
        }
        if (repeatingObject.size()>1){
            System.out.println("######################有多个对象,请手动再继续处理对象里面的内容######################");
        }
        System.out.println(res.toString());
        return res.toString();
    }
    /**
     * 避免上面看起来太长,加个查找字段类型的方法
     */
    static String searchFieldType(String explainStr){
        // string[] 这种的判断放在  string前面,必经string[]里面也包含string这个字符串
        String res = " | String";
        if (explainStr.contains("string[]") || explainStr.contains("String[]")) {
            res=" | String[] ";
        } else if (explainStr.contains("int[]") || explainStr.contains("Integer[]") || explainStr.contains("integer[]")) {
            res=" | int[] ";
        } else if (explainStr.contains("boolean") || explainStr.contains("Boolean")) {
            res=" | boolean ";
        } else if (explainStr.contains("string") || explainStr.contains("String")){
            res=" | String ";
        }else if (explainStr.contains("int")){
            res=" | int ";
        }
        return res;
    }

    /**
     * @Description json转为markdown的表格
     * 暂时处理不了多个对象的情况,还有层级太多有多个对象也麻烦
     */
    private static String toShowDocResMultiLevelv2(String origin, int pp) {// ,int pp
        System.out.println("==========================尝试多级返参===========================");
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
        forOne:for (int i = 0; i < sArr.length; i++) {
            // 不同层级的objectId给区分开来,同层级的不允许重复出现
//            这个是用于判断是否该写入markdown表格的的key
            String key = sArr[i]+""+spaceArr[i].length();
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

            if (spaceArr[i].length()>=8 && (explainArr[i - 1].replace(" ", "").equals("{") || explainArr[i - 1].replace(" ", "").equals("["))) {
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
                variableName.add(sArr[i]+""+spaceArr[i].length());
            }
            res.append("|");
            int spaceCount = (spaceArr[i].replace("\t","     ").length()) / 8;
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
            String explainStr = explainArr[i].replace("\"\", //","");
            if (explainStr.replace(" ","").equals("{") ){
                explainStr = "是个对象";
            }else if (explainStr.replace(" ","").equals("[")){
                explainStr = "是个数组";
            }
            res.append(" |" + explainStr + "   |\n");
        }
        if (repeatingObject.size()>1){
            System.out.println("######################有多个对象,请手动再继续处理对象里面的内容######################");
        }
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

}
