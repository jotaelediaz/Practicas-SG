/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaSolar;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Light;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 *
 * @author JotaEle & Maximetinu
 */
class LuzAmbiental extends BranchGroup {
    LuzAmbiental () {
		Light aLight;

		// Se crea la luz ambiente
		aLight = new AmbientLight (new Color3f (0.1f, 0.1f, 0.1f));
		aLight.setInfluencingBounds (new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 999.0));
		aLight.setEnable(true);
		this.addChild(aLight);
	}
}
