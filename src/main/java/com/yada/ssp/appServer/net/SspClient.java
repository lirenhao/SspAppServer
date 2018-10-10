package com.yada.ssp.appServer.net;

import com.yada.sdk.net.IPackageSplitterFactory;
import com.yada.sdk.net.TcpClient;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class SspClient {

    private final Logger logger = LoggerFactory.getLogger(SspClient.class);
    private InetSocketAddress hostAddress;
    private IPackageSplitterFactory pkgSplitterFactory;
    private int timeout;

    public SspClient(InetSocketAddress hostAddress, IPackageSplitterFactory pkgSplitterFactory, int timeout) {
        this.hostAddress = hostAddress;
        this.pkgSplitterFactory = pkgSplitterFactory;
        this.timeout = timeout;
    }

    public ByteBuffer send(ByteBuffer req) throws IOException {
        TcpClient client = new TcpClient(hostAddress, pkgSplitterFactory, timeout);
        try {
            logger.info("send to Ssp: [{}]", Hex.encodeHexString(req.array()));
            client.open();
            ByteBuffer resp = client.send(req);
            logger.info("resp from Ssp: [{}]", Hex.encodeHexString(resp.array()));
            return resp;
        } catch (IOException e) {
            logger.warn("[{}] has a error: [{}]", hostAddress.toString(), e);
            throw e;
        } finally {
            client.close();
        }
    }
}
