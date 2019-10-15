/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.solvation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class GridList extends ArrayList<Grid> implements Serializable {
    
    private static final long serialVersionUID = 6053340124961244739L;

    public GridList() {
        super();
    }
    
    public GridList(GridList theGridList) {
        super(theGridList);
    }
    
    public GridList(List<Grid> theGridList) {
        super(theGridList);
    }
}
