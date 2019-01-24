attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
uniform vec4 u_cameraPosition;

varying vec3 v_normal;
varying vec3 v_cameraVector;
varying vec3 v_position;
varying vec2 v_uv;

void main() {
    v_normal = a_normal;
    vec4 position = u_worldTrans * vec4(a_position, 1.0);
    v_position = position.xyz;
    v_cameraVector = u_cameraPosition.xyz - v_position;
    v_uv = a_texCoord0;

    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}