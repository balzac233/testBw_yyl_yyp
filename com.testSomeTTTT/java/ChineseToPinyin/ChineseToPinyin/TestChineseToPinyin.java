//package ChineseToPinyin.ChineseToPinyin;
//
//import cn.hutool.extra.pinyin.PinyinUtil;
//
//import java.util.*;
//
///**
// * @description
// * @author: yyp
// * @create: 2021-09-28
// **/
//public class TestChineseToPinyin {
//
//    static List<String> dirtyWord = Arrays.asList("傻逼", "脑残", "狗蛋");
//
//    public static void main(String[] args) {
//
////        String imy = PinyinUtil.getPinyin("我是源xian生");
////        System.out.println(imy);
//
//        System.out.println("脏字列表 : ");
//        dirtyWord.stream().forEach(a -> System.out.print(a + " , "));
//        System.out.println("");
//        String origin = " 你是煞笔 ";
//        String origin2 = " 你是撒币 ";
//        String origin3 = " 你是shabi ";
//        String origin4 = " 你是sabi ";
//        String origin5 = " 你是sha逼 ";
//
//        printWhetherDirty(origin);
//        printWhetherDirty(origin2);
//        printWhetherDirty(origin3);
//        printWhetherDirty(origin4);
//        printWhetherDirty(origin5);
//
//    }
//
//    static String printWhetherDirty(String origin) {
//        String isDirty = dirtyWordDetection(origin, dirtyWord) == true ? "是" : "否";
//        String outStr = " [ " + origin + " ]这串字符串是否含有脏字列表里面的脏字(根据拼音) : " + isDirty;
//        System.out.println(outStr);
//        return outStr;
//    }
//
//    public static boolean dirtyWordDetection(String origin, List<String> dirtyWord) {
//        Set<String> dirtyWordPinyin = new HashSet<>();
//        for (String dw : dirtyWord) {
//            dirtyWordPinyin.add(PinyinUtil.getPinyin(dw).replace(" ", ""));
//        }
//        String originPinyin = PinyinUtil.getPinyin(origin).replace(" ", "");
//        for (String dwp : dirtyWordPinyin) {
//            if (originPinyin.contains(dwp)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
