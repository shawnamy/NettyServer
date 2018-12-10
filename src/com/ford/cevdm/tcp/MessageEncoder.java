package com.ford.cevdm.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<XEVProtocol> {

	@Override
	protected void encode(ChannelHandlerContext ctx, XEVProtocol msg, ByteBuf out) throws Exception {
		// 写入消息SmartCar的具体内容
		// 1.写入消息的开头的信息标志(short类型)
		out.writeShort(msg.getHead_data());
		out.writeByte(msg.getCmd());
		out.writeByte(msg.getRes());
		String vin = msg.getUnique_id();
		out.writeBytes(vin.getBytes());
		out.writeByte(msg.getEncry_type());
		// 2.写入消息的长度(short 类型)
		out.writeShort(msg.getContentLength());
		// 3.写入消息的内容(byte[]类型)
		out.writeBytes(msg.getContent());
		out.writeByte(msg.getBcc());
	}

}
