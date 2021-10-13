package main.java.dto;

public class Provider {

    private String providerName;
    private String packageSize;
    private double price;

    public Provider(String providerName, String packageSize, double price) {
        this.providerName = providerName;
        this.packageSize = packageSize;
        this.price = price;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
