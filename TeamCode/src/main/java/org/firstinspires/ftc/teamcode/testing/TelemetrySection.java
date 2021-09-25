package org.firstinspires.ftc.teamcode.testing;

import java.util.ArrayList;
import java.util.List;

public class TelemetrySection {
    private List<TelemetryEntry> entries;

    private int level;
    private String sign;

    public TelemetrySection() {
        entries = new ArrayList<>();
    }

    public TelemetrySection(int level, String sign) {
        this.level = level;
        this.sign = sign;

        entries = new ArrayList<>();
    }

    public TelemetrySection(int level, String sign, List<TelemetryEntry> entries) {
        this.level = level;
        this.sign = sign;

        this.entries = entries;
    }

    public void add(TelemetryEntry entry) {
        entries.add(entry);
    }

    public void remove(int index) {
        try {
            entries.remove(index);
        } catch (Exception e) {

        }
    }

    /*
        Getters
     */
    public int getLevel() {
        return level;
    }

    public String getSign() {
        return sign;
    }

    public List<TelemetryEntry> getEntries() {
        return entries;
    }

    /*
        Setters
     */
    public void setLevel(int level) {
        this.level = level;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setEntries(List<TelemetryEntry> entries) {
        this.entries = entries;
    }
}
