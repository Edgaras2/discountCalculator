package main.java.service;

import main.java.dto.Provider;
import main.java.exceptions.IoExecpeasdasd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static main.java.constants.ProviderConstants.*;
import static main.java.utils.ProvidersUtils.providers;

public class ShipmentDiscountService {

    private static final String IGNORED = "Ignored";
    private static final int SHIPMENT_DISCOUNT_ALLOWANCE = 10;
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String INPUT_TXT = "input.txt";

    public void calculateShipmentDiscount() {
        try {
            URL resource = ShipmentDiscountService.class.getClassLoader().getResource(INPUT_TXT);
            Path path = Paths.get(resource.toURI());
            // todo handle nullpointer

            BufferedReader br = new BufferedReader(new FileReader(path.toFile()));

            double lowestShippingPrice = getLowestSmallPackageShippingPrice(providers, PACKAGE_SIZE_S);
            String line;
            int monthCounter = 0;
            double shipmentDiscountAllowance = 10;
            Month lastMonth = null;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");

                if (isTransactionInvalid(parts)) {
                    continue;
                }

                String date = parts[0].trim();
                String packageSize = parts[1].trim();
                String providerName = parts[2].trim();

                double reducedShipmentPrice = 0;
                double shipmentDiscount = 0;

                Month currentMonth = LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN)).getMonth();
                if (lastMonth == null) {
                    lastMonth = currentMonth;
                }

                if (!lastMonth.equals(currentMonth)) {
                    shipmentDiscountAllowance = SHIPMENT_DISCOUNT_ALLOWANCE;
                    lastMonth = currentMonth;
                    monthCounter = 0;
                }

                for (Provider provider : providers) {
                    if (provider.getProviderName().equalsIgnoreCase(providerName) &&
                            provider.getPackageSize().equalsIgnoreCase(packageSize)) {

                        if (provider.getPackageSize().equals(PACKAGE_SIZE_S)) {
                            double tempShipmentDiscount = provider.getPrice() - lowestShippingPrice;

                            if (shipmentDiscountAllowance < tempShipmentDiscount) {
                                reducedShipmentPrice = provider.getPrice() - shipmentDiscountAllowance;
                                shipmentDiscount = shipmentDiscountAllowance;
                            } else {
                                shipmentDiscount = tempShipmentDiscount;
                                reducedShipmentPrice = lowestShippingPrice;
                            }
                        } else {
                            reducedShipmentPrice = provider.getPrice();
                        }
                    }
                }

                if (PROVIDER_NAME_LP.equalsIgnoreCase(providerName) && PACKAGE_SIZE_L.equalsIgnoreCase(packageSize)) {
                    monthCounter++;
                    if (monthCounter == 3) {
                        shipmentDiscount = reducedShipmentPrice;
                        reducedShipmentPrice = 0;
                    }
                }

                shipmentDiscountAllowance = calculateRemainingShipmentDiscountAllowance(shipmentDiscountAllowance,
                        shipmentDiscount);

                if (shipmentDiscount == 0.0) {
                    print(date, packageSize, providerName, reducedShipmentPrice, "-");
                } else {
                    print(date, packageSize, providerName, reducedShipmentPrice,
                            String.format("%.2f", shipmentDiscount));
                }

            }
            // TODO handle exceptions
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(INPUT_TXT + " file not found", e);
        }

    }

    private boolean isTransactionInvalid(String[] parts) {
        if (parts.length < 3) {
            String date = parts[0].trim();
            String packageSize = parts[1].trim();
            System.out.println(date + " " + packageSize + " " + IGNORED);
            return true;
        }
        return false;
    }

    private double calculateRemainingShipmentDiscountAllowance(double shipmentDiscountAllowance,
                                                               double shipmentDiscount) {
        return Math.round((shipmentDiscountAllowance - shipmentDiscount) * 100.0) / 100.0;
    }

    private void print(String date, String packageSize, String providerName, double reducedShipmentPrice,
                       String shipmentDiscount) {
        System.out.println(date + " " + packageSize + " " + providerName + " " +
                String.format("%.2f", reducedShipmentPrice) + " " + shipmentDiscount);
    }

    public double getLowestSmallPackageShippingPrice(List<Provider> providers, String packageSize) {
        double lowestSmallPackagePrice = Double.MAX_VALUE;
        for (Provider provider : providers) {
            Double price = provider.getPrice();
            if (packageSize.equalsIgnoreCase(provider.getPackageSize()) &&
                    Double.compare(price, lowestSmallPackagePrice) < 0) {
                lowestSmallPackagePrice = price;
            }
        }
        return lowestSmallPackagePrice;
    }
}
