package com.imq.myhome.Objects;

import android.content.Context;
import android.util.AttributeSet;

public class EditTextPreference extends android.support.v7.preference.EditTextPreference {

    public EditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        super.onSetInitialValue(defaultValue);
        setSummary(this.getText());
    }

    @Override
    public CharSequence getSummary() {
        return this.getText();
    }
}