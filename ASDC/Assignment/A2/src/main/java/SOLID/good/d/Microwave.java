package SOLID.good.d;

public class Microwave {

    private HeatingElement heatingElement;
    private Display display;

    public Microwave(HeatingElement heatingElement, Display display) {
        this.heatingElement = heatingElement;
        this.display = display;
    }

    public void startHeating() {
        display.setTimer(120);
        heatingElement.turnOn();
        heatingElement.heatUp();
        display.displayStatus();
    }

    public void stopHeating() {
        heatingElement.coolDown();
        heatingElement.turnOff();
        display.displayStatus();
    }

    public void setTimer(int seconds) {
        display.setTimer(seconds);
    }

    public void setTemperature(int temperature) {
        heatingElement.setTemperature(temperature);
        display.displayStatus();
    }

}
