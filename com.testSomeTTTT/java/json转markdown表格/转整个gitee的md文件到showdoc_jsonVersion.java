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
public class 转整个gitee的md文件到showdoc_jsonVersion {

    //    private static Set<String> isRequired = new HashSet<>(Arrays.asList("非必传"));
    private static List<String> notRequired = Arrays.asList("非必传", "不必传输", "不必传", "可选", "选填");
    private static List<String> isRequired = Arrays.asList("必传", "必填", "必须");

    public static void main(String[] args) throws Exception {

        String jsonStr = "```\n" +
                "{\n" +
                "    \"code\": 200,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"val\": [\n" +
                "        {\n" +
                "            \"objectId\": \"b23ce8b9-558d-4f06-93c2-7a6bfda230bc\",\n" +
                "            \"categoryName\": \"电\",\n" +
                "            \"categoryCode\": \"E1000\",\n" +
                "            \"parentId\": \"\",\n" +
                "            \"systemCategoryId\": \"1f7e41ec-fd90-488d-bb2e-89cc4318ed13\",\n" +
                "            \"categoryLevel\": 0,\n" +
                "            \"meterType\": 1,\n" +
                "            \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "            \"selectStatus\": null,\n" +
                "            \"mpcs\": [\n" +
                "                {\n" +
                "                    \"objectId\": \"61434626-2505-4d5c-93a2-d84d15b1fed4\",\n" +
                "                    \"categoryName\": \"公区用电\",\n" +
                "                    \"categoryCode\": \"E1100\",\n" +
                "                    \"parentId\": \"1f7e41ec-fd90-488d-bb2e-89cc4318ed13\",\n" +
                "                    \"systemCategoryId\": \"ce8c0ef3-f95d-4202-bfc3-e00c303bfdb9\",\n" +
                "                    \"categoryLevel\": 1,\n" +
                "                    \"meterType\": 1,\n" +
                "                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                    \"selectStatus\": null,\n" +
                "                    \"mpcs\": [\n" +
                "                        {\n" +
                "                            \"objectId\": \"1241308a-62d9-49a6-a115-1d7edc4ea7cf\",\n" +
                "                            \"categoryName\": \"其他用电\",\n" +
                "                            \"categoryCode\": \"E1160\",\n" +
                "                            \"parentId\": \"ce8c0ef3-f95d-4202-bfc3-e00c303bfdb9\",\n" +
                "                            \"systemCategoryId\": \"1dede08d-822f-4094-aebe-21e56498444e\",\n" +
                "                            \"categoryLevel\": 2,\n" +
                "                            \"meterType\": 1,\n" +
                "                            \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                            \"selectStatus\": null,\n" +
                "                            \"mpcs\": [\n" +
                "                                {\n" +
                "                                    \"objectId\": \"1259a95c-5852-4d48-8e9b-420d28c87264\",\n" +
                "                                    \"categoryName\": \"消防控制室\",\n" +
                "                                    \"categoryCode\": \"E1122\",\n" +
                "                                    \"parentId\": \"1dede08d-822f-4094-aebe-21e56498444e\",\n" +
                "                                    \"systemCategoryId\": \"9d4cb2bc-5f10-43a9-a71d-bea084e8edf0\",\n" +
                "                                    \"categoryLevel\": 3,\n" +
                "                                    \"meterType\": 1,\n" +
                "                                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                                    \"selectStatus\": null,\n" +
                "                                    \"mpcs\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"objectId\": \"2a66dab0-b7f1-4693-aa20-4b29bbd6fe7e\",\n" +
                "                                    \"categoryName\": \"洗衣房\",\n" +
                "                                    \"categoryCode\": \"E1126\",\n" +
                "                                    \"parentId\": \"1dede08d-822f-4094-aebe-21e56498444e\",\n" +
                "                                    \"systemCategoryId\": \"177898b7-9cb2-4cd2-b246-87aba8a8251c\",\n" +
                "                                    \"categoryLevel\": 3,\n" +
                "                                    \"meterType\": 1,\n" +
                "                                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                                    \"selectStatus\": null,\n" +
                "                                    \"mpcs\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"objectId\": \"6126c546-7c4d-4ef8-b94e-3b2a98e2f3ff\",\n" +
                "                                    \"categoryName\": \"厨房餐厅\",\n" +
                "                                    \"categoryCode\": \"E1124\",\n" +
                "                                    \"parentId\": \"1dede08d-822f-4094-aebe-21e56498444e\",\n" +
                "                                    \"systemCategoryId\": \"dbc86d70-2096-412f-8fd0-4bb942493bb6\",\n" +
                "                                    \"categoryLevel\": 3,\n" +
                "                                    \"meterType\": 1,\n" +
                "                                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                                    \"selectStatus\": null,\n" +
                "                                    \"mpcs\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"objectId\": \"a3f7641b-7672-4c48-867f-6ab7aa1b1f34\",\n" +
                "                                    \"categoryName\": \"健身房\",\n" +
                "                                    \"categoryCode\": \"E1127\",\n" +
                "                                    \"parentId\": \"1dede08d-822f-4094-aebe-21e56498444e\",\n" +
                "                                    \"systemCategoryId\": \"b14a9c8f-f424-44cd-b780-a699324a7e85\",\n" +
                "                                    \"categoryLevel\": 3,\n" +
                "                                    \"meterType\": 1,\n" +
                "                                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                                    \"selectStatus\": null,\n" +
                "                                    \"mpcs\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"objectId\": \"a4e5c208-6ab4-4e56-9776-8c09c5b5b0a6\",\n" +
                "                                    \"categoryName\": \"变电所\",\n" +
                "                                    \"categoryCode\": \"E1123\",\n" +
                "                                    \"parentId\": \"1dede08d-822f-4094-aebe-21e56498444e\",\n" +
                "                                    \"systemCategoryId\": \"2d28121f-cd65-4c7e-9ad2-9f971d9dd727\",\n" +
                "                                    \"categoryLevel\": 3,\n" +
                "                                    \"meterType\": 1,\n" +
                "                                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                                    \"selectStatus\": null,\n" +
                "                                    \"mpcs\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"objectId\": \"ae5fb4f4-0b44-46e4-b52b-8b45f7933573\",\n" +
                "                                    \"categoryName\": \"充电桩\",\n" +
                "                                    \"categoryCode\": \"E1121\",\n" +
                "                                    \"parentId\": \"1dede08d-822f-4094-aebe-21e56498444e\",\n" +
                "                                    \"systemCategoryId\": \"ff4e46ae-d13f-469e-9479-917c0a2fcdd2\",\n" +
                "                                    \"categoryLevel\": 3,\n" +
                "                                    \"meterType\": 1,\n" +
                "                                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                                    \"selectStatus\": null,\n" +
                "                                    \"mpcs\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"objectId\": \"d0a5b969-033e-46be-92ef-c21ea234a509\",\n" +
                "                                    \"categoryName\": \"其他\",\n" +
                "                                    \"categoryCode\": \"E1128\",\n" +
                "                                    \"parentId\": \"1dede08d-822f-4094-aebe-21e56498444e\",\n" +
                "                                    \"systemCategoryId\": \"a378457b-015b-4536-a6f1-9f7ed78c6ce3\",\n" +
                "                                    \"categoryLevel\": 3,\n" +
                "                                    \"meterType\": 1,\n" +
                "                                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                                    \"selectStatus\": null,\n" +
                "                                    \"mpcs\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"objectId\": \"df2ec81a-001c-4631-bd47-fb22507d87b0\",\n" +
                "                                    \"categoryName\": \"数据中心\",\n" +
                "                                    \"categoryCode\": \"E1125\",\n" +
                "                                    \"parentId\": \"1dede08d-822f-4094-aebe-21e56498444e\",\n" +
                "                                    \"systemCategoryId\": \"8b3aaf72-e290-4669-a287-46c1e45d1817\",\n" +
                "                                    \"categoryLevel\": 3,\n" +
                "                                    \"meterType\": 1,\n" +
                "                                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                                    \"selectStatus\": null,\n" +
                "                                    \"mpcs\": []\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"objectId\": \"651dc092-b901-4366-bc9a-93310283946c\",\n" +
                "                    \"categoryName\": \"分摊用电\",\n" +
                "                    \"categoryCode\": \"E1300\",\n" +
                "                    \"parentId\": \"1f7e41ec-fd90-488d-bb2e-89cc4318ed13\",\n" +
                "                    \"systemCategoryId\": \"f78052ca-0c38-457a-b67b-4dc3ba08ec02\",\n" +
                "                    \"categoryLevel\": 1,\n" +
                "                    \"meterType\": 1,\n" +
                "                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                    \"selectStatus\": null,\n" +
                "                    \"mpcs\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"objectId\": \"e1b8d28c-3098-49c9-8b1c-2717c83718ae\",\n" +
                "                    \"categoryName\": \"租区用电\",\n" +
                "                    \"categoryCode\": \"E1200\",\n" +
                "                    \"parentId\": \"1f7e41ec-fd90-488d-bb2e-89cc4318ed13\",\n" +
                "                    \"systemCategoryId\": \"ce8290ea-a6dd-4612-b3f4-3a978bcac4e0\",\n" +
                "                    \"categoryLevel\": 1,\n" +
                "                    \"meterType\": 1,\n" +
                "                    \"projectId\": \"b9de0652-7e15-4178-bdb2-c9900b51496e\",\n" +
                "                    \"selectStatus\": null,\n" +
                "                    \"mpcs\": []\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"success\": 1\n" +
                "}\n" +
                "```";
        System.out.println("=====================================返参");
        System.out.println( toShowDocMultiLevel(jsonStr,0,true));
        System.out.println("==================================传参");
        System.out.println(toShowDocMultiLevel(jsonStr,0,false));

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
        String param1 = part01.substring(part01.indexOf("```json"), part01.lastIndexOf("```") + 3);
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
        // 请求url的正则 #*\s*(url|URL|Url)(\s*:\s*)(.*)(\n)  (\s*-\s*请求地址\s*:\s*)(\n*)(```.*\n)(.*\n)(```\s*?\n)
        String urlExp = "(\\s*-\\s*请求地址\\s*:\\s*)(\\n*)(```.*\\n)(.*\\n)(```\\s*?\\n)";
        // url 备用正则 (\s*-\s*请求地址\s*:\s*)(\n*)(.*\n)
        String urlExp2 = "(\\s*-\\s*请求地址\\s*:\\s*)(\\n*)(.*\\n)";
        // 请求方式的正则
        String postExp = "(\\x20*-\\s*请求方式\\s*:\\s*)(\\n*)(```.*\\n)(.*\\n)(```\\x20*?)";
        // 请求方式的备用正则 (\x20*-\s*请求方式\s*:\s*)(\n*)(.*\n)
        String postExp2 = "(\\x20*-\\s*请求方式\\s*:\\s*)(\\n*)(.*\\n)";
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
        }else{
            pattern = Pattern.compile(urlExp2);
            matcher = pattern.matcher(origin);
            if (matcher.find()){
                res.append(matcher.group(3) + "    `\n");
            }
        }
        res.append("\n" +
                "##### 请求方式\n" +
                "\n" +
                "-   ");
        pattern = Pattern.compile(postExp);
        matcher = pattern.matcher(origin);
        if (matcher.find()) {
            res.append(matcher.group(4) + "  \n");
        }else{
            pattern = Pattern.compile(postExp2);
            matcher = pattern.matcher(origin);
            if (matcher.find()){
                res.append(matcher.group(3) + "  \n");
            }
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
        StringBuilder wholeLine = new StringBuilder();
        List<Integer> enterNum = new ArrayList<>();
        // 前面的空格到参数名位置,有个地方要用到.
        StringBuilder sb3 = new StringBuilder();

//        (\s*)(".*?)([0-9a-zA-Z]*)(".*?:.*?\s*?)(.*)(\s*?.*?)
//        (\s*)(".*?)([0-9a-zA-Z]*)(".*?:.*?\s*?)(.*)(\s*?.*?)
//        (^\x20\s*)(".*?)([0-9a-zA-Z]*)(".*?:.*?\s*?)(.*)(\s*?.*?)
        // 更改,必须以空格开头,否则会匹配到换行符   (还是用原来的,但是上下相隔4个空字符才算孩子),其实两个就够了,因为一般也就多出一个\n,或者可以折中改为3个
//     (\s*)(".*?)([0-9a-zA-Z]*)(".*?:.*?\s*?)(.*)(\s*?.*?)
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
            wholeLine.append(matcher.group(0)).append("SplitTagYYP001");
            // 匹配到的这串文本在原文本中所处的下标
            enterNum.add(matcher.end()-2);
//            System.out.println("  +matcher.end() "+matcher.end());
        }
        String[] sHeadArr = sb3.toString().split(",");
        String[] sArr = changeUnderToUpperLetter(sb.toString()).split(",");
        String[] spaceArr = sb2.toString().split(",");
        String[] explainArr = explain.toString().split("&");
        String[] wholeLineArr = wholeLine.toString().split("SplitTagYYP001");
//        boolean isChild = false;
        // 标记上次的缩进个数  刚开始初始化应该用
        int spaceNum = 0;
        if (sArr.length > 0) {
            spaceNum = spaceArr[0].replace("\t", "    ").length();
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
            String nowSpaceLen = spaceArr[i].replace("\t", "    ");
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
            int spaceLen = nowSpaceLen.length();
//            if (spaceArr[i].length() >= 8 && ("{".equals(explainArr[i - 1].replace(" ", "")) || "[".equals(explainArr[i - 1].replace(" ", "")))) {
            if (nowSpaceLen.length() >= 0) {
                // 对象的孩子退四格
                // 对象数组的孩子退格   ,  因为\n的干扰,所以用当前的减去记录的值大于2(或者大于1就行,大于一可以避免有些退格符只有两个的情况
                // 但是鉴于一般都是缩进四个空格,而且万一啥时候多按了个空格也会出错,还是大于2好了,大于3也行,但是如果他少按个空格也会错,)
                // 总共可能的情况是 2,   3,4,5   而因为是否有\n的情况会向上浮动1个字符,   再加   3    4,5,6.  \n发生概率也高,因为只要不符合有参数名的,前面一行的\n会匹配到现在这行,所以最常见的情况是
//          2,4    然后加1就是    3,5      也就是 2,3,4,5最常见                  但是0和1绝对不算是缩进,     综上     spaceLen - spaceNum  > 1 来判断
                if (spaceLen - spaceNum > 1) {
                    // 这里说明进入了对象或者对象数组,4以上的是不会判断的,那个肯定是第一层级,然后这个tabSize初始化的时候也就是第一层级
                    isChild = true;
                    isChildStack.push(spaceLen);
                    spaceNum = spaceLen;
                } else if (spaceLen - spaceNum < -1) {
                    isChild = false;
                    // 有来有回,回来了说明已经又到同级了.  有时候回来是一次性回两层的,所以这里好像有些不对.
//                    回来的层数不一定能完全确定,所以有些不好判断()
//                    暂时有个方法,把这回的参数之间的字符串截取出来,看有几个 }   这个
                    // 如果两个对象内有两个相同参数,那么indexOf就会取到比较先的那个,这个好像不大准,取最后一个什么的也有隐患.
                    // 主要是相同变量名相同缩进的变量这个有些多
//                    int nowIndex =
                    int substrEnd = origin.indexOf(sHeadArr[i],enterNum.get(i)-wholeLineArr[i].length());
                    int substrStart = origin.indexOf(sHeadArr[i - 1],enterNum.get(i-1)-wholeLineArr[i-1].length());
                    while (substrStart < substrEnd) {
                        int tmp = origin.indexOf(sHeadArr[i - 1], substrStart + 1);
                        if (tmp != -1 && tmp < substrEnd) {
                            substrStart = tmp;
                        } else {
                            break;
                        }

                    }

//                    int substrEnd2 = origin.indexOf(wholeLineArr[i] + wholeLineArr[i + 1]);
//                    int substrStart2 = origin.indexOf(wholeLineArr[i - 2] + wholeLineArr[i - 1]) + (wholeLineArr[i - 2] + wholeLineArr[i - 1]).length() - 1;
                    String bt = " 为了避免符号前后没东西  " + origin.substring(substrStart, substrEnd) + " 为了避免符号前后没东西  ";
//                    \}\s*,\s*\n\s*\{
//                    两个同级对象上面一个对象有个子对象,然后会出现这种情况
//                    "required": "1",
//                            "sort": 3
//                }
//                ]
//            },
//            {
//                "type": 2,
//                    "sort": 4,
                    // 偶尔层级会很多
                    if (bt.split("\n").length > 8) {
//                        throw new Exception("是否有对象数组且里面的对象都字段相同的例子?麻烦把对象数组里面的对象只留下一个");
                        System.out.println("##################是否有对象数组且里面的对象都字段相同的例子?麻烦把对象数组里面的对象只留下一个#########################");
                    }
                    String[] btArr = bt.split("}");

                    String pRightCommaLEftRegExp = "\\}\\s*,\\s*\\n\\s*\\{";
                    Pattern pRightCommaLEft = Pattern.compile(pRightCommaLEftRegExp);
                    Matcher mRightCommaLEft = pRightCommaLEft.matcher(bt);


//                    if (bt)
                    int backspaceNum = btArr.length - 1;
                    if (mRightCommaLEft.find()) {
                        backspaceNum = backspaceNum - 1;
                    }
                    for (int l = 0; l < backspaceNum; l++) {
                        isChildStack.pop();
                    }

                    spaceNum = spaceLen;
                }
                if (isChildStack.size() > 0 && i > 0) {
                    flagStr = sArr[i - 1];
                    String repeatingKey = flagStr + key;
                    repeatingObject.add(repeatingKey);
                }
            }
            plusNum = isChildStack.size();
            if (variableName.contains(key)) {
                // 重复的虽然不会进入表格,但是这里之前的上面的判断是否升级降级还是会走的
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
            int spaceCount = (nowSpaceLen.length()) / 8;

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
            String fieldTypePure = fieldType.replace(" ", "").replace("|", "");
            String fieldTypePureUpper = fieldTypePure.substring(0,1).toUpperCase() + fieldTypePure.substring(1);
            // 字段类型默认就是小写吧,一般情况下也都是小写,除非当时写接口的人切换到英文输入法再切大写弄成首字母大写,一般都是小写的.不去做其他判断了
            explainStr = explainStr.replace("[" + fieldTypePure + "]", "").replace("(" + fieldTypePure + ")", "")
                    .replace("（" + fieldTypePure + "）", "").replace(fieldTypePure, "")
                    .replace("[" + fieldTypePureUpper + "]", "").replace("(" + fieldTypePureUpper + ")", "")
                    .replace("（" + fieldTypePureUpper + "）", "").replace(fieldTypePureUpper, "")
//                    就BigDecimal是有俩驼峰的
                    .replace("BigDecimal","");
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
            // 简单判断一下,童喜玲写注释的方式是很多----------------这里是注释
            if (explainStr.contains("---")) {
                explainStr = explainStr.substring(explainStr.lastIndexOf("---") + 3);
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
        if (resStr.contains("+success")) {
            // 一般都是返回高级的缩进出问题,相同层级同名字段过多会出现这个问题,但是概率小,如果出现就抛异常
            System.out.println("####################### 缩进可能出问题了,检查一下层级(也就是字段名前面的+号个数是否正确),应该就几个,可以手动更改一下." +
                    "或者把json放进vscode格式化一下再放回去执行一下这个工具,或者检查一下是否有花括号漏写了");
//            throw new Exception("缩进可能出问题了,检查一下层级(也就是字段名前面的+号个数是否正确),应该就几个,可以手动更改一下.");
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
        }else if (explainStr.contains("bigdecimal") || explainStr.contains("BigDecimal")) {
            res = " | bigdecimal ";
        }
        else if (explainStr.contains("Double") || explainStr.contains("Double")) {
            res = " | double ";
        }
        else if (explainStr.contains("decimal") || explainStr.contains("Decimal")) {
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

//    public static String formatJson(String json) {
//        StringBuffer result = new StringBuffer();
//        int length = json.length();
//        int number = 0;
//        char key = 0;
////遍历输入字符串
//        for (int i = 0; i < length; i++) {
////获取当前字符
//            key = json.charAt(i);
////如果当前字符是前方括号、前花括号做如下处理
//            if ((key == '[') || (key == '{')) {
//                if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
//                    result.append('\n');
//                    result.append(indent(number));
//                }
////打印当前的字符
//                result.append(key);
//                result.append('\n');
//                number++;
//                result.append(indent(number));
//                continue;
//            }
//            if ((key == ']' || (key == '}'))) {
//                result.append('\n');
//                number--;
//                result.append(indent(number));
//                result.append(key);
//                if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
//                    result.append('\n');
//                }
//                continue;
//            }
//            if ((key == ',')) {
//                result.append(key);
//                result.append('\n');
//                result.append(indent(number));
//                continue;
//            }
//            result.append(key);
//        }
//        return result.toString();
//    }

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
