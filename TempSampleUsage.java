package JVE;

import JVE.Parsers.Video;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static JVE.Parsers.ParseUtils.readLines;

public class TempSampleUsage {

    public static void writeLines(ArrayList<String> input, String filename) throws IOException {
        FileWriter fw = new FileWriter(filename);
        for (String anInput : input) fw.write(anInput + "\n");
        fw.close();
    }

    public static void main(String[] args) throws Exception {

        String[] names = (
                "defFont2=0x2d\n"+
                "defRed=242; defGreen=242; defBlue=83\n"+
                "p1=-3184; p2=-3250; p3=-3184; p4=-3184; p5=-3184; p6=-3184\n" +
                        "Лекция 3. Словообразование\n" +
                        //"Часть 1. Понятие о морфеме.\\nКлассификация морфем\\nрусского языка\n" +
                        //"Часть 2. Морфемный и\\nсловообразовательный\\nанализ слова\n" +
                        //"Часть 3. Способы словообразования\n" +
                        //"Часть 4. Выразительные возможности\\nсловообразования\n" +
                        "defFont2=0xff\n"+
                        "defRed=175; defGreen=226; defBlue=129\n"+
                        "p1=-3184; p2=-3184; p3=-3250; p4=-3184; p5=-3184; p6=-3184\n" +
                        "Лекция 4. Морфология\n" +
                        "Часть 1. Основные понятия морфологии.\\nКлассификация частей речи\\nв русском языке\n" +
                        //"Часть 2. Именные части речи:\\nимя существительное\n" +
                        //"Часть 3. Именные части речи:\\nимя прилагательное\n" +
                        //"Часть 4. Именные части речи:\\nимя числительное\n" +
                        //"Часть 5. Именные части речи:\\nместоимение\n" +
                        //"Часть 6. Глагол.\\nПричастие.\\nДеепричастие\n" +
                        //"Часть 7. Наречие.\\nСлова категории состояния\n" +
                        //"Часть 8. Служебные части речи\n" +
                        //"Часть 9. Особые части речи\\nв русском языке\n" +
                        "defFont2=0xff\n"+
                        "defRed=70; defGreen=185; defBlue=197\n"+
                        "p1=-3184; p2=-3184; p3=-3184; p4=-3250; p5=-3184; p6=-3184;\n" +
                        "Лекция 5. Лексикология и фразеология\n" +
                        //"Часть 1. Основные понятия\\nлексикологии\n" +
                        //"Часть 2. Лексическое значение слова.\\nОсновные типы\\nлексических значений\n" +
                        //"Часть 3. Омонимы. Синонимы.\\nАнтонимы. Паронимы\n" +
                        //"Часть 4. Лексика\\nсовременного русского языка\\nс точки зрения ее происхождения\n" +
                        //"Часть 5. Лексика \\nсовременного русского языка\\nс точки зрения \\nее активного и пассивного запаса\n" +
                        //"Часть 6. Лексика\\nсовременного русского языка\\nс точки зрения\\nфункционально-стилевого расслоения\n" +
                        //"Часть 7. Лексика\\nсовременного русского языка\\nс точки зрения сферы употребления\n" +
                        //"Часть 8. Фразеология\\nсовременного русского языка\n" +
                        "defFont2=0xff\n"+
                        "defRed=255; defGreen=116; defBlue=116\n"+
                        "p1=-3184; p2=-3184; p3=-3184; p4=-3184; p5=-3184; p6=-3250;\n" +
                        "Лекция 10. Текст\n" +
                        //"Часть 1. Текст и его свойства\n" +
                        //"Часть 2. Функционально-смысловые\\nтипы речи\\n(способы изложения)\n" +
                        //"Часть 3. Текст монологический\\nи диалогический\n" +
                        //"Часть 4. Текст с точки зрения\\nфункционально-стилистической\\nпринадлежности\n" +
                        "Часть 5. Текст прозаический\\nи стихотворный" +
                        "").split("\n");


        ArrayList<String> data = readLines("/home/nameless/Projects/ЭОРСРЯ/pattern.tex");

        String lect = null;
        String lectNum = null;
        String lectName = null;
        String part = null;
        String partNum = null;
        String partName = null;
        String deltaPos = null;
        String colors = null;
        String font = null;
        for (String s : names) {
            if (s.startsWith("defFont2")) {
                font = s;
            } else
            if (s.startsWith("defRed")) {
                colors = s;
            } else
            if (s.startsWith("p1")) {
                deltaPos = s;
            } else if (s.startsWith("Лекция")) {
                String[] parts = s.split("\\. ");
                lectNum = parts[0].trim() + '.';
                lectName = parts[1].trim();
            } else if (s.startsWith("Часть")) {
                String[] parts = s.split("\\. ");
                partNum = parts[0].trim() + '.';
                partName = parts[1].trim();
                for (int i=2; i<parts.length; i++) {
                    partName+=". "+parts[i];
                }
                ArrayList<String> dataClone = new ArrayList<>();
                for (String s2 : data) {
                    s2 = s2.replace("defFont2=0x2d;", font);
                    s2 = s2.replace("defRed=0xf2; defGreen=0xb3; defBlue=0x69", colors);
                    s2 = s2.replace("p1=-3250; p2=-3184; p3=-3184; p4=-3184; p5=-3184; p6=-3184;", deltaPos);
                    s2 = s2.replace("Лекция NO", lectNum);
                    s2 = s2.replace("Название Лекции", lectName);
                    s2 = s2.replace("Часть NO", partNum);
                    s2 = s2.replace("Название Части", partName);
                    dataClone.add(s2);
                }
                System.out.println("LectNum: " + lectNum);
                System.out.println("LectName: " + lectName);
                System.out.println("PartNum: " + partNum);
                System.out.println("PartName: " + partName);
                writeLines(dataClone, "/home/nameless/Projects/ЭОРСРЯ/patternChanged.tex");

                Video video = new Video("/home/nameless/Projects/ЭОРСРЯ/patternChanged.tex",
                        state -> {
                        });
                video.render(state -> {
                });

                File f = new File("/home/nameless/Desktop/temp2/out.mp4");
                if (!
                        f.renameTo(new File("/home/nameless/Projects/ЭОРСРЯ/intros/" + lectNum+ partNum+ "mp4"))) {
                    System.err.println("3rr0r");
                    System.exit(0);
                }


            }

        }
    }


}
