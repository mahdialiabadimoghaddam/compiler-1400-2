import gen.jythonListener;
import gen.jythonParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Stack;

public class ProgramPrinter implements jythonListener {
    private final Stack<SymbolTable> scopes = new Stack<>();

    @Override
    public void enterProgram(jythonParser.ProgramContext ctx) {
        scopes.push(new SymbolTable("program", ctx.start.getLine()));
        SymbolTable.root = scopes.peek();
    }

    @Override
    public void exitProgram(jythonParser.ProgramContext ctx) {
        scopes.pop();
    }

    @Override
    public void enterImportclass(jythonParser.ImportclassContext ctx) {
        scopes.peek().insert("import_" + ctx.CLASSNAME(), "import" + " (name: " + ctx.CLASSNAME() + ")");
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
            parents.append("object");
        }
        parents.deleteCharAt(parents.length()-1);

        scopes.peek().insert("class_"+ctx.CLASSNAME(0), String.format("class (name: %s) (parent: %s)", ctx.CLASSNAME(0), parents));
        SymbolTable newScope = new SymbolTable(ctx.CLASSNAME(0).toString(), ctx.start.getLine());
        scopes.peek().children.add(newScope);
        scopes.push(newScope);
    }

    @Override
    public void exitClassDef(jythonParser.ClassDefContext ctx){
        scopes.pop();
    }

    @Override
    public void enterClass_body(jythonParser.Class_bodyContext ctx) {}

    @Override
    public void exitClass_body(jythonParser.Class_bodyContext ctx) {}

    @Override
    public void enterVarDec(jythonParser.VarDecContext ctx) {
        String fieldType;
        String identifier = ctx.ID().toString();
        String dataType = (ctx.CLASSNAME()==null) ? ctx.TYPE().toString()+", isDefined: True" : "ClassType= "+ctx.CLASSNAME().toString()+", isDefined: "+ checkIsDefined(ctx.CLASSNAME().toString());
        switch (ctx.parent.getRuleIndex()){
            case 3: //class field
                fieldType = "ClassField";
                break;
            case 19, 9: //assignment/method field
                fieldType = "MethodField";
                break;
            default: return;
        }
        scopes.peek().insert("Field_"+identifier, String.format("%s (name:%s) (type: [%s])", fieldType, identifier, dataType));
    }

    @Override
    public void exitVarDec(jythonParser.VarDecContext ctx) {}

    @Override
    public void enterArrayDec(jythonParser.ArrayDecContext ctx) { //TODO
        if (ctx.parent.getRuleIndex() == 3) { //class_body
            String dataType = (ctx.CLASSNAME() == null) ? ctx.TYPE().toString()+", isDefined: True" : ctx.CLASSNAME().toString()+", isDefined: False"+ checkIsDefined(ctx.CLASSNAME().toString());
            scopes.peek().insert("Field_"+ctx.ID().toString(), String.format("ClassArrayField (name: %s) (type: [%s])", ctx.ID().toString(), dataType));
        }
    }

    @Override
    public void exitArrayDec(jythonParser.ArrayDecContext ctx) {}

    @Override
    public void enterMethodDec(jythonParser.MethodDecContext ctx) {
        SymbolTable newScope = new SymbolTable(ctx.ID().toString(), ctx.start.getLine());

        String returnType = "void";
        if(ctx.TYPE() != null)
            returnType = ctx.TYPE().toString();
        else if(ctx.CLASSNAME() != null)
            returnType = "class type= " + ctx.CLASSNAME().toString();

        StringBuilder parameterList = new StringBuilder();
        if(ctx.parameter().size() != 0){
            int index = 0;
            parameterList.append("[parameter list: ");
            for (var entry: ctx.parameter(0).varDec()){
                index++;
                String dataType;
                String fullDataType;
                if(entry.CLASSNAME() == null) {
                    dataType = fullDataType = entry.TYPE().toString()+", isDefined: True";
                }
                else {
                    dataType = entry.CLASSNAME().toString();
                    fullDataType = String.format("[classType= %s, isDefined= %s]", dataType, checkIsDefined(entry.CLASSNAME().toString()));
                }

                newScope.insert("Field_"+entry.ID(), String.format("Parameter (name: %s) (type: %s) (index: %d)", entry.ID(), fullDataType, index));
                parameterList.append(String.format("[type: %s, index: %d],", dataType, index));
            }
            parameterList.deleteCharAt(parameterList.length()-1).append(']');
        }

        scopes.peek().insert("Method_"+ctx.ID().toString(), String.format("Method (name: %s) (return type: [%s] %s)", ctx.ID(), returnType, parameterList));
        scopes.peek().children.add(newScope);
        scopes.push(newScope);
    }

    @Override
    public void exitMethodDec(jythonParser.MethodDecContext ctx) {
        scopes.pop();
    }

    @Override
    public void enterConstructor(jythonParser.ConstructorContext ctx) {
        SymbolTable newScope = new SymbolTable(ctx.CLASSNAME().toString(), ctx.start.getLine());

        StringBuilder parameterList = new StringBuilder();
        if(ctx.parameter().size() != 0){
            int index = 0;
            parameterList.append("[parameter list: ");
            for (var entry: ctx.parameter(0).varDec()){
                index++;
                String dataType;
                String fullDataType;
                if(entry.CLASSNAME() == null) {
                    dataType = fullDataType = entry.TYPE().toString()+", isDefined: True";
                }
                else {
                    dataType = entry.CLASSNAME().toString();
                    fullDataType = String.format("[classType= %s, isDefined= %s]", dataType, checkIsDefined(entry.CLASSNAME().toString()));
                }

                newScope.insert("Field_"+entry.ID(), String.format("Parameter (name: %s) (type: %s) (index: %d)", entry.ID(), fullDataType, index));
                parameterList.append(String.format("[type: %s, index: %d],", dataType, index));
            }
            parameterList.deleteCharAt(parameterList.length()-1).append(']');
        }
        scopes.peek().insert("Constructor_"+ctx.CLASSNAME(), String.format("Constructor (name: %s) [parameter list: %s]", ctx.CLASSNAME(), parameterList));
        scopes.peek().children.add(newScope);
        scopes.push(newScope);
    }

    @Override
    public void exitConstructor(jythonParser.ConstructorContext ctx) {
        scopes.pop();
    }

    @Override
    public void enterParameter(jythonParser.ParameterContext ctx) {}

    @Override
    public void exitParameter(jythonParser.ParameterContext ctx) {}

    @Override
    public void enterStatement(jythonParser.StatementContext ctx) {}

    @Override
    public void exitStatement(jythonParser.StatementContext ctx) {}

    @Override
    public void enterReturn_statment(jythonParser.Return_statmentContext ctx) {}

    @Override
    public void exitReturn_statment(jythonParser.Return_statmentContext ctx) {}

    @Override
    public void enterCondition_list(jythonParser.Condition_listContext ctx) {}

    @Override
    public void exitCondition_list(jythonParser.Condition_listContext ctx) {}

    @Override
    public void enterCondition(jythonParser.ConditionContext ctx) {}

    @Override
    public void exitCondition(jythonParser.ConditionContext ctx) {}

    @Override
    public void enterIf_statment(jythonParser.If_statmentContext ctx) {
        SymbolTable newScope = new SymbolTable("if", ctx.start.getLine());
        scopes.peek().children.add(newScope);
        scopes.push(newScope);
    }

    @Override
    public void exitIf_statment(jythonParser.If_statmentContext ctx) {
        scopes.pop();
    }

    @Override
    public void enterWhile_statment(jythonParser.While_statmentContext ctx) {
        SymbolTable newScope = new SymbolTable("while", ctx.start.getLine());
        scopes.peek().children.add(newScope);
        scopes.push(newScope);
    }

    @Override
    public void exitWhile_statment(jythonParser.While_statmentContext ctx) {scopes.pop();}

    @Override
    public void enterIf_else_statment(jythonParser.If_else_statmentContext ctx) {
        SymbolTable newScope = new SymbolTable("if-else", ctx.start.getLine());
        scopes.peek().children.add(newScope);
        scopes.push(newScope);
    }

    @Override
    public void exitIf_else_statment(jythonParser.If_else_statmentContext ctx) {scopes.pop();}

    @Override
    public void enterPrint_statment(jythonParser.Print_statmentContext ctx) {}

    @Override
    public void exitPrint_statment(jythonParser.Print_statmentContext ctx) {}

    @Override
    public void enterFor_statment(jythonParser.For_statmentContext ctx) {
        SymbolTable newScope = new SymbolTable("for", ctx.start.getLine());
        scopes.peek().children.add(newScope);
        scopes.push(newScope);
    }

    @Override
    public void exitFor_statment(jythonParser.For_statmentContext ctx) {
        scopes.pop();
    }

    @Override
    public void enterMethod_call(jythonParser.Method_callContext ctx) {}

    @Override
    public void exitMethod_call(jythonParser.Method_callContext ctx) {}

    @Override
    public void enterAssignment(jythonParser.AssignmentContext ctx) {}

    @Override
    public void exitAssignment(jythonParser.AssignmentContext ctx) {}

    @Override
    public void enterExp(jythonParser.ExpContext ctx) {}

    @Override
    public void exitExp(jythonParser.ExpContext ctx) {}

    @Override
    public void enterPrefixexp(jythonParser.PrefixexpContext ctx) {}

    @Override
    public void exitPrefixexp(jythonParser.PrefixexpContext ctx) {}

    @Override
    public void enterArgs(jythonParser.ArgsContext ctx) {}

    @Override
    public void exitArgs(jythonParser.ArgsContext ctx) {}

    @Override
    public void enterExplist(jythonParser.ExplistContext ctx) {}

    @Override
    public void exitExplist(jythonParser.ExplistContext ctx) {}

    @Override
    public void enterArithmetic_operator(jythonParser.Arithmetic_operatorContext ctx) {}

    @Override
    public void exitArithmetic_operator(jythonParser.Arithmetic_operatorContext ctx) {}

    @Override
    public void enterRelational_operators(jythonParser.Relational_operatorsContext ctx) {}

    @Override
    public void exitRelational_operators(jythonParser.Relational_operatorsContext ctx) {}

    @Override
    public void enterAssignment_operators(jythonParser.Assignment_operatorsContext ctx) {}

    @Override
    public void exitAssignment_operators(jythonParser.Assignment_operatorsContext ctx) {}

    @Override
    public void visitTerminal(TerminalNode terminalNode) {}

    @Override
    public void visitErrorNode(ErrorNode errorNode) {}

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {}

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {}

    private String checkIsDefined(String className){
        return (SymbolTable.root.lookup("import_"+className)!=null || SymbolTable.root.lookup("class_"+className)!=null) ? "True" : "False";
    }
}
