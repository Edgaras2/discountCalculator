package main.java.service;

import main.java.dto.Provider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CalcService {

    private static List<Provider> providers = new ArrayList<>();

    static {
        try {
            URL resource = CalcService.class.getClassLoader().getResource("providers.txt");
            Path path = Paths.get(resource.toURI());
            // todo handle nullpointer

            BufferedReader br = new BufferedReader(new FileReader(path.toFile()));

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");

                String providerName = parts[0].trim();
                String packageSize = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());

                providers.add(new Provider(providerName, packageSize, price));
            }
            // TODO handle exceptions
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void calculation() throws URISyntaxException, IOException {
        try {
            URL resource = CalcService.class.getClassLoader().getResource("input.txt");
            Path path = Paths.get(resource.toURI());
            // todo handle nullpointer

            BufferedReader br = new BufferedReader(new FileReader(path.toFile()));

            double lowestShippingPrice = getLowestSmallPackageShippingPrice(providers, "S");
            String line;
            int monthCounter = 0;
            double dinscountPerMonth = 10;


            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");

                // TODO HANDLE BAD LENGHT
                if (parts.length < 3) {
                    System.out.println("asd");
                    continue;
                }

                String date = parts[0].trim();
                String packageSize = parts[1].trim();
                String providerName = parts[2].trim();

                double reducedShipmentPrice = 0;
                double shipmentDiscount = 0;

                for (Provider provider : providers) {
                    if (provider.getProviderName().equals(providerName) &&
                            provider.getPackageSize().equals(packageSize)) {

                        if (provider.getPackageSize().equals("S")) {
                            shipmentDiscount = provider.getPrice() - lowestShippingPrice;
                            reducedShipmentPrice = lowestShippingPrice;
                        } else {
                            reducedShipmentPrice = provider.getPrice();

                        }
                    }
                }


                if ("LP".equalsIgnoreCase(providerName) && "L".equalsIgnoreCase(packageSize)) {
                    monthCounter++;
                    if (monthCounter == 3) {
                        shipmentDiscount = reducedShipmentPrice;
                        reducedShipmentPrice = 0;

                    }
                }

                String discountMessage = " " + shipmentDiscount;


                // Todo calc discount

                double v = Math.round((dinscountPerMonth - shipmentDiscount) * 100.0) / 100.0;
                if (v < 0.0) {
//                    System.out.println(dinscountPerMonth);
                    reducedShipmentPrice = reducedShipmentPrice - dinscountPerMonth;
                } else {
                    dinscountPerMonth = Math.round((dinscountPerMonth - shipmentDiscount) * 100.0 ) / 100.0;
//                    System.out.println(dinscountPerMonth);
                }

                if (shipmentDiscount == 0.0) {
                    discountMessage = "-";
                }


                System.out.println(date + " " + packageSize + " " + providerName + " " + reducedShipmentPrice + " " + discountMessage);

            }
            // TODO handle exceptions
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

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
