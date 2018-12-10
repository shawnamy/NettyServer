package com.ford.cevdm.tcp;

public class Util {
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
}
