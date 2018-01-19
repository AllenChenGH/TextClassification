import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class RequestClassify {
    private static String request;

    private static String reqClass[] = {"APT", "Core", "Debug", "Doc", "Text", "UI", "Uncertain"};

    private static String path[] = {
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\tag\\APT",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\tag\\Core",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\tag\\Debug",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\tag\\Doc",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\tag\\Text",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\tag\\UI",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\word\\APT",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\word\\Core",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\word\\Debug",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\word\\Doc",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\word\\Text",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\word\\UI"
    };

    private static Vector<Vector<String[]>> data = new Vector<>(6);

    public RequestClassify(String request) {
        this.request = request;
    }

    // set classify standard
    private static void setData() {
        BufferedReader bufferedReader;
        for (int i = 0; i < 12; i++) {
            try {
                bufferedReader = new BufferedReader(new FileReader(path[i]));
                Vector<String[]> dataPart = new Vector<>();
                String line = bufferedReader.readLine();
                while (line != null) {
                    String parts[] = line.split(",");
                    parts[0] = parts[0].substring(1, parts[0].length());
                    parts[1] = parts[1].substring(0, parts[1].length() - 1);
                    //System.out.println(parts[0] + "," + parts[1]);
                    dataPart.add(parts);
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
                data.add(dataPart);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        /*
        for (String[] i : data.elementAt(9)) {
            System.out.println(i[0] + "," + i[1]);
        }
        */
    }

    public static String classify() {
        return classify(request);
    }

    // classify request by standard
    public static String classify(String request) {
        if (request.length() < 10) {
            System.out.println("request too short");
            return null;
        }

        setData();

        int classIndex = -1;

        //first look for tag
        Vector <String> tags = new Vector<>();
        for (int i = 0; i < request.length(); i++) {
            if (request.charAt(i) == '[') {
                for (int j = i + 2; j < request.length(); j++) {
                    if (request.charAt(j) == ']') {
                        String tag = request.substring(i, j + 1);
                        tags.add(tag);
                        break;
                    }
                }
            }
        }
        /*
        for (String i : tags) {
            System.out.println(i + " ");
        }
        */

        // then deal with the request description
        String words[] = request.split("\\W");
        int wordContained = words.length;
        for (String word : words) {
            if (word.equals("")) {
                wordContained--;
            }
        }

        Vector <Integer> candidate = new Vector<>();

        // compare tag
        int tagNum[] = new int[6];
        if (tags.size() > 0) {
            for (String reqTag : tags) {
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < data.elementAt(i).size(); j++) {
                        if (reqTag.equals(data.elementAt(i).elementAt(j)[0])) {
                            tagNum[i]++;
                        }
                    }
                }
            }

            for (int i = 0; i < 6; i++) {
                if (tagNum[i] > 0) {
                    candidate.add(i);
                }
            }

            if (candidate.size() == 1) {
                return reqClass[candidate.elementAt(0)];
            }
        }

        //compare words
        int wordNum[][] = new int[6][2];
        for (String word : words) {
            for (int i = 6; i < 12; i++) {
                for (int j = 0; j < data.elementAt(i).size(); j++) {
                    if (word.equals(data.elementAt(i).elementAt(j)[0])) {
                        wordNum[i - 6][0]++;
                        wordNum[i - 6][1] += Integer.parseInt(data.elementAt(i).elementAt(j)[1]);
                    }
                }
            }
        }
        /*
        for (int i = 0; i < 6; i++) {
            System.out.print(wordNum[i][0] + " ");
        }
        */
        int allCount[] = new int[6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < data.elementAt(i + 6).size(); j++) {
                allCount[i] += Integer.parseInt(data.elementAt(i + 6).elementAt(j)[1]);
            }
        }

        // calculate relativity
        double relativity[] = new double[6];
        if (candidate.size() == 0) {
            for (int i = 0; i < 6; i++) {
                double index1 = (double)wordNum[i][0] / (double)wordContained * 0.75;
                double index2 = (double)wordNum[i][1] / (double)allCount[i] * 0.25;
                relativity[i] = (index1 + index2) * 0.7;
            }
        }
        else if (candidate.size() > 1) {
           for (int i : candidate) {
               double index1 = (double)tagNum[i] / (double)tags.size() * 0.6;
               double index2 = (double)wordNum[i][0] / (double)wordContained * 0.3;
               double index3 = (double)wordNum[i][1] / (double)allCount[i] * 0.1;
               relativity[i] = index1 + index2 + index3;
           }
        }

        int maxRelativityIndex = 0;
        for (int i = 1; i < 6; i++) {
            if (relativity[i] > relativity[maxRelativityIndex]) {
                maxRelativityIndex = i;
            }
        }

        if (relativity[maxRelativityIndex] < 0.4) {
            classIndex = 6;
        }
        else {
            classIndex = maxRelativityIndex;
        }

        return reqClass[classIndex];
    }
}