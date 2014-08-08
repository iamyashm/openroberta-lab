package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.functions.Funct.Function;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.helper.Helper;

public class TextFunctions {

    @Test
    public void getPresedence() {
        ArrayList<Expr> param = new ArrayList<Expr>();
        StringConst stringConst = StringConst.make("AS");
        param.add(stringConst);
        Funct funct = Funct.make(Function.ABS, param);
        Assert.assertEquals(10, funct.getPrecedence());
    }

    @Test
    public void getAssoc() {
        ArrayList<Expr> param = new ArrayList<Expr>();
        StringConst stringConst = StringConst.make("AS");
        param.add(stringConst);
        Funct funct = Funct.make(Function.ABS, param);
        Assert.assertEquals(Assoc.LEFT, funct.getAssoc());
    }

    @Test
    public void getOpSymbol() {
        ArrayList<Expr> param = new ArrayList<Expr>();
        StringConst stringConst = StringConst.make("AS");
        param.add(stringConst);
        Funct funct = Funct.make(Function.POWER, param);
        Assert.assertEquals("^", funct.getFunctName().getOpSymbol());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        Funct.Function funct = Function.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        Funct.Function funct = Function.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        Funct.Function funct = Function.get("asdf");
    }

    @Test
    public void concatination() throws Exception {
        Helper.generateSyntax("/syntax/functions/text_concat.xml");

        String a = "BlockAST [project=[[Funct [UPPERCASE, [Var [text]]]]]]";
        // Assert.assertEquals(a, transformer.toString());
    }
}