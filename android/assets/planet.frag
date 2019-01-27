precision mediump float;

uniform vec3 u_lightPos;
uniform samplerCube u_cubeMap;
uniform samplerCube u_normalCubeMap;
varying vec3 v_normal;
varying vec3 v_position;
varying vec3 v_cameraVector;

varying vec2 v_uv;

mat4 rotationMatrix(vec3 axis, float angle) {
    axis = normalize(axis);
    float s = sin(angle);
    float c = cos(angle);
    float oc = 1.0 - c;

    return mat4(oc * axis.x * axis.x + c,           oc * axis.x * axis.y - axis.z * s,  oc * axis.z * axis.x + axis.y * s,  0.0,
                oc * axis.x * axis.y + axis.z * s,  oc * axis.y * axis.y + c,           oc * axis.y * axis.z - axis.x * s,  0.0,
                oc * axis.z * axis.x - axis.y * s,  oc * axis.y * axis.z + axis.x * s,  oc * axis.z * axis.z + c,           0.0,
                0.0,                                0.0,                                0.0,                                1.0);
}

// 法线贴图中的法线是以球面点法线为参照y轴，所以需要转换至世界坐标系中
vec3 bumpNormal(samplerCube normalCubeMap, vec3 dir) {
    vec3 bumpedNormal = normalize(textureCube(normalCubeMap, dir).xyz * 2.0 - 1.0);

    vec3 y_axis = vec3(0,1,0);
    float rot_angle = acos(dot(bumpedNormal,y_axis));
    vec3 rot_axis = normalize(cross(bumpedNormal,y_axis));
    return vec3(rotationMatrix(rot_axis, rot_angle) * vec4(v_normal, 1.0));
}

void main() {
    float PI = 3.14159265358979323846264;
    vec3 lightDir = normalize(u_lightPos - v_position);
    vec3 cameraDir = normalize(v_cameraVector);
    vec3 center = vec3(0.0);
    vec3 newNormal = bumpNormal(u_normalCubeMap, v_position-center);

    float diffuse = max(0.0, dot(newNormal, lightDir));
    float viewAngle = max(0.0, dot(v_normal, cameraDir));
//    float adjustedDiffuse = min(0.6, diffuse) / 0.6;
//    float adjustedViewAngle = min(0.65, viewAngle) / 0.65;
    float invertedViewAngle = pow(acos(viewAngle), 3.0) * 0.4;

    float dProd = 0.0;
    dProd += 0.5 * diffuse;
    dProd += 0.2 * diffuse * (invertedViewAngle - 0.1);
    dProd += invertedViewAngle * 0.5 * (max(-0.35, dot(v_normal, lightDir)) + 0.35);
    dProd *= 0.7 + pow(invertedViewAngle/(PI/2.0), 2.0);

    dProd *= 0.5;
    vec4 atmColor = vec4(dProd, dProd, dProd, 1.0);

    vec4 texelColor = textureCube(u_cubeMap, v_position-center) * min(asin(diffuse), 1.0);
    gl_FragColor = texelColor + min(atmColor, 0.8);
//    gl_FragColor = textureCube(u_normalCubeMap, v_position-center);
}