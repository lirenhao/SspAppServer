package com.yada.ssp.appServer.config;

import com.yada.sdk.net.IPackageSplitter;
import com.yada.sdk.net.IPackageSplitterFactory;
import com.yada.sdk.net.TcpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

@Configuration
public class TcpClientConfig {

    @Bean
    public TcpClient tcpClient(TcpClientProperties properties) {
        InetSocketAddress hostAddress = new InetSocketAddress(properties.getHostName(), properties.getPort());
        IPackageSplitterFactory pkgSplitterFactory = new IPackageSplitterFactory() {
            @Override
            public IPackageSplitter create() {
                return new PackageSplitter(properties.getLenSize(),properties.getVer());
            }
        };

        return new TcpClient(hostAddress, pkgSplitterFactory, properties.getTimeOut());
    }

    private class PackageSplitter implements IPackageSplitter {

        private int lenSize;
        private String ver;

        PackageSplitter(int lenSize, String ver) {
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
}
