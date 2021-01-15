package io.github.junhuhdev.dracarys.jobrunr.dashboard.sse;

import com.sun.net.httpserver.HttpExchange;
import io.github.junhuhdev.dracarys.jobrunr.storage.BackgroundJobServerStatus;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.storage.listeners.BackgroundJobServerStatusChangeListener;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;

import java.io.IOException;
import java.util.List;

public class BackgroundJobServerStatusSseExchange extends AbstractObjectSseExchange implements BackgroundJobServerStatusChangeListener {

    private static String lastMessage;

    private final StorageProvider storageProvider;

    public BackgroundJobServerStatusSseExchange(HttpExchange httpExchange, StorageProvider storageProvider, JsonMapper jsonMapper) throws IOException {
        super(httpExchange, jsonMapper);
        this.storageProvider = storageProvider;
        storageProvider.addJobStorageOnChangeListener(this);
        sendMessage(lastMessage);
    }

    @Override
    public void onChange(List<BackgroundJobServerStatus> changedServerStates) {
        lastMessage = sendObject(changedServerStates);
    }

    @Override
    public void close() {
        storageProvider.removeJobStorageOnChangeListener(this);
        super.close();
    }
}