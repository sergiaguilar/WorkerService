package com.everis.ocrworker;

import com.recognition.software.jdeskew.ImageDeskew;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TesseractImpl {

    static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;

    public TesseractImpl() {}

    public String Run(String imagePath, String exportPath, Integer maxVolume, Integer actualVolume, Integer precision) throws Exception {
        ITesseract instance = new Tesseract();
        File file = new File(imagePath);

        final String[] result = {""};
        Integer numPags = 0;

        String extension = FilenameUtils.getExtension(file.getName());
        String justName = FilenameUtils.getBaseName(file.getName());

        if (extension.equals("pdf")) {
            final PDDocument document = PDDocument.load(file);
            int pages = document.getNumberOfPages();
            if(pages + actualVolume > maxVolume) return "Max volume exceeded!";
            if (precision == 3) {
                File carpeta = new File("Z:\\ocr\\entrada\\" + justName);
                carpeta.mkdir();
                try {

                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    numPags = document.getNumberOfPages();
                    for (int page = 0; page < document.getNumberOfPages(); ++page) {
                        BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                        String fileName = file.getParent() + "\\" + justName + "\\" + justName + "-" + page + ".jpg";
                        ImageIOUtil.writeImage(bim, fileName, 300);
                    }
                    document.close();

                    Files.walk(Paths.get(carpeta.getAbsolutePath())).forEach((ruta) -> {
                        if (Files.isRegularFile(ruta)) {
                            File aux = ruta.toFile();
                            BufferedImage image = null;
                            try {
                                image = ImageIO.read(aux);
                                ImageDeskew id = new ImageDeskew(image);
                                double imageSkewAngle = id.getSkewAngle();
                                if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
                                    image = ImageHelper.rotateImage(image, -imageSkewAngle);
                                }
                                instance.setOcrEngineMode(1);
                                instance.setPageSegMode(1);
                                instance.setDatapath("data\\tessdata");
                                instance.setLanguage("spa");
                                try {
                                    String imgText = instance.doOCR(image);
                                    result[0] = result[0] + imgText;
                                } catch (TesseractException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        result[0] = result[0] + "\n";
                    });
                } catch (IOException e) {
                    System.err.println("Exception while trying to create pdf document - " + e);
                }
                String[] entries = carpeta.list();
                for (String s : entries) {
                    File currentFile = new File(carpeta.getPath(), s);
                    currentFile.delete();
                }
                carpeta.delete();
                BufferedWriter writer = new BufferedWriter(new FileWriter(exportPath));
                writer.write(result[0]);
                writer.close();
                return numPags.toString();
            }

            else if (precision == 2) {
                File carpeta = new File("Z:\\ocr\\entrada\\" + justName);
                carpeta.mkdir();
                try {
                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    numPags = document.getNumberOfPages();
                    for (int page = 0; page < document.getNumberOfPages(); ++page) {
                        BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                        String fileName = file.getParent() + "\\" + justName + "\\" + justName + "-" + page + ".jpg";
                        ImageIOUtil.writeImage(bim, fileName, 300);
                    }
                    document.close();

                    Files.walk(Paths.get(carpeta.getAbsolutePath())).forEach((ruta) -> {
                        if (Files.isRegularFile(ruta)) {
                            File aux = ruta.toFile();
                            BufferedImage image = null;
                            try {
                                image = ImageIO.read(aux);

                                instance.setOcrEngineMode(1);
                                instance.setPageSegMode(1);
                                instance.setDatapath("data\\tessdata");
                                instance.setLanguage("spa");
                                try {
                                    String imgText = instance.doOCR(image);
                                    result[0] = result[0] + imgText;
                                } catch (TesseractException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        result[0] = result[0] + "\n";
                    });
                } catch (IOException e) {
                    System.err.println("Exception while trying to create pdf document - " + e);
                }
                String[] entries = carpeta.list();
                for (String s : entries) {
                    File currentFile = new File(carpeta.getPath(), s);
                    currentFile.delete();
                }
                carpeta.delete();
                BufferedWriter writer = new BufferedWriter(new FileWriter(exportPath));
                writer.write(result[0]);
                writer.close();
                return numPags.toString();
            }

            else {
                numPags = document.getNumberOfPages();
                document.close();
                try {
                    instance.setOcrEngineMode(1);
                    instance.setPageSegMode(1);
                    instance.setDatapath("data\\tessdata");
                    instance.setLanguage("spa");
                    String imgText = instance.doOCR(file);
                    result[0] = imgText;
                } catch (TesseractException e) {
                    e.printStackTrace();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(exportPath));
                writer.write(result[0]);
                writer.close();

                return numPags.toString();
            }
        }

        else {
            if(1 + actualVolume > maxVolume) return "Max volume exceeded!";

            BufferedImage image = ImageIO.read(file);
            if(precision == 3 || precision == 2) {
                ImageDeskew id = new ImageDeskew(image);
                double imageSkewAngle = id.getSkewAngle();
                if((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
                    image = ImageHelper.rotateImage(image, -imageSkewAngle);
                }
            }

            try{
                instance.setOcrEngineMode(1);
                instance.setPageSegMode(1);
                instance.setDatapath("data\\tessdata");
                instance.setLanguage("spa");
                String imgText = instance.doOCR(file);
                result[0] = imgText;
            } catch (TesseractException e) {
                e.getMessage();
                return "Error while reading image!!";
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(exportPath));
            writer.write(result[0]);
            writer.close();
            Integer r = 1;
            return r.toString();
        }
    }

}
