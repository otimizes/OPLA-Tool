/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.dinf.gres.domain.oldgui.logs;

import br.ufpr.dinf.gres.loglog.Listener;
import br.ufpr.dinf.gres.loglog.LogLogData;
import br.ufpr.dinf.gres.domain.oldgui.utils.Time;

import javax.swing.*;

/**
 * @author elf
 */
public class LogListener implements Listener {

    private JTextArea textArea;

    public LogListener(JTextArea logs) {
        this.textArea = logs;
    }

    @Override
    public void message() {

        this.textArea.append(Time.timeNow() + LogLogData.printLog() + "\n");
    }

}
