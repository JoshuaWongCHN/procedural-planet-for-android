# procedural-planet-for-android
Proceural planet in Android and libgdx

参考[WebGL版本][1]

[动态效果][2]

## 效果图
<img src="https://github.com/JoshuaWongCHN/procedural-planet-for-android/blob/master/android/assets/planet.png" />

## 实现过程
1. 建立球体模型
 - 生成立方体模型顶点
 - 通过normalize、scale变换将立方体顶点映射到球面

2. 生成立方体贴图和球面法线贴图
 - 通过分形perlin噪声算法生成立方体贴图
 - 将立方体贴图转换为法线贴图
3. 加载Shader, 进行渲染
 
 
 [1]:https://github.com/holgerl/procedural-planet
 [2]:https://holgerl.github.io/procedural-planet/
