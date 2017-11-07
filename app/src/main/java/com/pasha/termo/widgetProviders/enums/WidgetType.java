package com.pasha.termo.widgetProviders.enums;

import com.pasha.termo.R;

public enum WidgetType {
    Unknown(0),
    Complex(1),
    Simple(2),
    Wide4x1(3);

    private int value;

    WidgetType(int value) {
            this.value = value;
    }

    int toInt() {
        return value;
    }

    static WidgetType fromInt(int value) {
        WidgetType type = Unknown;
        switch (value) {
            case 0:
                type = Unknown;
                break;
            case 1:
                type = Complex;
                break;
            case 2:
                type = Simple;
                break;
            case 3:
                type = Wide4x1;
                break;
        }
        return type;
    }

    public static WidgetType fromString(String widgetName) {
        WidgetType type = Unknown;
        if (widgetName.contains("Complex")) {
            type = WidgetType.Complex;
        } else if (widgetName.contains("Simple")) {
            type = WidgetType.Simple;
        } else if (widgetName.contains("4x1")) {
            type = WidgetType.Wide4x1;
        }
        return type;
    }

    public int getLayoutId() {
        int layoutId = R.layout.widget_complex;
        switch (fromInt(value)) {
            case Complex:
                layoutId = R.layout.widget_complex;
                break;
            case Simple:
                layoutId = R.layout.widget_simple;
                break;
            case Wide4x1:
                layoutId = R.layout.widget_4x1;
                break;
        }
        return layoutId;
    }

    public int getViewId() {
        int layoutId = R.id.layComplex;
        switch (fromInt(value)) {
            case Complex:
                layoutId = R.id.layComplex;
                break;
            case Simple:
                layoutId = R.id.laySimple;
                break;
            case Wide4x1:
                layoutId = R.id.lay4x1;
                break;
        }
        return layoutId;
    }

    public boolean isSecontTempVisible() {
        return ((this == Complex) || (this == Wide4x1));
    }

    public boolean isTimeSingleLine() {
        return false;
    }

    public boolean isRefreshByButton() {
        return ((this == Complex) || (this == Wide4x1));
    }

    public boolean isTimeVisible() {
        return (this == Wide4x1);
    }

    public boolean isGraphVisible() {
        return ((this == Complex) || (this == Wide4x1));
    }

    public boolean isSimple() {
        return (this == Simple);
    }
}
