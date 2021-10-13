import main.java.service.ShipmentDiscountService;

public class MainClass {

    public static void main(String[] args) {
        ShipmentDiscountService shipmentDiscountService = new ShipmentDiscountService();
        shipmentDiscountService.calculateShipmentDiscount();
    }
}
