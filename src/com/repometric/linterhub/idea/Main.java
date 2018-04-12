package com.repometric.linterhub.idea;

import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.openapi.components.ApplicationComponent;
import com.repometric.linterhub.integration.Integration;
import com.repometric.linterhub.integration.LinterhubMode;
import com.repometric.linterhub.integration.Settings;
import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystems;

public class Main extends ApplicationComponent.Adapter implements InspectionToolProvider {

    @NotNull
    @Override
    public String getComponentName() {
        return "Linterhub";
    }

    private static final Class[] INSPECTION_CLASSES = {
            AnalysisInspections.class
    };

    public Class[] getInspectionClasses() {
        return INSPECTION_CLASSES;
    }

    @Override
    public void initComponent() {
        Settings settings = new Settings();
        settings.cliRoot = FileSystems.getDefault().getPath(System.getProperty("idea.plugins.path"), "Linterhub IDEA").toString();
        settings.cliPath = FileSystems.getDefault().getPath(settings.cliRoot, "bin", "dotnet").toString();
        settings.mode = LinterhubMode.DOTNET;
        Integration.project = "";
        Integration.initialize(settings, "0.3.4");
    }
}