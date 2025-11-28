package org.wmb.gui.component.text;

public final class FloatField extends TextField {

    private float value;

    public FloatField() {
        super();
        setValue(0.0f);
    }

    public void setValue(float value) {
        this.value = value;
        setText(String.format("%.2f", value));
    }

    public float getValue() {
        return this.value;
    }

    @Override
    public void onLooseFocus() {
        super.onLooseFocus();

        float newValue = this.value;
        try {
            newValue = Float.parseFloat(getText());
        } catch(NumberFormatException exception) {

        }

        setValue(newValue);
    }
}
