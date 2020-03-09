package de.fhg.iais.roberta.ast;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class FestoConfigCompTest extends AstTest {

    @Test
    public void led_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult = "BlockAST [project=[ConfigurationComponent[isActor=true,userDefinedName=L,portName=null,componentProperties={INPUT=LED_BUILTIN}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/festo/robConf_led.xml");
    }

    @Test
    public void led_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/festo/robConf_led.xml");
    }

    @Test
    public void servo_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult = "BlockAST [project=[ConfigurationComponent[isActor=true,userDefinedName=S,portName=null,componentProperties={PULSE=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/festo/robConf_servo.xml");
    }

    @Test
    public void servo_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/festo/robConf_servo.xml");
    }
}
