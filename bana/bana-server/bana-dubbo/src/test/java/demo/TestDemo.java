package demo;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.junit.Test;

public class TestDemo{
    @Test
    public void testPinyin() throws Exception{
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 没有音调数字
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);// u显示

        String resultPinyin = "";

        String hanzi = "你好，中国123123cfff";
        char[] chars = hanzi.toCharArray();
        for (char aChar : chars) {
            if(aChar > 128){
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(aChar, format);
                if(pinyin != null){
                    resultPinyin += pinyin[0].charAt(0);
                }
            }else{
                resultPinyin += aChar;
            }
        }
        System.out.println(resultPinyin);
    }
}
