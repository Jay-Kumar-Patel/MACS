package SOLID.good.d;

public class DisplayImpl implements Display{

    private int seconds;
    private HeatingElement heatingElement;

    public DisplayImpl(HeatingElement heatingElement) {
        this.seconds = 0;
        this.heatingElement = heatingElement;
    }

    @Override
    public void displayStatus() {
        System.out.println("Display: Current temperature is " + heatingElement.getTemperature() + " degrees.");
        if (heatingElement.getTemperature() > 550) {
            System.out.println("Display: Warning! Temperature is high.");
        }
        if (seconds > 0) {
            System.out.println("Display: Timer remaining: " + seconds + " seconds.");
        }
    }

    @Override
    public void setTimer(int seconds) {
        this.seconds = seconds;
        System.out.println("Display: Timer set to " + seconds + " seconds.");
    }

    @Override
    public void addSeconds(int seconds) {
        this.seconds += seconds;
        System.out.println("Display: Added " + seconds + " seconds to the timer. New timer: " + this.seconds + " seconds.");
    }

    @Override
    public void clearTimer() {
        this.seconds = 0;
        System.out.println("Display: Timer cleared.");
    }
}
