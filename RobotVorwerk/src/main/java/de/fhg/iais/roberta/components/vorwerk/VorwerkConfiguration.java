package de.fhg.iais.roberta.components.vorwerk;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.SC;

public class VorwerkConfiguration extends ConfigurationAst {
    private static List<ConfigurationComponent> components;
    static {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent(SC.LARGE, true, "left", "LEFT", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent(SC.LARGE, true, "right", "RIGHT", motorBproperties);

        ConfigurationComponent leftUltrasonic = new ConfigurationComponent(SC.ULTRASONIC, false, "left_ultrasonic", "LEFT_ULTRASONIC", Collections.emptyMap());
        ConfigurationComponent centerUltrasonic =
            new ConfigurationComponent(SC.ULTRASONIC, false, "center_ultrasonic", "CENTER_ULTRASONIC", Collections.emptyMap());
        ConfigurationComponent rightUltrasonic =
            new ConfigurationComponent(SC.ULTRASONIC, false, "right_ultrasonic", "RIGHT_ULTRASONIC", Collections.emptyMap());
        ConfigurationComponent leftTouch = new ConfigurationComponent(SC.TOUCH, false, "left", "LEFT_TOUCH", Collections.emptyMap());
        ConfigurationComponent rightTouch = new ConfigurationComponent(SC.TOUCH, false, "right", "RIGHT_TOUCH", Collections.emptyMap());
        ConfigurationComponent leftDropoff = new ConfigurationComponent(SC.TOUCH, false, "left", "LEFT_DROPOFF", Collections.emptyMap());
        ConfigurationComponent rightDropoff = new ConfigurationComponent(SC.TOUCH, false, "right", "RIGHT_DROPOFF", Collections.emptyMap());
        ConfigurationComponent x = new ConfigurationComponent(SC.ACCELEROMETER, false, "x", "X", Collections.emptyMap());
        ConfigurationComponent y = new ConfigurationComponent(SC.ACCELEROMETER, false, "y", "Y", Collections.emptyMap());
        ConfigurationComponent z = new ConfigurationComponent(SC.ACCELEROMETER, false, "z", "Z", Collections.emptyMap());
        ConfigurationComponent strength = new ConfigurationComponent(SC.ACCELEROMETER, false, "strength", "STRENGTH", Collections.emptyMap());

        components = Lists
            .newArrayList(
                    motorA,
                    motorB,
                    leftUltrasonic,
                    rightUltrasonic,
                    centerUltrasonic,
                    leftTouch,
                    rightTouch,
                    x,
                    y,
                    z,
                    strength,
                    leftDropoff,
                    rightDropoff);
    }

    private VorwerkConfiguration(
        Collection<ConfigurationComponent> configurationComponents,
        String robottype,
        String xmlversion,
        String description,
        String tags,
        float wheelDiameter,
        float trackWidth,
        String ipAddress,
        String portNumber,
        String userName,
        String password) {
        super(components, robottype, xmlversion, description, tags, wheelDiameter, trackWidth, ipAddress, portNumber, userName, password);
    }

    private static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }
}
