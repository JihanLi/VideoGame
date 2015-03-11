/** 
 * 3D Video Game: Journey of a Dragon.
 * 
 * Author: Jihan Li
 * 
 * This is the face class. It defines the face data of an obj model.
 * 
 * Reference: 
 *  
 * LWJGL Tutorials, by Oskar Veerhoek
 * https://www.youtube.com/playlist?list=PL19F2453814E0E315
 * 
 */

package src;

import org.lwjgl.util.vector.Vector3f;

public class Face {
	private Vector3f indices;
	private Vector3f textures;
	private Vector3f normals;
	private int materials;
	
	public Face(Vector3f indice, Vector3f texture, Vector3f normal, int matNum)
	{
		this.setIndices(indice);
		this.setTextures(texture);
		this.setNormals(normal);
		this.setMaterials(matNum);
	}

	public Vector3f getIndices() {
		return indices;
	}

	public void setIndices(Vector3f indices) {
		this.indices = indices;
	}

	public Vector3f getTextures() {
		return textures;
	}

	public void setTextures(Vector3f textures) {
		this.textures = textures;
	}

	public Vector3f getNormals() {
		return normals;
	}

	public void setNormals(Vector3f normals) {
		this.normals = normals;
	}

	public int getMaterials() {
		return materials;
	}

	public void setMaterials(int materials) {
		this.materials = materials;
	}
}
