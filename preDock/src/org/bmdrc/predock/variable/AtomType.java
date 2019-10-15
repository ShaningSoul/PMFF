package org.bmdrc.predock.variable;

import java.io.Serializable;
import java.util.Objects;
import org.bmdrc.sbff.atomtype.HydrogenBondAtomTypeGenerator;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import org.bmdrc.sbff.interenergy.SbffNonbondingEnergyFunction;
import org.bmdrc.sbff.tool.MpeoeChargeCalculator;
import org.openscience.cdk.interfaces.IAtom;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class AtomType implements Serializable, Comparable<AtomType> {

    private static final long serialVersionUID = -6882119350584646352L;

    private Integer itsMpeoeAtomType;
    private Integer itsHydrogenBondType;
    private String itsNonBondAtomType;
    private Double itsAtomicCharge;

    public AtomType(IAtom theAtom) {
        this.itsMpeoeAtomType = theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, Integer.class);
        this.itsHydrogenBondType = theAtom.getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, Integer.class);
        this.itsNonBondAtomType = theAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, String.class);
        this.setAtomicCharge(theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY, Double.class));
    }

    public Integer getMpeoeAtomType() {
        return this.itsMpeoeAtomType;
    }

    public void setMpeoeAtomType(Integer theMpeoeAtomType) {
        this.itsMpeoeAtomType = theMpeoeAtomType;
    }

    public String getNonBondAtomType() {
        return this.itsNonBondAtomType;
    }

    public void setNonBondAtomType(String theNonBondAtomType) {
        this.itsNonBondAtomType = theNonBondAtomType;
    }

    public Double getAtomicCharge() {
        return itsAtomicCharge;
    }

    public void setAtomicCharge(Double theAtomicCharge) {
        Double theValue = theAtomicCharge;
        
        theValue *= 100000.0;
        theValue = (double)(Math.round(theValue));
        theValue /= 100000.0;
        
        this.itsAtomicCharge = theValue;
    }

    public Integer getHydrogenBondType() {
        return itsHydrogenBondType;
    }

    public void setHydrogenBondType(Integer theHydrogenBondType) {
        this.itsHydrogenBondType = theHydrogenBondType;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.itsMpeoeAtomType);
        hash = 61 * hash + Objects.hashCode(this.itsHydrogenBondType);
        hash = 61 * hash + Objects.hashCode(this.itsNonBondAtomType);
        hash = 61 * hash + Objects.hashCode(this.itsAtomicCharge);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AtomType other = (AtomType) obj;
        if (!Objects.equals(this.itsNonBondAtomType, other.itsNonBondAtomType)) {
            return false;
        }
        if (!Objects.equals(this.itsMpeoeAtomType, other.itsMpeoeAtomType)) {
            return false;
        }
        if (!Objects.equals(this.itsHydrogenBondType, other.itsHydrogenBondType)) {
            return false;
        }
        if (!Objects.equals(this.itsAtomicCharge, other.itsAtomicCharge)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        theStringBuilder.append("[")
                .append("Mpeoe Atom Type : ").append(this.itsMpeoeAtomType)
                .append(", Hydrogen bond type : ").append(this.itsHydrogenBondType)
                .append(", Nonbonding atom type : ").append(this.itsNonBondAtomType)
                .append(", Charge : ").append(this.itsAtomicCharge)
                .append("]");

        return theStringBuilder.toString();
    }

    @Override
    public int compareTo(AtomType theAtomType) {
        int theResult = 0;

        if (this.itsMpeoeAtomType != null && theAtomType.getMpeoeAtomType() != null
                && (theResult = this.itsMpeoeAtomType.compareTo(theAtomType.getMpeoeAtomType())) != 0) {
            return theResult;
        } else if(this.itsMpeoeAtomType == null && theAtomType.getMpeoeAtomType() == null) {
            return 0;
        } else if (this.itsMpeoeAtomType == null) {
            return -1;
        } else if(theAtomType.getMpeoeAtomType() == null) {
            return 1;
        } else if (this.itsHydrogenBondType != null && theAtomType.getHydrogenBondType()!= null
                && (theResult = this.itsHydrogenBondType.compareTo(theAtomType.getHydrogenBondType())) != 0) {
            return theResult;
        } else if(this.itsHydrogenBondType == null && theAtomType.getHydrogenBondType() == null) {
            return 0;
        } else if(this.itsHydrogenBondType == null) {
            return -1;
        } else if(theAtomType.getHydrogenBondType() == null) {
            return 1;
        } else if (this.itsNonBondAtomType != null && theAtomType.getNonBondAtomType() != null
                && (theResult = this.itsNonBondAtomType.compareTo(theAtomType.getNonBondAtomType())) != 0) {
            return theResult;
        } else if(this.itsNonBondAtomType == null && theAtomType.getNonBondAtomType() == null) {
            return 0;
        } else if(this.itsNonBondAtomType == null) {
            return -1;
        } else if(theAtomType.getNonBondAtomType() == null) {
            return 1;
        } else if (this.itsAtomicCharge != null && theAtomType.getAtomicCharge() != null
                && (theResult = this.itsAtomicCharge.compareTo(theAtomType.getAtomicCharge())) != 0) {
            return theResult;
        } else if(this.itsAtomicCharge == null && theAtomType.getAtomicCharge() == null) {
            return 0;
        } else if(this.itsAtomicCharge == null) {
            return -1;
        } else if(theAtomType.getAtomicCharge() == null) {
            return 1;
        }

        return theResult;
    }
}
