import main.java.service.CalcService;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainClass {

    public static void main(String[] args) throws IOException, URISyntaxException {

        CalcService calcService = new CalcService();
        calcService.calculation();


    }
}
