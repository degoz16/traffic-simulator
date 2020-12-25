package ru.nsu.fit.traffic.controller.saveload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ru.nsu.fit.traffic.controller.BaseControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.json.parse.MapJsonStruct;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.UpdateObserver;

import java.io.*;

public class SaveLoadControl extends BaseControl {
    private String pathToProjectDir;


    public String getPathToProjectDir() {
        return pathToProjectDir;
    }

    public SaveLoadControl(
            SceneElementsControl sceneElementsControl) {
        super(sceneElementsControl);
        pathToProjectDir = getDocsPath();
    }

    private String getDocsPath() {
        String myDocuments = null;
        try {
            Process p =
                    Runtime.getRuntime()
                            .exec(
                                    "reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion"
                                            + "\\Explorer\\Shell Folders\" /v personal");
            p.waitFor();
            InputStream in = p.getInputStream();
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();
            myDocuments = new String(b);
            myDocuments = myDocuments.split("\\s\\s+")[4];
            int num = 0;
            String projectName = "myTrafficProject";
            File file = new File(myDocuments, projectName + ".tsp");
            while (file.exists()) {
                file = new File(myDocuments, projectName + (num++) + ".tsp");
            }
            if (num == 0) {
                myDocuments += "\\" + projectName + ".tsp";
            } else {
                myDocuments += "\\" + projectName + "(" + (num - 1) + ").tsp";
            }
        } catch (Throwable t) {
            assert false;
        }
        return myDocuments;
    }

    private void saveCurrentProject(File file) {
        try {
            Writer writer = new FileWriter(file);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonMap = gson.toJson(new MapJsonStruct(editOperationsManager.getMap()));
            writer.write(jsonMap);
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void onOpenProject(File file) {
        if (file != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                Reader fileReader = new FileReader(file);
                MapJsonStruct mapJsonStruct = gson.fromJson(fileReader, MapJsonStruct.class);
//                File oldFile =
//                        new File(pathToProjectDir); // TODO Должно быть диалоговое окно с выбором СОХРАНиТЬ/НЕТ
//                saveCurrentProject(oldFile);
                mapJsonStruct.toTrafficMap(editOperationsManager.getMap());
                pathToProjectDir = file.getAbsolutePath();
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
        update.update(editOperationsManager);
    }

    public void onNewProject() {
//        File file =
//                new File(pathToProjectDir); // TODO Должно быть диалоговое окно с выбором СОХРАНиТЬ/НЕТ
//        saveCurrentProject(file);
        editOperationsManager.getMap().clearMap();
        pathToProjectDir = getDocsPath();
        update.update(editOperationsManager);
    }

    public void onSave() {
        File file = new File(pathToProjectDir);
        saveCurrentProject(file);
    }

    public void onSaveAs(File file) {
        if (file != null) {
            saveCurrentProject(file);
            pathToProjectDir = file.getAbsolutePath();
        }
    }
}
