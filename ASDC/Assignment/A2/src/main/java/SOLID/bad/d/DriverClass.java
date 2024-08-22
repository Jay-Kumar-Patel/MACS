package SOLID.bad.d;

public class DriverClass {

    public static void main(String[] args) {

        HeatingElement toasterHeatingElement = new HeatingElement();
        HeatingElement microwaveHeatingElement = new HeatingElement();

        Display toasterDisplay = new Display(toasterHeatingElement);
        Display microwaveDisplay = new Display(microwaveHeatingElement);

        Toaster toaster = new Toaster(toasterHeatingElement, toasterDisplay);
        Microwave microwave = new Microwave(microwaveHeatingElement, microwaveDisplay);

        toaster.setTemperature(200);
        toaster.setTimer(30);
        toaster.addSeconds(5);
        toaster.startToasting();
        toaster.stopToasting();

        microwave.setTemperature(150);
        microwave.setTimer(60);
        microwave.startHeating();
        microwave.stopHeating();
    }

}
