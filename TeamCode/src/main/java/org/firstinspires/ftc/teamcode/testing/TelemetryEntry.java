package org.firstinspires.ftc.teamcode.testing;

import org.jetbrains.annotations.NotNull;

public class TelemetryEntry {
    private TestTelemetryCommand.TYPE type;
    private String string;
    private String format;
    private Object data;

    /**
     * Constructor for a Telemetry entry
     *
     * @param type Type of entry
     * @see TestTelemetryCommand.TYPE
     * @param caption Caption to be displayed
     * @param format Format data lists should be displayed in
     * @param data The data to pass in
     */
    public TelemetryEntry(@NotNull TestTelemetryCommand.TYPE type, String caption, String format, Object data) {
        this.type = type;
        this.string = caption;
        this.format = format;
        this.data = data;
    }

    /**
     * Gets the Entry's Type
     *
     * @return The Type
     * @see TestTelemetryCommand.TYPE
     */
    public TestTelemetryCommand.TYPE getType() {
        return type;
    }

    /**
     * Gets the Entry's Caption
     *
     * @return The Caption
     */
    public String getCaption() {
        return string;
    }

    /**
     * Gets the Entry's Format
     *
     * @return The Format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Gets the Entry's Data
     *
     * @return The Data
     */
    public Object getData() {
        return data;
    }
}
