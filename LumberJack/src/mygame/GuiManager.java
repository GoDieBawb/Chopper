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
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.extras.android.Joystick;
import tonegod.gui.controls.text.TextElement;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;

/**
 *
 * @author Bob
 */
public class GuiManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private Indicator         treeInd;
  public  Indicator         woodInd;
  private TextElement       scoreText;
  private TextElement       titleText;
  private ButtonAdapter     startButton;
  private Joystick          stick;
  private Screen            screen;
  private BitmapFont        font;
  private Player            player;
  private Node              treeNode;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.player       = this.app.getStateManager().getState(PlayerManager.class).player;
    font              = this.assetManager.loadFont("Interface/Fonts/Impact.fnt");
    screen            = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
    treeNode          = stateManager.getState(TreeManager.class).treeNode;
    screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
    screen.setUseMultiTouch(true);
    this.app.getGuiNode().addControl(screen);
    this.app.getInputManager().setSimulateMouse(true);
    this.app.getGuiViewPort().setBackgroundColor(ColorRGBA.Cyan);
    
    stateManager.getState(InteractionManager.class).setEnabled(false);
    stateManager.getState(PlayerManager.class).setEnabled(false);
    stateManager.getState(TreeManager.class).setEnabled(false);
    
    //initTreeLevel();
    initScoreDisplay();
    initTitleDisplay();
    initJoyStick();
    initStartButton();
    }
  
  private void initStartButton(){
    startButton = new ButtonAdapter( screen, "StartButton", new Vector2f(15, 15) ) {
    
    @Override
      public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
        System.out.println("Start Button Pressed");
        startButton.hide();
        titleText.hide();
        stateManager.getState(PlayerManager.class).player.isDead = true;
        stateManager.getState(InteractionManager.class).setEnabled(true);
        stateManager.getState(PlayerManager.class).setEnabled(true);
        stateManager.getState(TreeManager.class).treeNode.detachAllChildren();
        stateManager.getState(TreeManager.class).setEnabled(true);
        //Resets the player
        player.isDead = false;
        player.score = 0;
        }
      };
    
    //Sets up the start button details
    screen.addElement(startButton);
    startButton.setDimensions(screen.getWidth()/5, screen.getHeight()/10);
    startButton.setPosition(screen.getWidth() / 2 - startButton.getWidth()/2, screen.getHeight() / 2);
    startButton.setFont("Interface/Fonts2/Impact.fnt");
    startButton.setText("Start");
    }
  
  private void initTitleDisplay(){
    titleText = new TextElement(screen, "TitleText", Vector2f.ZERO, new Vector2f(300,50), font) {
    @Override
    public void onUpdate(float tpf) { }
    @Override
    public void onEffectStart() { }
    @Override
    public void onEffectStop() { }
    };
    
    //Sets up the details of the title display
    titleText.setIsResizable(false);
    titleText.setIsMovable(false);
    titleText.setTextWrap(LineWrapMode.NoWrap);
    titleText.setTextVAlign(VAlign.Center);
    titleText.setTextAlign(Align.Center);
 
    //Add the title display
    screen.addElement(titleText);
    
    titleText.setFontColor(ColorRGBA.Red);
    
    titleText.setText("LumberJack");
    titleText.setLocalTranslation(screen.getWidth() / 2f - titleText.getWidth()/1.8f, screen.getHeight() /2f + titleText.getHeight(), -1);
    }
  
  public void showTitleText(int highScore, boolean isHigh){
    startButton.show();
    titleText.show();
    if (isHigh)
    titleText.setText("New HighScore: " + highScore);
    else
    titleText.setText("HighScore: " + highScore);    
    titleText.setLocalTranslation(screen.getWidth() / 2f - titleText.getWidth()/2f, screen.getHeight() /2f + titleText.getHeight()*1.2f, -1);
    startButton.setText("Play Again");
    }

  private void initJoyStick(){
    stick = new Joystick(screen, Vector2f.ZERO, (int)screen.getWidth()/6) {
    @Override
    public void onUpdate(float tpf, float deltaX, float deltaY) {
        float dzVal = .2f;  // Dead zone threshold

            if (deltaX < -dzVal) {
              stateManager.getState(InteractionManager.class).left  = true;
              stateManager.getState(InteractionManager.class).right = false;
              } 
            
            else if (deltaX > dzVal) {
              stateManager.getState(InteractionManager.class).right = true;
              stateManager.getState(InteractionManager.class).left  = false;
              }
            
            else {
              stateManager.getState(InteractionManager.class).right = false;
              stateManager.getState(InteractionManager.class).left  = false; 
              }
            
        
            if (deltaY < -dzVal) {
              stateManager.getState(InteractionManager.class).down = true;
              stateManager.getState(InteractionManager.class).up   = false;
              } 
            
            else if (deltaY > dzVal) {
              stateManager.getState(InteractionManager.class).down = false;
              stateManager.getState(InteractionManager.class).up   = true;
              }
            
            else {
              stateManager.getState(InteractionManager.class).up   = false;
              stateManager.getState(InteractionManager.class).down = false;    
              }
            
          player.speedMult  = FastMath.abs(deltaY);
          player.strafeMult = FastMath.abs(deltaX);
          
          }
    
        };
      // getGUIRegion returns region info “x=0|y=0|w=50|h=50″, etc
      TextureKey key = new TextureKey("Textures/barrel/D.png", false);
      Texture tex = assetManager.loadTexture(key);
      stick.setTextureAtlasImage(tex, "x=20|y=20|w=120|h=35");
      stick.getThumb().setTextureAtlasImage(tex, "x=20|y=20|w=120|h=35");
      screen.addElement(stick, true);
      stick.setPosition(screen.getWidth()/10 -  stick.getWidth()/2, screen.getHeight()/10 -  stick.getHeight()/2);
      // Add some fancy effects
      Effect fxIn = new Effect(Effect.EffectType.FadeIn, Effect.EffectEvent.Show,.5f);
      stick.addEffect(fxIn);
      Effect fxOut = new Effect(Effect.EffectType.FadeOut, Effect.EffectEvent.Hide,.5f);
      stick.addEffect(fxOut);
      stick.show();
      }


  //Sets up the score display
  private void initScoreDisplay(){
    scoreText = new TextElement(screen, "ScoreText", Vector2f.ZERO, new Vector2f(300,50), font) {
    @Override
    public void onUpdate(float tpf) { }
    @Override
    public void onEffectStart() { }
    @Override
    public void onEffectStop() { }
    };
    
    //Sets up the details of the score display
    scoreText.setIsResizable(false);
    scoreText.setIsMovable(false);
    scoreText.setTextWrap(LineWrapMode.NoWrap);
    scoreText.setTextVAlign(VAlign.Center);
    scoreText.setTextAlign(Align.Center);
    scoreText.setFontSize(18);
 
    //Add the score display
    screen.addElement(scoreText);
    scoreText.setFontColor(ColorRGBA.Red);
    
    scoreText.setText("Trees Murdered: " + player.score);
    scoreText.setLocalTranslation(screen.getWidth() / 1.1f - scoreText.getWidth()/1.8f, screen.getHeight() / 1.05f - scoreText.getHeight()/2, -1);
    }
  
  private void initTreeLevel(){
    treeInd = new Indicator(
      screen,
      "Tree Ind",
      new Vector2f(12,12),
      Indicator.Orientation.HORIZONTAL
      ) {

    @Override
      public void onChange(float currentValue, float currentPercentage) {
        }
      };
    
    treeInd.setMaxValue(20);
    treeInd.setCurrentValue(10f);
    treeInd.setIndicatorColor(ColorRGBA.Red);
    treeInd.setText("Trees");
    treeInd.setTextWrap(LineWrapMode.NoWrap);
    treeInd.setTextVAlign(VAlign.Center);
    treeInd.setTextAlign(Align.Center);
    treeInd.setFont("Interface/Fonts2/Impact.fnt");
    treeInd.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
    treeInd.setIndicatorPadding(new Vector4f(7,7,7,7));
    treeInd.setDimensions(150, 30);
    screen.addElement(treeInd);
    }
  
  private void updateHud(){
    scoreText.setText("Trees Murdered: " + player.score);
    }
  
  
  
  @Override
  public void update(float tpf){

    updateHud();
  }
}
