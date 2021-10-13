package main.java.utils;

import main.java.dto.Provider;
import main.java.service.ShipmentDiscountService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProvidersUtils {

    public static List<Provider> providers = new ArrayList<>();
    private static final String PROVIDERS_TXT = "providers.txt";

    static {
        try {
            URL resource = ShipmentDiscountService.class.getClassLoader().getResource(PROVIDERS_TXT);
            Path path = Paths.get(resource.toURI());

            BufferedReader br = new BufferedReader(new FileReader(path.toFile()));

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");

                String providerName = parts[0].trim();
                String packageSize = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());

                providers.add(new Provider(providerName, packageSize, price));
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(PROVIDERS_TXT + " file not found", e);
        }
    }
}
