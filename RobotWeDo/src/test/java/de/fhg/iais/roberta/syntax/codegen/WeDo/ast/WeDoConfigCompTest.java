package de.fhg.iais.roberta.syntax.codegen.WeDo.ast;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WeDoConfigCompTest extends AstTest {
    @Test
    public void WeDoBrick_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult = "BlockAST [project=[ConfigurationComponent[isActor=true,userDefinedName=W,portName=null,componentProperties={}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/robBrick_WeDo-Brick.xml");
    }

    @Test
    public void WeDoBrick_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/robBrick_WeDo-Brick.xml");
    }

    @Test
    public void buzzer_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult = "BlockAST [project=[ConfigurationComponent[isActor=true,userDefinedName=S,portName=null,componentProperties={VAR=W}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/robConf_buzzer.xml");
    }

    @Test
    public void buzzer_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/robConf_buzzer.xml");
    }

    @Test
    public void gyro_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult = "BlockAST [project=[ConfigurationComponent[isActor=false,userDefinedName=N,portName=null,componentProperties={VAR=W,CONNECTOR=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/robConf_gyro.xml");
    }

    @Test
    public void gyro_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/robConf_gyro.xml");
    }

    @Test
    public void infrared_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult = "BlockAST [project=[ConfigurationComponent[isActor=false,userDefinedName=I,portName=null,componentProperties={VAR=W,CONNECTOR=2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/robConf_infrared.xml");
    }

    @Test
    public void infrared_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/robConf_infrared.xml");
    }

    @Test
    public void key_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult = "BlockAST [project=[ConfigurationComponent[isActor=false,userDefinedName=T,portName=null,componentProperties={VAR=W}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/robConf_key.xml");
    }

    @Test
    public void key_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/robConf_key.xml");
    }

    @Test
    public void led_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult = "BlockAST [project=[ConfigurationComponent[isActor=false,userDefinedName=L,portName=null,componentProperties={VAR=W}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/robConf_led.xml");
    }

    @Test
    public void led_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/robConf_led.xml");
    }
}
