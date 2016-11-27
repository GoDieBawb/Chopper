/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package mygame;

import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;

/**
*
* @author Bob
*/
public class PlayerManager extends AbstractAppState {
    
    private SimpleApplication app;
    private AppStateManager   stateManager;
    private AssetManager      assetManager;
    private Node              rootNode;
    private Node              treeNode;
    private BulletAppState    physics;
    public Player             player;
    private long              fireDie;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app          = (SimpleApplication) app;
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.physics      = this.app.getStateManager().getState(SceneManager.class).physics;
        this.treeNode     = this.app.getStateManager().getState(TreeManager.class).treeNode;
        initPlayer();
    }
    
    private void initPlayer(){
        player             = new Player();
        player.model       = (Node) assetManager.loadModel("Models/Person/Person.j3o");
        player.animControl = player.model.getChild("Person").getControl(AnimControl.class);
        player.armChannel  = player.animControl.createChannel();
        player.legChannel  = player.animControl.createChannel();
        TextureKey key     = new TextureKey("Models/Person/Person.png", true);
        Texture    tex     = assetManager.loadTexture(key);
        Material   mat     = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        player.playerPhys  = new BetterCharacterControl(.1f, 1.5f, 100f);
        player.highScore   = player.readScore(stateManager);
        
        mat.setTexture("ColorMap", tex);
        player.model.setLocalTranslation(0, 1.75f, 0);
        
        fireDie = System.currentTimeMillis()/1000;
        
        player.hasSwung = false;
        player.swingDelay = System.currentTimeMillis()/1000;
        
        Node     axe    = (Node) assetManager.loadModel("Models/axe/axe.j3o");
        Material axeMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //Node bla        = (Node) player.model.getChild("Person");
        //Node hand       = (Node) bla.getChild("armNode");
        axe.setName("Axe");
        axe.scale(.07f);
        axe.setMaterial(axeMat);
        player.attachChild(axe);
        axe.rotate(45, 0, 45);
        axe.setLocalTranslation(-.5f, .4f, -1.4f);
        
        player.addControl(player.playerPhys);
        physics.getPhysicsSpace().add(player.playerPhys);
        player.playerPhys.warp(new Vector3f(15, 2, -5));
        
        player.scale(.3f);
        
        player.swing = false;
        player.wood  = 0;
        player.score = 0;
        player.fireLevel = 20;
        
        player.armChannel.addFromRootBone("TopSpine");
        player.legChannel.addFromRootBone("BottomSpine");
        player.model.setMaterial(mat);
        player.attachChild(player.model);
        rootNode.attachChild(player);
        
        player.armChannel.setAnim("ArmIdle");
        player.legChannel.setAnim("LegsIdle");
        
    }
    
    private void rotate(){
        
        InteractionManager inter = app.getStateManager().getState(InteractionManager.class);
        boolean up    = inter.up;
        boolean down  = inter.down;
        boolean left  = inter.left;
        boolean right = inter.right;
        
        if (up) {
            
            if (left) {
                player.playerPhys.setViewDirection(new Vector3f(999,0,999));
            }
            
            else if (right) {
                player.playerPhys.setViewDirection(new Vector3f(-999,0,999));
            }
            
            else {
                player.playerPhys.setViewDirection(new Vector3f(0,0,999));
            }
            
        }
        
        else if (down) {
            
            if (left) {
                player.playerPhys.setViewDirection(new Vector3f(999,0,-999));
            }
            
            else if (right) {
                player.playerPhys.setViewDirection(new Vector3f(-999,0,-999));
            }
            
            else {
                player.playerPhys.setViewDirection(new Vector3f(0,0,-999));
            }
            
        }
        
        else if (left) {
            player.playerPhys.setViewDirection(new Vector3f(999,0,0));
        }
        
        else if (right){
            player.playerPhys.setViewDirection(new Vector3f(-999,0,0));
        }
    }
    
    private Vector3f getHandPosition() {
        return player.animControl.getSkeleton().getBone("RightForearm").getModelSpacePosition();
    }
    
    @Override
    public void update(float tpf){
        
        rotate();
        
        CollisionResults results = new CollisionResults();
        treeNode.collideWith(player.model.getWorldBound(), results);
        
        if (results.size() > 0){
            Tree hitTree = (Tree) results.getCollision(0).getGeometry().getParent().getParent().getParent();
            hitTree.health--;
            player.swing();
        }
        
        if (System.currentTimeMillis()/1000 - player.swingDelay > .01f){
            player.hasSwung   = false;
        }
        
        player.getChild("Axe").setLocalTranslation(getHandPosition().addLocal(-.5f, .4f, -1.4f));
        
    }
    
}
