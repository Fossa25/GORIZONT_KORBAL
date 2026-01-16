package com.example.proburok.New_Class;

//для создания внести и выдать данные нажми alt+insert
public class Baza_Geolg {
    private int  idG;           // Уникальный идентификатор (автоинкремент в БД)
    private String NOM_PAS;
    private String columnOTDO;
    private String columnOT;   // Значение из 1-го столбца
    private String columnDO;   // Значение из 2-го столбца
    private String columnKLASS;   // Значение из 3-го столбца
    private String columnOPIS;

    public Baza_Geolg(String col1, String col2, String col3, String col4, String col5, String col6) {
        this.NOM_PAS = col1;
        this.columnOTDO = col2;
        this.columnOT = col3;
        this.columnDO = col4;
        this.columnKLASS = col5;
        this.columnOPIS = col6;
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


}
