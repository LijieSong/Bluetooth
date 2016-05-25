package com.example.asus.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StrUtil {

	/**
	 * 转化十六进制编码为字节数组
	 * */
	public static byte[] toByteHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
						i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return baKeyword;
	}
	/**
	 * 字符串转十六进制字符串
	 * */
	public static String str2HexStr(String str,String fenge)
	{
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++)
		{
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
			if(fenge!=null) sb.append(fenge);
		}
		return sb.toString().trim();
	}
	/**
	 * 按长度分割字符串
	 * */
	public static String[] subStringByLength(String str,int len){

		int key = 0;
		int slen = str.length();
		int status = slen%len;
		if(status!=0){
			slen = str.length()-status;
		}
		String res[] = new String[slen/len];
		for(int i=0;i<slen;i=i+len){
			res[key++] = str.substring(i, i+len);
		}
		return res;
	}



	/**
	 * 左对齐补全字符串到制定长度
	 * */
	public static byte[] rjust(byte[] str,int l,byte t){
		int sl = str.length;
		int cl = l-sl;
		if(cl<1)
			return str;
		byte[] b = new byte[l];
		int i=0;
		for(int k=0;k<cl;k++) b[i++] = t;
		for(int s=0;s<sl;s++) b[i++] = str[s];

		return b;
	}

	/**
	 * 右对齐补全字符串到制定长度
	 * */
	public static byte[] ljust(byte[] str,int l,byte t){
		//str.length();
		int sl = str.length;
		int cl = l-sl;
		int i = 0;
		if(cl<1)
			return str;
		byte[] temp = new byte[l];
		for(int s=0;s<sl;s++) temp[i++] = str[s];
		for(int k=0;k<cl;k++) temp[i++] = t;
		return temp;
	}

	/**
	 * 返回指定一段byte
	 * */
	public static byte[] splitByte(byte[] base, int start, int end) {
		int length = end-start;
		if(length<0) return null;

		byte[] temp = new byte[length];
		int i = 0;
		while (i < length) {
			temp[i] = base[start + i];
			i++;
		}
		return temp;
	}

	/**
	 * 返回指定一段char
	 * */
	public static char[] splitChar(char[] base, int start, int end) {
		int length = end-start;
		if(length<0) return null;

		char[] temp = new char[length];
		int i = 0;
		while (i < length) {
			temp[i] = base[start + i];
			i++;
		}
		return temp;
	}
	/**
	 * Long转bytes
	 * */
	public static byte[] long2bytes(long num) {
		byte[] b = new byte[8];
		for (int i=0;i<8;i++) {
			b[i] = (byte)(num>>>(56-(i*8)));
		}
		return b;
	}
	/**
	 * bytes转long
	 * */
	public static long bytes2long(byte[] b) {
		long temp = 0;
		long res = 0;
		for (int i=0;i<8;i++) {
			res <<= 8;
			temp = b[i] & 0xff;
			res |= temp;
		}
		return res;
	}

	/**
	 * byte数组转字符串
	 * */
	public static String byteTOString(byte[] in) {

		InputStream is = byteTOInputStream(in);
		return InputStreamTOString(is);
	}

	public static InputStream byteTOInputStream(byte[] in){

		ByteArrayInputStream is = new ByteArrayInputStream(in);
		return is;
	}

	public static String InputStreamTOString(InputStream in) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int count = -1;
		String str = "";
		try {
			while ((count = in.read(data, 0, 4096)) != -1)
				outStream.write(data, 0, count);
			data = null;
			str =  new String(outStream.toByteArray(), "ISO-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 统计左边byte数组不为空的长度
	 * */
	public static int getLenToL(byte[] b){
		int key=0;
		for(int i=b.length;i>0;i--){
			if(b[i-1]!=0){
				key = i;
				break;
			}
		}
		return key;
	}
	/**
	 * 统计右边byte数组不为空的长度
	 * */
	public static int getLenToR(byte[] b){
		int key=0;
		for(int i=0;i<b.length;i++){
			if(b[i]!=0){
				key = i+1;
				break;
			}
		}
		return b.length-key+1;
	}

	/**
	 * 清除byte数组左边的0
	 * */
	public static byte[] byteCleanToL(byte[] b){
		int l = getLenToR(b);
		byte[] tb = new byte[l];
		l = b.length-l;
		int i=0;
		for(;l<b.length;l++){
			tb[i++]=b[l];
		}
		return tb;
	}
	/**
	 * 清除byte数组右边的0
	 * */
	public static byte[] byteCleanToR(byte[] b){

		return null;
	}
	/*
    * 把16进制字符串转换成字节数组 @param hex @return
    */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 把字节数组转换成16进制字符串
	 *
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * @函数功能: BCD码转为10进制串(阿拉伯数据)
	 * @输入参数: BCD码
	 * @输出结果: 10进制串
	 */
	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}
	/**
	 * @函数功能: 10进制串转为BCD码
	 * @输入参数: 10进制串
	 * @输出结果: BCD码
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}
}
