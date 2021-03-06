package com.example.pouyakarimi.testappone.utils;

import android.content.Intent;
import android.net.Uri;

import com.example.pouyakarimi.testappone.objects.Note;
import com.example.pouyakarimi.testappone.objects.User;

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

    public static Intent sendEmail(ArrayList<Note> bodyPlus, ArrayList<Note> bodyyDelta, ArrayList<User> users) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mailto:"));
        String[] to = listOfUsers(users);
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "+ / ∆");
        intent.putExtra(Intent.EXTRA_TEXT, concatBody(bodyPlus, bodyyDelta));
        intent.setType("message/rfc822");
        return Intent.createChooser(intent, "Send Email");
    }

    private static String[] listOfUsers(ArrayList<User> users) {
        ArrayList<String> resultList = new ArrayList<>();
        if (!users.isEmpty()) {
            for (User user : users) {
                resultList.add(user.getEmail());
            }
        }

        String[] resultArr = new String[resultList.size()];
        resultArr = resultList.toArray(resultArr);

        return resultArr;
    }

    public static String concatBody(ArrayList<Note> bodyPlus, ArrayList<Note> bodyDelta) {
        String result = null;
        if (!bodyPlus.isEmpty()) {
            result = "PLUSES:\n";
            for (Note plus : bodyPlus) {
                result += "\t--  " + plus.getText() + ".\n";
            }
        }
        if (!bodyDelta.isEmpty()) {
            result += "\n\n\n";
            result += "DELTAS:\n";
            for (Note delta : bodyDelta) {
                result += "\t--  " + delta.getText() + ".\n";
            }
        }
        return result;
    }
}
