package sb.rest.springcloud;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class StringTest {

    @Test
    public void test1() {
        String str1 = "123";
        String str2 = new String("123");
        char[] value = {'a', 'b', 'c', 'd'};
        String str3 = new String(value);
        String str4 = new String(value, 1, 2);
        byte[] strb = new byte[]{65, 66};
        String str5 = new String(strb);
        System.out.println(str1 + " " + str2 + " " + str3 + " " + str4 + " " + str5);
    }

    @Test
    public void test2() {
        String str = "asdfzxc";
        int strlength = str.length();
        System.out.println(strlength);
    }

    // 求字符串某一位置字符
    @Test
    public void test3() {
        String str = "asdfzxc";
        char ch = str.charAt(4);
        System.out.println(ch);
    }


    //提取子串
    @Test
    public void test4() {
        String str = "asdfzxc";
        String str2 = str.substring(2);
        String str3 = str.substring(2, 5);
    }

    //字符串比较
    @Test
    public void test5() {
        String str1 = "abc";
        String str2 = "ABC";
        int a = str1.compareTo(str2);           //a>0
        System.out.println(a);
        int b = str1.compareToIgnoreCase(str2); //b=0
        System.out.println(b);
        boolean c = str1.equals(str2);//c=false
        System.out.println(c);
        boolean d = str1.equalsIgnoreCase(str2);//d=true
        System.out.println(d);
    }

    //字符串连接，用StringBuilder
    @Test
    public void test6() {
        String str = "aa".concat("bb").concat("cc");
        str = "aa" + "bb" + "cc";
        System.out.println(str);
    }

    //字符串中单个字符查找
    @Test
    public void test7() {
        String str = "I am a good student";
        int a = str.indexOf('a');//a = 2
        System.out.println(a);
        int b = str.indexOf("good");//b = 7
        System.out.println(b);
        int c = str.indexOf("w", 2);//c = -1
        System.out.println(c);
        int d = str.lastIndexOf("a");//d = 5
        System.out.println(d);
        int e = str.lastIndexOf("a", 3);//e = 2
        System.out.println(e);
    }

    //字符的大小写转换
    @Test
    public void test8() {

        String str = new String("asDF");
        String str1 = str.toLowerCase();//str1 = "asdf"
        String str2 = str.toUpperCase();//str2 = "ASDF"
        System.out.println(str1);
        System.out.println(str2);

    }

    // 字符的替换
    @Test
    public void test9() {
        String str = "asdzxcasd";
        String str1 = str.replace('a', 'g');//str1 = "gsdzxcgsd"
        String str2 = str.replace("asd", "fgh");//str2 = "fghzxcfgh"
        String str3 = str.replaceFirst("asd", "fgh");//str3 = "fghzxcasd"
        String str4 = str.replaceAll("asd", "fgh");//str4 = "fghzxcfgh"
        System.out.println(str1);
        System.out.println(str2);
        System.out.println(str3);
        System.out.println(str4);
    }

    //trim
    @Test
    public void test10() {
        String str = " a sd ";
        String str1 = str.trim();
        int a = str.length();//a = 6
        int b = str1.length();//b = 4
        System.out.println(a);
        System.out.println(b);

    }

    //regionMatches
    @Test
    public void test11() {
        String Str1 = new String("www.runoob.com");
        String Str2 = new String("runoob");
        String Str3 = new String("RUNOOB");

        System.out.print("返回值 :");
        System.out.println(Str1.regionMatches(4, Str2, 0, 5));

        System.out.print("返回值 :");
        System.out.println(Str1.regionMatches(4, Str3, 0, 5));

        System.out.print("返回值 :");
        System.out.println(Str1.regionMatches(true, 4, Str3, 0, 5));
    }

    //
    @Test
    public void test12() {
        String str = "asd!qwe|zxc#";
        String[] str1 = str.split("!|#");//str1[0] = "asd";str1[1] = "qwe";str1[2] = "zxc";
        System.out.println(str1);
    }

    // 编码转换
    @Test
    public void test13() throws UnsupportedEncodingException {
        // 假设接收到的数据时GBK编码的
        byte[] gbks = "abcdefg".getBytes("GBK");

        String gbk = new String(gbks, "GBK");
        System.out.println(gbk);

    }
}
