package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    private BulletAppState bulletState;
    private Material mat;

    @Override
    public void simpleInitApp() 
    {
        initKeys();
        initLight();
        flyCam.setMoveSpeed(15);
        flyCam.setRotationSpeed(3);
        
        bulletState = new BulletAppState();
        stateManager.attach(bulletState);
        
        mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
        
        Spatial ground = assetManager.loadModel("Models/container.obj");
        ground.setShadowMode(RenderQueue.ShadowMode.Receive);
        ground.setLocalScale(50);
        ground.setLocalTranslation(0, 40, 0);
        ground.setMaterial(mat);
        rootNode.attachChild(ground);
        
        CollisionShape groundShape = CollisionShapeFactory.createMeshShape(ground);
        RigidBodyControl groundControl = new RigidBodyControl(groundShape, 0);
        ground.addControl(groundControl);
        bulletState.getPhysicsSpace().add(groundControl);
        groundControl.setRestitution(1);
        
        for(int i = 0; i < 10; i++)
        {
            Spatial suzanne = assetManager.loadModel("Models/suzanne.obj");
            suzanne.setMaterial(mat);
            rootNode.attachChild(suzanne);

            CollisionShape suzanneShape = CollisionShapeFactory.createDynamicMeshShape(suzanne);
            RigidBodyControl suzanneControl = new RigidBodyControl(suzanneShape, .5f);
            suzanne.addControl(suzanneControl);
            bulletState.getPhysicsSpace().add(suzanneControl);
            suzanneControl.setPhysicsLocation(new Vector3f(0,i * 2 + 10, 0));
            suzanneControl.setRestitution(1);
        }
    }
    
    private void initLight()
    {
        AmbientLight ambient = new AmbientLight();
        rootNode.addLight(ambient);
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1,-1,0));
        rootNode.addLight(sun);
        
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        final int SHADOWMAP_SIZE=2048;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 4);
        dlsr.setLight(sun);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        viewPort.addProcessor(dlsr);
    }
    
    private void initKeys()
    {
        inputManager.addMapping("FireMonkey", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener,"FireMonkey");
    }
    
    private ActionListener actionListener = new ActionListener()
    {
        public void onAction(String name, boolean isPressed, float tpf) 
        {
            if (name.equals("FireMonkey") && !isPressed)
            {
                Spatial suzanne = assetManager.loadModel("Models/suzanne.obj");
                suzanne.setMaterial(mat);
                rootNode.attachChild(suzanne);

                CollisionShape suzanneShape = CollisionShapeFactory.createDynamicMeshShape(suzanne);
                RigidBodyControl suzanneControl = new RigidBodyControl(suzanneShape, .5f);
                suzanne.addControl(suzanneControl);
                bulletState.getPhysicsSpace().add(suzanneControl);
                suzanneControl.setPhysicsLocation(cam.getLocation());
                suzanneControl.setLinearVelocity(cam.getDirection().mult(50));
                suzanneControl.setRestitution(1);
            }  
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
