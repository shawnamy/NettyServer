package com.ford.cevdm.tcp;

import java.util.Arrays;

/**
 * <pre>
 * 自己定义的协议
 *  数据包格式
 * +——----——+——-----——+——----——+
 * |协议开始标志|  长度             |   数据       |
 * +——----——+——-----——+——----——+
 * 1.协议开始标志head_data，为short类型的数据，16进制表示为0X2323，以及包头内容
 * 2.传输数据的长度contentLength，short类型
 * 3.要传输的数据
 * 4.bcc校验码
 * </pre>
 */
public class XEVProtocol {
	/**
	 * 消息的开头的信息标志
	 */
	private short head_data;
	/**
	 * 命令标识
	 */
	private byte cmd;

	/**
	 * 应答标识
	 */
	private byte res;

	/**
	 * 唯一识别码
	 */
	private String unique_id;
	/**
	 * 数据单元加密方式
	 */
	private byte encry_type;
	/**
	 * 消息的长度
	 */
	private short contentLength;
	/**
	 * 消息的内容
	 */
	private byte[] content;

	/**
	 * bcc校验
	 */
	private byte bcc;

	public short getHead_data() {
		return head_data;
	}

	public void setHead_data(short head_data) {
		this.head_data = head_data;
	}

	public byte getCmd() {
		return cmd;
	}

	public void setCmd(byte cmd) {
		this.cmd = cmd;
	}

	public byte getRes() {
		return res;
	}

	public void setRes(byte res) {
		this.res = res;
	}

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	public byte getEncry_type() {
		return encry_type;
	}

	public void setEncry_type(byte encry_type) {
		this.encry_type = encry_type;
	}

	public short getContentLength() {
		return contentLength;
	}

	public void setContentLength(short contentLength) {
		this.contentLength = contentLength;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public byte getBcc() {
		return bcc;
	}

	public void setBcc(byte bcc) {
		this.bcc = bcc;
	}

	@Override
	public String toString() {
		return "SmartCarProtocol [head_data=" + head_data + ", cmd=" + cmd + ", res=" + res + ", unique_id=" + unique_id
				+ ", encry_type=" + encry_type + ", contentLength=" + contentLength + ", content="
				+ Arrays.toString(content) + ", bcc=" + bcc + "]";
	}
}
