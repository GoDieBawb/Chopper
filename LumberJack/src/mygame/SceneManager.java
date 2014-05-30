/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 *
 * @author Bob
 */
public class SceneManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private Node              rootNode;
  public  Node              building;
  public  BulletAppState    physics;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.rootNode     = this.app.getRootNode();
    this.physics      = new BulletAppState();
    stateManager.attach(physics);
    initFloor();
    initHouse();
    }
  
  private void initFloor(){
    RigidBodyControl floorPhys = new RigidBodyControl();
    Material mat   = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    Box box        = new Box(150, .2f, 150);
    Geometry floor = new Geometry("The Ground", box);
    mat.setColor("Color", ColorRGBA.White);
    floor.setMaterial(mat);
    floorPhys.setMass(0f);
    floor.addControl(floorPhys);
    physics.getPhysicsSpace().add(floorPhys);
    rootNode.attachChild(floor);
    }
  
  private void initHouse(){
    building         = new Node("House");
    building         =  (Node) assetManager.loadModel("Models/cg_house_A.j3o");

    Material mat1    = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key1  = new TextureKey("Textures/mortar_brick/D.png", false);
    Texture tex1     = assetManager.loadTexture(key1);
    mat1.setTexture("ColorMap", tex1);

    Material mat2    = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key2  = new TextureKey("Textures/floor/D.png", false);
    Texture tex2     = assetManager.loadTexture(key2);
    mat2.setTexture("ColorMap", tex2);

    Material mat3    = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key3  = new TextureKey("Textures/roof_shale/D.png", false);
    Texture tex3     = assetManager.loadTexture(key3);
    mat3.setTexture("ColorMap", tex3); 

    Material mat4    = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key4  = new TextureKey("Textures/roof_straw/D.png", false);
    Texture tex4     = assetManager.loadTexture(key4);
    mat4.setTexture("ColorMap", tex4); 
    
    building.getChild("house").setMaterial(mat1);
    building.getChild("floor").setMaterial(mat2);
    building.getChild("shale_roof").setMaterial(mat3);
    building.getChild("straw_roof").setMaterial(mat4);
    building.scale(1f);
    RigidBodyControl buildPhys = new RigidBodyControl(0f);
    building.addControl(buildPhys);
    physics.getPhysicsSpace().add(buildPhys);
    rootNode.attachChild(building);
    buildPhys.setPhysicsLocation(new Vector3f(0, 0, 0));
    }
  
  }
