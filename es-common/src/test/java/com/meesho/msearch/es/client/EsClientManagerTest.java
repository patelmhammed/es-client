package com.meesho.msearch.es.client;

import com.meesho.msearch.es.EsVersion;
import com.meesho.msearch.es.config.EsConnectionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EsClientManagerTest {

    private EsClientManager clientManager;

    @BeforeEach
    void setUp() {
        clientManager = new EsClientManager();
    }

    @Test
    void registerAndCreateClient() {
        EsClient mockClient = new EsClient() {};
        clientManager.registerClientFactory(EsVersion.V8_5, props -> mockClient);

        assertTrue(clientManager.hasFactory(EsVersion.V8_5));
        assertFalse(clientManager.hasFactory(EsVersion.V7));

        EsClient created = clientManager.createClient(new EsConnectionProperties(), EsVersion.V8_5);
        assertSame(mockClient, created);
    }

    @Test
    void createClient_noFactory_throws() {
        assertThrows(IllegalStateException.class,
                () -> clientManager.createClient(new EsConnectionProperties(), EsVersion.V7));
    }
}
