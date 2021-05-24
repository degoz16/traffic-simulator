package ru.nsu.fit.traffic.interfaces.control;

import java.io.File;

public interface MenuControlInterface {
    void onOpenProject(File file);
    void onNewProject();
    void onSave();
    void onSaveAs(File file);
    void onConnectToServer(String ip, String port);
}
