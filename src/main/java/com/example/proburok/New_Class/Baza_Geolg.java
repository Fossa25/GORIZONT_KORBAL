package com.example.proburok.New_Class;

public class Baza_Geolg {
    private int  idG;
    private String NOM_PAS;
    private String columnOTDO;
    private String columnOT;
    private String columnDO;
    private String columnKLASS;
    private String columnOPIS;
    private String columnLIST;
    private String columnFAKTOR;
    private String columnFAKTOR_TEXT;

    public Baza_Geolg(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8, String col9) {
        this.NOM_PAS = col1;
        this.columnOTDO = col2;
        this.columnOT = col3;
        this.columnDO = col4;
        this.columnKLASS = col5;
        this.columnOPIS = col6;
        this.columnLIST = col7;
        this.columnFAKTOR= col8;
        this.columnFAKTOR_TEXT= col9;
    }
    @Override
    public String toString() {
        return NOM_PAS; // Возвращаем название для отображения в ComboBox
    }
    public Baza_Geolg() {}

    public int  getidG() {return idG;}
    public void setidG(int  idG) {
        this.idG = idG;
    }
    public String getNOM_PAS() {
        return NOM_PAS;
    }
    public void setNOM_PAS(String NOM_PAS) {
        this.NOM_PAS = NOM_PAS;
    }
    public String getcolumnOTDO() {
        return columnOTDO;
    }
    public void setcolumnOTDO(String columnOTDO) {
        this.columnOTDO = columnOTDO;
    }

    public String getcolumnOT() {
        return columnOT;
    }
    public void setcolumnOT(String columnOT) {
        this.columnOT = columnOT;
    }

    public String getcolumnDO() {
        return columnDO;
    }
    public void setcolumnDO(String columnDO) {
        this.columnDO = columnDO;
    }

    public String getcolumnKLASS() {
        return columnKLASS;
    }
    public void setcolumnKLASS(String columnKLASS) {this.columnKLASS = columnKLASS;}

    public String getcolumnOPIS() {
        return  columnOPIS;
    }
    public void setcolumnOPIS(String  columnOPIS) {
        this. columnOPIS =  columnOPIS;
    }

    public String getcolumnLIST() {
        return  columnLIST;
    }
    public void setcolumnLIST(String  columnLIST) {
        this. columnLIST =  columnLIST;
    }

    public String getColumnFAKTOR() {
        return columnFAKTOR;
    }

    public void setColumnFAKTOR(String columnFAKTOR) {
        this.columnFAKTOR = columnFAKTOR;
    }

    public String getColumnFAKTOR_TEXT() {
        return columnFAKTOR_TEXT;
    }

    public void setColumnFAKTOR_TEXT(String columnFAKTOR_TEXT) {
        this.columnFAKTOR_TEXT = columnFAKTOR_TEXT;
    }
}
