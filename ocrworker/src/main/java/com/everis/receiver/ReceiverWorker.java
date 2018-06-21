package com.everis.receiver;

import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Component
public class ReceiverWorker {

    private String UPLOADED_FOLDER = "C:\\Temp\\";

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


    @Autowired
    private QueueClient queueClientForReceiving;

    @PostConstruct
    private void postConstruct() throws InterruptedException {
        try {
            queueClientForReceiving.registerMessageHandler(new MessageHandler(),
                    new MessageHandlerOptions());
        } catch (InterruptedException e) {
            System.out.println("Error registering message handler :" + e);
        } catch (ServiceBusException e) {
            System.out.println("Error registering message handler: " + e);
        }
    }

    public class MessageHandler implements IMessageHandler {
        public CompletableFuture<Void> onMessageAsync(IMessage message) {
            final String messageString = new String(message.getBody(),
                    StandardCharsets.UTF_8);

            separar(messageString);


            imagePath = UPLOADED_FOLDER + "Entrada\\" + ticket + extension;
            exportPath = UPLOADED_FOLDER + "Salida\\" + ticket + ".txt";

            if(ocr.equals("ABYYFR")) {
                finereaderWorker.ocrFineReaderExec(imagePath,exportPath,precision,token,ticket);
            }
            else if (ocr.equals("tesseract")) {
                doTess(imagePath, exportPath, precision, token, ticket);
            }

            else {}

            removeFile(imagePath);

            System.out.println("Worker process finished");

            return CompletableFuture.completedFuture(null);
        }

        public void notifyException(Throwable exception, ExceptionPhase phase) {
            System.out.println(phase + " encountered exception:" + exception);
        }
    }

    public void doTess(String imagePath, String exportPath, Integer precision, String token, String ticket) {
        tesseractWorker.ocrTesseractExec(imagePath, exportPath, precision, token, ticket);
    }


    public void removeFile(String path) {
        File fichero = new File(path);

        fichero.delete();
    }

}
