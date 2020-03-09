package de.fhg.iais.roberta.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * This class represents model of the hardware configuration of a robot (assume we have "left" and "right" motor). It is used in the code generation. <br>
 * <br>
 * The {@link ConfigurationAst} contains four sensor ports and four actor ports. Client cannot connect more than that.
 */
public class ConfigurationAst {
    private final LinkedHashMap<String, ConfigurationComponent> configurationComponents;

    private String robotName;

    private final List<String> componentTypes;

    private final String robotType;
    private final String xmlVersion;
    private final String description;
    private final String tags;

    private final float wheelDiameter;
    private final float trackWidth;

    private final String ipAddress;
    private final String portNumber;
    private final String userName;
    private final String password;

    protected ConfigurationAst(
        Collection<ConfigurationComponent> configurationComponents,
        String robotType,
        String xmlVersion,
        String description,
        String tags,
        float wheelDiameter,
        float trackWidth,
        String ipAddress,
        String portNumber,
        String userName,
        String password) {
        this.configurationComponents = buildConfigurationComponentMap(configurationComponents);
        this.robotType = robotType;
        this.xmlVersion = xmlVersion;
        this.description = description;
        this.tags = tags;
        this.wheelDiameter = wheelDiameter;
        this.trackWidth = trackWidth;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.userName = userName;
        this.password = password;
        this.componentTypes = new ArrayList<>();
        for ( ConfigurationComponent configurationComponent : this.configurationComponents.values() ) {
            this.componentTypes.add(configurationComponent.getComponentType());
        }
    }

    public void accept(IVisitor<Void> visitor) {
        for ( ConfigurationComponent configurationComponent : this.configurationComponents.values() ) {
            configurationComponent.acceptImpl(visitor);
        }
    }

    public String getRobotName() {
        return this.robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getRobotType() {
        return this.robotType;
    }

    public String getXmlVersion() {
        return this.xmlVersion;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTags() {
        return this.tags;
    }

    /**
     * @return the robot's wheel diameter in cm
     */
    public float getWheelDiameter() {
        return this.wheelDiameter;
    }

    /**
     * @return the robot's track width in cm
     */
    public float getTrackWidth() {
        return this.trackWidth;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getPortNumber() {
        return this.portNumber;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public Map<String, ConfigurationComponent> getConfigurationComponents() {
        return this.configurationComponents;
    }

    public boolean isComponentTypePresent(String componentType) {
        return this.componentTypes.contains(componentType);
    }

    public Collection<ConfigurationComponent> getConfigurationComponentsValues() {
        return this.configurationComponents.values();
    }

    public ConfigurationComponent getConfigurationComponentByType(String type) {
        for ( ConfigurationComponent cc : this.configurationComponents.values() ) {
            if ( cc.getComponentType().equals(type) ) {
                return cc;
            }
        }
        return null;
    }

    public Collection<ConfigurationComponent> getActors() {
        return this.configurationComponents.values().stream().filter(ConfigurationComponent::isActor).collect(Collectors.toList());
    }

    public Collection<ConfigurationComponent> getSensors() {
        return this.configurationComponents.values().stream().filter(ConfigurationComponent::isSensor).collect(Collectors.toList());
    }

    public ConfigurationComponent getConfigurationComponent(String userDefinedName) {
        ConfigurationComponent configurationComponent = this.configurationComponents.get(userDefinedName);
        Assert.notNull(configurationComponent, "configuration component missing for user defined name " + userDefinedName);
        return configurationComponent;
    }

    public ConfigurationComponent optConfigurationComponent(String userDefinedName) {
        return this.configurationComponents.get(userDefinedName);
    }

    public String getFirstMotorPort(String side) {
        return getFirstMotor(side).getUserDefinedPortName();
    }

    public ConfigurationComponent getFirstMotor(String side) {
        List<ConfigurationComponent> found = getMotors(side);
        if ( found.size() == 0 ) {
            return null;
        } else {
            return found.get(0);
        }
    }

    public List<ConfigurationComponent> getMotors(String side) {
        List<ConfigurationComponent> found = new ArrayList<>();
        for ( ConfigurationComponent component : this.configurationComponents.values() ) {
            if ( component.isActor() && side.equals(component.getOptProperty(SC.MOTOR_DRIVE)) ) {
                found.add(component);
            }
        }
        return found;
    }

    public boolean isMotorRegulated(String port) {
        if ( getConfigurationComponent(port).getOptProperty(SC.MOTOR_REGULATION) == null ) {
            return false;
        } else {
            return getConfigurationComponent(port).getOptProperty(SC.MOTOR_REGULATION).equals(SC.TRUE);
        }
    }

    private LinkedHashMap<String, ConfigurationComponent> buildConfigurationComponentMap(Collection<ConfigurationComponent> configurationComponents) {
        LinkedHashMap<String, ConfigurationComponent> map = new LinkedHashMap<>();
        for ( ConfigurationComponent configurationComponent : configurationComponents ) {
            map.put(configurationComponent.getUserDefinedPortName(), configurationComponent);
        }
        return map;
    }

    /**
     * This class is a builder of {@link ConfigurationAst}
     */
    public static class Builder {
        private List<ConfigurationComponent> configurationComponents = new ArrayList<>();

        private String robotType = "";
        private String xmlVersion = "";
        private String description = "";
        private String tags = "";

        private float wheelDiameter = 0.0f;
        private float trackWidth = 0.0f;
        private String ipAddress = "";
        private String portNumber = "";
        private String userName = "";
        private String password = "";

        public Builder setRobotType(String robotType) {
            this.robotType = robotType;
            return this;
        }

        public Builder setXmlVersion(String xmlVersion) {
            this.xmlVersion = xmlVersion;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setTags(String tags) {
            this.tags = tags;
            return this;
        }

        /**
         * Set the wheel diameter
         *
         * @param wheelDiameter in cm
         */
        public Builder setWheelDiameter(float wheelDiameter) {
            this.wheelDiameter = wheelDiameter;
            return this;
        }

        /**
         * Set the track width
         *
         * @param trackWidth in cm
         */

        public Builder setTrackWidth(float trackWidth) {
            this.trackWidth = trackWidth;
            return this;
        }

        public Builder setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder setPortNumber(String portNumber) {
            this.portNumber = portNumber;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * Client must provide list of hardware components ({@link ConfigurationComponent})
         *
         * @param components we want to connect to the brick configuration
         */
        public Builder addComponents(List<ConfigurationComponent> components) {
            this.configurationComponents = components;
            return this;
        }

        public ConfigurationAst build() {
            return new ConfigurationAst(this.configurationComponents,
                robotType, xmlVersion, description, tags, wheelDiameter, trackWidth, ipAddress, portNumber, userName, password);
        }

        public <C> C build(Class<C> configAstClass) {
            try {
                return configAstClass.getDeclaredConstructor(Collection.class, String.class, String.class, String.class, String.class, Float.class, Float.class, String.class, String.class, String.class, String.class).newInstance(this.configurationComponents,
                    robotType, xmlVersion, description, tags, wheelDiameter, trackWidth, ipAddress, portNumber, userName, password);
            } catch ( Exception e ) {
                throw new DbcException("configuration class " + configAstClass.getSimpleName() + " has no constructor usable by the configuration builder", e);
            }
        }
    }
}
