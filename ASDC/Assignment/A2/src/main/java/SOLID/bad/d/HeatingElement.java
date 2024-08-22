package SOLID.bad.d;

public class HeatingElement {

    private int temperature;
    private boolean isOn;
    private boolean isCooling;

    public HeatingElement() {
        this.temperature = 0;
        this.isOn = false;
        this.isCooling = false;
    }

    public void heatUp() {
        if (!isOn) {
            System.out.println("Heating element is not turned on. Cannot heat up.");
            return;
        }

        System.out.println("Heating element is heating up.");
        this.temperature += 10;
        System.out.println("Current temperature: " + temperature);
    }

    public void coolDown() {
        if (!isOn || !isCooling) {
            System.out.println("Heating element is not cooling down or is turned off.");
            return;
        }

        System.out.println("Heating element is cooling down.");
        this.temperature -= 5;
        System.out.println("Current temperature: " + temperature);
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        System.out.println("Heating element temperature set to " + temperature + " degrees.");
    }

    public int getTemperature() {
        return this.temperature;
    }

    public void turnOn() {
        if (isOn) {
            System.out.println("Heating element is already turned on.");
            return;
        }

        isOn = true;
        System.out.println("Heating element turned on.");
    }

    public void turnOff() {
        if (!isOn) {
            System.out.println("Heating element is already turned off.");
            return;
        }

        isOn = false;
        System.out.println("Heating element turned off.");
    }

    public void startCooling() {
        if (!isOn) {
            System.out.println("Cannot start cooling. Heating element is turned off.");
            return;
        }

        isCooling = true;
        System.out.println("Heating element cooling started.");
    }

    public void stopCooling() {
        if (!isCooling) {
            System.out.println("Heating element is not cooling. No action taken.");
            return;
        }

        isCooling = false;
        System.out.println("Heating element cooling stopped.");
    }
}