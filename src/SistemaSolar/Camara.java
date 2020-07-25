package SistemaSolar;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.util.ArrayList;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author JotaEle
 */

public class Camara extends BranchGroup {

    Canvas3D canvasCam;
    View view;
    
    public Camara(Canvas3D canvas, Point3d vis1 , Point3d vis2, Vector3d vis3){
        
            canvasCam = canvas;
            this.setVistaPerspectiva(vis1, vis2, vis3);
           
            this.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
            this.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND | BranchGroup.ALLOW_CHILDREN_WRITE);
    }

	
    public void setVistaPerspectiva(Point3d vis1, Point3d vis2, Vector3d vis3){
		
        if (canvasCam.getView() != null) {
            if (canvasCam.getView().numCanvas3Ds() > 0) {
		canvasCam.getView().removeAllCanvas3Ds();
            }
        }
        
        // Creamos la matriz de transformación3D de la cámara. La visión es la recibida como argumento.
        Transform3D matrizTransformacion3D = new Transform3D();
        matrizTransformacion3D.lookAt(vis1, vis2, vis3);
        matrizTransformacion3D.invert();
        
        //Creamos el nodo detransformación y le asignamos la matriz de transformación.
        TransformGroup tgPerspectiva = new TransformGroup(matrizTransformacion3D);
        
        // Creo el comportamiento de la vista para que se pueda mover la cámara con el ratón
        OrbitBehavior orbit = new OrbitBehavior(canvasCam, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(new BoundingSphere(new Point3d (0.0f, 0.0f, 0.0f), 1000.0f));
        orbit.setZoomFactor(1.5f);
        
        
        ViewingPlatform VPPerspectiva = new ViewingPlatform();
        tgPerspectiva.addChild(VPPerspectiva); // Se cuelga el ViewPlatform del nodo de transformación
        VPPerspectiva.setViewPlatformBehavior(orbit);
        
        // Se crea la vista y se establece el angulo de vision (45 grados) y el plano de recorte trasero
        Viewer vision = new Viewer(canvasCam);
        view = vision.getView();
        view.setFieldOfView(Math.toRadians(45));
        view.setBackClipDistance(50.0);
	
        // Establecemos el canvas
	view.removeCanvas3D(canvasCam);
	view.addCanvas3D(canvasCam);
        
	view.attachViewPlatform(VPPerspectiva.getViewPlatform());
	this.addChild(tgPerspectiva);
    }
    
     public void activarCamara(){
        if (canvasCam.getView() != null) {
            if (canvasCam.getView().numCanvas3Ds() > 0) {
		canvasCam.getView().removeAllCanvas3Ds();
            }
        }
        // Establecemos el canvas
	view.removeCanvas3D(canvasCam);
	view.addCanvas3D(canvasCam);
         
     }
    
}