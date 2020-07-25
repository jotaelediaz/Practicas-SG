/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaSolar;

import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;



/**
 *
 * @author JotaEle & Maximetinu
 */

public class Estrella extends CuerpoCeleste {
    
    private PointLight luzEstrella;
    
    public Estrella(String textura, float radio, float incl, long vRot){
        
	// La estrella, en este caso el Sol, es un cuerpo celeste con distancia al centro 0 y velocidad de traslación 0
        super(textura, radio, 0, incl, vRot, 0); 
		
	setEmisive();
	addLuz();
		
		
    }
	
    private void setEmisive(){
        // Obtenemos el material de la esfera que compone el sol, y ese material lo establecemos emisivo
        Appearance aparienciaSol = super.esfera.getAppearance(Sphere.BODY);
        Material materialSol = aparienciaSol.getMaterial();
        materialSol.setEmissiveColor(new Color3f(1.0f, 1.0f, 1.0f));

        // -- No hace falta volver a asignarlos porque se obtienen por referencia
        //aparienciaSol.setMaterial(materialSol);
        //super.esfera.setAppearance(aparienciaSol);
    }
	
    private void addLuz(){
        luzEstrella = new PointLight(new Color3f(0.9f, 0.9f, 0.9f), new Point3f(0.0f, 0.0f, 0.0f), new Point3f(1.0f, 0.0f, 0.0f));

        // BoundingSphere (o esfera de limitación) la creamos para delimitar el alcance de la luz.
        // Los argumentos pasados son centro de la esfera y radio.
        BoundingSphere limites = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 999999.9) ;

        // Activamos los límites de influencia a la luz
        luzEstrella.setInfluencingBounds(limites) ;
        luzEstrella.setEnable(true) ;
        
	// Colgamos la luz del mismo nodo de la estrella
        this.addChild(luzEstrella) ;
    }
    
}
