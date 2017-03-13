package hu.oe.nik.szfmv17t.automatedcar.hmi;

/**
 * Created by SebestyenMiklos on 2017. 03. 05..
 */
public class SteeringWheel {
    private int state;
    private HmiTimer timer;
    private int steeringStep = 5;
    public static int maxLeft = -100;
    public static int maxRight = 100;

    public SteeringWheel() {
        this.state = 0;
        this.timer = new HmiTimer();
    }

    public void steerLeft() {
        if(state >= maxLeft + steeringStep) {
            state -= steeringStep;
        }
    }

    public void steerRight() {
        if(state <= maxRight - steeringStep) {
            state += steeringStep;
        }
    }

    public void steerRelease() {
        if(isSteeringWheelLeftToCenter()){
            wheelToCenterFromLeft();
        }else if(isSteeringWheelRightToCenter()) {
            wheelToCenterFromRight();
        }
    }

    private void wheelToCenterFromLeft() {
        while(!isSteeringWheelCentered()){
            steerRight();
        }
    }
    private void wheelToCenterFromRight() {
        while(!isSteeringWheelCentered()){
            steerLeft();
        }
    }

    public void quickLeft() {
        while(state != maxLeft){
            state -= steeringStep;
        }
    }

    public void quickRight() {
        while(state != maxRight){
            state += steeringStep;
        }
    }

    public int getState(){
        return this.state;
    }

    public boolean isSteeringWheelCentered() {
        if(this.state == 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean isSteeringWheelLeftToCenter() {
        if(this.state < 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean isSteeringWheelRightToCenter() {
        if(this.state > 0){
            return true;
        }else {
            return false;
        }
    }
    public void start() {
        timer.Start();
    }


}