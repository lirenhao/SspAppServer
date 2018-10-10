package com.yada.ssp.appServer.net;

import com.yada.sdk.net.IPackageSplitter;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;

public class SspPkgSplitter implements IPackageSplitter {

    private int lenSize;
    private String ver;

    public SspPkgSplitter(int lenSize, String ver) {
        this.lenSize = lenSize;
        this.ver = ver;
    }

    @Override
    public ByteBuffer getPackage(ByteBuffer newData) {
        ByteBuffer lenBuffer = ByteBuffer.allocate(lenSize);
        while(lenBuffer.hasRemaining()) {
            lenBuffer.put(newData.get());
        }
        ByteBuffer verBuffer = ByteBuffer.allocate(ver.length());
        while(verBuffer.hasRemaining()) {
            verBuffer.put(newData.get());
        }
        ByteBuffer buffer = ByteBuffer.allocate(newData.remaining());
        while(buffer.hasRemaining()){
            buffer.put(newData.get());
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public ByteBuffer pack(ByteBuffer newData) {
        String len = StringUtils.leftPad(String.valueOf(ver.length() + newData.remaining()), lenSize, "0");
        ByteBuffer buffer = ByteBuffer.allocate(lenSize + ver.length() + newData.remaining());

        buffer.put(len.getBytes());
        buffer.put(ver.getBytes());
        buffer.put(newData);

        buffer.flip();
        return buffer;
    }
}