package com.vulpes.datamodel;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Entry {
    private String zamiar;
    private String nrUmowy;
    private String nrRej;
    private XSSFSheet sheet;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public String toString() {
        return nrRej + ": " + zamiar;
    }

    public String generateGps2Preview(){
        StringBuilder sb = new StringBuilder();
//        sb.append(getZamiar()).append("\t").append(getNrUmowy()).append("\t").append(getNrRej()).append("\t");
//        sb.append(getStartTime()).append("\t").append(getStartGps()).append("\t");
//        sb.append(getEndTime()).append("\t").append(getDistance()).append("\n");
        sb.append("H").append("\t").append(zamiar).append("\t").append(nrUmowy).append("\n");
        for (int i = 1; i < getSheet().getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            LocalDateTime dateTime = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String date = dateTime.format(formatter);
            Cell cell7 = row.getCell(7);
            Cell cell8 = row.getCell(8);
            double cell7d = cell7.getNumericCellValue();
            double cell8d = cell8.getNumericCellValue();
            String combined = "N" + cell7d + ",E" + cell8d;
            sb.append(combined).append("\t").append(date).append("\n");
        }
        return sb.toString();
    }

    public String getStartTime() {
        Row row = sheet.getRow(1);
        Cell cell = row.getCell(0);
        LocalDateTime dateTime = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dateTime.format(formatter);
    }

    public String getEndTime() {
        Row row = sheet.getRow(sheet.getLastRowNum());
        Cell cell = row.getCell(0);
        LocalDateTime dateTime = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dateTime.format(formatter);
    }

    public String getStartGps() {
        Row row = sheet.getRow(1);
        Cell cell7 = row.getCell(7);
        Cell cell8 = row.getCell(8);
        double cell7text = cell7.getNumericCellValue();
        double cell8text = cell8.getNumericCellValue();
        return "N" + cell7text + ",E" + cell8text;
    }

    public String getDistance() {
        Row row = sheet.getRow(1);
        Cell cell = row.getCell(46);
        double before = cell.getNumericCellValue();
        row = sheet.getRow(sheet.getLastRowNum());
        cell = row.getCell(46);
        double after = cell.getNumericCellValue();
        double distance = after - before;
        return String.format("%.2f", distance);
    }

    public XSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public void setZamiar(String zamiar) {
        this.zamiar = zamiar;
    }

    public void setNrUmowy(String nrUmowy) {
        this.nrUmowy = nrUmowy;
    }

    public void setNrRej(String nrRej) {
        this.nrRej = nrRej;
    }

    public String getZamiar() {
        return zamiar;
    }

    public String getNrUmowy() {
        return nrUmowy;
    }

    public String getNrRej() {
        return nrRej;
    }

}
