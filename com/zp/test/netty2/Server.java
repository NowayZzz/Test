package com.zp.test.netty2;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	
	public static void main(String[] args) throws InterruptedException {
		run(port);
	}
	
	private static int port = 9090;
	static void run(int port) {
		EventLoopGroup bootGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		ServerBootstrap sb = new ServerBootstrap();
		sb.option(ChannelOption.SO_BACKLOG, 500);
		sb.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
		sb.option(ChannelOption.SO_TIMEOUT, 3000);
		sb.childOption(ChannelOption.SO_KEEPALIVE, true);
		sb.group(bootGroup, workGroup)
		  .channel(NioServerSocketChannel.class)
		  .childHandler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel arg0) throws Exception {
				arg0.pipeline().addLast(new MyChannel());
				
			}
		});
		
		try {
			ChannelFuture cf = sb.bind(port).sync();
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bootGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	static class MyChannel extends SimpleChannelInboundHandler<ByteBuf>{

		private StringBuffer sb = new StringBuffer(); 
		@Override
		protected void channelRead0(ChannelHandlerContext arg0, ByteBuf arg1) throws Exception {
			sb.append(arg1.toString(Charset.forName("UTF-8")));
		}
		
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			
			if(StringUtils.isNotEmpty(sb)){
				System.out.println("server接收到:"+sb);
				System.out.println("------channelReadComplete---------");
			}
			sb.setLength(0);
//			ctx.writeAndFlush("server应答:我已经收到");
			ByteBuf encoded = ctx.alloc().buffer();
			encoded.writeCharSequence("server应答:我已经收到", Charset.forName("UTF-8"));
			ctx.writeAndFlush(encoded);
//			super.channelReadComplete(ctx);
			ctx.close();
		}
		
		/*@Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	            throws Exception {
	        System.out.println("-------------------------------server exceptionCaught.."+cause.getMessage()+cause.getStackTrace());
	        ctx.close();
	    }*/
	}
}
