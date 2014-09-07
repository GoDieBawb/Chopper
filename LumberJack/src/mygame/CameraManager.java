/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.math.Vector3f;

/**
*
* @author Bob
*/
public class CameraManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private Player            player;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.player       = this.stateManager.getState(PlayerManager.class).player;
    }
  
  @Override
  public void update(float tpf){
    
    app.getCamera().setLocation(player.getLocalTranslation().addLocal(0,30,0));
    app.getCamera().lookAt(player.getLocalTranslation().multLocal(1,0,1), new Vector3f(0,1,0));
    
    }
  
  }