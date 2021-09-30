package json转markdown表格;

/**
 * @description
 * @author: yyp
 * @create: 2021-09-26
 **/
public class FormatJson {
    public static void main(String[] args) {

        String a = "    \n";//
        String b = a.substring(a.indexOf("\n")+1);
        System.out.println("============================================");

    }

//        public static String formatJson(String json) {
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
}
