/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.reaction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 07. 09
 */
public class Reaction implements Serializable {

    private static final long serialVersionUID = -1910058409991965480L;

    private IAtomContainerSet itsSubstrateSet;
    private IAtomContainerSet itsProductSet;
    private String itsName;
    private Map itsProperties;

    public Reaction() {
        this.itsProperties = new HashMap<>();
    }

    public Reaction(IAtomContainerSet theSubstrateSet, IAtomContainerSet theProductSet, Map theProperties, String theName) {
        try {
            this.itsSubstrateSet = (IAtomContainerSet) theSubstrateSet.clone();
            this.itsProductSet = (IAtomContainerSet) theProductSet.clone();
            this.itsProperties = new HashMap<>(theProperties);
            this.itsName = theName;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public Reaction(Reaction theReaction) {
        try {
            this.itsSubstrateSet = (IAtomContainerSet) theReaction.itsSubstrateSet.clone();
            this.itsProductSet = (IAtomContainerSet) theReaction.itsProductSet.clone();
            this.itsProperties = new HashMap<>(theReaction.itsProperties);
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public IAtomContainerSet getSubstrateSet() {
        return itsSubstrateSet;
    }

    public void setSubstrateSet(IAtomContainerSet theSubstrateSet) {
        this.itsSubstrateSet = theSubstrateSet;
    }

    public IAtomContainerSet getProductSet() {
        return itsProductSet;
    }

    public void setProductSet(IAtomContainerSet theProductSet) {
        this.itsProductSet = theProductSet;
    }

    public String getName() {
        return itsName;
    }

    public void setName(String theName) {
        this.itsName = theName;
    }

    public Map<Object, Object> getProperties() {
        return itsProperties;
    }

    public void setProperties(Map<Object, Object> theProperties) {
        this.itsProperties = theProperties;
    }
    
    public Object getProperty(Object theObject) {
        return this.itsProperties.get(theObject);
    }

    public IAtomContainer getSubstrate(int theIndex) {
        return this.itsSubstrateSet.getAtomContainer(theIndex);
    }

    public IAtomContainer getProduct(int theIndex) {
        return this.itsProductSet.getAtomContainer(theIndex);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.itsSubstrateSet);
        hash = 71 * hash + Objects.hashCode(this.itsProductSet);
        hash = 71 * hash + Objects.hashCode(this.itsProperties);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reaction other = (Reaction) obj;
        if (!Objects.equals(this.itsSubstrateSet, other.itsSubstrateSet)) {
            return false;
        }
        if (!Objects.equals(this.itsProductSet, other.itsProductSet)) {
            return false;
        }
        if (!Objects.equals(this.itsProperties, other.itsProperties)) {
            return false;
        }
        return true;
    }
}
