import gen.jythonListener;
import gen.jythonParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ProgramPrinter implements jythonListener {
    private int indention = 0;

    @Override
    public void enterProgram(jythonParser.ProgramContext ctx) {
        System.out.println("program start{");
        indention += 4;
    }

    @Override
    public void exitProgram(jythonParser.ProgramContext ctx) {
        indention -= 4;
        System.out.println("}".indent(indention).stripTrailing());
    }

    @Override
    public void enterImportclass(jythonParser.ImportclassContext ctx) {
        System.out.printf("import class: %s".indent(indention), ctx.CLASSNAME());
    }

    @Override
    public void exitImportclass(jythonParser.ImportclassContext ctx) {}

    @Override
    public void enterClassDef(jythonParser.ClassDefContext ctx) {
        StringBuilder parents = new StringBuilder();
        if(ctx.CLASSNAME(1) != null){
            for (int i=1;i<ctx.CLASSNAME().size();i++){
                parents.append(ctx.CLASSNAME(i)).append(",");
            }
        }
        else {
            parents.append("object, ");
        }
        System.out.printf("class: %s/ class parents: %s{".indent(indention), ctx.CLASSNAME(0),parents.toString());
        indention += 4;
    }

    @Override
    public void exitClassDef(jythonParser.ClassDefContext ctx){
        indention -= 4;
        System.out.println("}".indent(indention));
    }

    @Override
    public void enterClass_body(jythonParser.Class_bodyContext ctx) {}

    @Override
    public void exitClass_body(jythonParser.Class_bodyContext ctx) {}

    @Override
    public void enterVarDec(jythonParser.VarDecContext ctx) { //TODO
        switch (ctx.parent.getRuleIndex()){
            case 3: //class_body
            case 9: //statement
                System.out.printf("field: %s/ type= %s".indent(indention), ctx.ID(), ((ctx.CLASSNAME()==null)?(ctx.TYPE().getText()):(ctx.CLASSNAME().getText())));
                break;
            case 8: //parameter
                    System.out.printf("%s %s,", ((ctx.CLASSNAME()==null)?(ctx.TYPE().getText()):(ctx.CLASSNAME().getText())), ctx.ID().getText());
                break;
            case 19: //assignment
                break;
        }
    }

    @Override
    public void exitVarDec(jythonParser.VarDecContext ctx) {
    }

    @Override
    public void enterArrayDec(jythonParser.ArrayDecContext ctx) { //TODO
        switch (ctx.parent.getRuleIndex()) { //class_body
            case 3, 19 -> { //assignment
                System.out.printf("field: %s/ type= %s".indent(indention).stripTrailing(), ctx.ID(), ctx.CLASSNAME().getText());
                System.out.println();
            }
        }
    }

    @Override
    public void exitArrayDec(jythonParser.ArrayDecContext ctx) { //TODO

    }

    @Override
    public void enterMethodDec(jythonParser.MethodDecContext ctx) {
        String toPrint = "";
        if(ctx.ID().getText().equals("main")){
            toPrint = "main method{";
        }
        else{
            String returnType;
            if(ctx.TYPE() != null){
                returnType = ctx.TYPE().getText();
            }
            else if(ctx.CLASSNAME() != null){
                returnType = ctx.CLASSNAME().getText();
            }
            else{
                returnType = "void";
            }
            toPrint = String.format("class method: " + ctx.ID().getText() + "/ return Type=%s{", returnType);
        }
        System.out.println(toPrint.indent(indention).stripTrailing());
        indention += 4;
    }

    @Override
    public void exitMethodDec(jythonParser.MethodDecContext ctx) {
        indention -= 4;
        System.out.print("}".indent(indention));
    }

    @Override
    public void enterConstructor(jythonParser.ConstructorContext ctx) {
        System.out.printf("class constructor: %s".indent(indention),ctx.CLASSNAME()+"{");
        indention += 4;
    }

    @Override
    public void exitConstructor(jythonParser.ConstructorContext ctx) {
        indention -= 4;
        System.out.print("}".indent(indention));
    }

    @Override
    public void enterParameter(jythonParser.ParameterContext ctx) {
        System.out.print("parameter list: [".indent(indention).stripTrailing());
    }

    @Override
    public void exitParameter(jythonParser.ParameterContext ctx) {
        System.out.println("]");
    }

    @Override
    public void enterStatement(jythonParser.StatementContext ctx) {}

    @Override
    public void exitStatement(jythonParser.StatementContext ctx) {}

    @Override
    public void enterReturn_statment(jythonParser.Return_statmentContext ctx) {

    }

    @Override
    public void exitReturn_statment(jythonParser.Return_statmentContext ctx) {

    }

    @Override
    public void enterCondition_list(jythonParser.Condition_listContext ctx) {

    }

    @Override
    public void exitCondition_list(jythonParser.Condition_listContext ctx) {

    }

    @Override
    public void enterCondition(jythonParser.ConditionContext ctx) {

    }

    @Override
    public void exitCondition(jythonParser.ConditionContext ctx) {

    }

    @Override
    public void enterIf_statment(jythonParser.If_statmentContext ctx) {
        System.out.print(ctx.getText());
    }

    @Override
    public void exitIf_statment(jythonParser.If_statmentContext ctx) {

    }

    @Override
    public void enterWhile_statment(jythonParser.While_statmentContext ctx) {

    }

    @Override
    public void exitWhile_statment(jythonParser.While_statmentContext ctx) {

    }

    @Override
    public void enterIf_else_statment(jythonParser.If_else_statmentContext ctx) {
//        System.out.println("if(");
//        System.out.println(ctx.condition_list(1));

    }

    @Override
    public void exitIf_else_statment(jythonParser.If_else_statmentContext ctx) {

    }

    @Override
    public void enterPrint_statment(jythonParser.Print_statmentContext ctx) {

    }

    @Override
    public void exitPrint_statment(jythonParser.Print_statmentContext ctx) {

    }

    @Override
    public void enterFor_statment(jythonParser.For_statmentContext ctx) {

    }

    @Override
    public void exitFor_statment(jythonParser.For_statmentContext ctx) {

    }

    @Override
    public void enterMethod_call(jythonParser.Method_callContext ctx) {

    }

    @Override
    public void exitMethod_call(jythonParser.Method_callContext ctx) {

    }

    @Override
    public void enterAssignment(jythonParser.AssignmentContext ctx) {

    }

    @Override
    public void exitAssignment(jythonParser.AssignmentContext ctx) {

    }

    @Override
    public void enterExp(jythonParser.ExpContext ctx) {

    }

    @Override
    public void exitExp(jythonParser.ExpContext ctx) {

    }

    @Override
    public void enterPrefixexp(jythonParser.PrefixexpContext ctx) {

    }

    @Override
    public void exitPrefixexp(jythonParser.PrefixexpContext ctx) {

    }

    @Override
    public void enterArgs(jythonParser.ArgsContext ctx) {

    }

    @Override
    public void exitArgs(jythonParser.ArgsContext ctx) {

    }

    @Override
    public void enterExplist(jythonParser.ExplistContext ctx) {

    }

    @Override
    public void exitExplist(jythonParser.ExplistContext ctx) {

    }

    @Override
    public void enterArithmetic_operator(jythonParser.Arithmetic_operatorContext ctx) {

    }

    @Override
    public void exitArithmetic_operator(jythonParser.Arithmetic_operatorContext ctx) {

    }

    @Override
    public void enterRelational_operators(jythonParser.Relational_operatorsContext ctx) {

    }

    @Override
    public void exitRelational_operators(jythonParser.Relational_operatorsContext ctx) {

    }

    @Override
    public void enterAssignment_operators(jythonParser.Assignment_operatorsContext ctx) {

    }

    @Override
    public void exitAssignment_operators(jythonParser.Assignment_operatorsContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
