package com.meesho.msearch.es;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EsVersionTest {

    @Test
    void fromValue_validValues() {
        assertEquals(EsVersion.V7, EsVersion.fromValue("7"));
        assertEquals(EsVersion.V8_5, EsVersion.fromValue("8.5"));
        assertEquals(EsVersion.V8_13, EsVersion.fromValue("8.13"));
        assertEquals(EsVersion.V9_1, EsVersion.fromValue("9.1"));
    }

    @Test
    void fromValue_invalidValue() {
        assertThrows(IllegalArgumentException.class, () -> EsVersion.fromValue("10"));
    }

    @Test
    void getValue() {
        assertEquals("7", EsVersion.V7.getValue());
        assertEquals("8.5", EsVersion.V8_5.getValue());
        assertEquals("8.13", EsVersion.V8_13.getValue());
        assertEquals("9.1", EsVersion.V9_1.getValue());
    }
}
