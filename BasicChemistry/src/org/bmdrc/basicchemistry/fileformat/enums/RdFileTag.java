/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.basicchemistry.fileformat.enums;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public enum RdFileTag {

    RXN("$RXN"), MOL("$MOL"), DTYPE("$DTYPE"), DATUM("$DATUM");

    public final String TAG;

    private RdFileTag(String TAG) {
        this.TAG = TAG;
    }
}
