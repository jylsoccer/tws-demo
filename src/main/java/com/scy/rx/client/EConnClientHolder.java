package com.scy.rx.client;

import com.ib.client.EClientSocket;
import com.scy.rx.wrapper.MultiplexWrapperImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EConnClientHolder {
    private static EConnClient eConnClient = new EConnClient();

    public static EConnClient getClient() {
        return eConnClient;
    }

    public static EClientSocket getClientSocket() {
        return eConnClient.getClientSocket();
    }

    public static MultiplexWrapperImpl getWrapper() {
        return eConnClient.getWrapper();
    }
}
