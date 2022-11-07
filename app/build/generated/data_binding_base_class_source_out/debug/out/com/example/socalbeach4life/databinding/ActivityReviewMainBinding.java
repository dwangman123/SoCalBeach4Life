// Generated by view binder compiler. Do not edit!
package com.example.socalbeach4life.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.socalbeach4life.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityReviewMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final LinearLayout reviewsScrollView;

  private ActivityReviewMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull LinearLayout reviewsScrollView) {
    this.rootView = rootView;
    this.reviewsScrollView = reviewsScrollView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityReviewMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityReviewMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_review_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityReviewMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.reviewsScrollView;
      LinearLayout reviewsScrollView = ViewBindings.findChildViewById(rootView, id);
      if (reviewsScrollView == null) {
        break missingId;
      }

      return new ActivityReviewMainBinding((ConstraintLayout) rootView, reviewsScrollView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
