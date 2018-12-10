package com.ford.cevdm.tcp.client;

import com.ford.cevdm.tcp.Util;
import com.ford.cevdm.tcp.XEVProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	// 客户端与服务端，连接成功的售后
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 发送SmartCar协议的消息
		// 要发送的信息
		String data = "23 23 02 fe 4c 56 53 48 46 46 46 43 34 48 46 36 33 37 35 32 34 01 01 17 11 09 1a 0d 20 13 01 01 03 01 00 00 00 00 03 de 0c b2 29 fe 64 02 2f 05 f4 1a 00 02 01 01 01 4a 57 f9 51 36 40 0c 9e 29 d8 04 01 00 00 ff ff 05 01 07 14 96 39 01 e6 a1 3a 06 01 54 0f 5f 01 0b 0f 41 01 09 3f 01 05 3c 07 02 00 00 20 00 00 00 00 00 08 01 01 0c b2 29 fe 00 54 00 01 54 0f 4c 0f 4b 0f 49 0f 49 0f 48 0f 4a 0f 4b 0f 4a 0f 50 0f 48 0f 41 0f 4a 0f 4b 0f 4b 0f 4b 0f 4a 0f 49 0f 4a 0f 4d 0f 4d 0f 4e 0f 4d 0f 4c 0f 4d 0f 4c 0f 4c 0f 4a 0f 4b 0f 4a 0f 4b 0f 4c 0f 4c 0f 4c 0f 4c 0f 4a 0f 4c 0f 4c 0f 4c 0f 4a 0f 4a 0f 4b 0f 4d 0f 4c 0f 4a 0f 4b 0f 49 0f 4b 0f 4b 0f 4c 0f 4c 0f 4c 0f 4c 0f 4b 0f 4c 0f 4d 0f 4c 0f 4d 0f 4c 0f 4c 0f 4b 0f 4e 0f 4e 0f 4c 0f 4b 0f 4d 0f 4e 0f 4e 0f 4d 0f 4c 0f 4b 0f 4c 0f 4c 0f 4b 0f 4a 0f 4b 0f 4c 0f 4c 0f 4d 0f 4c 0f 4c 0f 4c 0f 4b 0f 5d 0f 5f 09 01 01 00 0c 3d 3d 3e 3e 3c 3e 3e 3e 3f 3f 3e 3e 85";
		// 获得要发送信息的字节数组
		byte[] sendcontent = Util.str2HexBytes(data);
		ByteBuf buf = Unpooled.buffer(500);
		buf.writeBytes(sendcontent);
		buf.resetReaderIndex();

		// 要发送信息
		XEVProtocol protocol = new XEVProtocol();
		protocol.setHead_data(buf.readShort());
		protocol.setCmd(buf.readByte());
		protocol.setRes(buf.readByte());
		byte[] vin = new byte[17];
		buf.readBytes(vin);
		String unique_id = new String(vin);
		protocol.setUnique_id(unique_id);
		protocol.setEncry_type(buf.readByte());
		short contentLength = buf.readShort();
		protocol.setContentLength(contentLength);
		byte[] content = new byte[contentLength];
		buf.readBytes(content);
		protocol.setContent(content);
		protocol.setBcc(buf.readByte());

		System.out.println("Client发送给服务端的信息 :" + protocol.toString());

		ctx.writeAndFlush(protocol);
	}

	// 只是读数据，没有写数据的话
	// 需要自己手动的释放的消息
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			// 用于获取客户端发来的数据信息
			XEVProtocol body = (XEVProtocol) msg;
			System.out.println("Client接受的客户端的信息 :" + body.toString());

		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
