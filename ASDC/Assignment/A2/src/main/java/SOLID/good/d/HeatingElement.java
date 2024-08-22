package SOLID.good.d;

public interface HeatingElement {

    void heatUp();
    void coolDown();
    void setTemperature(int temperature);
    int getTemperature();
    void turnOn();
    void turnOff();
    void startCooling();
    void stopCooling();

}
