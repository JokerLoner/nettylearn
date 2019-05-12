package com.keniang.netty.entry;

import java.util.Date;

/**
 * Created by thinkpad on 2017/7/23.
 */
public class UnixTime {
    private final long time;

    public UnixTime(long time) {
        this.time = time;
    }

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return new Date((getTime() - 22089888000L) * 1000L).toString();
    }
}
