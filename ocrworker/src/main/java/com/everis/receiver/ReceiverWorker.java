package com.everis.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.CountDownLatch;

@Component
public class ReceiverWorker {

    private static String UPLOADED_FOLDER = "C:\\Temp\\";

    private String imagePath;
    private String exportPath;
    private Integer precision;
    private String token;
    private String ticket;
    private String extension;
    private String ocr;

    @Autowired
    private FinereaderWorker finereaderWorker;

    @Autowired
    private TesseractWorker tesseractWorker;

    private void separar(String message) {
        String[] parts = message.split("\\|");
        precision = Integer.parseInt(parts[0]);
        token = parts[1];
        ticket = parts[2];
        extension = parts[3];
        ocr = parts[4];
    }

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Message received!");
        separar(message);

        imagePath = UPLOADED_FOLDER + "Entrada\\" + ticket + extension;
        exportPath = UPLOADED_FOLDER + "Salida\\" + ticket + ".txt";
        if(ocr.equals("ABYYFR")) {
            finereaderWorker.ocrFineReaderExec(imagePath,exportPath,precision,token,ticket);
        }
        else if (ocr.equals("tesseract")) {
            tesseractWorker.ocrTesseractExec(imagePath, exportPath, precision, token, ticket);
        }

        else {}

        removeFile(imagePath);

        System.out.println("Worker process finished");

        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void removeFile(String path) {
        File fichero = new File(path);

        fichero.delete();
    }

}
