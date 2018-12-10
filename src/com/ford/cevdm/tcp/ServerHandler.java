package com.ford.cevdm.tcp;

import java.nio.charset.Charset;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 与客户端建立连接后
		System.out.println("服务端-客户端连接通道激活！");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 用于获取客户端发来的数据信息
		XEVProtocol body = (XEVProtocol) msg;
		System.out.println("Server接受的客户端的信息 :" + body.toString());

		// 回写数据给客户端
		XEVProtocol response = new XEVProtocol();
		response.setHead_data(body.getHead_data());
		response.setCmd(body.getCmd());
		response.setRes((byte) 1);
		response.setUnique_id(body.getUnique_id());
		response.setEncry_type(body.getEncry_type());
		response.setContentLength((short) 6);
		byte[] date = new byte[6];
		date = Arrays.copyOfRange(body.getContent(), 0, 6);
		response.setContent(date);

		ByteBuf buf = Unpooled.buffer(500);
		buf.writeByte(body.getCmd());
		buf.writeByte((byte) 1);
		String vin = body.getUnique_id();
		byte[] vinByte = new byte[17];
		vinByte = vin.getBytes();
		buf.writeBytes(vinByte);
		buf.writeByte(body.getEncry_type());
		buf.writeShort((short) 6);
		buf.writeBytes(date);
		byte[] bccData = new byte[buf.readableBytes()];
		buf.readBytes(bccData);

		byte bcc = Util.getBcc(bccData);
		response.setBcc(bcc);

		// 当服务端完成写操作后，关闭与客户端的连接
		ctx.writeAndFlush(response);
		// .addListener(ChannelFutureListener.CLOSE);
		System.out.println("Server发送给客户端的信息 :" + response.toString());

		// 当有写操作时，不需要手动释放msg的引用
		// 当只有读操作时，才需要手动释放msg的引用
	}

	/**
	 * @description: 当读取不到消息后时触发（注：会受到粘包、断包等影响，所以未必是客户定义的一个数据包读取完成即调用）
	 */
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("服务端读取客户端发送消息完毕！");
		ctx.channel().writeAndFlush(Unpooled.copiedBuffer("server has been received.", Charset.forName("UTF-8")));
	}

	/**
	 * @description: 异常捕捉方法
	 */
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("服务端通道异常，异常消息：" + cause.getMessage());
		ctx.close();
	}
}
