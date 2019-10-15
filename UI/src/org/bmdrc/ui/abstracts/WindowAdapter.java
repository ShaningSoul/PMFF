package org.bmdrc.ui.abstracts;

import java.io.Serializable;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class WindowAdapter implements Serializable {

    private static final long serialVersionUID = -1457834745610963367L;

    public static Mode getEditorMode() {
        return WindowManager.getDefault().findMode("editor");
    }
    
    public static Mode getNavigatorMode() {
        return WindowManager.getDefault().findMode("navigator");
    }
    
    public static Mode getPropertiesMode() {
        return WindowManager.getDefault().findMode("properties");
    }
    
    public static Mode getOutputMode() {
        return WindowManager.getDefault().findMode("output");
    }
    
    public static <Type extends TopComponent> Type getTopComponentInEditor(String theName, Class<Type> theClass) {
        return WindowAdapter.__getTopComponent(WindowAdapter.getEditorMode(), theName, theClass);
    }
    
    public static <Type extends TopComponent> Type getTopComponentInNavigator(String theName, Class<Type> theClass) {
        return WindowAdapter.__getTopComponent(WindowAdapter.getNavigatorMode(), theName, theClass);
    }
    
    public static <Type extends TopComponent> Type getTopComponentInProperties(String theName, Class<Type> theClass) {
        return WindowAdapter.__getTopComponent(WindowAdapter.getPropertiesMode(), theName, theClass);
    }
    
    public static <Type extends TopComponent> Type getTopComponentInOutput(String theName, Class<Type> theClass) {
        return WindowAdapter.__getTopComponent(WindowAdapter.getOutputMode(), theName, theClass);
    }
    
    private static <Type extends TopComponent> Type __getTopComponent(Mode theMode, String theName, Class<Type> theClass) {
        TopComponent[] theTopComponents = theMode.getTopComponents();
        
        for(TopComponent theTopComponent : theTopComponents) {
            if(theTopComponent.getName().equals(theName)) {
                return theClass.cast(theTopComponent);
            }
        }
        
        return null;
    }
}
