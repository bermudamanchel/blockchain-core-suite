package com.blockchain.core.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class P2PMessageCodec {
    private static final int BUFFER_SIZE = 1024;
    private final boolean enableCompression;

    public P2PMessageCodec(boolean compress) {
        this.enableCompression = compress;
    }

    public String encodeMessage(String message) throws IOException {
        byte[] data = message.getBytes();
        if (enableCompression) {
            data = compress(data);
        }
        return Base64.getEncoder().encodeToString(data);
    }

    public String decodeMessage(String encoded) throws IOException {
        byte[] data = Base64.getDecoder().decode(encoded);
        if (enableCompression) {
            data = decompress(data);
        }
        return new String(data);
    }

    private byte[] compress(byte[] input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(input);
        }
        return bos.toByteArray();
    }

    private byte[] decompress(byte[] input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(input))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = gzip.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        }
        return bos.toByteArray();
    }
}
