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
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Random;

/**
 *
 * @author Bob
 */
public class TreeManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private Material          leafMat;
  private Material          barkMat;
  private Node              rootNode;
  private BulletAppState    physics;
  private long              treeSpread;
  public  Node              treeNode;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.rootNode     = this.app.getRootNode();
    this.treeNode     = new Node("TreeNode");
    this.physics      = this.stateManager.getState(SceneManager.class).physics;
    leafMat           = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    barkMat           = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    rootNode.attachChild(treeNode);
    leafMat.setColor("Color", ColorRGBA.Green);
    barkMat.setColor("Color", ColorRGBA.Brown);
    treeSpread = System.currentTimeMillis()/1000;
    }
  
  private void createTree() {
    Tree tree     = new Tree();
    tree.model    = (Node) assetManager.loadModel("Models/Tree/Tree.j3o");
    tree.treePhys = new RigidBodyControl();
    tree.health  = 5;
    Node a       = (Node) tree.model.getChild("Tree");
    tree.setName("Tree");
    a.getChild(0).setMaterial(leafMat);
    a.getChild(1).setMaterial(barkMat);
    tree.treePhys.setMass(0f);
    tree.model.getChild("Trunk").addControl(tree.treePhys);
    physics.getPhysicsSpace().add(tree.treePhys);
    tree.attachChild(tree.model);
    treeNode.attachChild(tree);
    placeTree(tree);
    }
  
  private void placeTree(Tree tree) {
    Random rand = new Random();
    int xSpot   = rand.nextInt(100) - 50;
    int zSpot   = rand.nextInt(100) - 50;
    
    tree.setLocalTranslation(xSpot, .5f, zSpot);
    tree.treePhys.setPhysicsLocation(new Vector3f(xSpot, 3, zSpot));  
    while (tree.getLocalTranslation().distance(new Vector3f(0,0,0)) < 20) {
      placeTree(tree);
      } 
    }
  
  @Override
  public void update(float tpf){
    
    if (System.currentTimeMillis()/1000 - treeSpread > 3) {
      treeSpread = System.currentTimeMillis()/1000;
      createTree();
      }  
      
    for (int i = 0; i < treeNode.getChildren().size(); i++) {
      Tree currentTree = (Tree) treeNode.getChild(i);
      
      
      
      if (currentTree.health <= 0) {
        Player player = stateManager.getState(PlayerManager.class).player;
        player.score++;
        
        if(player.wood < 10){
        player.wood++;
        //stateManager.getState(GuiManager.class).woodInd.setCurrentValue(player.wood);
        }
        
        physics.getPhysicsSpace().remove(currentTree.treePhys);
        currentTree.removeFromParent();
        }
      
      }
      
    if (treeNode.getChildren().size() < 10) {
      createTree();
      }
    
    if (treeNode.getChildren().size() > 30) {
      stateManager.getState(PlayerManager.class).player.isDead = true;
      stateManager.getState(InteractionManager.class).setEnabled(false);
      stateManager.getState(PlayerManager.class).setEnabled(false);
      this.setEnabled(false);
      stateManager.getState(GuiManager.class).showTitleText("You Lose");
      } 

    }
  
  }
