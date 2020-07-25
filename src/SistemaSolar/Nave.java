/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaSolar;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import java.io.FileNotFoundException;
import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.RotPosPathInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;

/**
 *
 * @author metinu
 */
public class Nave extends BranchGroup{

    //Posicion inicial = (0,0,0) Mirando al eje Y positivo y con la parte de arriba mirando al Z positivo
    private final float[] alphas;
    private final Point3f[] positions;
    private final Quat4f[] rotations;

    public final TransformGroup nodoMovimiento;

    public Nave(String ruta_obj, long velocidad, Point3f[] posiciones){

            this.positions = posiciones;
            this.alphas = new float[this.positions.length];
            this.rotations = new Quat4f[this.positions.length];	

            this.inicializarTemporizador();
            this.inicializarRotaciones();

            this.nodoMovimiento = new TransformGroup();

            this.generarAnimacion(velocidad);

            this.nodoMovimiento.addChild ( this.cargarModeloNave(ruta_obj) );
            this.addChild(nodoMovimiento);

            this.nodoMovimiento.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

            this.setPickable(false);

    }

    private BranchGroup cargarModeloNave(String ruta_obj){
            Scene modelo = null; 
            ObjectFile archivo = new ObjectFile (ObjectFile.RESIZE | ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE );
            try {
              modelo = archivo.load (ruta_obj);
            } catch (FileNotFoundException | ParsingErrorException | IncorrectFormatException e) {
              System.err.println (e);
              System.exit(1);
            }
            return modelo.getSceneGroup();
    }

    private void inicializarTemporizador(){
            float auxf = 1.0f / this.positions.length;
            this.alphas[0] = 0.0f;
            this.alphas[this.alphas.length - 1] = 1.0f;
            for (int i = 1; i<this.alphas.length-1; i++){
                    this.alphas[i] = this.alphas[i-1] + auxf;
            }
    }

    private void inicializarRotaciones(){	
            for (int i = 0; i<this.rotations.length; i++)
                    rotations[i] = new Quat4f ();

            Transform3D naveTrans = new Transform3D();
            Quat4f aux = new Quat4f();
            for (int i = 0; i<this.rotations.length; i++){
                    if (i == this.rotations.length - 1)
                            naveTrans.lookAt(new Point3d(0,0,0), new Point3d (positions[0].getX() - positions[i].getX(),  positions[0].getY() - positions[i].getY(),  positions[i].getZ() - positions[0].getZ()), new Vector3d(0f,1f,0f));
                    else 
                            naveTrans.lookAt(new Point3d(0,0,0), new Point3d (positions[i+1].getX() - positions[i].getX(),  positions[i+1].getY() - positions[i].getY(),  positions[i].getZ() - positions[i+1].getZ()), new Vector3d(0f,1f,0f));

                    naveTrans.get(aux);
                    rotations[i].set(new AxisAngle4f(aux.x,aux.y,aux.z,aux.w));
            }
    }

    private void generarAnimacion(long velocidad){
            this.nodoMovimiento.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            Transform3D transformacion = new Transform3D();
            Alpha alpha = new Alpha (-1, Alpha.INCREASING_ENABLE, 0, 0, velocidad, 0, 0, 0, 0, 0);
            RotPosPathInterpolator interpolator = new RotPosPathInterpolator (alpha, nodoMovimiento, transformacion, alphas, rotations, positions);
            interpolator.setSchedulingBounds(new BoundingSphere (new Point3d (0.0, 0.0, 0.0 ), 1000000.0));
             interpolator.setEnable(true);

            nodoMovimiento.addChild(interpolator);
    }

    public void addCamara(Camara camara){
        nodoMovimiento.addChild(camara);
    }
	
}