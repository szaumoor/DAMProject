package com.rumpel.rumpelandroid.utils.textanalysis;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.function.Consumer;

/**
 * Utility class to extract text from an image with Google's MLKit. Not yet implemented fully.
 */
public final class TextAnalysis {
    private TextAnalysis() {
        throw new AssertionError("Utility class");
    }

    private static final TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

    /**
     * Extracts text from an input image
     * @param image the image to extract text from
     * @param onSuccess the function to call when the text is extracted
     * @param onFailure the function to call when an error occurs
     */
    public static void extractText(final InputImage image, final Consumer<Text> onSuccess, final Runnable onFailure) {
        recognizer.process(image)
                .addOnSuccessListener(onSuccess::accept)
                .addOnFailureListener(runnable -> onFailure.run());
    }

//    public static String fixSuspiciousOes(final String input) {
//        return input.replaceAll("O\\.", "0.");
//    }
}
