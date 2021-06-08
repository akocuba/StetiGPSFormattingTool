package com.vulpes;

import com.vulpes.datamodel.Entry;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class EntryDialogController {
    private XSSFSheet loadedSheet;
    @FXML
    private DialogPane dialogPanel;
    @FXML
    private TextField zamiarField;
    @FXML
    private TextField nrUmowyField;
    @FXML
    private TextField nrRejField;
    @FXML
    private Label filePath;
    private File rememberedDirectory;

    public Entry processResults(){
        Entry entry = new Entry();
        entry.setZamiar(zamiarField.getText());
        entry.setNrRej(nrRejField.getText());
        entry.setNrUmowy(nrUmowyField.getText());
        entry.setSheet(loadedSheet);
        return entry;
    }

    @FXML
    public void openFile() {
        loadPath();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Wczytaj plik");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik Excel", "*.xlsx; *.xls"));
        if(rememberedDirectory != null){
            chooser.setInitialDirectory(rememberedDirectory);
        }
        File file = chooser.showOpenDialog(dialogPanel.getScene().getWindow());
        if (file != null) {
            try (FileInputStream fileInputStream = new FileInputStream(file)){
                XSSFWorkbook loadedWorkbook = new XSSFWorkbook(file);
                loadedSheet = loadedWorkbook.getSheetAt(0);
                filePath.setText(file.getName());
                rememberedDirectory = file.getParentFile();
                savePath();
                String[] arr = file.getName().split(" ");
                zamiarField.setText(arr[1]);
                nrRejField.setText(arr[0]);
            } catch (IOException | InvalidFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void savePath(){
        String path = rememberedDirectory.getPath();
        try {
            FileWriter fw = new FileWriter("lastPath.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(path);
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPath(){
        File file = new File("lastPath.txt");
        if(file.exists()){
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String path = br.readLine();
                rememberedDirectory = new File(path);
                br.close();
                fr.close();
            } catch (IOException e) {
                System.out.println("Brak sciezki");
            }
        }
    }

}