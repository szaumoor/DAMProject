package com.rumpel.rumpeldesktop.fxutils;

import com.rumpel.rumpeldesktop.mvc.views.utils.Dimension;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Utility class to handle IO operations in this app. Currently, this only includes exporting PDF and PNG files.
 */
public final class IO {
    /**
     * Private constructor to prevent instantiation
     */
    private IO() {
        throw new AssertionError("Utility class");
    }

    /**
     * Generates a writable image of the provided JavaFX Pane.
     *
     * @param mainPane the JavaFX Pane from which to generate the image
     * @return the generated writable image
     */
    private static WritableImage getWritableImage(final Pane mainPane) {
        var img = new WritableImage((int) mainPane.getWidth(), (int) mainPane.getHeight());
        return mainPane.snapshot(null, img);
    }

    /**
     * Scales the dimensions of an object based on the maximum width and height provided.
     *
     * @param width     the original width of the object
     * @param height    the original height of the object
     * @param maxWidth  the maximum width allowed for the scaled object
     * @param maxHeight the maximum height allowed for the scaled object
     * @return the scaled dimensions of the object as a Dimension object
     */
    private static Dimension scaleDimensions(final double width, final double height, final double maxWidth, final double maxHeight) {
        double scale = Math.min(maxWidth / width, maxHeight / height);
        return new Dimension(new Dimension.Width((width * scale)), new Dimension.Height(height * scale));
    }

    /**
     * Extracts an image from the specified file and saves it as a PNG file.
     *
     * @param file     the file from which to extract the image
     * @param mainPane the JavaFX pane containing the image to extract
     * @throws IOException if an error occurs during the image extraction or saving process
     */
    public static void extractImage(final File file, final Pane mainPane) throws IOException {
        var snapshot = getWritableImage(mainPane);
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
    }

    /**
     * Extracts a PDF from the given file and captures a specific pane.
     *
     * @param file          the file from which to extract the PDF
     * @param paneToCapture the pane to capture in the PDF
     * @throws IOException if an I/O error occurs while extracting the PDF
     */
    public static void extractPdf(final File file, final Pane paneToCapture) throws IOException {
        var document = new PDDocument();
        var a4 = PDRectangle.A4;
        var page = new PDPage(a4);
        document.addPage(page);
        WritableImage writableImage = getWritableImage(paneToCapture);
        var pdImage = LosslessFactory.createFromImage(document, SwingFXUtils.fromFXImage(writableImage, null));
        var contentStream = new PDPageContentStream(document, page);
        var scaledDimensions = scaleDimensions(writableImage.getWidth(), writableImage.getHeight(), a4.getWidth(), a4.getHeight());
        contentStream.drawImage(pdImage, 0.0F, (float) (a4.getHeight() - scaledDimensions.height().value() - 50), (float) scaledDimensions.width().value(), (float) scaledDimensions.height().value());
        contentStream.close();
        document.save(file);
        document.close();
    }
}
