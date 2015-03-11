// This is the fragment shader for dragon.

uniform vec3 Kd;
uniform vec3 Ks;
uniform vec3 Ka;
uniform float Ns;
uniform float illum;
uniform float dissolve;

uniform sampler2D texKd; 
uniform sampler2D texKs; 
uniform sampler2D texKa;
uniform sampler2D texD;  
uniform sampler2D texNs; 


varying vec3 diffuse;
varying float intensity;

void main()
{
	/*vec3 ct, cf;
	float at, af;
	vec4 texel;
	
	cf = intensity * diffuse;
	af = 1;
	texel = texture2D(texKd, gl_TexCoord[0].st);
	ct = texel.rgb;
	at = texel.a;
	gl_FragColor = vec4(ct*cf, at*af);*/
	
	gl_FragColor = gl_Color;
	
	
}
