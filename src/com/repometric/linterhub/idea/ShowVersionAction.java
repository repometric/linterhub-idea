package com.repometric.linterhub.idea;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.repometric.linterhub.integration.Integration;

public class ShowVersionAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        String content = Integration.version();
        if(content != null)
            Notifications.Bus.notify(new Notification("Linterhub", "Linterhub Versions", content, NotificationType.INFORMATION));
    }
}
