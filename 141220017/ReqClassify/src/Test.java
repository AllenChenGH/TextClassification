import java.io.BufferedReader;
import java.io.FileReader;

public class Test {
    private static String testAPTPath = "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\source\\test\\testAPT.txt";
    private static String testCorePath = "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\source\\test\\testCore.txt";
    private static String testDebugPath = "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\source\\test\\testDebug.txt";
    private static String testDocPath = "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\source\\test\\testDoc.txt";
    private static String testTextPath = "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\source\\test\\testText.txt";
    private static String testUIPath = "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\source\\test\\testUI.txt";

    private static String testPath[] = {
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\data for test\\testAPT.txt",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\data for test\\testCore.txt",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\data for test\\testDebug.txt",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\data for test\\testDoc.txt",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\data for test\\testText.txt",
            "C:\\Users\\Allen\\Desktop\\ReqClassify\\src\\Data\\data for test\\testUI.txt"
    };

    private static String reqClass[] = {"APT", "Core", "Debug", "Doc", "Text", "UI", "Uncertain"};

    public static void main(String[] args) {
        int trueNum;
        for (int i = 0; i < 6; i++) {
            trueNum = 0;
            String test[] = loadTest(testPath[i], 10);  // test 10 because there are only 11 case of Doc class. can write new func to test other class with more case(like 100)
            for (String s : test) {
                if (RequestClassify.classify(s).equals(reqClass[i])) {
                    trueNum++;
                }
            }
            System.out.println(reqClass[i] + ":" + trueNum + "/" + test.length);
        }
    }

    private static String[] loadTest(String path, int caseNum) {
        String request4Test[] = new String[caseNum];
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line = bufferedReader.readLine();
            int count = 0;
            while (line != null && count < caseNum) {
                request4Test[count] = line;
                count++;
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        return request4Test;
    }
}
