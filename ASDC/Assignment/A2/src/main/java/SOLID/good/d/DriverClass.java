package SOLID.good.d;

public class DriverClass {

    public static void main(String[] args) {

        HeatingElement toasterHeatingElement = new HeatingElementImpl();
        HeatingElement microwaveHeatingElement = new HeatingElementImpl();

        Display toasterDisplay = new DisplayImpl(toasterHeatingElement);
        Display microwaveDisplay = new DisplayImpl(microwaveHeatingElement);

        Toaster toaster = new Toaster(toasterHeatingElement, toasterDisplay);
        Microwave microwave = new Microwave(microwaveHeatingElement, microwaveDisplay);

        toaster.setTemperature(200);
        toaster.setTimer(30);
        toaster.startToasting();
        toaster.stopToasting();

        microwave.setTemperature(150);
        microwave.setTimer(60);
        microwave.startHeating();
        microwave.stopHeating();

    }

}
