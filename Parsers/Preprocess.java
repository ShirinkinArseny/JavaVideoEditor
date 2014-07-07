package JVE.Parsers;

import java.io.File;
import java.util.ArrayList;

import static JVE.Parsers.ParseUtils.*;

public class Preprocess {

    private static ArrayList<String> inlineInputs(String url) throws Exception {
        ArrayList<String> code=readLines(url);
        ArrayList<String> preprocessed_1 = new ArrayList<>();
        for (String aCode : code) {
            if (aCode.startsWith("\\input")) {
                String urlIns=getArguments(aCode)[0];
                if (!urlIns.startsWith("/")) {
                    urlIns=new File(url).getParent()+"/"+urlIns;
                }
                preprocessed_1.addAll(readLines(urlIns));
                printMessage("File " + urlIns + " inserted");
            } else preprocessed_1.add(aCode);
        }
        return preprocessed_1;
    }

    private static ArrayList<String> removeComments(ArrayList<String> preprocessed_1) {
        ArrayList<String> preprocessed_2 = new ArrayList<>();
        for (String aPreprocessed_1 : preprocessed_1) {
            String used = getUsablePart(aPreprocessed_1);
            if (used != null) {
                preprocessed_2.add(used);
            }
        }
        return preprocessed_2;
    }

    private static ArrayList<String> reLine(ArrayList<String> preprocessed_2) throws Exception {
        ArrayList<String> preprocessed_3 = new ArrayList<>();
        for (int i=0; i<preprocessed_2.size(); i++) {
            if (preprocessed_2.get(i).endsWith("}")) {
                preprocessed_3.add(preprocessed_2.get(i));
            }
            else {
                if (i>=preprocessed_2.size()-1)
                    exit("Command has not got closing bracket: " + preprocessed_2.get(i));
                preprocessed_2.set(i, preprocessed_2.get(i) + " "+preprocessed_2.get(i+1));
                preprocessed_2.remove(i + 1);
                if (i>0) i--;
            }
        }
        return preprocessed_3;
    }

    public static ArrayList<String> preprocess(String url) throws Exception {
        return reLine(removeComments(inlineInputs(url)));
    }

    private static String getUsablePart(String s) {
        s = s.trim();
        if (s.startsWith("%")) return null;
        if (s.equals("")) return null;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '%') return s.substring(0, i);
        }
        return s;
    }
}
