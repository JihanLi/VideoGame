// This is the fragment shader for terrain.

varying vec4 diffuse;
varying vec4 ambient;
varying float intensity;

void main()
{
	vec4 color = intensity * diffuse + ambient;
	gl_FragColor = color;	
	
}
