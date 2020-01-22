package com.scy.rx.client;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.scy.rx.thread.ThreadPools;
import com.scy.rx.wrapper.MultiplexWrapperImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
public class EConnClient {
    private MultiplexWrapperImpl wrapper;

    private EClientSocket clientSocket;
    private EReaderSignal signal;

    private ExecutorService executorService = ThreadPools.createFixedPool("EConnClientHolder", 1);

    public EClientSocket getClientSocket() {
        return clientSocket;
    }

    public EConnClient() {
        wrapper = new MultiplexWrapperImpl();
        clientSocket = wrapper.getClientSocket();
        signal = wrapper.getSignal();
        connect();
    }

    private void connect() {
        log.info("EConnClientHolder init.");
        clientSocket.eConnect("127.0.0.1", 7496, 0);

        final EReader reader = new EReader(clientSocket, signal);
        reader.start();
        executorService.submit(() -> {
            while (clientSocket.isConnected()) {
                signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    log.error("EConnClientHolder reader.processMsgs Exception.", e);
                }
            }
        });
    }

    public MultiplexWrapperImpl getWrapper() {
        return wrapper;
    }
}
