package ru.nsu.fit.traffic.interfaces.control;

import java.io.File;

public interface SaveLoadControlInterface {
    void onOpenProject(File file);
    void onNewProject();
    void onSave();
    void onSaveAs(File file);
}
