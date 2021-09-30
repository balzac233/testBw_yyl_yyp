package json转markdown表格;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description
 * @author: yyp
 * @create: 2021-09-28
 **/
public class underLineToCamel {

    public static void main(String[] args) {

    }

    /**
     * @Description 下划线转驼峰式
     */
    static String changeUnderToUpperLetter(String str) {
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

}
