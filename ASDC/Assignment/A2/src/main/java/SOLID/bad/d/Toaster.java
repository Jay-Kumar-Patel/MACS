package SOLID.bad.d;

public class Toaster {

    private HeatingElement heatingElement;
    private Display display;

    public Toaster(HeatingElement heatingElement, Display display) {
        this.heatingElement = heatingElement;
        this.display = display;
    }

    public void startToasting() {
        display.clearTimer();
        display.setTimer(60);
        heatingElement.turnOn();
        heatingElement.heatUp();
        heatingElement.setTemperature(400);
        display.displayStatus();
    }

    public void stopToasting() {
        heatingElement.coolDown();
        heatingElement.turnOff();
        heatingElement.setTemperature(0);
        display.displayStatus();
    }

    public void setTimer(int seconds) {
        display.setTimer(seconds);
    }

    public void addSeconds(int seconds){
        display.addSeconds(seconds);
    }

    public void setTemperature(int temperature) {
        heatingElement.setTemperature(temperature);
        display.displayStatus();
    }

}
