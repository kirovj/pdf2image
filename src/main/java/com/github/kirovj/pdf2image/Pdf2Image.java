package com.github.kirovj.pdf2image;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pdf2Image {

//    static void pdf2imageWithBox3(String file) {
//        try (PDDocument document = Loader.loadPDF(new File("example/" + file + ".pdf"))) {
//            int numberOfPages = document.getNumberOfPages();
//            PDFRenderer renderer = new PDFRenderer(document);
//            for (int i = 0; i < numberOfPages; i++) {
//                System.out.println("render " + i);
//                BufferedImage bufferedImage = renderer.renderImage(i, 2, ImageType.RGB);
//                ImageIO.write(bufferedImage, "png", new File("example/" + file + "-" + i + ".png"));
//            }
//
////            BufferedImage img1 = renderer.renderImage(1, 2, ImageType.RGB);
////            BufferedImage img2 = renderer.renderImage(2, 2, ImageType.RGB);
////            ImageIO.write(img1, "png", new File("example/" + file + "-test-1.png"));
////            ImageIO.write(img2, "png", new File("example/" + file + "-test-2.png"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    static void pdf2image(String file) {
        try (PDDocument document = PDDocument.load(new File("example/" + file + ".pdf"))) {
            int numberOfPages = document.getNumberOfPages();
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < numberOfPages; i++) {
                System.out.println("render " + i);
                BufferedImage bufferedImage = renderer.renderImage(i, 2, ImageType.RGB);
                ImageIO.write(bufferedImage, "png", new File("example/" + file + "-" + i + ".png"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void image2pdf() throws IOException {
        try (PDDocument document = new PDDocument()) {
            for (int i = 277; i < 285; i++) {
                System.out.println("put image " + i);
                File image = new File("./example/err-" + i + ".png");
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);
//                PDPage page = document.getPage(i);
                PDImageXObject pdImage = PDImageXObject.createFromFileByContent(image, document);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                float height = page.getMediaBox().getHeight();
                float width = page.getMediaBox().getWidth();
                float imageHeight = pdImage.getHeight();
                float imageWight = pdImage.getWidth();
//                contentStream.drawImage(pdImage, 0, 0, imageWidth, imageHeight);
                if (imageHeight > imageWight) {
                    float v = imageHeight / height;
                    float h = imageWight / width;

                    if (v > h) {
                        float actWidth = width * (imageHeight / imageWight);
                        contentStream.drawImage(pdImage, (width - actWidth) / 2, 0, actWidth, height);
                    } else {
                        float actHeight = height * (imageWight / imageHeight);
                        contentStream.drawImage(pdImage, 0, (height - actHeight) / 2, width, actHeight);
                    }
                } else {
                    float actHeight = width * (imageHeight / imageWight);
                    contentStream.drawImage(pdImage, 0, (height - actHeight) / 2, width, actHeight);
                }
                contentStream.setHorizontalScaling(200);
                contentStream.close();
            }
            document.save("err.pdf");
        }
    }

    public static void main(String[] args) throws IOException {
//        pdf2imageWithBox3("err1");
        pdf2image("err0");
//        image2pdf();
    }
}
