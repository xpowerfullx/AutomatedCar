package hu.oe.nik.szfmv17t.automatedcar.powertrainsystem;

import hu.oe.nik.szfmv17t.automatedcar.SystemComponent;
import hu.oe.nik.szfmv17t.automatedcar.bus.Signal;
import hu.oe.nik.szfmv17t.physics.SpeedControl;

public class PowertrainSystem extends SystemComponent {

	// signal id table for PowertrainSystem
	public static final int DEMO = 0;
	public static final int SMI_BrakePedal = 10;
	public static final int SMI_Gaspedal = 11;
	public static final int SMI_Gear =  12;
	public static final int SMI_SteeringWheel =  13;
	public static final int Modelling = 20;
	public static final int Physics = 30;
	public static final int Physics_Speed =31;
	public static final int Physics_Gear = 32;
	public static final int Visualisation = 40;

	// physics
	private SpeedControl speedControl;

	// input signals
	private int gasPedal = 0;
	private int brakePedal = 0;

	// Output signals
	// Only these are available trough getters
	private int x = 0;
	private int y = 0;
	private double wheelAngle = 0;

	public PowertrainSystem(int x, int y, double carWeight) {
		super();
		this.x = x;
		this.y = y;
		this.speedControl = new SpeedControl(carWeight);
	}

	@Override
	public void loop() {
		//TODO write this
	}

	@Override
	public void receiveSignal(Signal s) {
		switch(s.getId()) {
		// Handle demo signal
		case DEMO:
			x += (int) s.getData();
			break;
		case SMI_BrakePedal:
			brakePedal = (int) s.getData();
			this.speedControl.setBrakePedal(brakePedal);
			break;
		case SMI_Gaspedal:
			int gasPedal = (int) s.getData();
			this.speedControl.setGasPedal(gasPedal);
			break;
		case SMI_Gear:
			int gear = (int) s.getData();
			this.speedControl.setGearShift(gear);
			break;
		default:
			// ignore other signals
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public double getWheelAngle() {
		return wheelAngle;
	}

	public int getGasPedal() {
		return gasPedal;
	}

	public double getVelocity() {
		return speedControl.calculateVelocity();
	}
}
