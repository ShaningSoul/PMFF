/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.predock.enums;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public enum RotationStepSize {
    One(1.0), 
    Two(2.0),
    Three(3.0),
    Four(4.0);
    
    public final Double STEP_SIZE;

    private RotationStepSize(Double STEP_SIZE) {
        this.STEP_SIZE = STEP_SIZE;
    }
}
