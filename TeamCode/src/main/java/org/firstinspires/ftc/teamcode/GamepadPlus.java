package org.firstinspires.ftc.teamcode;


import static com.qualcomm.robotcore.hardware.Gamepad.Type.SONY_PS4;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


public class GamepadPlus {
    private Gamepad gp;
    private ElapsedTime timer;

    //private ArrayList<String> indexHandler = new ArrayList<>();
    //private ArrayList<Long> timeActivated = new ArrayList<>();

    private HashMap<String, Double> currentInputs;
    private HashMap<String, Double> previousInputs;

    private static final float TRIGGER_THRESHOLD = 0.7f;

    private boolean isPS4Controller;
    private boolean ltButton;
    private boolean rtButton;

    public enum BUTTONS{
        a,b,x,y,
        dpad_up,dpad_down,dpad_left,dpad_right,
        left_bumper,right_bumper,left_trigger,right_trigger,
        circle,cross,triangle,square;

        public String toString(){
            switch (this){
                case a: return "a";
                case b: return "b";
                case x: return "x";
                case y: return "y";

                case dpad_down:     return "dd";
                case dpad_left:     return "dl";
                case dpad_right:    return "dr";
                case dpad_up:       return "du";

                case cross:     return "a";
                case circle:    return "b";
                case square:    return "x";
                case triangle:  return "y";

                case left_bumper:   return "bl";
                case right_bumper:  return "br";

                case left_trigger:  return "tl";
                case right_trigger: return "tr";

                default: return null;
            }
        }
    }



    public GamepadPlus(@NotNull Gamepad gp) {
        this.gp = gp;

        isPS4Controller = gp.type().equals(SONY_PS4);

        currentInputs = new HashMap<String, Double>();
        previousInputs = new HashMap<String, Double>();

        ltButton = false;
        rtButton = false;

        timer = new ElapsedTime();
    }

    public GamepadPlus(@NotNull Gamepad gp, boolean makeTriggersButtons){
        this.gp = gp;

        isPS4Controller = gp.type().equals(SONY_PS4);

        currentInputs = new HashMap<String, Double>();
        previousInputs = new HashMap<String, Double>();

        ltButton = makeTriggersButtons;
        rtButton = makeTriggersButtons;

        timer = new ElapsedTime();
    }

    public GamepadPlus(@NotNull Gamepad gp, boolean makeLeftTriggerButton, boolean makeRightTriggerButton){
        this.gp = gp;

        isPS4Controller = gp.type().equals(SONY_PS4);

        currentInputs = new HashMap<String, Double>();
        previousInputs = new HashMap<String, Double>();

        ltButton = makeLeftTriggerButton;
        rtButton = makeRightTriggerButton;

        timer = new ElapsedTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(){
        /*if(gp.type().equals(UNKNOWN)){ //Prevents update from running if not controller is plugged in
            return;
        }*/

        //timer = new ElapsedTime();             // Update the timer

        // Run button updates
        if(isPS4Controller){ // Have to separate PS4 buttons
            updateButtonTime(gp.circle,     BUTTONS.circle.toString());
            updateButtonTime(gp.cross,      BUTTONS.cross.toString());
            updateButtonTime(gp.triangle,   BUTTONS.triangle.toString());
            updateButtonTime(gp.square,     BUTTONS.square.toString());
        } else {
            updateButtonTime(gp.a,  BUTTONS.a.toString());
            updateButtonTime(gp.b,  BUTTONS.b.toString());
            updateButtonTime(gp.x,  BUTTONS.x.toString());
            updateButtonTime(gp.y,  BUTTONS.y.toString());
        }

        updateButtonTime(gp.dpad_up,    BUTTONS.dpad_up.toString());
        updateButtonTime(gp.dpad_down,  BUTTONS.dpad_down.toString());
        updateButtonTime(gp.dpad_left,  BUTTONS.dpad_left.toString());
        updateButtonTime(gp.dpad_right, BUTTONS.dpad_right.toString());

        updateButtonTime(gp.left_bumper,    BUTTONS.left_bumper.toString());
        updateButtonTime(gp.right_bumper,   BUTTONS.right_bumper.toString());

        if(ltButton){
            updateButtonTime((gp.left_trigger > TRIGGER_THRESHOLD), BUTTONS.left_trigger.toString());
        }
        if(rtButton){
            updateButtonTime((gp.right_trigger > TRIGGER_THRESHOLD), BUTTONS.right_trigger.toString());
        }
    }

    public boolean isPressed(String button){
        return currentInputs.containsKey(normalizeButtonInputs(button)) ? currentInputs.get(normalizeButtonInputs(button)) != 0 : false;
        //return indexHandler.indexOf(convertButtonInputs(button)) != -1;
    }

    public double howLongPressed(String button){
        return currentInputs.containsKey(normalizeButtonInputs(button)) ? timer.milliseconds() - currentInputs.get(normalizeButtonInputs(button)) : -1;
        //if(!isPressed(button)) return 0;

        //return timer.getTime() - timeActivated.get(indexHandler.indexOf(convertButtonInputs(button)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean wasJustPressed(String button){
        return currentInputs.getOrDefault(normalizeButtonInputs(button),-1.0) != -1.0 &&
               previousInputs.getOrDefault(normalizeButtonInputs(button),-1.0) == -1.0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean hasBeenPressed(String button){
        return previousInputs.getOrDefault(normalizeButtonInputs(button),-1.0) != -1.0;
    }

    public double getTimer(){
        return timer.milliseconds();
    }

    /*public ArrayList<String> getIndexHandler(){
        return indexHandler;
    }

    public ArrayList<Long> getTimeActivated(){
        return timeActivated;
    }*/
    public Set<String> getCurrentIndexes(){
        return currentInputs.keySet();
    }

    public Collection<Double> getCurrentTimes(){
        return currentInputs.values();
    }

    public Set<String> getPreviousIndexes(){
        return previousInputs.keySet();
    }

    public Collection<Double> getPreviousTimes(){
        return previousInputs.values();
    }

    public void resetTimer(){timer.reset();}


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateButtonTime(boolean button, String name) {
        // This will only fail if someone presses and then releases a button in consecutive cycles
        if(button){
            if(!currentInputs.containsKey(name)){
                currentInputs.put(name,timer.milliseconds());
            } else if(!previousInputs.containsKey(name)) {
                previousInputs.put(name,currentInputs.get(name));
            }
        } else {
            if(currentInputs.containsKey(name)){
                currentInputs.put(name,-1.0);
            } else if(previousInputs.containsKey(name)){
                previousInputs.put(name,-1.0);
            }
        }

        //previousInputs.put(name,currentInputs.getOrDefault(name,-1.0));
        //currentInputs.put(name,button ? timer.milliseconds() : -1.0);
        /*if (button) { // button clicked
            inputs.put(name,timer.getTime());
            if (indexHandler.indexOf(name) == -1) { // button not in (cycle 0)
                indexHandler.add(name);
                timeActivated.add(timer.getTime());
            } else { // button is in; update time (cycle 1,2,3..,n-1)
                //timeActivated.set(indexHandler.indexOf(name), timer.getTime());
            }
        } else {
            if (indexHandler.indexOf(name) != -1) { // button just released (cycle n); remove from list
                timeActivated.remove(indexHandler.indexOf(name));
                indexHandler.remove(name);

                indexHandler.trimToSize();
                timeActivated.trimToSize();
            }
        }*/

    }


    // ensures compatibility between ps4 and non-ps4 controllers
    private String normalizeButtonInputs(String button){
        switch (button){
            case  "cross":
                return "a";
            case "circle":
                 return "b";
            case "square":
                return  "x";
            case "triangle":
                return  "y";
            default:
                return button;
        }
    }
}
