package com.example.pouyakarimi.testappone.utils;

import android.content.Intent;
import android.net.Uri;

import com.example.pouyakarimi.testappone.objects.Note;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pouyakarimi on 11/2/15.
 */
public class EmailUtil {

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static Intent sendEmail(ArrayList<Note> bodyPlus, ArrayList<Note> bodyyDelta) {
        Intent intent = new Intent();


        intent.setData(Uri.parse("mailto:"));
        String[] to = {"pouya06@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Plus & Delta");
        intent.putExtra(Intent.EXTRA_TEXT, concatBody(bodyPlus, bodyyDelta));
        intent.setType("message/rfc822");
        return Intent.createChooser(intent, "Send Email");
    }

    public static String concatBody(ArrayList<Note> bodyPlus, ArrayList<Note> bodyyDelta) {
        String result = null;
        if (!bodyPlus.isEmpty()) {
            result = "+Pluses+\n\n";
            for (Note plus : bodyPlus) {
                result += "+  " + plus.getText() + ".\n";
            }
        }
        if (!bodyyDelta.isEmpty()) {
            result += "\n\n\n\n";
            result += "∆Deltas∆\n\n";
            for (Note delta : bodyyDelta) {
                result += "∆  " + delta.getText() + ".\n";
            }
        }
        return result;
    }
}
