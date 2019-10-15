/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.reaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 07. 09
 */
public class ReactionList implements Serializable, Iterable<Reaction> {
    private static final long serialVersionUID = -907856143379185950L;

    private List<Reaction> itsReactionList;

    public ReactionList() {
        this.itsReactionList = new ArrayList<>();
    }

    public ReactionList(List<Reaction> theReactionList) {
        this.itsReactionList = new ArrayList<>(theReactionList);
    }
    
    public Reaction getReaction(int theIndex) {
        return this.itsReactionList.get(theIndex);
    }
    
    public void setReaction(int theIndex, Reaction theReaction) {
        this.itsReactionList.set(theIndex, theReaction);
    }
    
    public void addReaction(Reaction theReaction) {
        this.itsReactionList.add(theReaction);
    }
    
    public void addReactionList(ReactionList theReactionList) {
        this.itsReactionList.addAll(theReactionList.itsReactionList);
    }
    
    public void removeReaction(int theIndex) {
        this.itsReactionList.remove(theIndex);
    }
    
    public boolean isEmpty() {
        return this.itsReactionList.isEmpty();
    }

    @Override
    public Iterator<Reaction> iterator() {
        return this.itsReactionList.iterator();
    }
    
    public int size() {
        return this.itsReactionList.size();
    }
}
