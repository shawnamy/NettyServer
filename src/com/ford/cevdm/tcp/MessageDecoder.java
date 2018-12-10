package com.ford.cevdm.tcp;

import java.util.List;

import javax.xml.bind.DatatypeConverter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
	/**
	 * <pre>
	 * 协议开始的标准head_data，short类型，占据2个字节.
	 * cmd_unit 2个字节,unique_id 17个字节,encry_type 1个字节
	 * 表示数据的长度contentLength，short类型，占据2个字节.
	 * </pre>
	 */
	public final int BASE_LENGTH = 24;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		// 可读长度必须大于基本长度
		if (buffer.readableBytes() >= BASE_LENGTH) {
			// 防止socket字节流攻击
			// 防止，客户端传来的数据过大
			// 因为，太大的数据，是不合理的
			if (buffer.readableBytes() > 2048) {
				buffer.skipBytes(buffer.readableBytes());
			}

			byte cmd;
			byte res;
			String unique_id;
			byte encry_type;

			// 记录包头开始的index
			int beginReader;
			while (true) {
				// 获取包头开始的index
				beginReader = buffer.readerIndex();
				// 标记包头开始的index
				buffer.markReaderIndex();
				// 读到了协议的开始标志，结束while循环
				if (buffer.readShort() == 0x2323) {
					cmd = buffer.readByte();
					res = buffer.readByte();
					byte[] vin = new byte[17];
					buffer.readBytes(vin);
					unique_id = new String(vin);
					encry_type = buffer.readByte();
					break;
				}

				// 未读到包头，略过一个字节
				// 每次略过，一个字节，去读取，包头信息的开始标记
				buffer.resetReaderIndex();
				buffer.readByte();

				// 当略过，一个字节之后，
				// 数据包的长度，又变得不满足
				// 此时，应该结束。等待后面的数据到达
				if (buffer.readableBytes() < BASE_LENGTH) {
					return;
				}
			}

			// 消息的长度
			short contentLength = buffer.readShort();
			// 判断请求数据包数据是否到齐
			if (buffer.readableBytes() < contentLength + 1) {
				// 还原读指针
				buffer.readerIndex(beginReader);
				return;
			}

			// 读取data数据
			byte[] content = new byte[contentLength];
			buffer.readBytes(content);

			byte bcc = buffer.readByte();

			XEVProtocol protocol = new XEVProtocol();
			protocol.setHead_data((short) 0x2323);
			protocol.setCmd(cmd);
			protocol.setRes(res);
			protocol.setUnique_id(unique_id);
			protocol.setEncry_type(encry_type);
			protocol.setContentLength(contentLength);
			protocol.setContent(content);
			protocol.setBcc(bcc);
			out.add(protocol);
		}
	}

	/**
	 * @Title:doDecode
	 * @Description: 此方法处理同mina中doDecode方法
	 * @param ctx
	 * @param msg
	 * @param out
	 * @return boolean
	 */
	private boolean doDecode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
		if (msg.readableBytes() < 4)
			return false;
		msg.markReaderIndex();
		byte[] header = new byte[4];
		msg.readBytes(header);
		int len = Integer.parseInt(DatatypeConverter.printHexBinary(header), 16);
		if (msg.readableBytes() < len) {
			msg.resetReaderIndex();
			return false;
		}
		byte[] body = new byte[len];
		msg.readBytes(body);
		out.add(Unpooled.copiedBuffer(body));
		if (msg.readableBytes() > 0)
			return true;
		return false;
	}
}
