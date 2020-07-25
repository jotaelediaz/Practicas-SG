/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaSolar;

//import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.pickfast.PickCanvas;
import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.PickInfo;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.WakeupOnAWTEvent;

/**
 *
 * @author metinu
 */

public class PickRotar extends Behavior{
	
	private final WakeupOnAWTEvent condicion;
	private final PickCanvas pickCanvas;
	private final Canvas canvas3D;
	
	public PickRotar (Canvas3D canvas3D, BranchGroup raiz){
		this.canvas3D = canvas3D;
		this.condicion = new WakeupOnAWTEvent (MouseEvent.MOUSE_CLICKED);
		this.pickCanvas = new PickCanvas(canvas3D, raiz);
		
		pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
        pickCanvas.setFlags(PickInfo.SCENEGRAPHPATH | PickInfo.CLOSEST_INTERSECTION_POINT);
        pickCanvas.setTolerance((float) 0.0);
	}
	
	@Override
	public void initialize (){
		wakeupOn (condicion);
		setEnable(true);
	}
	
	@Override
	public void processStimulus (Enumeration criterios){
	
		WakeupOnAWTEvent c = (WakeupOnAWTEvent) criterios.nextElement();
		AWTEvent[] e = c.getAWTEvent();
		MouseEvent mouse =(MouseEvent) e[0];
		
		// null pointer exception en esta línea
		pickCanvas.setShapeLocation (mouse);
		PickInfo pi = pickCanvas.pickClosest();
		
		if (pi != null){
			SceneGraphPath arbol = pi.getSceneGraphPath();
			CuerpoCeleste planeta = (CuerpoCeleste) arbol.getNode(arbol.nodeCount()-2);
			planeta.cambiarEstadoRotacion();
		}

		wakeupOn(condicion);
	}
}
