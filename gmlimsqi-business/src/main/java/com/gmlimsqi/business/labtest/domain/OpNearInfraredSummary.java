package com.gmlimsqi.business.labtest.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 近红外汇总对象 op_near_infrared_summary
 *
 * @author hhy
 * @date 2025-10-29
 */
@Data
public class OpNearInfraredSummary extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** Dairyland ID */
    @Excel(name = "Dairyland ID")
    private String dairylandId;

    /** Date Processed */
    @Excel(name = "Date Processed")
    private Date dateProcessed;

    /** Account Number */
    @Excel(name = "Account Number")
    private Long accountNumber;

    /** Sub-Account Number */
    @Excel(name = "Sub-Account Number")
    private Long subAccountNumber;

    /** Sampled For */
    @Excel(name = "Sampled For")
    private String sampledFor;

    /** Product Type */
    @Excel(name = "Product Type")
    private String productType;

    /** Sub Type */
    @Excel(name = "Sub Type")
    private String subType;

    /** ESample No. */
    @Excel(name = "ESample No.")
    private String eSampleNo;

    /** Alias */
    @Excel(name = "Alias")
    private String alias;

    /** Description 1 */
    @Excel(name = "Description 1")
    private String description1;

    /** Description 2 */
    @Excel(name = "Description 2")
    private String description2;

    /** Description 3 */
    @Excel(name = "Description 3")
    private String description3;

    /** Analysis Package */
    @Excel(name = "Analysis Package")
    private String analysisPackage;

    /** Moisture */
    @Excel(name = "Moisture")
    private BigDecimal moisture;

    /** Dry Matter */
    @Excel(name = "Dry Matter")
    private BigDecimal dryMatter;

    /** RDM */
    @Excel(name = "RDM")
    private BigDecimal rdm;

    /** pH */
    @Excel(name = "pH")
    private BigDecimal ph;

    /** CP */
    @Excel(name = "CP")
    private BigDecimal cp;

    /** AD-ICP */
    @Excel(name = "AD-ICP")
    private BigDecimal adIcp;

    /** ND-ICP */
    @Excel(name = "ND-ICP")
    private BigDecimal ndIcp;

    /** ND-ICPss */
    @Excel(name = "ND-ICPss")
    private BigDecimal ndIcpss;

    /** 16h RUP %CP */
    @Excel(name = "16h RUP %CP")
    private BigDecimal hRUPPCP16;

    /** UCP %CP */
    @Excel(name = "UCP %CP")
    private BigDecimal ucpCP;

    /** Soluble Protein */
    @Excel(name = "Soluble Protein")
    private BigDecimal solubleProtein;

    /** KOH solubility */
    @Excel(name = "KOH solubility")
    private BigDecimal kohSolubility;

    /** Ammonia */
    @Excel(name = "Ammonia")
    private BigDecimal ammonia;

    /** Ammonia %CP */
    @Excel(name = "Ammonia %CP")
    private BigDecimal ammoniaCP;

    /** Ammonia %SP */
    @Excel(name = "Ammonia %SP")
    private BigDecimal ammoniaSP;

    /** Lysine */
    @Excel(name = "Lysine")
    private BigDecimal lysine;

    /** Methionine */
    @Excel(name = "Methionine")
    private BigDecimal methionine;

    /** Cysteine */
    @Excel(name = "Cysteine")
    private BigDecimal cysteine;

    /** Alanine */
    @Excel(name = "Alanine")
    private BigDecimal alanine;

    /** Aspartic Acid */
    @Excel(name = "Aspartic Acid")
    private BigDecimal asparticAcid;

    /** Glutamic Acid */
    @Excel(name = "Glutamic Acid")
    private BigDecimal glutamicAcid;

    /** Glycine */
    @Excel(name = "Glycine")
    private BigDecimal glycine;

    /** Isoleucine */
    @Excel(name = "Isoleucine")
    private BigDecimal isoleucine;

    /** Leucine */
    @Excel(name = "Leucine")
    private BigDecimal leucine;

    /** Proline */
    @Excel(name = "Proline")
    private BigDecimal proline;

    /** Threonine */
    @Excel(name = "Threonine")
    private BigDecimal threonine;

    /** Valine */
    @Excel(name = "Valine")
    private BigDecimal valine;

    /** Arginine */
    @Excel(name = "Arginine")
    private BigDecimal arginine;

    /** Histidine */
    @Excel(name = "Histidine")
    private BigDecimal histidine;

    /** Phenylalanine */
    @Excel(name = "Phenylalanine")
    private BigDecimal phenylalanine;

    /** Tyrosine */
    @Excel(name = "Tyrosine")
    private BigDecimal tyrosine;

    /** Tryptophan */
    @Excel(name = "Tryptophan")
    private BigDecimal tryptophan;

    /** Serine */
    @Excel(name = "Serine")
    private BigDecimal serine;

    /** Total Amino Acids */
    @Excel(name = "Total Amino Acids")
    private BigDecimal totalAminoAcids;

    /** ADF */
    @Excel(name = "ADF")
    private BigDecimal adf;

    /** aNDF */
    @Excel(name = "aNDF")
    private BigDecimal aNdf;

    /** aNDFom */
    @Excel(name = "aNDFom")
    private BigDecimal aNdfom;

    /** Lignin */
    @Excel(name = "Lignin")
    private BigDecimal lignin;

    /** Crude Fiber */
    @Excel(name = "Crude Fiber")
    private BigDecimal crudeFiber;

    /** NDFD12 */
    @Excel(name = "NDFD12")
    private BigDecimal ndfd12;

    /** NDFD24 */
    @Excel(name = "NDFD24")
    private BigDecimal ndfd24;

    /** NDFD30 */
    @Excel(name = "NDFD30")
    private BigDecimal ndfd30;

    /** NDFD48 */
    @Excel(name = "NDFD48")
    private BigDecimal ndfd48;

    /** NDFD72 */
    @Excel(name = "NDFD72")
    private BigDecimal ndfd72;

    /** NDFD120 */
    @Excel(name = "NDFD120")
    private BigDecimal ndfd120;

    /** NDFD240 */
    @Excel(name = "NDFD240")
    private BigDecimal ndfd240;

    /** uNDFom12 */
    @Excel(name = "uNDFom12")
    private BigDecimal uNdfom12;

    /** uNDFom24 */
    @Excel(name = "uNDFom24")
    private BigDecimal uNdfom24;

    /** uNDFom30 */
    @Excel(name = "uNDFom30")
    private BigDecimal uNdfom30;

    /** uNDFom48 */
    @Excel(name = "uNDFom48")
    private BigDecimal uNdfom48;

    /** uNDFom72 */
    @Excel(name = "uNDFom72")
    private BigDecimal uNdfom72;

    /** uNDFom240 */
    @Excel(name = "uNDFom240")
    private BigDecimal uNdfom240;

    /** uNDFom120 */
    @Excel(name = "uNDFom120")
    private BigDecimal uNdfom120;

    /** Calibrate NDF */
    @Excel(name = "Calibrate NDF")
    private BigDecimal calibrateNdf;

    /** FPN */
    @Excel(name = "FPN")
    private BigDecimal fpn;

    /** Starch */
    @Excel(name = "Starch")
    private BigDecimal starch;

    /** Calibrate Starch */
    @Excel(name = "Calibrate Starch")
    private BigDecimal calibrateStarch;

    /** GPN */
    @Excel(name = "GPN")
    private BigDecimal gpn;

    /** Calibrate Leaf% */
    @Excel(name = "Calibrate Leaf%")
    private BigDecimal calibrateLeafPercent;

    /** CSPS */
    @Excel(name = "CSPS")
    private BigDecimal csps;

    /** IVSD7 */
    @Excel(name = "IVSD7")
    private BigDecimal ivsd7;

    /** IVSD7-o */
    @Excel(name = "IVSD7-o")
    private BigDecimal ivsd7o;

    /** IVSD7-o est */
    @Excel(name = "IVSD7-o est")
    private BigDecimal ivsd7oEst;

    /** ESC (Sugar) */
    @Excel(name = "ESC (Sugar)")
    private BigDecimal escSugar;

    /** WSC (Sugar) */
    @Excel(name = "WSC (Sugar)")
    private BigDecimal wscSugar;

    /** Sucrose */
    @Excel(name = "Sucrose")
    private BigDecimal sucrose;

    /** Sucrose %Total sugars */
    @Excel(name = "Sucrose %Total sugars")
    private BigDecimal sucroseTotalSugars;

    /** Total sugar */
    @Excel(name = "Total sugar")
    private BigDecimal totalSugar;

    /** Lactose */
    @Excel(name = "Lactose")
    private BigDecimal lactose;

    /** EE (Fat) */
    @Excel(name = "EE (Fat)")
    private BigDecimal eeFat;

    /** AH Fat */
    @Excel(name = "AH Fat")
    private BigDecimal ahFat;

    /** TFA */
    @Excel(name = "TFA")
    private BigDecimal tfa;

    /** C120 */
    @Excel(name = "C120")
    private BigDecimal c120;

    /** C140 */
    @Excel(name = "C140")
    private BigDecimal c140;

    /** C141 */
    @Excel(name = "C141")
    private BigDecimal c141;

    /** C160 */
    @Excel(name = "C160")
    private BigDecimal c160;

    /** C161 */
    @Excel(name = "C161")
    private BigDecimal c161;

    /** C170 */
    @Excel(name = "C170")
    private BigDecimal c170;

    /** C180 */
    @Excel(name = "C180")
    private BigDecimal c180;

    /** C181 */
    @Excel(name = "C181")
    private BigDecimal c181;

    /** C182 */
    @Excel(name = "C182")
    private BigDecimal c182;

    /** C183 */
    @Excel(name = "C183")
    private BigDecimal c183;

    /** C190 */
    @Excel(name = "C190")
    private BigDecimal c190;

    /** C200 */
    @Excel(name = "C200")
    private BigDecimal c200;

    /** C201 */
    @Excel(name = "C201")
    private BigDecimal c201;

    /** C202 */
    @Excel(name = "C202")
    private BigDecimal c202;

    /** C203 */
    @Excel(name = "C203")
    private BigDecimal c203;

    /** C204 */
    @Excel(name = "C204")
    private BigDecimal c204;

    /** C205 */
    @Excel(name = "C205")
    private BigDecimal c205;

    /** C220 */
    @Excel(name = "C220")
    private BigDecimal c220;

    /** C221 */
    @Excel(name = "C221")
    private BigDecimal c221;

    /** C226 */
    @Excel(name = "C226")
    private BigDecimal c226;

    /** C240 */
    @Excel(name = "C240")
    private BigDecimal c240;

    /** C241 */
    @Excel(name = "C241")
    private BigDecimal c241;

    /** Other Fatty Acids */
    @Excel(name = "Other Fatty Acids")
    private BigDecimal otherFattyAcids;

    /** C120_TFA */
    @Excel(name = "C120_TFA")
    private BigDecimal c120TFA;

    /** C140_TFA */
    @Excel(name = "C140_TFA")
    private BigDecimal c140TFA;

    /** C141_TFA */
    @Excel(name = "C141_TFA")
    private BigDecimal c141TFA;

    /** C160_TFA */
    @Excel(name = "C160_TFA")
    private BigDecimal c160TFA;

    /** C161_TFA */
    @Excel(name = "C161_TFA")
    private BigDecimal c161TFA;

    /** C170_TFA */
    @Excel(name = "C170_TFA")
    private BigDecimal c170TFA;

    /** C180_TFA */
    @Excel(name = "C180_TFA")
    private BigDecimal c180TFA;

    /** C181_TFA */
    @Excel(name = "C181_TFA")
    private BigDecimal c181TFA;

    /** C182_TFA */
    @Excel(name = "C182_TFA")
    private BigDecimal c182TFA;

    /** C183_TFA */
    @Excel(name = "C183_TFA")
    private BigDecimal c183TFA;

    /** C190_TFA */
    @Excel(name = "C190_TFA")
    private BigDecimal c190TFA;

    /** C200_TFA */
    @Excel(name = "C200_TFA")
    private BigDecimal c200TFA;

    /** C201_TFA */
    @Excel(name = "C201_TFA")
    private BigDecimal c201TFA;

    /** C202_TFA */
    @Excel(name = "C202_TFA")
    private BigDecimal c202TFA;

    /** C203_TFA */
    @Excel(name = "C203_TFA")
    private BigDecimal c203TFA;

    /** C204_TFA */
    @Excel(name = "C204_TFA")
    private BigDecimal c204TFA;

    /** C205_TFA */
    @Excel(name = "C205_TFA")
    private BigDecimal c205TFA;

    /** C220_TFA */
    @Excel(name = "C220_TFA")
    private BigDecimal c220TFA;

    /** C221_TFA */
    @Excel(name = "C221_TFA")
    private BigDecimal c221TFA;

    /** C226_TFA */
    @Excel(name = "C226_TFA")
    private BigDecimal c226TFA;

    /** C240_TFA */
    @Excel(name = "C240_TFA")
    private BigDecimal c240TFA;

    /** C241_TFA */
    @Excel(name = "C241_TFA")
    private BigDecimal c241TFA;

    /** Other Fatty Acids_TFA */
    @Excel(name = "Other Fatty Acids_TFA")
    private BigDecimal otherFattyAcidsTFA;

    /** Ash */
    @Excel(name = "Ash")
    private BigDecimal ash;

    /** Ca */
    @Excel(name = "Ca")
    private BigDecimal ca;

    /** P */
    @Excel(name = "P")
    private BigDecimal p;

    /** Mg */
    @Excel(name = "Mg")
    private BigDecimal mg;

    /** K */
    @Excel(name = "K")
    private BigDecimal k;

    /** S */
    @Excel(name = "S")
    private BigDecimal s;

    /** Na */
    @Excel(name = "Na")
    private BigDecimal na;

    /** Cl */
    @Excel(name = "Cl")
    private BigDecimal cl;

    /** Al */
    @Excel(name = "Al")
    private BigDecimal al;

    /** B */
    @Excel(name = "B")
    private BigDecimal b;

    /** Mn */
    @Excel(name = "Mn")
    private BigDecimal mn;

    /** Zn */
    @Excel(name = "Zn")
    private BigDecimal zn;

    /** Cu */
    @Excel(name = "Cu")
    private BigDecimal cu;

    /** Fe */
    @Excel(name = "Fe")
    private BigDecimal fe;

    /** Lactic Acid */
    @Excel(name = "Lactic Acid")
    private BigDecimal lacticAcid;

    /** Acetic Acid */
    @Excel(name = "Acetic Acid")
    private BigDecimal aceticAcid;

    /** Propionic Acid */
    @Excel(name = "Propionic Acid")
    private BigDecimal propionicAcid;

    /** Butyric Acid */
    @Excel(name = "Butyric Acid")
    private BigDecimal butyricAcid;

    /** Iso-Butryic Acid */
    @Excel(name = "Iso-Butryic Acid")
    private BigDecimal isoButryicAcid;

    /** Ethanol */
    @Excel(name = "Ethanol")
    private BigDecimal ethanol;

    /** 1,2 Propanediol */
    @Excel(name = "1,2 Propanediol")
    private BigDecimal Propanediol12;

    /** Methanol */
    @Excel(name = "Methanol")
    private BigDecimal methanol;

    /** Propanol */
    @Excel(name = "Propanol")
    private BigDecimal propanol;

    /** Butanol */
    @Excel(name = "Butanol")
    private BigDecimal butanol;

    /** Mold Count */
    @Excel(name = "Mold Count")
    private BigDecimal moldCount;

    /** Yeast Count */
    @Excel(name = "Yeast Count")
    private BigDecimal yeastCount;

    /** Aflatoxin B1 */
    @Excel(name = "Aflatoxin B1")
    private BigDecimal aflatoxinB1;

    /** Aflatoxin B2 */
    @Excel(name = "Aflatoxin B2")
    private BigDecimal aflatoxinB2;

    /** Aflatoxin G1 */
    @Excel(name = "Aflatoxin G1")
    private BigDecimal aflatoxinG1;

    /** Aflatoxin G2 */
    @Excel(name = "Aflatoxin G2")
    private BigDecimal aflatoxinG2;

    /** Zearalenone */
    @Excel(name = "Zearalenone")
    private BigDecimal zearalenone;

    /** T2 */
    @Excel(name = "T2")
    private BigDecimal t2;

    /** HT2 */
    @Excel(name = "HT2")
    private BigDecimal ht2;

    /** Neosolaniol */
    @Excel(name = "Neosolaniol")
    private BigDecimal neosolaniol;

    /** DAS */
    @Excel(name = "DAS")
    private BigDecimal das;

    /** Ochratoxin A */
    @Excel(name = "Ochratoxin A")
    private BigDecimal ochratoxinA;

    /** Citrinin */
    @Excel(name = "Citrinin")
    private BigDecimal citrinin;

    /** Patulin */
    @Excel(name = "Patulin")
    private BigDecimal patulin;

    /** Fumonisin B1 */
    @Excel(name = "Fumonisin B1")
    private BigDecimal fumonisinB1;

    /** Fumonisin B2 */
    @Excel(name = "Fumonisin B2")
    private BigDecimal fumonisinB2;

    /** Fumonisin B3 */
    @Excel(name = "Fumonisin B3")
    private BigDecimal fumonisinB3;

    /** Vomitoxin (DON) */
    @Excel(name = "Vomitoxin (DON)")
    private BigDecimal vomitoxinDON;

    /** 3 Acetyl DON */
    @Excel(name = "3 Acetyl DON")
    private BigDecimal AcetylDON3;

    /** 15 Acetyl DON */
    @Excel(name = "15 Acetyl DON")
    private BigDecimal AcetylDON15;

    /** Fusarenon X */
    @Excel(name = "Fusarenon X")
    private BigDecimal fusarenonX;

    /** Nivalenol */
    @Excel(name = "Nivalenol")
    private BigDecimal nivalenol;

    /** Fusaric Acid */
    @Excel(name = "Fusaric Acid")
    private BigDecimal fusaricAcid;

    /** Total Aflatoxins */
    @Excel(name = "Total Aflatoxins")
    private BigDecimal totalAflatoxins;

    /** Roquefortine C */
    @Excel(name = "Roquefortine C")
    private BigDecimal roquefortineC;

    /** Total Fumonisins */
    @Excel(name = "Total Fumonisins")
    private BigDecimal totalFumonisins;

    /** Total DONs */
    @Excel(name = "Total DONs")
    private BigDecimal totalDONs;

    /** Total T2/HT2 */
    @Excel(name = "Total T2/HT2")
    private BigDecimal totalT2HT2;

    /** DON (Vomitoxin) threshold 2 ppm */
    @Excel(name = "DON (Vomitoxin) threshold 2 ppm")
    private BigDecimal donVomitoxinThreshold2Ppm;

    /** Zearalenone threshold 100 ppb */
    @Excel(name = "Zearalenone threshold 100 ppb")
    private BigDecimal zearalenoneThreshold100Ppb;

    /** T2/HT2 threshold 10 ppb */
    @Excel(name = "T2/HT2 threshold 10 ppb")
    private BigDecimal t2HT2Threshold10Ppb;

    /** Fumonisin threshold 0.5 ppm */
    @Excel(name = "Fumonisin threshold 0.5 ppm")
    private BigDecimal fumonisinThreshold05Ppm;

    /** Mucor */
    @Excel(name = "Mucor")
    private BigDecimal mucor;

    /** Rhizopus */
    @Excel(name = "Rhizopus")
    private BigDecimal rhizopus;

    /** Cladosporium */
    @Excel(name = "Cladosporium")
    private BigDecimal cladosporium;

    /** Penicillium */
    @Excel(name = "Penicillium")
    private BigDecimal penicillium;

    /** Aspergillius */
    @Excel(name = "Aspergillius")
    private BigDecimal aspergillius;

    /** Fusarium */
    @Excel(name = "Fusarium")
    private BigDecimal fusarium;

    /** Other Mold */
    @Excel(name = "Other Mold")
    private BigDecimal otherMold;

    /** Coliform */
    @Excel(name = "Coliform")
    private BigDecimal coliform;

    /** Clostridia perfringens */
    @Excel(name = "Clostridia perfringens")
    private BigDecimal clostridiaPerfringens;

    /** E.coli */
    @Excel(name = "E.coli")
    private BigDecimal eColi;

    /** Enterobacteriaceae */
    @Excel(name = "Enterobacteriaceae")
    private BigDecimal enterobacteriaceae;

    /** Nitrate Nitrogen */
    @Excel(name = "Nitrate Nitrogen")
    private BigDecimal nitrateNitrogen;

    /** Adjusted CP */
    @Excel(name = "Adjusted CP")
    private BigDecimal adjustedCP;

    /** RDP-NRC01 %CP */
    @Excel(name = "RDP-NRC01 %CP")
    private BigDecimal rdpNrc01CP;

    /** RDP-NRC01 %DM */
    @Excel(name = "RDP-NRC01 %DM")
    private BigDecimal rdpNrc01DM;

    /** RUP-NRC01 %CP */
    @Excel(name = "RUP-NRC01 %CP")
    private BigDecimal rupNrc01CP;

    /** RUP-NRC01 %DM */
    @Excel(name = "RUP-NRC01 %DM")
    private BigDecimal rupNrc01DM;

    /** NFC */
    @Excel(name = "NFC")
    private BigDecimal nfc;

    /** Lactic:Acetic Ratio */
    @Excel(name = "Lactic:Acetic Ratio")
    private BigDecimal lacticAceticRatio;

    /** RFV */
    @Excel(name = "RFV")
    private BigDecimal rfv;

    /** RFQ */
    @Excel(name = "RFQ")
    private BigDecimal rfq;

    /** DOMI-90% dry */
    @Excel(name = "DOMI-90% dry")
    private BigDecimal domi90PercentDry;

    /** California TDN-90% dry */
    @Excel(name = "California TDN-90% dry")
    private BigDecimal californiaTDN90PercentDry;

    /** DCAD */
    @Excel(name = "DCAD")
    private BigDecimal dcad;

    /** NDF kd rate Van Amburgh */
    @Excel(name = "NDF kd rate Van Amburgh")
    private BigDecimal ndfKdRateVanAmburgh;

    /** NDF kd rate MIR */
    @Excel(name = "NDF kd rate MIR")
    private BigDecimal ndfKdRateMIR;

    /** TTNDFD */
    @Excel(name = "TTNDFD")
    private BigDecimal ttnDfd;

    /** Starch kd rate MIR_P1T1 */
    @Excel(name = "Starch kd rate MIR_P1T1")
    private BigDecimal starchkdratemirP1t1;

    /** TDN - ADF */
    @Excel(name = "TDN - ADF")
    private BigDecimal tdnADF;

    /** NEG - ADF */
    @Excel(name = "NEG - ADF")
    private BigDecimal negADF;

    /** NEM - ADF */
    @Excel(name = "NEM - ADF")
    private BigDecimal nemADF;

    /** NEL - ADF */
    @Excel(name = "NEL - ADF")
    private BigDecimal nelADF;

    /** TDN OARDC */
    @Excel(name = "TDN OARDC")
    private BigDecimal tdnOARDC;

    /** NEG OARDC */
    @Excel(name = "NEG OARDC")
    private BigDecimal negOARDC;

    /** NEM OARDC */
    @Excel(name = "NEM OARDC")
    private BigDecimal nemOARDC;

    /** NEL OARDC */
    @Excel(name = "NEL OARDC")
    private BigDecimal nelOARDC;

    /** ME OARDC */
    @Excel(name = "ME OARDC")
    private BigDecimal meOARDC;

    /** TDN Milk 2006 */
    @Excel(name = "TDN Milk 2006")
    private BigDecimal tdnMilk2006;

    /** NEG Milk 2006 */
    @Excel(name = "NEG Milk 2006")
    private BigDecimal negMilk2006;

    /** NEM Milk 2006 */
    @Excel(name = "NEM Milk 2006")
    private BigDecimal nemMilk2006;

    /** NEL Milk 2006 Processed */
    @Excel(name = "NEL Milk 2006 Processed")
    private BigDecimal nelMilk2006Processed;

    /** NEL Milk 2006 Non-Processed */
    @Excel(name = "NEL Milk 2006 Non-Processed")
    private BigDecimal nelMilk2006NonProcessed;

    /** Milk/ton Corn Silage Milk 2006 */
    @Excel(name = "Milk/ton Corn Silage Milk 2006")
    private BigDecimal milkTonCornSilageMilk2006;

    /** Milk/acre - MLK06 Proc */
    @Excel(name = "Milk/acre - MLK06 Proc")
    private BigDecimal milkAcreMLK06Proc;

    /** Milk/acre - MLK06 NonProc */
    @Excel(name = "Milk/acre - MLK06 NonProc")
    private BigDecimal milkAcreMLK06NonProc;

    /** TDN - Milk 13 */
    @Excel(name = "TDN - Milk 13")
    private BigDecimal tdnMilk13;

    /** NEL - Milk 13 */
    @Excel(name = "NEL - Milk 13")
    private BigDecimal nelMilk13;

    /** NEG - Milk 13 */
    @Excel(name = "NEG - Milk 13")
    private BigDecimal negMilk13;

    /** NEM - Milk 13 */
    @Excel(name = "NEM - Milk 13")
    private BigDecimal nemMilk13;

    /** Milk Per Ton - Milk 13 */
    @Excel(name = "Milk Per Ton - Milk 13")
    private BigDecimal milkPerTonMilk13;

    /** Beef per Ton */
    @Excel(name = "Beef per Ton")
    private BigDecimal beefPerTon;

    /** Neg - ISU Beef Mcal/cwt */
    @Excel(name = "Neg - ISU Beef Mcal/cwt")
    private BigDecimal negISUBeefMcalCwt;

    /** Nem - ISU Beef Mcal/cwt */
    @Excel(name = "Nem - ISU Beef Mcal/cwt")
    private BigDecimal nemISUBeefMcalCwt;

    /** Neg - ISU Beef Mcal/lb */
    @Excel(name = "Neg - ISU Beef Mcal/lb")
    private BigDecimal negISUBeefMcalLb;

    /** Nem - ISU Beef Mcal/lb */
    @Excel(name = "Nem - ISU Beef Mcal/lb")
    private BigDecimal nemISUBeefMcalLb;

    /** Neg - ISU Beef Mcal/kg */
    @Excel(name = "Neg - ISU Beef Mcal/kg")
    private BigDecimal negISUBeefMcalKg;

    /** Nem - ISU Beef Mcal/kg */
    @Excel(name = "Nem - ISU Beef Mcal/kg")
    private BigDecimal nemISUBeefMcalKg;

    /** Neg - ISU Beef MJ/kg */
    @Excel(name = "Neg - ISU Beef MJ/kg")
    private BigDecimal negISUBeefMJKg;

    /** Nem - ISU Beef MJ/kg */
    @Excel(name = "Nem - ISU Beef MJ/kg")
    private BigDecimal nemISUBeefMJKg;

    /** TDN 1x - ISU Beef */
    @Excel(name = "TDN 1x - ISU Beef")
    private BigDecimal tdn1xISUBeef;

    /** Swine DE */
    @Excel(name = "Swine DE")
    private BigDecimal swineDE;

    /** Swine ME */
    @Excel(name = "Swine ME")
    private BigDecimal swineME;

    /** Swine NE */
    @Excel(name = "Swine NE")
    private BigDecimal swineNE;

    /** Equine DE */
    @Excel(name = "Equine DE")
    private BigDecimal equineDE;

    /** Equine TDN */
    @Excel(name = "Equine TDN")
    private BigDecimal equineTDN;

    /** DM Yield */
    @Excel(name = "DM Yield")
    private BigDecimal dmYield;

    /** ME - GfE 2001 */
    @Excel(name = "ME - GfE 2001")
    private BigDecimal meGfE2001;

    /** Rohfaser - GfE 2001 */
    @Excel(name = "Rohfaser - GfE 2001")
    private BigDecimal rohfaserGfE2001;

    /** nXP - GfE 2001 */
    @Excel(name = "nXP - GfE 2001")
    private BigDecimal nXPGfE2001;

    /** RNB - GfE 2001 */
    @Excel(name = "RNB - GfE 2001")
    private BigDecimal rnbGfE2001;

    /** NEL - Milk24 Mcal/cwt */
    @Excel(name = "NEL - Milk24 Mcal/cwt")
    private BigDecimal nelMilk24McalCwt;

    /** Milk Per Ton - Milk24 lbs/ton */
    @Excel(name = "Milk Per Ton - Milk24 lbs/ton")
    private BigDecimal milkPerTonMilk24LbsTon;

    /** 导入时间 */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date importTime;

    /** 导入人 */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String importBy;

}