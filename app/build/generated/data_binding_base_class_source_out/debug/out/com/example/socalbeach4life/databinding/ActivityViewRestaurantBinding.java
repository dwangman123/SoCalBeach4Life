// Generated by view binder compiler. Do not edit!
package com.example.socalbeach4life.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.socalbeach4life.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityViewRestaurantBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button backToMap;

  @NonNull
  public final GridLayout trips;

  private ActivityViewRestaurantBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button backToMap, @NonNull GridLayout trips) {
    this.rootView = rootView;
    this.backToMap = backToMap;
    this.trips = trips;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityViewRestaurantBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityViewRestaurantBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_view_restaurant, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityViewRestaurantBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backToMap;
      Button backToMap = ViewBindings.findChildViewById(rootView, id);
      if (backToMap == null) {
        break missingId;
      }

      id = R.id.trips;
      GridLayout trips = ViewBindings.findChildViewById(rootView, id);
      if (trips == null) {
        break missingId;
      }

      return new ActivityViewRestaurantBinding((ConstraintLayout) rootView, backToMap, trips);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}