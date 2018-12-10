package com.ford.cevdm.tcp;

public class TcpServerTest {

	public static void main(String[] args) throws InterruptedException {
		NioSocketServer server = NioSocketServer.getInstance(19007);
		server.bind();
	}
}
