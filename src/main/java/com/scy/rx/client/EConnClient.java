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

    public static EConnClient INSTANCE = new EConnClient();

    public EConnClient() {
        connect();
    }

    private MultiplexWrapperImpl wrapper = MultiplexWrapperImpl.INSTANCE;

    private EClientSocket clientSocket;

    private ExecutorService executorService = ThreadPools.createFixedPool("EConnClient", 1);

    public EClientSocket getClientSocket() {
        return clientSocket;
    }

    public void connect() {
        log.info("EConnClient connect.");
        clientSocket = wrapper.getClientSocket();
        EReaderSignal signal = wrapper.getSignal();
        clientSocket.eConnect("127.0.0.1", 7497, 0);

        final EReader reader = new EReader(clientSocket, signal);
        reader.start();
        executorService.submit(() -> {
            while (clientSocket.isConnected()) {
                signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    log.error("EConnClient reader.processMsgs Exception.", e);
                }
            }
        });
    }
}
