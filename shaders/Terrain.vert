// This is the vertex shader for terrain, using Lambertian diffuse light model.


attribute vec4 Kd;

varying vec4 diffuse;
varying vec4 ambient;
varying float intensity;
void main()
{
	vec3 normal, lightDir;
	normal = normalize(gl_NormalMatrix * gl_Normal);
	lightDir = normalize(vec3(0, 1, 0));
	intensity = max(dot(normal, lightDir), 0);
	diffuse = Kd * vec4(1, 1, 1, 5);
	ambient = gl_LightSource[0].ambient;
	
	gl_Position = ftransform();
	
}