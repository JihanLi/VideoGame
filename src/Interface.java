/** 
 * 3D Video Game: Journey of a Dragon.
 * 
 * Author: Jihan Li
 * 
 * This is the main game interface. 
 * 
 */

package src;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.UnicodeFont;


public class Interface {
	
	private float[] rotation = new float[10];
	private Vector3f[] pos = new Vector3f[10];
	private float[] size = {1.0f, 2.0f, 2.5f, 3.0f, 1.5f, 2.5f, 3.0f, 1.0f, 2.8f, 3.2f};
	private float[] speed = {0.1f, 0.2f, 0.03f, 0.05f, 0.15f, 0.17f, 0.2f, 0.08f, 0.05f, 0.04f};
	private int menuImg;
	private int mainMenu;
	
	/** 
     * Load the texture of the main menu.
     * @throws IOException 
     */
	public void loadInterface(String name) throws IOException
	{
		for(int i = 0; i < pos.length; i++)
		{
			pos[i] = new Vector3f((float) (Math.random()*800 - 400), (float) (Math.random()*400 - 50), (float) (Math.random()*800 - 400));
		}
		BufferedImage img = ImageIO.read(new File("res/"+ name +".png"));
    	int width = img.getWidth();
    	int height = img.getHeight();
    	int[] pixels = new int[width*height];
    	img.getRGB(0, 0, width,height, pixels, 0, width);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4); 
		  
		for(int y = 0; y < height; y++)
		{
		    for(int x = 0; x < width; x++)
		    {
		        int pixel = pixels[y * width + x];
		        buffer.put((byte) ((pixel >> 16) & 0xFF));
		        buffer.put((byte) ((pixel >> 8) & 0xFF));
		        buffer.put((byte) (pixel & 0xFF)); 
		        buffer.put((byte) ((pixel >> 24) & 0xFF)); 
		    }
		}
		
		buffer.flip();
		menuImg = glGenTextures();
		
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
		glBindTexture(GL_TEXTURE_2D, menuImg);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	    
	    mainMenu = glGenLists(1);
		glNewList(mainMenu, GL_COMPILE);
		{
			glBindTexture(GL_TEXTURE_2D, menuImg);
			glBegin(GL_QUADS);
			{
				glTexCoord2f(0, 0);
				glVertex2i(0, 0);
		        glTexCoord2f(0, 1);
		        glVertex2i(0, 480);	
		        glTexCoord2f(1, 1);
		        glVertex2i(640, 480); 
		        glTexCoord2f(1, 0);
		        glVertex2i(640, 0);	
			}
		    glEnd();
		}
	    glEndList();
	}
	
	
	/** 
     * Draw the main menu.
     */
	public void mainMenu(Camera camera)
	{
		glMatrixMode(GL_PROJECTION);
		
		glLoadIdentity();
        glOrtho(0, 640, 480, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        
        glEnable(GL_TEXTURE_2D);   
		glCallList(mainMenu);
		glDisable(GL_TEXTURE_2D);
	}
	
	
	/** 
     * Draw the game including text, sky box, terrain and models.
     * Also set up the movement of those models.
     * @throws FileNotFoundException, IOException 
     */
	public void gameBody(Camera camera, Background skybox, Map terrain, List<Model> models, UnicodeFont font,
				DecimalFormat formatter, FloatBuffer perspectiveMatrix, FloatBuffer orthographicMatrix) throws FileNotFoundException, IOException
    {
		glPushMatrix();
	    {
	        camera.apply();
	        glTranslatef(0.0f, 0.0f, -7.0f);
			
	        glPushMatrix();
		    {
		        glEnable(GL_TEXTURE_2D);
		        skybox.drawBox();   
		        glDisable(GL_TEXTURE_2D);
		    }
		    glPopMatrix();
	        
	        glPushMatrix();
		    {
		        glTranslatef(0.0f, -400.0f, 0.0f);
		        terrain.drawTerrain();
		    }
		    glPopMatrix();
	        
		    for(int i = 1; i < models.size(); i++)
		    {
		        glPushMatrix();
			    {
			    	rotation[i-1] += speed[i-1];
			    	if (rotation[i-1] / 360 > 1) 
			        {
			            rotation[i-1] -= 360;
			        } 
			        else if (rotation[i-1] / 360 < -1) 
			        {
			            rotation[i-1] += 360;
			        }
			    	
			    	glRotatef(rotation[i-1], 0, 1, 0);
			    	glScalef(size[i-1],size[i-1],size[i-1]);
			    	glTranslatef(pos[i-1].x, pos[i-1].y, pos[i-1].z);
			    	glRotatef(90, 0, 1, 0);
			        Model model = models.get(i);
			        model.draw();
			    }
			    glPopMatrix();
		    }
	    }
	    glPopMatrix();  
        
	    glPushMatrix();
	    {
	        Model model = models.get(0);	        
	        glTranslatef(0.0f, 0.0f, -20.0f);
	        glEnable(GL_TEXTURE_2D);
	        model.draw();
	        glDisable(GL_TEXTURE_2D);
	    }
	    glPopMatrix();
	    
        //Set up text of position.
        glMatrixMode(GL_PROJECTION);
        glLoadMatrix(orthographicMatrix);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        {
	        glLoadIdentity();
	        glDisable(GL_LIGHTING);
	        font.drawString(10, 10, "Position: x = " + formatter.format(camera.getX()) + ", y = "+formatter.format(camera.getY()) 
	        					+ ", z = "+formatter.format(camera.getZ()));
	        glEnable(GL_LIGHTING);
        }
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glLoadMatrix(perspectiveMatrix);
        glMatrixMode(GL_MODELVIEW);
	         
    }
}
