package com.joshua.planet;

import com.badlogic.gdx.graphics.Color;

public class PlanetScalarField {
    public static Color getColor(float x, float y, float z){
        return new Color();
    }
//    var resolution1 = 4;
//    var resolution2 = 16;
//    var resolution3 = 64;
//    var resolutionMax = maxDetail;
//
//    var coordFloat = new THREE.Vector3();
//
//    var randomScalarField = function(x, y, z) {
//        return SS.util.random4(x, y, z);
//    }
//
//    var helper = function(x, y, z, scalarField, resolution, interpolationMethod) {
//        // Because the sphere sample function gives normalized coordinates:
//        x = (x+1)/2*resolution;
//        y = (y+1)/2*resolution;
//        z = (z+1)/2*resolution;
//
//        coordFloat.set(x, y, z);
//        var interpolated = interpolationMethod(coordFloat, scalarField);
//        return interpolated*2 - 1; // Gives values (-1, 1)
//    }
//
//    var level1 = helper(x, y, z, randomScalarField, resolution1, SS.util.tricosineInterpolation);
//    var level2 = helper(x, y, z, randomScalarField, resolution2, SS.util.tricosineInterpolation);
//    var level3 = helper(x, y, z, randomScalarField, resolution3, SS.util.tricosineInterpolation);
//    var levelMax = helper(x, y, z, randomScalarField, resolutionMax, SS.util.nearestNeighbour);
//
//    var c = 0.5;
//    c *= 1 + level1*0.75;
//    c *= 1 + level2*0.25;
//    c *= 1 + level3*0.075;
//    c *= 1 + levelMax*(1/25);
//
//	if (c < 0.5) c *= 0.9;
//
//    c = SS.util.clamp(c, 0, 1);
//
//	return new THREE.Color().setRGB(c, c, c);
}
