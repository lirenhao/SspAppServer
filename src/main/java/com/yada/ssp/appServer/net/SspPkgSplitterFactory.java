package com.yada.ssp.appServer.net;

import com.yada.sdk.net.IPackageSplitter;
import com.yada.sdk.net.IPackageSplitterFactory;

public class SspPkgSplitterFactory implements IPackageSplitterFactory {

    private int pkgLenSize;
    private String version;

    public SspPkgSplitterFactory(int pkgLenSize, String version) {
        this.pkgLenSize = pkgLenSize;
        this.version = version;
    }

    @Override
    public IPackageSplitter create() {
        return new SspPkgSplitter(pkgLenSize, version);
    }
}
