package de.fhg.iais.roberta.components;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;

public final class ConfigurationComponent extends Phrase<Void> {
    private final String componentType;

    private final boolean isActor;
    private final String userDefinedPortName;
    private final String portName;
    private final LinkedHashMap<String, String> componentProperties;
    private final String x; // TODO should not be string
    private final String y;

    /**
     * Should only be used by tests!
     */
    public ConfigurationComponent(String componentType, boolean isActor, String portName, String userDefinedName, Map<String, String> componentProperties) {
        this(
            componentType,
            isActor,
            portName,
            userDefinedName,
            componentProperties,
            BlocklyBlockProperties.make(componentType, "this-will-be-regenerated-anyway"),
            BlocklyComment.make("empty-comment", false, "10", "10"),
            "0",
            "0");
    }

    public ConfigurationComponent(
        String componentType,
        boolean isActor,
        String portName,
        String userDefinedName,
        Map<String, String> componentProperties,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        String x,
        String y) {
        super(new BlockType(userDefinedName, Category.CONFIGURATION_BLOCK, ConfigurationComponent.class), properties, comment);
        this.componentType = componentType;
        this.isActor = isActor;
        this.portName = portName;
        this.userDefinedPortName = userDefinedName;
        this.componentProperties = new LinkedHashMap<>(componentProperties);
        this.x = x;
        this.y = y;
    }

    public String getComponentType() {
        return this.componentType;
    }

    public boolean isActor() {
        return this.isActor;
    }

    public boolean isSensor() {
        return !this.isActor;
    }

    public boolean isRegulated() {
        return getProperty(SC.MOTOR_REGULATION).equals(SC.TRUE);
    }

    public boolean isReverse() {
        return getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
    }

    public String getSide() {
        switch ( getProperty(SC.MOTOR_DRIVE) ) {
            case SC.LEFT:
                return SC.LEFT;
            case SC.RIGHT:
                return SC.RIGHT;
            default:
                return SC.NONE;
        }
    }

    public String getPortName() {
        return this.portName;
    }

    public String getUserDefinedPortName() {
        return this.userDefinedPortName;
    }

    public Map<String, String> getComponentProperties() {
        return Collections.unmodifiableMap(this.componentProperties);
    }

    public String getProperty(String propertyName) {
        Assert.nonEmptyString(propertyName, "No valid property name %s", propertyName);
        String propertyValue = this.componentProperties.get(propertyName);
        Assert.notNull(propertyValue, "No property with name %s", propertyName);

        return propertyValue;
    }

    public String getOptProperty(String propertyName) {
        Assert.nonEmptyString(propertyName, "No valid property name %s", propertyName);
        return this.componentProperties.get(propertyName);
    }

    public String getX() {
        return this.x;
    }

    public String getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "ConfigurationComponent ["
            + "isActor="
            + this.isActor
            + ", userDefinedName="
            + this.userDefinedPortName
            + ", portName="
            + this.portName
            + ", componentProperties="
            + this.componentProperties
            + "]";
    }

    @Override
    protected Void acceptImpl(IVisitor<Void> visitor) {
        // WE ACCEPT NOTHING!
        throw new DbcException("ConfigurationComponent should not be visited on it's own, instead the whole Configuration should be visited");
    }

    @Override
    public Block astToBlock() {
        Block destination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, destination);
        if (this.componentType.equals("WEDO")) {
            Ast2JaxbHelper.addField(destination, "VAR", this.userDefinedPortName); // TODO why is this necessary? change the way this works
        } else if (this.componentType.equals("SENSEBOX")) {
            Mutation mutation = new Mutation();
            mutation.setItems(BigInteger.valueOf((this.componentProperties.size() / 2))); // TODO !!
            destination.setMutation(mutation);
            Ast2JaxbHelper.addField(destination, "BOX_ID", this.userDefinedPortName); // TODO !!
        } else {
            Ast2JaxbHelper.addField(destination, "NAME", this.userDefinedPortName);
        }
        this.componentProperties.forEach((k, v) -> Ast2JaxbHelper.addField(destination, k, v));
        return destination;
    }

}
