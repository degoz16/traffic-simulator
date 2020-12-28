package ru.nsu.fit.traffic.controller.saveload;

import java.io.File;

public interface SaveLoadControlInterface {
    void onOpenProject(File file);
    void onNewProject();
    void onSave();
    void onSaveAs(File file);
}
