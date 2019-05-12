package com.keniang.netty.learntry;

import com.keniang.netty.encoder.TimeEncoder;
import com.keniang.netty.handler.TimeServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by thinkpad on 2017/7/23.
 */
public class DiscardServer {
    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 1  NioEventLoopGroup用来处理I/O操作的多线程事件循环器  Netty提供了不少EventLoopGroup处理不同传输
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();// 2  启动NIO服务的辅助类
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class) // 3  NioServerSocketChannel 举例说明一个新的channel如何接受进来连接
            .childHandler(new ChannelInitializer<SocketChannel>() { // 4 这里的处理类 经常会被用来处理一个最近的已接受的Channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new DiscardServerHandler()).addLast(new TimeEncoder(), new TimeServerHandler());
                }
            })
                    .option(ChannelOption.SO_BACKLOG, 128) //5  设定Channel实现的配置参数
            .childOption(ChannelOption.SO_KEEPALIVE, true); //6  option()是提供给NioServerSocketChannel用来接收进来的连接,也就是boss线程。
                                                                  // childOption()是提供给由父管道ServerChannel接收到的连接，也就是worker线程,在这个例子中也是NioServerSocketChannel

            // 绑定端口， 开始接收进来的连接
            ChannelFuture f = b.bind(port).sync();//7

            //等待服务器 scoket关闭
            // 这个例子不会发生 但可以优雅关闭服务器
            f.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new DiscardServer(port).run();
    }
}
