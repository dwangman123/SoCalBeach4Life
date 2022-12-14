// Generated by view binder compiler. Do not edit!
package com.example.socalbeach4life.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.socalbeach4life.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityStartBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button loginButton;

  @NonNull
  public final EditText loginPasswordTextInput;

  @NonNull
  public final EditText loginUsernameTextInput;

  @NonNull
  public final Button registerButton;

  @NonNull
  public final EditText registerNameTextInput;

  @NonNull
  public final EditText registerPasswordTextInput;

  @NonNull
  public final EditText registerPhoneTextInput;

  @NonNull
  public final EditText registerUsernameTextInput;

  private ActivityStartBinding(@NonNull ConstraintLayout rootView, @NonNull Button loginButton,
      @NonNull EditText loginPasswordTextInput, @NonNull EditText loginUsernameTextInput,
      @NonNull Button registerButton, @NonNull EditText registerNameTextInput,
      @NonNull EditText registerPasswordTextInput, @NonNull EditText registerPhoneTextInput,
      @NonNull EditText registerUsernameTextInput) {
    this.rootView = rootView;
    this.loginButton = loginButton;
    this.loginPasswordTextInput = loginPasswordTextInput;
    this.loginUsernameTextInput = loginUsernameTextInput;
    this.registerButton = registerButton;
    this.registerNameTextInput = registerNameTextInput;
    this.registerPasswordTextInput = registerPasswordTextInput;
    this.registerPhoneTextInput = registerPhoneTextInput;
    this.registerUsernameTextInput = registerUsernameTextInput;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityStartBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityStartBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_start, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityStartBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.loginButton;
      Button loginButton = ViewBindings.findChildViewById(rootView, id);
      if (loginButton == null) {
        break missingId;
      }

      id = R.id.loginPasswordTextInput;
      EditText loginPasswordTextInput = ViewBindings.findChildViewById(rootView, id);
      if (loginPasswordTextInput == null) {
        break missingId;
      }

      id = R.id.loginUsernameTextInput;
      EditText loginUsernameTextInput = ViewBindings.findChildViewById(rootView, id);
      if (loginUsernameTextInput == null) {
        break missingId;
      }

      id = R.id.registerButton;
      Button registerButton = ViewBindings.findChildViewById(rootView, id);
      if (registerButton == null) {
        break missingId;
      }

      id = R.id.registerNameTextInput;
      EditText registerNameTextInput = ViewBindings.findChildViewById(rootView, id);
      if (registerNameTextInput == null) {
        break missingId;
      }

      id = R.id.registerPasswordTextInput;
      EditText registerPasswordTextInput = ViewBindings.findChildViewById(rootView, id);
      if (registerPasswordTextInput == null) {
        break missingId;
      }

      id = R.id.registerPhoneTextInput;
      EditText registerPhoneTextInput = ViewBindings.findChildViewById(rootView, id);
      if (registerPhoneTextInput == null) {
        break missingId;
      }

      id = R.id.registerUsernameTextInput;
      EditText registerUsernameTextInput = ViewBindings.findChildViewById(rootView, id);
      if (registerUsernameTextInput == null) {
        break missingId;
      }

      return new ActivityStartBinding((ConstraintLayout) rootView, loginButton,
          loginPasswordTextInput, loginUsernameTextInput, registerButton, registerNameTextInput,
          registerPasswordTextInput, registerPhoneTextInput, registerUsernameTextInput);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
