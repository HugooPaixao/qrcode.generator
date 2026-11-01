package com.hugopaixao.qrcode.generator.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hugopaixao.qrcode.generator.dto.QrCodeGenerateResponse;
import com.hugopaixao.qrcode.generator.ports.StoragePort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class QrCodeGeneratorService {

    private StoragePort storage;

    public QrCodeGeneratorService(StoragePort storage) {
        this.storage = storage;
    }

    public QrCodeGenerateResponse generateAndUploadQrCode(String text) throws WriterException, IOException {

        // generate qrCode
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix , "PNG",byteArrayOutputStream);
        byte[] pngQrCodeData =byteArrayOutputStream.toByteArray();

        // upload storage
        String url = storage.uploadFile(pngQrCodeData, UUID.randomUUID().toString(), "image/png");

        return new QrCodeGenerateResponse(url);
    }
}
