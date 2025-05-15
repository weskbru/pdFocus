package com.pdfocus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PdfocusApplicationTest {
    @Test
    void testSomar() {
        int resultado = PdfocusApplication.somar(2, 3);
        assertEquals(5, resultado);
    }
}
