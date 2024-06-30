package com.rumpel.rumpelandroid.textanalysis;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.function.Consumer;

public enum TextAnalyzer {
    ;
//   private static final TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
//
//    public static void extractText(final InputImage image, final Consumer<String> consumer) {
//        recognizer.process(image).addOnSuccessListener(txt -> consumer.accept(txt.getText()))
//                .addOnFailureListener(runnable -> consumer.accept("Error detecting text"));
//    }
//
//    public static String fixSuspiciousOes(String input) {
//        return input.replaceAll("O\\.", "0.");
//
//    }

}
