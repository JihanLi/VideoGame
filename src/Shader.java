/** 
 * 3D Video Game: Journey of a Dragon.
 * 
 * Author: Jihan Li
 * 
 * This is the shader class used for shading different objects. 
 * Reference: LWJGL Tutorials, by Oskar Veerhoek
 * https://www.youtube.com/playlist?list=PL19F2453814E0E315
 * 
 */

package src;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class Shader {
	
	private int shaderProgram;

	
	
	public int getShaderProgram() {
		return shaderProgram;
	}

	public void setShaderProgram(int shaderProgram) {
		this.shaderProgram = shaderProgram;
	}

	
	/** 
     * Generate a shader.
     */
	public void generateShader(String filename) throws IOException
	{
		shaderProgram = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		StringBuilder vertexShaderSource = new StringBuilder();
		StringBuilder fragmentShaderSource = new StringBuilder();
		
		BufferedReader reader = new BufferedReader(new FileReader("shaders/" + filename +".vert"));
		String line;
		while((line = reader.readLine()) != null)
		{
			vertexShaderSource.append(line).append("\n");
		}
		reader.close();
		
		reader = new BufferedReader(new FileReader("shaders/" + filename + ".frag"));
		while((line = reader.readLine()) != null)
		{
			fragmentShaderSource.append(line).append("\n");
		}
		reader.close();
		
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		if(glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Vertex Shader is not compiled correctly.");
		}
		
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		if(glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Fragment Shader is not compiled correctly.");
		}
		
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		
		glLinkProgram(shaderProgram);
		
		glValidateProgram(shaderProgram);
		
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
	
	}
	
	public void enableShader()
	{
		glUseProgram(shaderProgram);
	}
	
	public void disableShader()
	{
		glUseProgram(0);
	}
	
	public void deleteShader()
	{
		glDeleteProgram(shaderProgram);
	}
}

