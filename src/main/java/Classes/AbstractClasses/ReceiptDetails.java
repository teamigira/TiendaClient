package Classes.AbstractClasses;

import java.util.Date;

public class ReceiptDetails {
    private int receiptId;
    private int salesId;
    private String companyName;
    private String footerMessage;
    private String address;
    private String createdBy;
    private String soldTo;
    private Date salesDate;
    private Date orderDate;
    private String createdDate;
    private String barcode;

    public ReceiptDetails(int receiptId, int salesId, String companyName, String footerMessage, String address,
                          String createdBy, String soldTo, Date salesDate, Date orderDate, String createdDate, String barcode) {
        this.receiptId = receiptId;
        this.salesId = salesId;
        this.companyName = companyName;
        this.footerMessage = footerMessage;
        this.address = address;
        this.createdBy = createdBy;
        this.soldTo = soldTo;
        this.salesDate = salesDate;
        this.orderDate = orderDate;
        this.createdDate = createdDate;
        this.barcode = barcode;
    }

    // Getters and setters
    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getSalesId() {
        return salesId;
    }

    public void setSalesId(int salesId) {
        this.salesId = salesId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFooterMessage() {
        return footerMessage;
    }

    public void setFooterMessage(String footerMessage) {
        this.footerMessage = footerMessage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(String soldTo) {
        this.soldTo = soldTo;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "ReceiptDetails{" +
                "receiptId=" + receiptId +
                ", salesId=" + salesId +
                ", companyName='" + companyName + '\'' +
                ", footerMessage='" + footerMessage + '\'' +
                ", address='" + address + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", soldTo='" + soldTo + '\'' +
                ", salesDate=" + salesDate +
                ", orderDate=" + orderDate +
                ", createdDate=" + createdDate +
                ", barcode='" + barcode + '\'' +
                '}';
    }
}
