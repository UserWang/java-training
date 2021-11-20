package week03.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/11/20
 */
public class NettyClient {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline channelPipeline = ch.pipeline();
                            channelPipeline.addLast(new HttpClientCodec());
                            channelPipeline.addLast(new HttpObjectAggregator(65536));
                            channelPipeline.addLast(new HttpClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect("127.0.0.1",8801).sync();
            System.out.println(future);
            future.channel().closeFuture().sync();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }finally{
            System.out.println("shutdownGracefully");
            group.shutdownGracefully();
        }



    }
}
