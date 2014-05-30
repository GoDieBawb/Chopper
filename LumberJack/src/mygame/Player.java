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
  public int         fireLevel;
  public float       speedMult;
  public float       strafeMult;
  public boolean     hasSwung;
  public long        swingDelay;
  
  public void swing() {
      
    if (!hasSwung) {
      armChannel.setAnim("ArmSwing");
      armChannel.setSpeed(2);
      armChannel.setLoopMode(LoopMode.DontLoop);
      swingDelay = System.currentTimeMillis()/1000;
      swing      = true;
      }
    
    }
  
  public void run(){
    if (!armChannel.getAnimationName().equals("ArmRun") && !hasSwung){
      armChannel.setAnim("ArmRun");
      }
    
    if (!legChannel.getAnimationName().equals("LegRun")){
      legChannel.setAnim("LegRun");
      }
    
    }
  
  public void idle(){

    if (!armChannel.getAnimationName().equals("ArmIdle") && !hasSwung){
      armChannel.setAnim("ArmIdle");
      }
    
    if (!legChannel.getAnimationName().equals("LegsIdle")){
      legChannel.setAnim("LegsIdle");  
      }
    
    }
    
}
