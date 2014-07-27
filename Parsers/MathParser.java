package JVE.Parsers;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MathParser {

    private static ScriptEngine engine;

    public static void init() throws ScriptException {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
        setTimes(0, 0, 0);
    }

    public static String prepareExpression(String s) {
        s=s.replaceAll("parabolicFadeIn", "pow(normalTime, 2)");
        s=s.replaceAll("parabolicFadeOut", "pow(normalTime-1, 2)");
        s=s.replaceAll("quadParabolicFadeIn", "pow(normalTime, 4)");
        s=s.replaceAll("quadParabolicFadeOut", "pow(normalTime-1, 4)");
        s=s.replaceAll("sineFadeIn", "sin(normalTime*1.57)");
        s=s.replaceAll("sineFadeOut", "sin(normalTime*1.57+1.57)");
        s=s.replaceAll("fullSineFadeIn", "(sin(normalTime*3.14-1.57)/2+0.5)");
        s=s.replaceAll("fullSineFadeOut", "(sin(normalTime*3.14+1.57)/2+0.5)");
        s=s.replaceAll("circleFadeIn", "sqrt(2*normalTime-pow(normalTime,2))");
        s=s.replaceAll("circleFadeOut", "sqrt(1-pow(normalTime,2))");
        s=s.replaceAll("sin[(]", "Math.sin(");
        s=s.replaceAll("cos[(]", "Math.cos(");
        s=s.replaceAll("tan[(]", "Math.tan(");
        s=s.replaceAll("sqrt[(]", "Math.sqrt(");
        s=s.replaceAll("max[(]", "Math.max(");
        s=s.replaceAll("min[(]", "Math.min(");
        s=s.replaceAll("abs[(]", "Math.abs(");
        s=s.replaceAll("pow[(]", "Math.pow(");
        return s;
    }

    public static int parseInt(String s) throws Exception {
        return (int)parseFloat(s);
    }

    public static float parseFloat(String s) throws Exception {
            engine.eval("v=" + s);
            return Float.valueOf(engine.get("v").toString());
    }

    public static void injectCode(String s) throws Exception {
        try {
            engine.eval(s);
        } catch (Exception e) {
            throw new Exception("Failed to inject code: " + s);
        }
    }

    public static void runInjections() throws Exception {
        injectCode(injections);
    }

    public static String getInjections() {
        return injections;
    }

    private static String injections="";

    public static void addInjection(String s) throws Exception {
        injections+=s+"\n";
        injectCode(s);
    }

    public static void setTimes(float nTime, float aTime, float aVideoTime) throws ScriptException {
        engine.eval("normalTime=" + nTime);
        engine.eval("absoluteTime=" + aTime);
        engine.eval("absoluteVideoTime=" + aVideoTime);
    }
}
