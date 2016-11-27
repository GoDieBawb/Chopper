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
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
*
* @author Bob
*/
public class InteractionManager extends AbstractAppState implements ActionListener {
    
    private SimpleApplication app;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private InputManager inputManager;
    private Player player;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    public boolean left = false, right = false, up = false, down = false, click = false;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.player       = this.stateManager.getState(PlayerManager.class).player;
        setUpKeys();
    }
    
    //Sets up key listeners for the action listener
    private void setUpKeys(){
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Click");
    }
    
    public void onAction(String binding, boolean isPressed, float tpf) {
        //Spray water on click
        if (binding.equals("Click") && !isPressed) {
            
        } else if (binding.equals("Left")) {
            
            left = isPressed;
            
        } else if (binding.equals("Right")) {
            
            right = isPressed;
            
        } else if (binding.equals("Down")){
            
            down = isPressed;
            
        } else if (binding.equals("Up")){
            
            up = isPressed;
        }
        
    }
    
    @Override
    public void update(float tpf) {
        
        camDir.set(this.app.getCamera().getDirection()).multLocal(10.0f, 0.0f, 10.0f);
        camLeft.set(this.app.getCamera().getLeft()).multLocal(10.0f);
        walkDirection.set(0, 0, 0);
        
        int xMove = 0;
        int zMove = 0;
        
        if (up) {
            zMove = 5;
        }
        else if (down) {
            zMove = -5;
        }
        
        if (left) {
            xMove = 5;
        }
        else if (right) {
            xMove = -5;
            
        }
        
        if(up||down||left||right) {
            
            player.run();
            
        } else {
            player.idle();
        }
        
        walkDirection.addLocal(xMove, 0, zMove);
        
        player.playerPhys.setWalkDirection(walkDirection.mult(1));
        
    }
    
}