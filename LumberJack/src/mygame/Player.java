/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.scene.Node;

/**
 *
 * @author Bob
 */
public class Player extends Node {

  public Node        model;
  public AnimControl animControl;
  public AnimChannel armChannel;
  public AnimChannel legChannel;
  public boolean     swing;
  public boolean     isDead;
  public int         score;
  public int         wood;
  public BetterCharacterControl playerPhys;
  public int fireLevel;
  
  public void swing() {
    armChannel.setAnim("ArmSwing");
    armChannel.setSpeed(2);
    armChannel.setLoopMode(LoopMode.DontLoop);
    swing = true;
    }
  
  public void run(){
    armChannel.setAnim("ArmRun");
    legChannel.setAnim("LegRun");
    }
  
  public void idle(){
    armChannel.setAnim("ArmIdle");
    legChannel.setAnim("LegsIdle");  
    }
    
}
