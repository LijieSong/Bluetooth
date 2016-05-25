package com.example.asus.bluetooth;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

public class FormatTransfer{
  
      
      
    public static long byteArray2Long(byte[] a) {  
        long res = 0L;  
        int[] t = new int[8];  
        for (int i = 0; i < 8; i++) {  
            t[i] = a[7 - i];  
        }  
        res = t[0] & 0x0ff;  
        for (int i = 1; i < 8; i++) {  
            res <<= 8;  
            res += (t[i] & 0x0ff);  
        }  
        return res;  
    }  
      
    public static long intArray2Long(int[] a) {  
        long res = 0L;  
        int[] t = new int[8];  
        for (int i = 0; i < 8; i++) {  
            t[i] = a[7 - i];  
        }  
        res = t[0] & 0x0ff;  
        for (int i = 1; i < 8; i++) {  
            res <<= 8;  
            res += (t[i] & 0x0ff);  
        }  
        return res;  
    }  
  
    public static String getLocalhostip() {  
        try {  
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostAddress();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "";  
    }  
  
    public static String longtoip(long longip) {  
        StringBuffer sb = new StringBuffer("");  
        sb.append(String.valueOf(longip >>> 24));// 直接右移24位  
        sb.append(".");  
        sb.append(String.valueOf((longip & 0x00ffffff) >>> 16)); // 将高8位置0，然后右移16位  
        sb.append(".");  
        sb.append(String.valueOf((longip & 0x0000ffff) >>> 8));  
        sb.append(".");  
        sb.append(String.valueOf(longip & 0x000000ff));  
        return sb.toString();  
    }  
  
    public static long iptolong(String strip) {  
        // int j = 0;  
        // int i = 0;  
        long[] ip = new long[4];  
        int position1 = strip.indexOf(".");  
        int position2 = strip.indexOf(".", position1 + 1);  
        int position3 = strip.indexOf(".", position2 + 1);  
        ip[0] = Long.parseLong(strip.substring(0, position1));  
        ip[1] = Long.parseLong(strip.substring(position1 + 1, position2));  
        ip[2] = Long.parseLong(strip.substring(position2 + 1, position3));  
        ip[3] = Long.parseLong(strip.substring(position3 + 1));  
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3]; // ip1*256*256*256+ip2*256*256+ip3*256+ip4  
    }  
  
    /** 
     * 将int转为低字节在前，高字节在后的byte数组 
     *  
     * @param n 
     *            int 
     * @return byte[] 
     */  
    public static byte[] toLH(int n) {  
        byte[] b = new byte[4];  
        b[0] = (byte) (n & 0xff);  
        b[1] = (byte) (n >> 8 & 0xff);  
        b[2] = (byte) (n >> 16 & 0xff);  
        b[3] = (byte) (n >> 24 & 0xff);  
        return b;  
    }  
  
    /** 
     * 将int转为高字节在前，低字节在后的byte数组 
     *  
     * @param n 
     *            int 
     * @return byte[] 
     */  
    public static byte[] toHH(int n) {  
        byte[] b = new byte[4];  
        b[3] = (byte) (n & 0xff);  
        b[2] = (byte) (n >> 8 & 0xff);  
        b[1] = (byte) (n >> 16 & 0xff);  
        b[0] = (byte) (n >> 24 & 0xff);  
        return b;  
    }  
  
    /** 
     * 将short转为低字节在前，高字节在后的byte数组 
     *  
     * @param n 
     *            short 
     * @return byte[] 
     */  
    public static byte[] toLH(short n) {  
        byte[] b = new byte[2];  
        b[0] = (byte) (n & 0xff);  
        b[1] = (byte) (n >> 8 & 0xff);  
        return b;  
    }  
  
    /** 
     * 将short转为高字节在前，低字节在后的byte数组 
     *  
     * @param n 
     *            short 
     * @return byte[] 
     */  
    public static byte[] toHH(short n) {  
        byte[] b = new byte[2];  
        b[1] = (byte) (n & 0xff);  
        b[0] = (byte) (n >> 8 & 0xff);  
        return b;  
    }  
  
    /** 
     * 将float转为低字节在前，高字节在后的byte数组 
     */  
    public static byte[] toLH(float f) {  
        return toLH(Float.floatToRawIntBits(f));  
    }  
  
    /** 
     * 将float转为高字节在前，低字节在后的byte数组 
     */  
    public static byte[] toHH(float f) {  
        return toHH(Float.floatToRawIntBits(f));  
    }  
  
    /** 
     * 将String转为byte数组 
     */  
    public static byte[] stringToBytes(String s, int length) {  
        while (s.getBytes().length < length) {  
            s += " ";  
        }  
        return s.getBytes();  
    }  
  
    /** 
     * 将字节数组转换为String 
     *  
     * @param b 
     *            byte[] 
     * @return String 
     */  
    public static String bytesToString(byte[] b) {  
        StringBuffer result = new StringBuffer("");
//        StringBuffer result = new StringBuffer();
        int length = b.length;  
        for (int i = 0; i < length; i++) {  
            result.append((char) (b[i] & 0xff));  
        }  
        return result.toString();  
    }  
  
    /** 
     * 将字符串转换为byte数组 
     *  
     * @param s 
     *            String 
     * @return byte[] 
     */  
    public static byte[] stringToBytes(String s)  
            throws UnsupportedEncodingException {
        return s.getBytes("UTF-8");
    }  
  
    /** 
     * 将高字节数组转换为int 
     *  
     * @param b 
     *            byte[] 
     * @return int 
     */  
    public static int hBytesToInt(byte[] b) {  
        int s = 0;  
        for (int i = 0; i < 3; i++) {  
            if (b[i] >= 0) {  
                s = s + b[i];  
            } else {  
                s = s + 256 + b[i];  
            }  
            s = s * 256;  
        }  
        if (b[3] >= 0) {  
            s = s + b[3];  
        } else {  
            s = s + 256 + b[3];  
        }  
        return s;  
    }  
  
    /** 
     * 将低字节数组转换为int 
     *  
     * @param b 
     *            byte[] 
     * @return int 
     */  
    public static int lBytesToInt(byte[] b) {  
        int s = 0;  
        for (int i = 0; i < 3; i++) {  
            if (b[3 - i] >= 0) {  
                s = s + b[3 - i];  
            } else {  
                s = s + 256 + b[3 - i];  
            }  
            s = s * 256;  
        }  
        if (b[0] >= 0) {  
            s = s + b[0];  
        } else {  
            s = s + 256 + b[0];  
        }  
        return s;  
    }  
  
    /** 
     * 将低字节数组转换为int 
     *  
     * @param b 
     *            byte[] 
     * @return int 
     */  
    public static int lBytesToInt(int[] b) {  
        int s = 0;  
        for (int i = 0; i < 3; i++) {  
            if (b[3 - i] >= 0) {  
                s = s + b[3 - i];  
            } else {  
                s = s + 256 + b[3 - i];  
            }  
            s = s * 256;  
        }  
        if (b[0] >= 0) {  
            s = s + b[0];  
        } else {  
            s = s + 256 + b[0];  
        }  
        return s;  
    }  
  
    /** 
     * 高字节数组到short的转换 
     *  
     * @param b 
     *            byte[] 
     * @return short 
     */  
    public static short hBytesToShort(byte[] b) {  
        int s = 0;  
        if (b[0] >= 0) {  
            s = s + b[0];  
        } else {  
            s = s + 256 + b[0];  
        }  
        s = s * 256;  
        if (b[1] >= 0) {  
            s = s + b[1];  
        } else {  
            s = s + 256 + b[1];  
        }  
        short result = (short) s;  
        return result;  
    }  
  
    /** 
     * 低字节数组到short的转换 
     *  
     * @param b 
     *            byte[] 
     * @return short 
     */  
    public static short lBytesToShort(byte[] b) {  
        int s = 0;  
        if (b[1] >= 0) {  
            s = s + b[1];  
        } else {  
            s = s + 256 + b[1];  
        }  
        s = s * 256;  
        if (b[0] >= 0) {  
            s = s + b[0];  
        } else {  
            s = s + 256 + b[0];  
        }  
        short result = (short) s;  
        return result;  
    }  
  
    /** 
     * 低字节数组到short的转换 
     *  
     * @param b 
     *            byte[] 
     * @return short 
     */  
    public static short lBytesToShort(int[] b) {  
        int s = 0;  
        if (b[1] >= 0) {  
            s = s + b[1];  
        } else {  
            s = s + 256 + b[1];  
        }  
        s = s * 256;  
        if (b[0] >= 0) {  
            s = s + b[0];  
        } else {  
            s = s + 256 + b[0];  
        }  
        short result = (short) s;  
        return result;  
    }  
  
    /** 
     * 高字节数组转换为float 
     *  
     * @param b 
     *            byte[] 
     * @return float 
     */  
    @SuppressWarnings("static-access")  
    public static float hBytesToFloat(byte[] b) {  
        int i = 0;  
        Float F = new Float(0.0);  
        i = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8  
                | (b[3] & 0xff);  
        return F.intBitsToFloat(i);  
    }  
  
    /** 
     * 低字节数组转换为float 
     *  
     * @param b 
     *            byte[] 
     * @return float 
     */  
    @SuppressWarnings("static-access")  
    public static float lBytesToFloat(byte[] b) {  
        int i = 0;  
        Float F = new Float(0.0);  
        i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8  
                | (b[0] & 0xff);  
        return F.intBitsToFloat(i);  
    }  
  
    /** 
     * 低字节数组转换为float 
     *  
     * @param b 
     *            byte[] 
     * @return float 
     */  
    @SuppressWarnings("static-access")  
    public static float lBytesToFloat(int[] b) {  
        int i = 0;  
        Float F = new Float(0.0);  
        i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8  
                | (b[0] & 0xff);  
        return F.intBitsToFloat(i);  
    }  
  
    /** 
     * 将byte数组中的元素倒序排列 
     */  
    public static byte[] bytesReverseOrder(byte[] b) {  
        int length = b.length;  
        byte[] result = new byte[length];  
        for (int i = 0; i < length; i++) {  
            result[length - i - 1] = b[i];  
        }  
        return result;  
    }  
  
    /** 
     * 打印byte数组 
     */  
    public static void printBytes(byte[] bb) {  
        int length = bb.length;  
        for (int i = 0; i < length; i++) {  
            Log.e("slj", "----bb: "+bb[i]);
        }  
    }
  
    public static void logBytes(byte[] bb) {  
        int length = bb.length;  
        String out = "";  
        for (int i = 0; i < length; i++) {  
            out = out + bb + " ";  
        }  
    }
  
    /** 
     * 将int类型的值转换为字节序颠倒过来对应的int值 
     *  
     * @param i 
     *            int 
     * @return int 
     */  
    public static int reverseInt(int i) {  
        int result = FormatTransfer.hBytesToInt(FormatTransfer.toLH(i));  
        return result;  
    }  
  
    /** 
     * 将short类型的值转换为字节序颠倒过来对应的short值 
     *  
     * @param s 
     *            short 
     * @return short 
     */  
    public static short reverseShort(short s) {  
        short result = FormatTransfer.hBytesToShort(FormatTransfer.toLH(s));  
        return result;  
    }  
  
    /** 
     * 将float类型的值转换为字节序颠倒过来对应的float值 
     *  
     * @param f 
     *            float 
     * @return float 
     */  
    public static float reverseFloat(float f) {  
        float result = FormatTransfer.hBytesToFloat(FormatTransfer.toLH(f));  
        return result;  
    }  
  
    /** 
     * 此方法将参数i 转换为 num bytes的byte 数组 (小端模式) 
     *  
     * @param i 
     * @param num 
     * @return 
     */  
    public static byte[] int2Array(Long i, int num) {  
        byte[] a = new byte[num];  
        for (int j = 0; j < a.length; j++) {  
            a[j] = (byte) (i & 0xff);  
            i >>= 8;  
        }  
        byte[] cc = new byte[a.length];  
        for (int x = 0; x < a.length; x++) {  
            cc[x] = a[x];  
        }  
        return cc;  
    }
    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 像字符串的指定位置插入字符串
     * @param src 原字符串
     * @param dec 要插入的字符串
     * @param position 要插入的位置
     * @return
     */
    public static String insertStringInParticularPosition(String src, String dec, int position){
        StringBuffer stringBuffer = new StringBuffer(src);

        return stringBuffer.insert(position, dec).toString();

    }

    /**
     * 把int转换成制定长度的byte数组
     * @param i
     * @param len
     * @return
     */
    public static byte[] little_intToByte(int i, int len) {
        byte[] abyte = new byte[len];
        if (len == 1) {
            abyte[0] = (byte) (0xff & i);
        } else if (len == 2) {
            abyte[0] = (byte) (0xff & i);
            abyte[1] = (byte) ((0xff00 & i) >> 8);
        } else {
            abyte[0] = (byte) (0xff & i);
            abyte[1] = (byte) ((0xff00 & i) >> 8);
            abyte[2] = (byte) ((0xff0000 & i) >> 16);
            abyte[3] = (byte) ((0xff000000 & i) >> 24);
        }
        return abyte;
    }

    /**
     * 把byte转换成int
     * @param bytes
     * @return
     */
    public static int little_bytesToInt(byte[] bytes) {
        int addr = 0;
        if (bytes.length == 1) {
            addr = bytes[0] & 0xFF;
        } else if (bytes.length == 2) {
            addr = bytes[0] & 0xFF;
            addr |= (((int) bytes[1] << 8) & 0xFF00);
        } else {
            addr = bytes[0] & 0xFF;
            addr |= (((int) bytes[1] << 8) & 0xFF00);
            addr |= (((int) bytes[2] << 16) & 0xFF0000);
            addr |= (((int) bytes[3] << 24) & 0xFF000000);
        }
        return addr;
    }

    /**
     * int to byte[] 支持 1或者 4 个字节
     *
     * @param i
     * @param len
     * @return
     */
    public static byte[] big_intToByte(int i, int len) {
        byte[] abyte = new byte[len];
        ;
        if (len == 1) {
            abyte[0] = (byte) (0xff & i);
        } else if (len == 2) {
            abyte[0] = (byte) ((i >>> 8) & 0xff);
            abyte[1] = (byte) (i & 0xff);
        } else {
            abyte[0] = (byte) ((i >>> 24) & 0xff);
            abyte[1] = (byte) ((i >>> 16) & 0xff);
            abyte[2] = (byte) ((i >>> 8) & 0xff);
            abyte[3] = (byte) (i & 0xff);
        }
        return abyte;
    }

    public static int big_bytesToInt(byte[] bytes) {
        int addr = 0;
        if (bytes.length == 1) {
            addr = bytes[0] & 0xFF;
        } else if (bytes.length == 2) {
            addr = bytes[0] & 0xFF;
            addr = (addr << 8) | (bytes[1] & 0xff);
        } else {
            addr = bytes[0] & 0xFF;
            addr = (addr << 8) | (bytes[1] & 0xff);
            addr = (addr << 8) | (bytes[2] & 0xff);
            addr = (addr << 8) | (bytes[3] & 0xff);
        }
        return addr;
    }




    // 获得本机ＣＰＵ大小端
    public static boolean isBigendian() {
        short i = 0x1;
        boolean bRet = ((i >> 8) == 0x1);
        return bRet;
    }

    /**
     * 把 字符串转成16进制的自己数租
     * @param src
     * @return byte[]
     */
   public static byte[] HexString2Byte(String src){
       if (null == src || 0 == src.length()){
           return null;
       }
       byte[] ret = new byte[src.length()/2];
       for (int i = 0;i < (src.length()/2);i++){
           ret[i] = unitBytes16(src.substring(i*2,i*2+2));
       }
       return ret;
   }
    public static byte unitBytes16(String str){
        return (byte)Byte.decode("0x"+str);
    }
//    public static byte[] strToHexByte(String hexString)
//    {
//        hexString = hexString.replace(" ", "");
//        if ((hexString.length() % 2) != 0)
//            hexString += " ";
//        byte[] returnBytes = new byte[hexString.length() / 2];
//        for (int i = 0; i < returnBytes.length; i++)
//            returnBytes[i] = Convert.ToByte(hexString.substring(i * 2, 2),16);
//        return returnBytes;
//    }
    /**
     *  java 合并两个byte数组
     */
public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
    byte[] byte_3 = new byte[byte_1.length+byte_2.length];
    System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
    System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
    return byte_3;
}
}