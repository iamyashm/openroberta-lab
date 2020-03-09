package de.fhg.iais.roberta.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.Callback;
import de.fhg.iais.roberta.util.dbc.DbcException;
import static de.fhg.iais.roberta.transformer.AbstractJaxb2Ast.extractBlockProperties;
import static de.fhg.iais.roberta.transformer.AbstractJaxb2Ast.extractComment;
import static de.fhg.iais.roberta.transformer.AbstractJaxb2Ast.extractFields;
import static de.fhg.iais.roberta.transformer.AbstractJaxb2Ast.extractValues;
import static de.fhg.iais.roberta.transformer.AbstractJaxb2Ast.optField;

public final class Jaxb2ConfigurationAst {

    public static Block getTopBlock(BlockSet blockSet, String topBlockName) {
        List<Instance> instances = blockSet.getInstance();
        List<Block> blocks;
        Block startingBlock = null;
        for ( Instance instance : instances ) {
            blocks = instance.getBlock();
            for ( Block block : blocks ) {
                if ( block.getType().equals(topBlockName) ) {
                    startingBlock = block;
                }
            }
        }
        if ( startingBlock == null ) {
            throw new DbcException("No valid base/starting block was found in the configuration");
        }
        return startingBlock;
    }

    public static ConfigurationAst blocks2OldConfig(BlockSet set, BlocklyDropdownFactory factory, String topBlockName, String sensorsPrefix) {
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        Block topBlock = Jaxb2ConfigurationAst.getTopBlock(set, topBlockName);
        List<Field> fields = extractFields(topBlock, (short) 255);
        setWithOptField(fields, "WHEEL_DIAMETER", wd -> builder.setWheelDiameter(Float.parseFloat(wd)));
        setWithOptField(fields, "TRACK_WIDTH", wd -> builder.setTrackWidth(Float.parseFloat(wd)));
        setWithOptField(fields, "IP_ADDRESS", builder::setIpAddress);
        setWithOptField(fields, "PORT", builder::setPortNumber);
        setWithOptField(fields, "USERNAME", builder::setUserName);
        setWithOptField(fields, "PASSWORD", builder::setPassword);

        List<ConfigurationComponent> allComponents = extractOldConfigurationComponent(topBlock, factory, sensorsPrefix);

        return builder.addComponents(allComponents).build();
    }

    private static void setWithOptField(List<Field> fields, String name, Callback<String> callback) {
        final String val = optField(fields, name);
        if ( val != null ) {
            callback.call(val);
        }
    }

    private static List<ConfigurationComponent> extractOldConfigurationComponent(Block topBlock, BlocklyDropdownFactory factory, String sensorsPrefix) {
        List<ConfigurationComponent> allComponents = new ArrayList<>();
        for ( Value value : extractValues(topBlock, (short) 255) ) {
            String portName = value.getName();
            String userDefinedName = portName.substring(1);
            boolean isActor = !portName.startsWith(sensorsPrefix);
            String blocklyName = value.getBlock().getType();
            List<Field> fields = extractFields(value.getBlock(), (short) 255);
            Map<String, String> properties = new HashMap<>();
            for ( Field field : fields ) {
                String fKey = field.getName();
                String fValue = field.getValue();
                properties.put(fKey, fValue);
            }
            ConfigurationComponent cc =
                new ConfigurationComponent(
                    factory.getConfigurationComponentTypeByBlocklyName(blocklyName),
                    isActor,
                    portName,
                    userDefinedName,
                    properties,
                    extractBlockProperties(topBlock),
                    extractComment(topBlock),
                    "0", // TODO
                    "0");
            allComponents.add(cc);
        }
        return allComponents;
    }

    public static ConfigurationAst blocks2NewConfig(BlockSet set, BlocklyDropdownFactory factory) {
        List<Instance> instances = set.getInstance();
        List<ConfigurationComponent> allComponents = new ArrayList<>();
        for ( Instance instance : instances ) {
            allComponents.add(instance2NewConfigComp(instance, factory));
        }

        return new ConfigurationAst.Builder().setRobotType(set.getRobottype()).setXmlVersion(set.getXmlversion()).setDescription(set.getDescription()).setTags(set.getTags()).addComponents(allComponents).build();
    }

    private static ConfigurationComponent instance2NewConfigComp(Instance instance, BlocklyDropdownFactory factory) {
        Block firstBlock = instance.getBlock().get(0);
        String componentType = factory.getConfigurationComponentTypeByBlocklyName(firstBlock.getType());
        String userDefinedName = firstBlock.getField().get(0).getValue();
        Map<String, String> m = new LinkedHashMap<>();
        for ( int i = 1; i < firstBlock.getField().size(); i++ ) {
            m.put(firstBlock.getField().get(i).getName(), firstBlock.getField().get(i).getValue());
        }
        return new ConfigurationComponent(componentType, true, null, userDefinedName, m, extractBlockProperties(firstBlock), extractComment(firstBlock), instance.getX(), instance.getY());
    }
}
