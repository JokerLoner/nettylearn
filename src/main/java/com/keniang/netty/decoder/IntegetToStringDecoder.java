package com.keniang.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by thinkpad on 2017/8/6.
 */
public class IntegetToStringDecoder extends MessageToMessageDecoder<Integer> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
