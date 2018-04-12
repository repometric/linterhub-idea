package com.repometric.linterhub.idea;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.psi.PsiFile;
import com.repometric.linterhub.idea.util.PsiUtil;
import com.repometric.linterhub.integration.Integration;
import com.repometric.linterhub.integration.Problem;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.Vector;

@NonNls
public class AnalysisInspections extends LocalInspectionTool {

    @Pattern("[a-zA-Z_0-9.-]+")
    @NotNull
    @Override
    public String getID() {
        return "Linterhub";
    }

    @Override
    public boolean runForWholeFile() {
        return true;
    }

    @Nullable
    @Override
    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        Integration.project = file.getProject().getBaseDir().getCanonicalPath();

        if (file.getFileType().isBinary() || !file.isValid()) {
            return null;
        }
        String file_path = PsiUtil.toPath(file).toAbsolutePath().toString();
        File f = new File(PsiUtil.toPath(file).toAbsolutePath().toString());
        Vector<Problem> result;
        if (String.valueOf(f.lastModified()).equals(Integration.checks.get(file_path))) {
            result = Integration.cache.get(file_path);
        }
        else
        {
            Integration.checks.remove(file_path);
            Integration.checks.put(file_path, String.valueOf(f.lastModified()));
            result = Integration.analyzeFile(PsiUtil.toPath(file).toAbsolutePath().toString());
        }

        Vector<ProblemDescriptor> problems = new Vector<>();
        if(result != null){
            for (Problem problem : result) {
                if (problem != null)
                    problems.add(manager.createProblemDescriptor(file, problem.range, problem.linter + ": " + problem.message,
                            problem.type, isOnTheFly));
            }
        }

        return problems.size() == 0 ? null : problems.toArray(new ProblemDescriptor[0]);
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "General";
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "Linterhub checks";
    }
}