/*
 * (c) Kitodo. Key to digital objects e. V. <contact@kitodo.org>
 *
 * This file is part of the Kitodo project.
 *
 * It is licensed under GNU General Public License version 3 or later.
 *
 * For the full copyright and license information, please read the
 * LICENSE file that was distributed with this source code.
 */

package org.kitodo.mediaserver.cli.commands;

import com.mortennobel.imagescaling.MultiStepRescaleOp;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.kitodo.mediaserver.cli.converter.TimespanConverter;
import org.kitodo.mediaserver.core.config.FileserverProperties;
import org.kitodo.mediaserver.core.util.FileDeleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Clears derivative files cache.
 */
@Command
@Component
public class PdfJpgTestCommand implements Callable, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfJpgTestCommand.class);

    private void createPdfFromTiffs() throws Exception {

        String masterDir = "/mnt/1tb/kitodo-metadata/2050/images/EiffDer_9921753682102884_media";

        MemoryUsageSetting memoryUsageSetting = MemoryUsageSetting
            .setupMixed(1024 * 1024 * 1024)
            .setTempDir(new File("/tmp"));

        PDDocument document = new PDDocument(memoryUsageSetting);

        for (Path file : Files.newDirectoryStream(Paths.get(masterDir), path -> path.toString().endsWith(".tif"))) {
            BufferedImage image = ImageIO.read(file.toFile());
            Dimension imageSize = new Dimension(image.getWidth(), image.getHeight());
            Dimension boundary = new Dimension(2500, 2500);
            Dimension newSize = getScaledDimension(imageSize, boundary);

            MultiStepRescaleOp rescaleOp = new MultiStepRescaleOp((int)newSize.getWidth(), (int)newSize.getHeight(),
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            BufferedImage resizedImage = rescaleOp.filter(image,
                new BufferedImage((int)newSize.getWidth(), (int)newSize.getHeight(), BufferedImage.TYPE_INT_RGB));

            PDPage page = new PDPage(new PDRectangle((float)newSize.getWidth(), (float)newSize.getHeight()));

            PDImageXObject imageObj = JPEGFactory.createFromImage(document, resizedImage);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(imageObj, 0, 0);

            contentStream.close();

            document.getPages().add(page);
        }

        document.save("/tmp/pdftest.pdf");
        document.close();
    }

    private void createPdfFromJpegs() throws Exception {

        String masterDir = "/mnt/1tb/kitodo-metadata/2050/images/EiffDer_9921753682102884_media";

        MemoryUsageSetting memoryUsageSetting = MemoryUsageSetting
            .setupMixed(1024 * 1024 * 1024)
            .setTempDir(new File("/tmp"));

        PDDocument document = new PDDocument(memoryUsageSetting);

        for (Path file : Files.newDirectoryStream(Paths.get(masterDir), path -> path.toString().endsWith(".jpeg"))) {

            Dimension newSize = getImageDimension(file.toFile());

            PDPage page = new PDPage(new PDRectangle((float)newSize.getWidth(), (float)newSize.getHeight()));

            PDImageXObject imageObj = JPEGFactory.createFromStream(document, new FileInputStream(file.toFile()));
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(imageObj, 0, 0);

            contentStream.close();

            document.getPages().add(page);
        }

        document.save("/tmp/pdftest.pdf");
        document.close();
    }

    private void createPdfFromPdfs() throws Exception {

        String masterDir = "/mnt/1tb/kitodo-metadata/2050/images/EiffDer_9921753682102884_media";

        MemoryUsageSetting memoryUsageSetting = MemoryUsageSetting
            .setupMixed(1024 * 1024 * 1024)
            .setTempDir(new File("/tmp"));

        PDDocument document = new PDDocument(memoryUsageSetting);

        List<PDDocument> documents = new ArrayList<>();

        for (Path file : Files.newDirectoryStream(Paths.get(masterDir), path -> path.toString().endsWith(".pdf"))) {
            PDDocument currentDocument = PDDocument.load(file.toFile());
            PDPage page = currentDocument.getPage(0);
            document.importPage(page);

            documents.add(currentDocument);
        }

        document.save("/tmp/pdftest.pdf");
        document.close();
    }

    private void createPdfsFromTiffs() throws Exception {
        String masterDir = "/mnt/1tb/kitodo-metadata/2050/images/EiffDer_9921753682102884_media";

        MemoryUsageSetting memoryUsageSetting = MemoryUsageSetting
            .setupMixed(1024 * 1024 * 1024)
            .setTempDir(new File("/tmp"));

        for (Path file : Files.newDirectoryStream(Paths.get(masterDir), path -> path.toString().endsWith(".tif"))) {
            PDDocument document = new PDDocument(memoryUsageSetting);

            BufferedImage image = ImageIO.read(file.toFile());
            Dimension imageSize = new Dimension(image.getWidth(), image.getHeight());
            Dimension boundary = new Dimension(2500, 2500);
            Dimension newSize = getScaledDimension(imageSize, boundary);

            MultiStepRescaleOp rescaleOp = new MultiStepRescaleOp((int)newSize.getWidth(), (int)newSize.getHeight(),
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            BufferedImage resizedImage = rescaleOp.filter(image,
                new BufferedImage((int)newSize.getWidth(), (int)newSize.getHeight(), BufferedImage.TYPE_INT_RGB));

            PDPage page = new PDPage(new PDRectangle((float)newSize.getWidth(), (float)newSize.getHeight()));

            PDImageXObject imageObj = JPEGFactory.createFromImage(document, resizedImage);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(imageObj, 0, 0);

            contentStream.close();

            document.getPages().add(page);

            String saveFilename = FilenameUtils.getBaseName(file.toString()) + ".pdf";
            document.save("/mnt/1tb/kitodo-metadata/2050/images/EiffDer_9921753682102884_media/" + saveFilename);
            document.close();
        }

    }

    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    private static Dimension getImageDimension(File imgFile) throws IOException {

        ImageInputStream stream = ImageIO.createImageInputStream(imgFile);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
        while(readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(stream);
            int width = reader.getWidth(reader.getMinIndex());
            int height = reader.getHeight(reader.getMinIndex());
            reader.dispose();
            return new Dimension(width, height);
        }

        throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
    }

    /**
     * Callable for on-demand CLI execution.
     * @return always null
     * @throws IOException on file access errors by FileDeleter
     */
    @Override
    public Object call() throws Exception {

//        Instant p1 = Instant.now();
//        createPdfFromJpeg();
//        Instant p2 = Instant.now();
//        System.out.println("From JPG: " + Duration.between(p1, p2));
//        createPdfFromTiff();
//        Instant p3 = Instant.now();
//        System.out.println("From TIF: " + Duration.between(p2, p3));

        //createPdfsFromTiff();

        Instant p1 = Instant.now();
        createPdfFromPdfs();
        Instant p2 = Instant.now();
        System.out.println("From PDF: " + Duration.between(p1, p2));

        return null;
    }

    /**
     * Runnable for execution in scheduled task.
     */
    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {
            LOGGER.error("Cache clearing failed: " + e, e);
        }
    }
}
