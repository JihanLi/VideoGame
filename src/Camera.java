/** 
 * 3D Video Game: Journey of a Dragon.
 * 
 * Author: Jihan Li
 * 
 * This is the class of camera. It controls the motion of the camera.
 * You can move the camera by either moving mouse or pressing keys.
 * The camera avoids collision with the terrain and skybox.
 * 
 * Reference: 
 * Class start codes
 * 
 */


package src;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.util.vector.Vector3f;


public class Camera {
    public float moveSpeed = 0.1f;
    private float speed;

    private float maxLook = 85;

    private float mouseSensitivity = 0.03f;
    
    private boolean flag = true;
    
    private float[][] altitude;
    private float skyHeight;

    private Vector3f pos;
    private Vector3f rotation;
    private float width, height;
    
    
    /** 
     * Initialize some values.
     */
    public void create(float w, float h, float[][] alt, float sky) 
    {
        pos = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        
        width = w;
        height = h;
        altitude = alt;
        skyHeight = sky;    
    }
    

    /** 
     * Apply the motion of the camera.
     */
    public void apply() 
    {
        if (rotation.y / 360 > 1) 
        {
            rotation.y -= 360;
        } 
        else if (rotation.y / 360 < -1) 
        {
            rotation.y += 360;
        }

        glRotatef(rotation.x, 1, 0, 0);
        glRotatef(rotation.y, 0, 1, 0);
        glRotatef(rotation.z, 0, 0, 1);
        glTranslatef(-pos.x, -pos.y, -pos.z);
    }
    

    /** 
     * Process the input.
     */
    public void acceptInput(float delta) {
    	if(Display.isActive())
    	{
    		if(flag)
    		{
		        acceptInputRotate(delta);
		        acceptInputMove(delta);
    		}
    		else
    			flag = true;
    	}
    }

    
    /** 
     * Process the mouse input.
     */
    public void acceptInputRotate(float delta) 
    {   
    	boolean keyFast = Keyboard.isKeyDown(Keyboard.KEY_Q);
        boolean keySlow = Keyboard.isKeyDown(Keyboard.KEY_E);
        
    	int mouseDX = Mouse.getDX();
    	int mouseDY = -Mouse.getDY();
    	int realX = (int) ((pos.z + altitude.length) * (width-1) / (2*altitude.length));
        int realZ = (int) ((pos.x + altitude[0].length) * (height-1) / (2*altitude[0].length));
        float alt;
        
        if(realX < 0 || realX >= altitude.length || realZ < 0 || realZ >= altitude[0].length)
        {
        	alt = skyHeight;
        }
        else
        {
        	alt = altitude[realX][realZ];
        }
        
        if (Mouse.isInsideWindow()) 
        {       	
        	if (keyFast) 
            {
                speed = moveSpeed * 5;
            } else if (keySlow) 
            {
                speed = moveSpeed / 2;
            } else {
                speed = moveSpeed;
            }
        	       	
            speed *= delta;
            if(Mouse.isButtonDown(0))
            {
                movePosX(speed, 0, 1, alt);
                movePosY(speed, 0, -1, alt);
                movePosZ(speed, 0, -1, alt);
            }
            else if(Mouse.isButtonDown(1))
            {
                movePosX(speed, 0, -1, alt);
                movePosY(speed, 0, 1, alt);
                movePosZ(speed, 0, 1, alt);
            }
            
            if(Math.abs(mouseDX) > 2 || Math.abs(mouseDY) > 2)
            {
	            if(rotation.x > 90)
	            {
	            	rotation.x = 90;
	            }
	            else if(rotation.x < -90)
	            {
	            	rotation.x = -90;
	            }
	        	else
	        	{
	        		rotation.x += mouseDY * mouseSensitivity * delta;
	        	}
	        	rotation.x = Math.max(-maxLook, Math.min(maxLook, rotation.x));
	        	rotation.y += mouseDX * mouseSensitivity * delta;
            }
        }
    }

    
    /** 
     * Process the keyboard input. 
     */
    public void acceptInputMove(float delta) {
        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keyFast = Keyboard.isKeyDown(Keyboard.KEY_Q);
        boolean keySlow = Keyboard.isKeyDown(Keyboard.KEY_E);
        boolean keyFlyUp = Keyboard.isKeyDown(Keyboard.KEY_Z);
        boolean keyFlyDown = Keyboard.isKeyDown(Keyboard.KEY_X);

        if (keyFast) 
        {
            speed = moveSpeed * 5;
        } else if (keySlow) 
        {
            speed = moveSpeed / 2;
        } else {
            speed = moveSpeed;
        }
        
        int realX = (int) ((pos.z + altitude.length) * (width-1) / (2*altitude.length));
        int realZ = (int) ((pos.x + altitude[0].length) * (height-1) / (2*altitude[0].length));
        float alt;
        
        if(realX < 0 || realX >= altitude.length || realZ < 0 || realZ >= altitude[0].length)
        {
        	alt = skyHeight;
        }
        else
        {
        	alt = altitude[realX][realZ];
        }

        speed *= delta;

        if (keyFlyUp) 
        {
        	float motion = pos.y + speed;
            pos.y = (motion >= skyHeight-120)?skyHeight-124:motion;
        }
        if (keyFlyDown) 
        {
        	float motion = pos.y - speed;
        	pos.y = (motion <= alt+2)?pos.y+4:motion;
        }

        if (keyDown) {
            movePosX(speed, 0, -1, alt);
            movePosY(speed, 0, 1, alt);
            movePosZ(speed, 0, 1, alt);
        }
        if (keyUp) {
            movePosX(speed, 0, 1, alt);
            movePosY(speed, 0, -1, alt);
            movePosZ(speed, 0, -1, alt);
        }
        if (keyLeft) {
        	movePosX(speed, -90, 1, alt);
            movePosZ(speed, -90, -1, alt);
        }
        if (keyRight) {
            movePosX(speed, 90, 1, alt);
            movePosZ(speed, 90, -1, alt);
        }
    }
    
    
    /** 
     * Avoid collision. If collides, move back.
     */
    public void movePosX(float speed, float angle, int forward, float alt)
    {
    	float movY = (float) (pos.y + forward * Math.sin(Math.toRadians(rotation.x + angle)) * speed);
    	float motion = (float) (pos.x + forward * Math.sin(Math.toRadians(rotation.y + angle)) * speed);
    	int offset = (pos.x > 0)?-5:5;
    	pos.x = (alt > movY)?pos.x + offset:motion;	
    	//pos.x = motion;
    }
    
    public void movePosY(float speed, float angle, int forward, float alt)
    {
    	float motion = (float) (pos.y + forward * Math.sin(Math.toRadians(rotation.x + angle)) * speed);
    	pos.y = (motion <= alt+2)?pos.y+4:motion;
    	pos.y = (alt - motion > 100)? pos.y-4:pos.y;
    	//pos.y = motion;
    	if(motion >= skyHeight-120)
    		pos.y = skyHeight-124;
    }
    
    public void movePosZ(float speed, float angle, int forward, float alt)
    {
    	float movY = (float) (pos.y + forward * Math.sin(Math.toRadians(rotation.x + angle)) * speed);
    	float motion = (float) (pos.z + forward * Math.cos(Math.toRadians(rotation.y + angle)) * speed);
    	int offset = (pos.z > 0)?-5:5;
    	pos.z = (alt > movY)?pos.z + offset:motion;
    	//pos.z = motion;
    }
    

    public void setSpeed(float speed) {
        moveSpeed = speed;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setX(float x) {
        pos.x = x;
    }

    public float getX() {
        return pos.x;
    }

    public void addToX(float x) {
        pos.x += x;
    }

    public void setY(float y) {
        pos.y = y;
    }

    public float getY() {
        return pos.y;
    }

    public void addToY(float y) {
        pos.y += y;
    }

    public void setZ(float z) {
        pos.z = z;
    }

    public float getZ() {
        return pos.z;
    }

    public void addToZ(float z) {
        pos.z += z;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotationX(float x) {
        rotation.x = x;
    }

    public float getRotationX() {
        return rotation.x;
    }

    public void addToRotationX(float x) {
        rotation.x += x;
    }

    public void setRotationY(float y) {
        rotation.y = y;
    }

    public float getRotationY() {
        return rotation.y;
    }

    public void addToRotationY(float y) {
        rotation.y += y;
    }

    public void setRotationZ(float z) {
        rotation.z = z;
    }

    public float getRotationZ() {
        return rotation.z;
    }

    public void addToRotationZ(float z) {
        rotation.z += z;
    }

    public void setMaxLook(float maxLook) {
        this.maxLook = maxLook;
    }

    public float getMaxLook() {
        return maxLook;
    }

    public void setMouseSensitivity(float mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }

    public float getMouseSensitivity() {
        return mouseSensitivity;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }
}
