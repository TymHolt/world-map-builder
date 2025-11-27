package org.wmb.gui.component.text;

public final class FloatField extends TextField {

    private float value;

    public FloatField() {
        this.value = 0.0f;
    }

    public void setValue(float value) {
        this.value = value;
        setText(Float.toString(value));
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

        this.value = newValue;
        setText(String.format("%.2f", newValue));
    }
}
