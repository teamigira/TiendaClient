package Classes.AbstractClasses;

import java.util.Objects;

public class ReceiptSettings {
    private int createdBy;
    private String printerName;
    private boolean automatically;
    private String template;

    // Constructors
    public ReceiptSettings() {
    }

    public ReceiptSettings(int createdBy, String printerName, boolean automatically, String template) {
        this.createdBy = createdBy;
        this.printerName = printerName;
        this.automatically = automatically;
        this.template = template;
    }

    // Getters and Setters
    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public boolean isAutomatically() {
        return automatically;
    }

    public void setAutomatically(boolean automatically) {
        this.automatically = automatically;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    // Override equals and hashCode for object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptSettings that = (ReceiptSettings) o;
        return createdBy == that.createdBy &&
                automatically == that.automatically &&
                Objects.equals(printerName, that.printerName) &&
                Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdBy, printerName, automatically, template);
    }

    // Override toString for debugging or logging
    @Override
    public String toString() {
        return "ReceiptSettings{" +
                "createdBy=" + createdBy +
                ", printerName='" + printerName + '\'' +
                ", automatically=" + automatically +
                ", template='" + template + '\'' +
                '}';
    }
}
