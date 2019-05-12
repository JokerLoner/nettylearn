package com.keniang.netty.client;

import com.keniang.netty.handler.TimeClientHandler;
import com.keniang.netty.decoder.TimeDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by thinkpad on 2017/7/23.
 */
public class TimeClient {
    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // 1 针对非服务端的连接 比如客户端或者无传输模式的连接
            b.group(workGroup); // 2 即作为bossGroup 也作为workGroup, 尽管客户端不需要bossGroup
            b.channel(NioSocketChannel.class); // 3
            b.option(ChannelOption.SO_KEEPALIVE, true); //4 不像在ServerBootstrap那样需要childOption方法，因为客户端的NioSocketChannel没有父亲
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
                }
            });
            // 启动客户端
            ChannelFuture f = b.connect(host, port).sync(); // 5

            // 等待连接关闭
            f.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
        }
    }
}
