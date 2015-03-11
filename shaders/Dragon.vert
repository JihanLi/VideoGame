// This is the vertex shader for dragon, using Lambertian diffuse light model.

uniform vec3 Kd;
uniform vec3 Ks;
uniform vec3 Ka;
uniform float Ns;
uniform float illum;
uniform float dissolve;

varying vec3 diffuse;
varying float intensity;

void main()
{
	vec3 normal, lightDir;
	normal = normalize(gl_NormalMatrix * gl_Normal);
	lightDir = normalize(vec3(0, 1, 0));
	intensity = max(dot(normal, lightDir), 0);
	diffuse = Kd * vec3(1, 0, 0);
	gl_FrontColor = vec4(intensity * diffuse, 1);
	
	gl_TexCoord[0] = gl_MultiTexCoord0;  
	
	gl_Position = ftransform();
	
}