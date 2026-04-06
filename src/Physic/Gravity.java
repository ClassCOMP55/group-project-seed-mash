package Physic;

public class Gravity {
	public static double GRAVITY = 0.5;
	public static double TERMINAL_VELOCITY = 15.0;
	public static double JUMP_VELOCITY = -12.0;
	
	private double velocityX;
	private double velocityY;
	
	public Gravity() {
		velocityX = 0;
		velocityY = 0;
	}
	
	public void applyGravity() {
		velocityY += GRAVITY;
		if(velocityY > TERMINAL_VELOCITY) {
			velocityY = TERMINAL_VELOCITY;
		}
	}
	
	public void JUMP() {
		velocityY = JUMP_VELOCITY;
	}
	
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public void setVelocityX(double vx) { velocityX = vx; }
    public void setVelocityY(double vy) { velocityY = vy; }
}
