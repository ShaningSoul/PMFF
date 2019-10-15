/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.sbff.solvation.parameter;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public enum SolventProperty {
    
    MBIm_BF4("[MBIm]+[BF4]-", 12.9, 1.4211, 44.7, 0.263, 0.32),
    MBIm_PF6("[MBIm]+[PF6]-", 14.0, 1.409, 42.9, 0.266, 0.216),
    MBIm_Tf2N("[MBIm]+[Tf2N]-", 9.4, 1.427, 34.0, 0.259, 0.238),
    MEIm_BF4("[MEIm]+[BF4]-", 14.8, 1.4121, 50.1, 0.229, 0.265),
    MEIm_CF3SO3("[MEIm]+[CF3SO3]-", 15.8, 1.4332, 44.42, 0.229, 0.265),
    MEIm_Tf2N("[MEIm]+[Tf2N]-", 11.5, 1.423, 38.02, 0.318, 0.261),
    MHIm_Tf2N("[MHIm]+[Tf2N]-", 7.0, 1.42958, 32.5, 0.272, 0.242),
    MHIm_PF6("[MHIm]+[PF6]-", 11.1, 1.41787, 37.1, 0.24, 0.445),
    MOIm_PF6("[MOIm]+[PF6]-", 9.7, 1.42302, 36.2, 0.244, 0.317),
    One_2_4_trimethylbenzene("1,2,4-trimethylbenzene", 2.353, 1.5048, 29.2, 0.0, 0.19),
    One_2_dibromoethane("1,2-dibromoethane", 4.9313, 1.5387, 39.55, 0.1, 0.17),
    One_2_dichloroethane("1,2-dichloroethane", 10.125, 1.4448, 31.86, 0.1, 0.11),
    One_4_dioxane("1,4-dioxane", 2.2099, 1.4224, 32.75, 0.0, 0.64),
    One_bromooctane("1-bromooctane", 5.0244, 1.4524, 28.68, 0.0, 0.12),
    One_butanol("1-butanol", 17.3323, 1.3993, 24.94, 0.37, 0.48),
    One_chlorobutane("1-chlorobutane", 7.09087, 1.402, 23.18, 0.0, 0.1),
    One_decanol("1-decanol", 7.5305, 1.4372, 28.51, 0.37, 0.48),
    One_fluorooctane("1-fluorooctane", 3.89, 1.3935, 23.5321, 0.0, 0.1),
    One_heptanol("1-heptanol", 11.321, 1.4242, 28.5, 0.37, 0.48),
    One_hexanol("1-hexanol", 12.5102, 1.4182, 25.81, 0.37, 0.48),
    One_iodohexadecane("1-iodohexadecane", 3.5338, 1.4806, 32.29, 0.0, 0.15),
    One_nonanol("1-nonanol", 8.5991, 1.4338, 27.89, 0.37, 0.48),
    One_octanol("1-octanol", 9.8629, 1.429, 27.1, 0.37, 0.48),
    One_pentanol("1-pentanol", 15.13, 1.4101, 25.36, 0.37, 0.48),
    One_propanol("1-propanol", 20.5237, 1.384, 23.32, 0.37, 0.48),
    Two_2_4_trimethylpentane("2,2,4-trimethylpentane", 1.9358, 1.3915, 16.8775, 0.0, 0.0),
    Two_6_dimethylpyridine("2,6-dimethylpyridine", 7.1735, 1.4953, 30.9692, 0.0, 0.63),
    Two_Pentanol("2-Pentanol", 13.17, 1.403, 23.45, 0.33, 0.56),
    Two_butanone("2-butanone", 18.2457, 1.3788, 23.97, 0.0, 0.51),
    Two_methoxyethanol("2-methoxyethanol", 17.2, 1.4024, 30.84, 0.3, 0.84),
    Two_methylpyridine("2-methylpyridine", 9.9533, 1.4957, 33.0, 0.0, 0.58),
    Three_Methyl_1_butanol("3-Methyl-1-butanol", 14.7, 1.407, 23.71, 0.37, 0.48),
    Four_methyl_2_pentanone("4-methyl-2-pentanone", 12.8871, 1.3962, 23.1375, 0.0, 0.51),
    Aniline("Aniline", 6.8882, 1.5863, 42.12, 0.26, 0.41),
    Methyl_tert_butyl_ether("Methyl tert-butyl ether", 4.5, 1.369, 17.75, 0.0, 0.55),
    N_Methyl_2_pyrrolidone("N-Methyl-2-pyrrolidone", 32.2, 1.47, 40.7, 0.0, 0.79),
    Propylene_Carbonate("Propylene Carbonate", 64.4, 1.4189, 41.93, 0.0, 0.6),
    Acetic_acid("Acetic acid", 6.2528, 1.372, 27.1, 0.61, 0.44),
    Acetone("Acetone", 20.4933, 1.3588, 23.46, 0.04, 0.49),
    Acetonitrile("Acetonitrile", 35.6881, 1.3442, 28.66, 0.07, 0.32),
    Anion_water("Anion_water", 78.2, 1.33, 78.99, 0.82, 0.35),
    Anisole("Anisole", 4.2247, 1.5174, 35.1, 0.0, 0.29),
    Benzene("Benzene", 2.2706, 1.5011, 28.22, 0.0, 0.14),
    Benzonitrile("Benzonitrile", 26.592, 1.5289, 38.79, 0.0, 0.33),
    Benzyl_alcohol("Benzyl alcohol", 12.4569, 1.5396, 34.7975, 0.33, 0.56),
    Bromobenzene("Bromobenzene", 5.3954, 1.5597, 35.24, 0.0, 0.09),
    Bromoethane("Bromoethane", 9.01, 1.4239, 23.62, 0.0, 0.12),
    Bromoform("Bromoform", 4.2488, 1.6005, 44.87, 0.15, 0.06),
    Carbon_disulfide("Carbon disulfide", 2.6105, 1.6319, 31.58, 0.0, 0.07),
    Carbon_tetrachloride("Carbon tetrachloride", 2.228, 1.4601, 26.43, 0.0, 0.0),
    Cation_methanol("Cation_methanol", 32.6131, 1.3288, 22.07, 0.43, 0.47),
    Cation_water("Cation_water", 78.2, 1.33, 71.99, 0.82, 0.35),
    Chlorobenzene("Chlorobenzene", 5.6968, 1.5241, 32.99, 0.0, 0.07),
    Chloroform("Chloroform", 4.7113, 1.4459, 26.67, 0.15, 0.02),
    Chlorohexane("Chlorohexane", 5.9491, 1.4199, 25.73, 0.0, 0.1),
    Cyclohexane("Cyclohexane", 2.0165, 1.4266, 24.65, 0.0, 0.0),
    Cyclohexanone("Cyclohexanone", 15.6186, 1.4507, 34.57, 0.0, 0.56),
    Decalin("Decalin", 2.196, 1.4528, 31.595, 0.0, 0.0),
    Di_n_butyl_ether("Di-n-butyl ether", 3.0473, 1.3992, 22.445, 0.0, 0.45),
    Dichloromethane("Dichloromethane", 8.93, 1.4242, 27.2, 0.1, 0.05),
    Diethyl_ether("Diethyl ether", 4.24, 1.3526, 16.65, 0.0, 0.41),
    Diisopropyl_ether("Diisopropyl ether", 3.38, 1.3679, 17.27, 0.0, 0.41),
    Dimethylacetamide("Dimethylacetamide", 37.7807, 1.438, 33.125, 0.0, 0.78),
    Dimethylformamide("Dimethylformamide", 37.219, 1.4305, 36.17, 0.0, 0.74),
    Dimethylsulfoxide("Dimethylsulfoxide", 46.8257, 1.4775, 42.92, 0.0, 0.88),
    Diphenyl_ether("diphenyl ether", 3.73, 1.4, 24.35, 0.0, 0.2),
    Ethanol("Ethanol", 24.852, 1.3611, 21.97, 0.37, 0.48),
    Ethoxybenzene("Ethoxybenzene", 4.1797, 1.5076, 32.41, 0.0, 0.32),
    Ethyl_acetate("Ethyl acetate", 5.9867, 1.3723, 23.39, 0.0, 0.45),
    Ethylbenzene("Ethylbenzene", 2.4339, 1.4959, 28.75, 0.0, 0.15),
    Ethylene_glycol("Ethylene glycol", 40.2455, 1.4318, 47.99, 0.58, 0.78),
    Fluorobenzene("Fluorobenzene", 5.42, 1.4684, 26.66, 0.0, 0.1),
    Iodobenzene("Iodobenzene", 4.547, 1.62, 38.71, 0.0, 0.12),
    Isobutanol("Isobutanol", 16.7766, 1.3955, 22.5425, 0.37, 0.48),
    Isopropanol("Isopropanol", 19.2645, 1.3776, 20.93, 0.33, 0.56),
    Isopropylbenzene("Isopropylbenzene", 2.3712, 1.4915, 27.685, 0.0, 0.16),
    M_cresol("m-Cresol", 12.44, 1.5438, 35.69, 0.57, 0.34),
    M_xylene("m-Xylene", 2.3478, 1.4972, 29.76, 0.0, 0.16),
    Mesitylene("Mesitylene", 2.265, 1.4994, 27.55, 0.0, 0.19),
    Methanol("Methanol", 32.6131, 1.3288, 22.07, 0.43, 0.47),
    Methyl_acetate("Methyl acetate", 6.8615, 1.3614, 24.73, 0.0, 0.45),
    Methyl_phenyl_ketone("Methyl phenyl ketone", 17.44, 1.5372, 39.04, 0.0, 0.48),
    Methylformamide("Methylformamide", 181.5619, 1.4319, 38.7, 0.4, 0.55),
    N_butyl_acetate("n-Butyl acetate", 4.9941, 1.3941, 24.88, 0.0, 0.45),
    N_butylbenzene("n-Butylbenzene", 2.36, 1.4898, 28.7175, 0.0, 0.15),
    N_decane("n-Decane", 1.9846, 1.4102, 22.445, 0.0, 0.0),
    N_dodecane("n-Dodecane", 2.006, 1.4216, 24.91, 0.0, 0.0),
    N_heptane("n-Heptane", 1.9113, 1.3878, 19.65, 0.0, 0.0),
    N_hexadecane("n-Hexadecane", 2.0402, 1.4345, 27.05, 0.0, 0.0),
    N_hexane("n-Hexane", 1.8819, 1.3749, 17.89, 0.0, 0.0),
    N_nonane("n-Nonane", 1.9605, 1.4054, 22.38, 0.0, 0.0),
    N_octane("n-Octane", 1.9406, 1.3974, 21.14, 0.0, 0.0),
    N_pentadecane("n-Pentadecane", 2.0333, 1.4315, 26.6375, 0.0, 0.0),
    N_pentane("n-Pentane", 1.8371, 1.3575, 15.4475, 0.0, 0.0),
    N_undecane("n-Undecane", 1.991, 1.4398, 24.21, 0.0, 0.0),
    Nitrobenzene("Nitrobenzene", 34.8091, 1.5562, 45.66, 0.0, 0.28),
    Nitroethane("Nitroethane", 28.2896, 1.3917, 32.13, 0.02, 0.33),
    Nitromethane("Nitromethane", 36.5623, 1.3817, 36.53, 0.06, 0.31),
    O_dichlorobenzene("o-Dichlorobenzene", 9.9949, 1.5515, 35.8, 0.0, 0.04),
    O_nitrotoluene("o-Nitrotoluene", 25.6692, 1.545, 41.17, 0.0, 0.27),
    P_isopropyltoluene("p-Isopropyltoluene", 2.2322, 1.4909, 26.6002, 0.0, 0.19),
    Perfluorobenzene("Perfluorobenzene", 2.029, 1.3777, 22.0, 0.0, 0.0),
    Pyridine("Pyridine", 12.9776, 1.5095, 36.56, 0.0, 0.52),
    Sec_butanol("sec-Butanol", 15.9436, 1.3978, 23.47, 0.33, 0.56),
    Sec_butylbenzene("sec-Butylbenzene", 2.3446, 1.4895, 28.0325, 0.0, 0.16),
    Tert_butanol("tert-Butanol", 12.47, 1.3878, 19.96, 0.31, 0.6),
    Tert_butylbenzene("tert-Butylbenzene", 2.3447, 1.4927, 27.6375, 0.0, 0.16),
    Tetrachloroethene("Tetrachloroethene", 2.268, 1.5053, 31.8, 0.0, 0.0),
    Tetrahydrofuran("Tetrahydrofuran", 7.4257, 1.405, 26.5, 0.0, 0.48),
    Tetrahydrothiophene_1_1_dioxide("Tetrahydrothiophene-1,1-dioxide", 43.9622, 1.4833, 35.5, 0.0, 0.88),
    Tetralin("Tetralin", 2.771, 1.5413, 33.1236, 0.0, 0.19),
    Toluene("Toluene", 2.3741, 1.4961, 27.93, 0.0, 0.14),
    Tributylphosphate("Tributylphosphate", 8.1781, 1.4224, 19.1141, 0.0, 1.21),
    Triethylamine("Triethylamine", 2.3832, 1.401, 20.22, 0.0, 0.79),
    Water("Water", 78.3553, 1.33, 71.99, 0.82, 0.35),
    Other("Other", 0.0, 0.0, 0.0, 0.0, 0.0)
    ;
    
    public final String NAME;
    public final Double DIELECTRIC_CONSTANT;
    public final Double REFLACTIVE_INDEX;
    public final Double SURFACE_TENSION;
    public final Double HBOND_ACIDITY;
    public final Double HBOND_BASICITY;

    private SolventProperty(String NAME, Double DIELECTRIC_CONSTANT, Double REFLACTIVE_INDEX, Double SURFACE_TENSION, Double HBOND_ACIDITY, Double HBOND_BASICITY) {
        this.NAME = NAME;
        this.DIELECTRIC_CONSTANT = DIELECTRIC_CONSTANT;
        this.REFLACTIVE_INDEX = REFLACTIVE_INDEX;
        this.SURFACE_TENSION = SURFACE_TENSION;
        this.HBOND_ACIDITY = HBOND_ACIDITY;
        this.HBOND_BASICITY = HBOND_BASICITY;
    }

    public static SolventProperty getProperty(String theName) {
        for(SolventProperty theProperty : SolventProperty.values()) {
            if(theProperty.NAME.equals(theName)) {
                return theProperty;
            }
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        return this.NAME;
    }
}
