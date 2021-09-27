package java读文件;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @description
 * @author: yyp
 * @create: 2021-09-24
 **/
public class java读文件 {

    public static void main(String[] args) {

//        D:\jdk8\bin\java.exe "-javaagent:D:\IntelliJ IDEA 2021.1.1\lib\idea_rt.jar=57186:D:\IntelliJ IDEA 2021.1.1\bin" -Dfile.encoding=UTF-8 -classpath D:\jdk8\jre\lib\charsets.jar;D:\jdk8\jre\lib\deploy.jar;D:\jdk8\jre\lib\ext\access-bridge-64.jar;D:\jdk8\jre\lib\ext\cldrdata.jar;D:\jdk8\jre\lib\ext\dnsns.jar;D:\jdk8\jre\lib\ext\jaccess.jar;D:\jdk8\jre\lib\ext\jfxrt.jar;D:\jdk8\jre\lib\ext\localedata.jar;D:\jdk8\jre\lib\ext\nashorn.jar;D:\jdk8\jre\lib\ext\sunec.jar;D:\jdk8\jre\lib\ext\sunjce_provider.jar;D:\jdk8\jre\lib\ext\sunmscapi.jar;D:\jdk8\jre\lib\ext\sunpkcs11.jar;D:\jdk8\jre\lib\ext\zipfs.jar;D:\jdk8\jre\lib\javaws.jar;D:\jdk8\jre\lib\jce.jar;D:\jdk8\jre\lib\jfr.jar;D:\jdk8\jre\lib\jfxswt.jar;D:\jdk8\jre\lib\jsse.jar;D:\jdk8\jre\lib\management-agent.jar;D:\jdk8\jre\lib\plugin.jar;D:\jdk8\jre\lib\resources.jar;D:\jdk8\jre\lib\rt.jar;E:\workspaceYP\testBw_yyl_yyp\out\production\testBw_yyl_yyp json转markdown表格.转整个gitee的md文件到showdoc
//                +matcher.end() 182
//                +matcher.end() 281
//                +matcher.end() 299
//                +matcher.end() 318
//                +matcher.end() 398
//                +matcher.end() 461
//                +matcher.end() 492
//                +matcher.end() 572
//                +matcher.end() 635
//                +matcher.end() 666
//                +matcher.end() 748
//                +matcher.end() 811
//######################有多个对象,请手动再继续处理对象里面的内容######################
//        +matcher.end() 25
//                +matcher.end() 47
//                +matcher.end() 98
//                +matcher.end() 115

//        String a = "#### URL:lyws-inventory/storeroom/add1\n" +
//                "#### 请求方式: POST\n" +
//                "#### 负责人：童喜玲\n" +
//                "#### 参数: \n" +
//                "```\n" +
//                "{\n" +
//                "    //-----在原参数基础上新增采购所需信息\n" +
//                "    \"purchaseApprove\": 0: 无须审批 1. 一级审批； 2： 二级审批-- -- -- -- -- - 数字类型,\n" +
//                "    \"purchaseSign\": 采购签字： 0: 无需签字 1. 只需一级审批需要签字； 2： 只需二级审批需要签字； 3： 一级、 二级审批都需签字-- -- -- -- - 数字类型,\n" +
//                "    \"purchase\": {";
//        System.out.println(a.length());
//
////        String a = "fefwefwefwe(必须)rgreger";
////        String b = "必须";
////        if (a.contains(b)){
////            a = a.replace("("+b+")","").replace("["+b+"]","");
////        }
////        System.out.println(a);

        String json = "#### URL:/lyws-inventory/takeStock/saveTakeStock\n" +
                "#### 请求方式: POST\n" +
                "#### 负责人：童喜玲\n" +
                "#### 参数: \n" +
                "```\n" +
                "{\n" +
                "    \"takeStockPlanId\": \"盘点计划ID\",\n" +
                "    \"materialBOList\":[\n" +
                "    \t{\n" +
                "    \t\t\"takeStockMaterialId\":\"盘点计划与物料关联关系ID\",\n" +
                "    \t\t\"materialId\":\"物料ID\",\n" +
                "    \t\t\"originalAmount\"原始库存数量,\n" +
                "    \t\t\"originalMoney\":\"原始库存金额\",\n" +
                "    \t\t\"actualAmount\":实际库存数量,\n" +
                "    \t\t\"remark\":\"备注\",\n" +
                "                \"status\":\"是否已经盘点，true：已经盘点；false：未盘点\",\n" +
                "                \"modified\":\"是否编辑过，true：编辑过；false：未编辑过-----------之前保存过的数据和新盘点的数据都为true\",\n" +
                "    \t\t\"adjustBatchList\":[\n" +
                "    \t\t\t{\n" +
                "                            \"originalAmount\": 原始库存数量,\n" +
                "                            \"actualAmount\":实际库存数量,\n" +
                "                            \"storeroomMaterialAmountId\": \"edd64464-6514-4714-80c4-48346ecb8ab1\"\n" +
                "    \t\t\t}\n" +
                "    \t\t]\n" +
                "\t}\n" +
                "    ]\n" +
                "}\n" +
                "```\n" +
                "#### 返回值：\n" +
                "```\n" +
                "{\n" +
                "    \"code\": 200,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"val\": \n" +
                "}\n" +
                "```\n";
        System.out.println(format(json));

    }

    /**
     * 这个严格意义不是格式化,就是帮你输出,缩进也没有变
     */
    public static String format(String json) {
        //缩进
        StringBuilder indent = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        for (char c : json.toCharArray()) {
            switch (c) {
                case '{':
                    indent.append(" ");
                    sb.append("{\n").append(indent);
                    break;
                case '}':
                    indent.deleteCharAt(indent.length() - 1);
                    sb.append("\n").append(indent).append("}");
                    break;
                case '[':
                    indent.append(" ");
                    sb.append("[\n").append(indent);
                    break;
                case ']':
                    indent.deleteCharAt(indent.length() - 1);
                    sb.append("\n").append(indent).append("]");
                    break;
                case ',':
                    sb.append(",\n").append(indent);
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

}
