/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaSolar;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Texture;
import javax.vecmath.Point3d;

/**
 *
 * @author JotaEle & Maximetinu
 */
class Fondo extends BranchGroup{
    Fondo (String rtextura){
		// Se crea el objeto para el fondo y se le asigna un área de influencia (una esfera)
		Background background = new Background ();
		background.setApplicationBounds (new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 100000000000000.0));

		// Se crea un aspecto basado en la textura a mostrar
		Appearance app = new Appearance ();
		Texture texture = new TextureLoader (rtextura, null).getTexture();
		app.setTexture (texture);

		// Se hace la esfera con un determinado radio indicándole:
		//    - Que genere coordenadas de textura
		//    - Que genere las normales hacia adentro
		//    - Que tenga el aspecto creado
		Primitive sphere = new Sphere (1.0f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS_INWARD, 100, app);

		// Se crea la rama para la geometría del fondo, 
		BranchGroup bgGeometry = new BranchGroup ();
		// Se le añade la esfera
		bgGeometry.addChild (sphere);
		// Y se establece como geometría del objeto background
		background.setGeometry(bgGeometry);

		// Finalmente, se cuelga el fondo creado
		this.addChild (background);  
	}
}
