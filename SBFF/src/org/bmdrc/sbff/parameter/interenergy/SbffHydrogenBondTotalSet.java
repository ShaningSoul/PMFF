/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.parameter.interenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bmdrc.sbff.atomtype.HydrogenBondAtomTypeGenerator;
import org.openscience.cdk.interfaces.IAtom;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffHydrogenBondTotalSet implements Serializable {
    private static final long serialVersionUID = -1018176864615842625L;

    private List<SbffHydrogenBondParameterSet> itsTotalParameterSet;
    //constant Integer variable
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    private final int THIRD_INDEX = 2;
    private final int FOURTH_INDEX = 3;

    public SbffHydrogenBondTotalSet() {
        this.__generateTotalParameterSet();
    }

    public List<SbffHydrogenBondParameterSet> getTotalParameterSet() {
        return itsTotalParameterSet;
    }

    public void setTotalParameterSet(List<SbffHydrogenBondParameterSet> theTotalParameterSet) {
        this.itsTotalParameterSet = theTotalParameterSet;
    }

    public List<SbffHydrogenBondParameterSet> setTotalParameterSet() {
        return itsTotalParameterSet;
    }

    private void __generateTotalParameterSet() {
        this.itsTotalParameterSet = new ArrayList<>();

        //add original paper
        this.itsTotalParameterSet.add(this.__getAceteMethanolParameter());
        this.itsTotalParameterSet.add(this.__getAcetamideDimerParameter());                                                //N3-H2-O2-C4
        this.itsTotalParameterSet.add(this.__getAceticAcidDimerParameterInOpenChain());                                    //O3-H4-O1-C3
        this.itsTotalParameterSet.add(this.__getFirstAcetamideAndAceticAcidDimer());                                       //O3-H4-O2-C4
        this.itsTotalParameterSet.add(this.__getSecondAcetamideAndAceticAcidDimer());                                      //N3-H2-O1-C3
        this.itsTotalParameterSet.add(this.__getAmmoniumAndCarboxylateIon());                                              //N5-H5-O5-C5

        //add approximation function
        //Amine donor
        this.itsTotalParameterSet.add(this.__getAlcholAndPrimaryAminePairFocusedOnPrimaryAmine());                         //N4-H1-O3-C1
        this.itsTotalParameterSet.add(this.__getAlcholAndSecondaryAminePairFocusedOnSecondaryAmine());                     //N4-H1-O3-C1
        this.itsTotalParameterSet.add(this.__getPrimaryAmineAndCarboxylAlcholFocusedOnPrimaryAmine());                     //N4-H1-O3-C1
        this.itsTotalParameterSet.add(this.__getSecondaryAmineAndCarboxylAlcholFocusedOnSecondaryAmine());                 //N4-H1-O3-C1
        this.itsTotalParameterSet.add(this.__getPrimaryAmineAndAmideNitrogenFocusedOnPrimaryAmine());                      //N4-H1-N3-C1
        this.itsTotalParameterSet.add(this.__getSecondaryAmineAndAmideNitrogenFocusedOnSecondaryAmine());                  //N4-H1-N3-C1
        this.itsTotalParameterSet.add(this.__getAmideOxygenAndPrimaryAmine());                                             //N4-H1-O2-C4
        this.itsTotalParameterSet.add(this.__getAmideOxygenAndSecondaryAmine());                                           //N4-H1-O2-C4
        this.itsTotalParameterSet.add(this.__getTwoPrimaryAmine());                                                        //N4-H1-N4-C1
        this.itsTotalParameterSet.add(this.__getTwoSecondaryAmine());                                                      //N4-H1-N4-C1
        this.itsTotalParameterSet.add(this.__getPrimaryAndSecondaryAmineFocusedOnPrimaryAmine());                          //N4-H1-N4-C1
        this.itsTotalParameterSet.add(this.__getPrimaryAndSecondaryAmineFocusedOnSecondaryAmine());                        //N4-H1-N4-C1
        this.itsTotalParameterSet.add(this.__getPrimaryAmineAndAmmoniumFocusedOnPrimaryAmine());                           //N4-H1-N5-C5
        this.itsTotalParameterSet.add(this.__getSecondaryAmineAndAmmoniumFocusedOnSecondaryAmine());                       //N4-H1-N5-C5
        this.itsTotalParameterSet.add(this.__getCarboxylateAndPrimaryAmine());                                             //N4-H1-O5-C5
        this.itsTotalParameterSet.add(this.__getCarboxylateAndSecondaryAmine());                                           //N4-H1-O5-C5
        this.itsTotalParameterSet.add(this.__getPrimaryAmineAndCarboxylDoubleBondedOxygenFocusedOnPrimaryAmine());         //N4-H1-O1-C3
        this.itsTotalParameterSet.add(this.__getSecondaryAmineAndCarboxylDoubleBondedOxygenFocusedOnSecondaryAmine());     //N4-H1-O1-C3

        //Amide donor
        this.itsTotalParameterSet.add(this.__getAmideAndAlcholFocusedOnAmide());                                           //N3-H2-O3-C1
        this.itsTotalParameterSet.add(this.__getAmideAndCarboxylAlcholFocusedOnAmide());                                   //N3-H2-O3-C1
        this.itsTotalParameterSet.add(this.__getTwoAmide());                                                               //N3-H2-N3-C1
        this.itsTotalParameterSet.add(this.__getPrimaryAmineAndAmideNitrogenFocusedOnAmide());                             //N3-H2-N4-C1
        this.itsTotalParameterSet.add(this.__getSecondaryAmineAndAmideNitrogenFocusedOnAmide());                           //N3-H2-N4-C1
        this.itsTotalParameterSet.add(this.__getAmideNitrogenAndAmmoniumIonByAmideNitrogen());                             //N3-H2-N5-C5
        this.itsTotalParameterSet.add(this.__getAmideNitrogenCarboxylateIonIonByAmideNitrogen());                          //N3-H2-O5-C5

        //Carboxyl OH & Sp3 OH donor
        this.itsTotalParameterSet.add(this.__getTwoAlchol());                                                              //O3-H4-O3-C1
        this.itsTotalParameterSet.add(this.__getAlcholAndCarboxylAlcholByAlchol());                                        //O3-H4-O3-C1
        this.itsTotalParameterSet.add(this.__getTwoCarboxylAlcholByAlchol());                                              //O3-H4-O3-C1
        this.itsTotalParameterSet.add(this.__getAlcholAndAmideNitrogenByAlchol());                                         //O3-H4-N3-C1
        this.itsTotalParameterSet.add(this.__getCarboxylAlcholAndAmideByCarboxylAlchol());                                 //O3-H4-N3-C1
        this.itsTotalParameterSet.add(this.__getAlcholAndAmideOxygenByAlchol());                                           //O3-H4-O2-C1
        this.itsTotalParameterSet.add(this.__getAlcholAndPrimaryAminePairFocusedOnAlchol());                               //O3-H4-N4-C1
        this.itsTotalParameterSet.add(this.__getAlcholAndSecondaryAminePairFocusedOnAlchol());                             //O3-H4-N4-C1
        this.itsTotalParameterSet.add(this.__getAlcholAndAmmoniumByAlchol());                                              //O3-H4-N5-C5
        this.itsTotalParameterSet.add(this.__getCarboxylAlcholAndAmmoniumByCarboxylAlchol());                              //O3-H4-N5-C5
        this.itsTotalParameterSet.add(this.__getCarboxylateAndAlchol());                                                   //O3-H4-O5-C5
        this.itsTotalParameterSet.add(this.__getCarboxylAlcholAndCarboxylateByCarboxylAlchol());                           //O3-H4-O5-C5

        //Ammonium donor
        this.itsTotalParameterSet.add(this.__getAmmoniumAndAlcholFocusedOnAmmonium());                                     //N5-H5-O3-C1
        this.itsTotalParameterSet.add(this.__getAmmoniumAndCarboxylAlcholFocusedOnAmmonium());                             //N5-H5-O3-C1
        this.itsTotalParameterSet.add(this.__getAmmoniumAndAmideNitrogenByAmmonium());                                     //N5-H5-N3-C1
        this.itsTotalParameterSet.add(this.__getAmmoniumAndAmideOxygenByAmmonium());                                       //N5-H5-O2-C4
        this.itsTotalParameterSet.add(this.__getAmmoniumAndPrimaryAmineFocusedOnAmmonium());                               //N5-H5-N4-C1
        this.itsTotalParameterSet.add(this.__getAmmoniumAndSecondaryAmineFocusedOnAmmonium());                             //N5-H5-N4-C1
        this.itsTotalParameterSet.add(this.__getTwoAmmonium());                                                            //N5-H5-N5-C5
        this.itsTotalParameterSet.add(this.__getAmmoniumAndCarboxylDoubleBondedOxygenByAmmonium());                        //N5-H5-O1-C3
    }

    private SbffHydrogenBondParameterSet __getAlcholAndAmideOxygenByAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 380.5, 10288.0, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 318.9, 418988.0, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 118.7, 241139.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }
    
    private SbffHydrogenBondParameterSet __getAmmoniumAndCarboxylDoubleBondedOxygenByAmmonium() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 134.5, 1367.8, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 224.4, 182838.6, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 60.5, 51996.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getTwoAmmonium() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 168.6, 1641.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 282.7, 221582.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 40.6, 29491.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmmoniumAndAmideOxygenByAmmonium() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 134.5, 1367.8, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 224.4, 182838.6, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 60.5, 51996.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmmoniumAndAmideNitrogenByAmmonium() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 160.3, 2332.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 257.0, 287937.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 66.0, 112772.9, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAlcholAndAmmoniumByAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 166.2, 1674.0, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 196.9, 139739.1, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 40.0, 29981.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getCarboxylAlcholAndAmmoniumByCarboxylAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 166.2, 1674.0, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 196.9, 139739.1, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 40.0, 29981.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getCarboxylAlcholAndAmideByCarboxylAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 157.7, 2368.4, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 179.4, 195263.7, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 64.7, 113669.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getTwoCarboxylAlcholByAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 115.3, 1122.8, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 137.1, 101158.4, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 64.7, 113669.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAlcholAndCarboxylAlcholByAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 115.3, 1122.8, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 137.1, 101158.4, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 64.7, 113669.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAlcholAndAmideNitrogenByAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 157.7, 2368.4, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 179.4, 195263.7, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 64.7, 113669.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmideNitrogenCarboxylateIonIonByAmideNitrogen() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 153.2, 2026.9, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 224.9, 322789.3, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 40.2, 29513.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmideNitrogenAndAmmoniumIonByAmideNitrogen() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 167.0, 1644.2, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 257.0, 287937.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 40.2, 29513.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getTwoAmide() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 158.7, 2333.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 229.0, 359440.1, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmideAndCarboxylAlcholFocusedOnAmide() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 115.9, 1102.5, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 179.4, 195263.7, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmideAndAlcholFocusedOnAmide() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 115.9, 1102.5, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 179.4, 195263.7, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAceteMethanolParameter() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 123.9, 1627.2, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 73.9, 165194.3, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 301.3, 273720.5, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAcetamideDimerParameter() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 158.2, 2690.0, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 412.2, 976358.0, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 118.7, 262468.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAceticAcidDimerParameterInOpenChain() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 288.3, 7516.0, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 318.9, 491722.0, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 118.7, 245891.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAceticAcidDimerParameterInCyclic() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 203.4, 2470.0, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 318.9, 180790.0, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 118.7, 211238.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getFirstAcetamideAndAceticAcidDimer() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 380.5, 10288.0, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 318.9, 418988.0, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 118.7, 241139.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getSecondAcetamideAndAceticAcidDimer() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 98.4, 867.0, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 412.2, 1328720.0, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 118.7, 235682.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAlcholAndPrimaryAminePairFocusedOnPrimaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 115.9, 1102.5, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 185.4, 201863.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAlcholAndSecondaryAminePairFocusedOnSecondaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 115.9, 1102.5, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 185.4, 201863.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getPrimaryAmineAndCarboxylAlcholFocusedOnPrimaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 115.9, 1102.5, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 185.4, 201863.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getSecondaryAmineAndCarboxylAlcholFocusedOnSecondaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 115.9, 1102.5, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 185.4, 201863.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAlcholAndPrimaryAminePairFocusedOnAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 157.7, 2368.4, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 185.4, 201863.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 64.7, 113669.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAlcholAndSecondaryAminePairFocusedOnAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 157.7, 2368.4, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 185.4, 201863.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 64.7, 113669.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getTwoPrimaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 158.7, 2333.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 236.8, 371588.3, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getTwoSecondaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 158.7, 2333.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 236.8, 371588.3, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getPrimaryAndSecondaryAmineFocusedOnPrimaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 158.7, 2333.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 236.8, 371588.3, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getPrimaryAndSecondaryAmineFocusedOnSecondaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 158.7, 2333.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 236.8, 371588.3, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getTwoAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 115.3, 1122.8, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 141.8, 104577.3, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 64.7, 113669.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmmoniumAndCarboxylateIon() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 337.0, 6760.0, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 508.0, 896086.0, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 82.4, 58517.0, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmmoniumAndAlcholFocusedOnAmmonium() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 117.0, 1100.3, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 196.9, 149739.1, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 66.0, 112772.9, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmmoniumAndCarboxylAlcholFocusedOnAmmonium() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 117.0, 1100.3, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 196.9, 149739.1, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 66.0, 112772.9, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmmoniumAndPrimaryAmineFocusedOnAmmonium() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 160.3, 2332.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 257.0, 287937.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 66.0, 112772.9, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmmoniumAndSecondaryAmineFocusedOnAmmonium() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 160.3, 2332.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 257.0, 287937.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 66.0, 112772.9, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getCarboxylateAndPrimaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 153.2, 2026.9, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 224.9, 322789.3, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 40.2, 29513.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getCarboxylateAndSecondaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 153.2, 2026.9, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 224.9, 322789.3, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 40.2, 29513.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getCarboxylAlcholAndCarboxylateByCarboxylAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 152.4, 2059.2, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 175.1, 173380.4, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 40.0, 29981.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getCarboxylateAndAlchol() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 152.4, 2059.2, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 175.1, 173380.4, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 40.0, 29981.2, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getPrimaryAmineAndAmideNitrogenFocusedOnPrimaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 169.0, 4612.4, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 229.0, 359440.1, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 64.9, 193335.4, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getSecondaryAmineAndAmideNitrogenFocusedOnSecondaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 169.0, 4612.4, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 229.0, 359440.1, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 64.9, 193335.4, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getPrimaryAmineAndAmideNitrogenFocusedOnAmide() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 158.7, 2333.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 229.0, 359440.1, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getSecondaryAmineAndAmideNitrogenFocusedOnAmide() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 158.7, 2333.1, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 229.0, 359440.1, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 65.3, 112534.8, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmideOxygenAndPrimaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 133.2, 1370.2, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 203.5, 236525.5, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 59.9, 52004.1, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getAmideOxygenAndSecondaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 133.2, 1370.2, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 203.5, 236525.5, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 59.9, 52004.1, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

        private SbffHydrogenBondParameterSet __getPrimaryAmineAndAmmoniumFocusedOnPrimaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 185.3, 3524.5, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 257.0, 287937.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 43.3, 59451.4, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getSecondaryAmineAndAmmoniumFocusedOnSecondaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 185.3, 3524.5, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 257.0, 287937.2, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBON_IN_AMMONIUM_ION);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 43.3, 59451.4, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getPrimaryAmineAndCarboxylDoubleBondedOxygenFocusedOnPrimaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 133.2, 1370.2, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 203.5, 236525.5, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 59.9, 52004.1, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    private SbffHydrogenBondParameterSet __getSecondaryAmineAndCarboxylDoubleBondedOxygenFocusedOnSecondaryAmine() {
        SbffHydrogenBondParameterSet theSbffHydrogenBondParameterSet = new SbffHydrogenBondParameterSet();
        List<Integer> theAtomTypeList = new ArrayList<>();
        List<Integer> theComponenetAtomTypeList = new ArrayList<>();

        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setAtomTypeList(theAtomTypeList);

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 133.2, 1370.2, SbffHydrogenBondParameter.HA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 203.5, 236525.5, SbffHydrogenBondParameter.XA_PARAMETER));
        theComponenetAtomTypeList.clear();

        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
        theComponenetAtomTypeList.add(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
        theSbffHydrogenBondParameterSet.setParameterSet().add(new SbffHydrogenBondParameter(new ArrayList<>(theComponenetAtomTypeList), 59.9, 52004.1, SbffHydrogenBondParameter.BH_PARAMETER));
        theComponenetAtomTypeList.clear();

        return theSbffHydrogenBondParameterSet;
    }

    public SbffHydrogenBondParameterSet getParameterSet(List<IAtom> theAtomList) {
        for(SbffHydrogenBondParameterSet theParameterSet : this.getTotalParameterSet()) {
            if(this.__isSameAtomType(theParameterSet, theAtomList)) {
                return theParameterSet;
            }
        }

        return null;
    }

    private boolean __isSameAtomType(SbffHydrogenBondParameterSet theParameterSet, List<IAtom> theAtomList) {
        for(int vi = 0, vEnd = theParameterSet.getAtomTypeList().size(); vi < vEnd; vi++) {
            Integer theAtomType = (Integer)theAtomList.get(vi).getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY);
            Integer theParameterAtomType = theParameterSet.getAtomTypeList().get(vi);

            if(!theParameterAtomType.equals(theAtomType)) {
                return false;
            }
        }

        return true;
    }
}
