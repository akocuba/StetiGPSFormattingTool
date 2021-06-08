package com.vulpes.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Engine {
    private XSSFWorkbook gps1workbook;
    private XSSFSheet gps1sheet;
    private XSSFWorkbook gps2workbook;
    private XSSFSheet gps2sheet;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static Engine engine = new Engine();
    private ObservableList<Entry> entries;


    private Engine() {
        this.gps1workbook = new XSSFWorkbook();
        this.gps1sheet = gps1workbook.createSheet();
        this.gps2workbook = new XSSFWorkbook();
        this.gps2sheet = gps2workbook.createSheet();
        entries = FXCollections.observableArrayList();
    }

    public static Engine getInstance() {
        return engine;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void removeEntry(Entry entry) {
        entries.remove(entry);
    }

    public ObservableList<Entry> getEntries() {
        return entries;
    }

    public void writeToGps(){
        for(Entry entry: entries){
            writeToGps(entry);
        }
    }

    private void writeToGps(Entry entry){
        writeToGps1(entry);
        writeToGps2(entry);
    }

    private void writeToGps1(Entry entry) {
        if (gps1sheet.getLastRowNum() == 0 && gps1sheet.getRow(0) == null) {
            Row row = gps1sheet.createRow(0);
            row.createCell(0).setCellValue(entry.getZamiar());
            row.createCell(1).setCellValue(entry.getNrUmowy());
            row.createCell(2).setCellValue(entry.getNrRej());
            row.createCell(3).setCellValue(entry.getStartTime());
            row.createCell(4).setCellValue(entry.getStartGps());
            row.createCell(5).setCellValue(entry.getEndTime());
            row.createCell(6).setCellValue(entry.getDistance());
        } else {
            Row row = gps1sheet.createRow(gps1sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(entry.getZamiar());
            row.createCell(1).setCellValue(entry.getNrUmowy());
            row.createCell(2).setCellValue(entry.getNrRej());
            row.createCell(3).setCellValue(entry.getStartTime());
            row.createCell(4).setCellValue(entry.getStartGps());
            row.createCell(5).setCellValue(entry.getEndTime());
            row.createCell(6).setCellValue(entry.getDistance());
        }
    }

    private void writeToGps2(Entry entry) {
        if (gps2sheet.getLastRowNum() == 0 && gps2sheet.getRow(0) == null) {
            Row row = gps2sheet.createRow(0);
            row.createCell(0).setCellValue("H");
            row.createCell(1).setCellValue(entry.getZamiar());
            row.createCell(2).setCellValue(entry.getNrUmowy());
            getAllGPSAndTimes(entry);
        } else {
            Row row = gps2sheet.createRow(gps2sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue("H");
            row.createCell(1).setCellValue(entry.getZamiar());
            row.createCell(2).setCellValue(entry.getNrUmowy());
            getAllGPSAndTimes(entry);
        }
    }

    private void getAllGPSAndTimes(Entry entry) {
        for (int i = 1; i < entry.getSheet().getPhysicalNumberOfRows(); i++) {
            Row row = entry.getSheet().getRow(i);
            Cell cell = row.getCell(0);
            LocalDateTime dateTime = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String date = dateTime.format(formatter);
            Cell cell7 = row.getCell(7);
            Cell cell8 = row.getCell(8);
            double cell7d = cell7.getNumericCellValue();
            double cell8d = cell8.getNumericCellValue();
            String combined = "N" + cell7d + ",E" + cell8d;
            Row gps2row = gps2sheet.createRow(gps2sheet.getLastRowNum() + 1);
            Cell gps2cell = gps2row.createCell(0);
            gps2cell.setCellValue(combined);
            gps2cell = gps2row.createCell(1);
            gps2cell.setCellValue(date);
        }
    }

    public void saveGps(){
        saveGps1();
        saveGps2();
    }

    private void saveGps1() {
        try (FileOutputStream fileOut = new FileOutputStream("gps1.xlsx")) {
            gps1workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveGps2() {
        try (FileOutputStream fileOut = new FileOutputStream("gps2.xlsx")) {
            gps2workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
