/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaSolar;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Texture;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Material;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author JotaEle & Maximetinu
 */
public class CuerpoCeleste extends BranchGroup{
    
    private final String rutaTextura;
    private final float radio;
    private final float distanciaOrigen;
    private final long velocidadRotacion;
    private final long velocidadTraslacion;
    private final float inclinacion;
    //private final long camaraCuerpoCeleste;

    private Alpha temporizadorRotacion;
    
    final TransformGroup nodoOrbita, nodoTraslacion, nodoRotacion, nodoInclinacion;
    protected Primitive esfera;
    private Primitive anillo;
    private Object vistaP;
	
    public CuerpoCeleste(String rtextura, float rad, float distOrigen, float incl, long vRot, long vTrans) {
		
        rutaTextura = rtextura;
        radio = rad;
        distanciaOrigen = distOrigen;
        velocidadRotacion = vRot;
        velocidadTraslacion = vTrans;
        inclinacion = incl;
        
        nodoOrbita = new TransformGroup();
        nodoTraslacion = new TransformGroup();
        nodoRotacion = new TransformGroup();
        nodoInclinacion = new TransformGroup();
        
        esfera = new Sphere();
        

        // Dibujamos la línea de la órbita
        addLineaOrbita();

        // Definimos la esfera 
        definirEsfera();


        // Definimos los movimientos de Traslación y Rotación
        definirMovTraslacion();
        
        Transform3D transInclinacion = new Transform3D();
        transInclinacion.rotZ(Math.toRadians(inclinacion));
        nodoInclinacion.setTransform(transInclinacion);
        
        definirMovRotacion();


        // nodoOrbita -> nodoTraslacion -> nodoRotacion -> Esfera
        this.addChild(nodoOrbita); // Colgamos el nodo de Rotacion de la Órbita al BG CuerpoCeleste
        nodoOrbita.addChild(nodoTraslacion); // Colgamos el nodo de distancia al Sol al nodo de rotación de la órbita
        nodoTraslacion.addChild(nodoInclinacion); // Colgamos el nodo del mov de Rotacion del Cuerpo Celeste al nodo de Traslación
        nodoInclinacion.addChild(nodoRotacion);
        nodoRotacion.addChild(esfera); // Colgamos la esfera al nodo de Rotación

        
        this.setCapability(BranchGroup.ENABLE_PICK_REPORTING);
        
    }
	
	
	private void definirEsfera() {
	// Creamos la apariencia (Y su textura y material) que aplicaremos al cuerpo celeste
        Appearance apariencia = new Appearance();
        
        Texture textura = new TextureLoader(rutaTextura, null).getTexture();
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(ta);
        apariencia.setTexture(textura);
        
        Material materialCCeleste = new Material(
                new Color3f(1.0f, 1.0f, 1.0f),		// Color ambiental (no se pone)
                new Color3f(0.0f, 0.0f, 0.0f),		// Color emisivo (se pone en el sol)
                new Color3f(1.0f, 1.0f, 1.0f),		// Color difuso (blanco al máximo)
                new Color3f(0.2f, 0.2f, 0.2f),		// Color especular (blanco al máximo) (es la luz que rebota)
                4.0f					// Brillo
        );
	apariencia.setMaterial(materialCCeleste);
        
        // Creamos la esfera que conformará la geometría del cuerpo celeste y le pasamos la apariencia que hemos creado
        esfera = new Sphere ((1.0f * radio), Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS, 60, apariencia);
		
    }	
	
    private void definirMovTraslacion() {
		
	//--- MOVIMIENTO DE GIRO SOBRE LA ÓRBITA ---
		
        //Vamos a definir el movimiento de giro de la traslación mediante el TG nodoOrbita. Permitimos su escritura
        nodoOrbita.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
	// Usamos la clase Alpha para crear un temporizador que vaya realizando el movimiento infinito (-1) según velocidadTraslacion
	Alpha temporizador = new Alpha(-1, velocidadTraslacion);
        
	// Creamos la transformación y la interpolación de rotación para que gire y le asignamos el temporizador y los params de giro            
	Transform3D transRotar = new Transform3D();
        //transRotar.rotZ(Math.toRadians(90));
	RotationInterpolator interpolator = new RotationInterpolator(temporizador, nodoOrbita, transRotar, 0.0f, (float) Math.PI * 2.0f);
	interpolator.setSchedulingBounds(new BoundingSphere(new Point3d (0.0f, 0.0f, 0.0f), 100000.0f));
        interpolator.setEnable(true) ;
        
	// Añadimos la transformación al nodo que genera el giro de la órbita
        nodoOrbita.addChild(interpolator);
		
		
	// --- DESPLAZAMIENTO DE LA DISTANCIA AL SOL ---
		
	nodoTraslacion.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
	Transform3D transDesplazar = new Transform3D();
		
	transDesplazar.setTranslation(new Vector3f(distanciaOrigen, 0, 0));
	nodoTraslacion.setTransform(transDesplazar);
		
		
    }
    
    private void definirMovRotacion() {
		
        //--- MOVIMIENTO DE ROTACIÓN DEL CUERPO CELESTE ---

        // Inicializamos el movimiento de rotación mediante el TG nodoRotacion y permitimos su escritura
        nodoRotacion.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Usamos la clase Alpha para definir un temporizador que vaya realizando el movimiento infinito (-1) según la velocidad de rotacion
        this.temporizadorRotacion = new Alpha(-1, velocidadRotacion);

        // Creamos la transformación y la interpolación de rotación para que gire y le asignamos el temporizador y los params de giro
        Transform3D transRotar = new Transform3D();
        //transRotar.rotZ(0.3);
        RotationInterpolator rotacionPlaneta = new RotationInterpolator(temporizadorRotacion, nodoRotacion, transRotar, 0.0f, (float) Math.PI * 2.0f);

        rotacionPlaneta.setSchedulingBounds(new BoundingSphere(new Point3d (0.0f, 0.0f, 0.0f), 100000.0f));
        rotacionPlaneta.setEnable(true) ;

        // Añadimos la transformación al nodo que genera el giro de la órbita
        nodoRotacion.addChild(rotacionPlaneta);	
	
    }
   
        
    public void cambiarEstadoRotacion(){
	if (this.temporizadorRotacion.isPaused())
		this.temporizadorRotacion.resume();
	else
		this.temporizadorRotacion.pause();
    }     
        
   
    public void addSatelite(CuerpoCeleste satelite) {	
	// Añadir satélites consiste en colgar de la traslación del Cuerpo Celeste original un segundo CuerpoCeleste que actuará como satélite del padre.
        nodoTraslacion.addChild(satelite);
    }
    
    public void addAnillo(String rtextura) {
        
        //crear círculo 2D (O CILINDRO MUY PLANO) más grande que el planeta, teniendo en cuenta el dato miembro radio
	//aplicar textura de anillo a ese planeta
	//colgar anillo de this o de nodoRotacion, como la bola
		
	// Creamos la apariencia (Y su textura y material) que aplicaremos al anillo
        Appearance apariencia = new Appearance();
		
	TransparencyAttributes transparencia = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.0f);
	apariencia.setTransparencyAttributes(transparencia);
        
        Texture textura = new TextureLoader(rtextura, null).getTexture();
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(ta);
        apariencia.setTexture(textura);
        
        Material materialCCeleste = new Material(
                new Color3f(1.6f, 1.6f, 1.6f),		// Color ambiental (no se pone)
                new Color3f(0.0f, 0.0f, 0.0f),		// Color emisivo (se pone en el sol)
                new Color3f(0.7f, 0.7f, 0.7f),		// Color difuso (blanco al máximo)
                new Color3f(0.05f, 0.05f, 0.05f),		// Color especular (blanco al máximo)
                0.0f								// Brillo
        );
	apariencia.setMaterial(materialCCeleste);
        
        // Creamos la esfera que conformará la geometría del cuerpo celeste y le pasamos la apariencia que hemos creado
        anillo = new Cylinder ((2.0f * radio), 0.01f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS, 40, 40, apariencia);
		
	nodoRotacion.addChild(anillo);
	this.definirMovTraslacion();
    }
	
    public void addNubes(String rtextura){
		
        // Creamos la apariencia (Y su textura y material) que aplicaremos a la esfera de las nubes
        Appearance apariencia = new Appearance();

        TransparencyAttributes transparencia = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.3f); // PARCHE
        apariencia.setTransparencyAttributes(transparencia);

        Texture textura = new TextureLoader(rtextura, null).getTexture();
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(ta);
        apariencia.setTexture(textura);

        Material materialCCeleste = new Material(
                new Color3f(1.0f, 1.0f, 1.0f),		// Color ambiental (no se pone)
                new Color3f(0.0f, 0.0f, 0.0f),		// Color emisivo (se pone en el sol)
                new Color3f(1.0f, 1.0f, 1.0f),		// Color difuso (blanco al máximo)
                new Color3f(1.0f, 1.0f, 1.0f),		// Color especular (blanco al máximo)
                4.0f					// Brillo
        );
                apariencia.setMaterial(materialCCeleste);

        // Creamos la esfera que conformará la geometría del cuerpo celeste y le pasamos la apariencia que hemos creado
        Primitive esfera_nubes = new Sphere ((1.01f * radio), Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS, 60, apariencia);

        nodoRotacion.addChild(esfera_nubes);
    }
	
    private void addLineaOrbita(){
        Appearance apariencia = new Appearance();

        TransparencyAttributes transparencia = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.95f);
        apariencia.setTransparencyAttributes(transparencia);

        Texture textura = new TextureLoader("img/orbita.png", null).getTexture();
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE); //REPLACE O DECAL SERÍA LO SUYO
        apariencia.setTextureAttributes(ta);
        apariencia.setTexture(textura);

        Material materialCCeleste = new Material(
                new Color3f(0.0f, 0.0f, 0.0f),		// Color ambiental (no se pone)
                new Color3f(0.5f, 0.5f, 0.5f),		// Color emisivo (se pone en el sol)
                new Color3f(0.0f, 0.0f, 0.0f),		// Color difuso (blanco al máximo)
                new Color3f(0.0f, 0.0f, 0.0f),		// Color especular (blanco al máximo)
                0.0f					// Brillo
        );
                apariencia.setMaterial(materialCCeleste);

        // Creamos la esfera que conformará la geometría del cuerpo celeste y le pasamos la apariencia que hemos creado
        Primitive lineaOrbita = new Cylinder ((this.distanciaOrigen * 1.055f), 0.01f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS, 40, 40, apariencia);

        this.addChild(lineaOrbita);
    }
    
    public void addCamara(Camara camara) {
        nodoOrbita.addChild(camara);
    }
        
        
}
