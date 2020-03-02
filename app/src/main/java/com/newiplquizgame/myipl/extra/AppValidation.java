package com.newiplquizgame.myipl.extra;

import android.content.Context;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.newiplquizgame.myipl.activity.RegistrationActivity;

import static com.newiplquizgame.myipl.extra.Common.isValidEmail;

public class AppValidation {


    public static boolean validateEmail(String email, TextInputLayout ti_email_id) {
        if (email.isEmpty()) {
            ti_email_id.setError("Field can't be empty");
            return false;
        } else if (!isValidEmail(email.trim())) {
            ti_email_id.setError("Please enter a valid email address");
            return false;
        } else {
            ti_email_id.setError(null);
            return true;
        }
    }

    public static boolean validateName(String name, TextInputLayout ti_name) {
        if (name.isEmpty()) {
            ti_name.setError("Field can't be empty");
            return false;
        } else {
            ti_name.setError(null);
            return true;
        }
    }
    public static boolean edvalidateName(String name, EditText ti_name) {
        if (name.isEmpty()) {
            ti_name.setError("Field can't be empty");
            return false;
        } else {
            ti_name.setError(null);
            return true;
        }
    }

    public static boolean validateNickName(String name, TextInputLayout ti_nick_name) {
        if (name.trim().isEmpty()) {
            ti_nick_name.setError("Field can't be empty");
            return false;
        } else {
            ti_nick_name.setError(null);
            return true;
        }
    }
    public static boolean validateImage(String name, Context context) {
        if (name.equals("")) {
            Common.displayToastMessageShort(context, "Please select image.", true);
            return false;
        } else {
            return true;
        }
    }


    public static boolean validatePassword(String password, TextInputLayout ti_password) {
        if (password.isEmpty()) {
            ti_password.setError("Field can't be empty");
            return false;
        } else {
            ti_password.setError(null);
            return true;
        }
    }
    public static boolean edvalidatePassword(String password, EditText ti_password) {
        if (password.isEmpty()) {
            ti_password.setError("Field can't be empty");
            return false;
        } else {
            ti_password.setError(null);
            return true;
        }
    }

    public static boolean edvalidateMatchingPassword(String co_password, EditText ti_con_password, String password) {
        if (co_password.isEmpty()) {
            ti_con_password.setError("Field can't be empty");
            return false;
        } else if (!co_password.equals(password)) {
            ti_con_password.setError("Password Not matching");
            return false;
        } else {
            ti_con_password.setError(null);
            return true;
        }
    }
    public static boolean validateMatchingPassword(String co_password, TextInputLayout ti_con_password, String password) {
        if (co_password.isEmpty()) {
            ti_con_password.setError("Field can't be empty");
            return false;
        } else if (!co_password.equals(password)) {
            ti_con_password.setError("Password Not matching");
            return false;
        } else {
            ti_con_password.setError(null);
            return true;
        }
    }
}
