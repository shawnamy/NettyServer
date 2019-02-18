package com.ford.cevdm.tcp;

import java.util.Arrays;

public class Util {
	public static final int[] HEX_VALUES = { 0, 0xFF, 0xFFFF, 0, 0xFFFFFFFF, 0 };

	public static byte getBcc(byte[] ss) {

		byte a = ss[0];
		for (int i = 1; i < ss.length; i++) {
			byte b = ss[i];
			a = (byte) (a ^ b);
		}

		return a;
	}

	public static byte[] str2HexBytes(String str) {

		String[] arr = str.trim().split(" ");
		byte[] out = new byte[arr.length];
		for (int i = 0; i < arr.length; i++) {
			out[i] = (byte) (Integer.valueOf(arr[i], 16) & 0xFF);
		}

		return out;
	}

	public static String dec2Hex(int value, int length) {

		byte[] out = new byte[length * 2];
		Arrays.fill(out, (byte) (48 & 0xFF));

		String hexstr = Integer.toHexString(value & HEX_VALUES[length]);
		byte[] hexval = hexstr.getBytes();
		System.arraycopy(hexval, 0, out, length * 2 - hexval.length, hexval.length);

		String outstr = "";
		for (int i = 0; i <= out.length - 2; i = i + 2) {
			String a = new String(new byte[] { out[i], out[i + 1] });
			outstr = outstr + a.toUpperCase() + " ";
		}

		return outstr.trim();
	}

	public static String byteArr2HexStr(byte[] out) {
		StringBuilder str = new StringBuilder();

		for (int i = 0; i < out.length; i++) {
			String hexstr = Integer.toHexString(out[i] & 0xFF);
			String val = hexstr.length() == 1 ? "0" + hexstr : hexstr;
			str.append(val + " ");
		}

		return str.toString().trim();
	}
}
