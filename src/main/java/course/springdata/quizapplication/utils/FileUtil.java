package course.springdata.quizapplication.utils;

import java.io.IOException;


public interface FileUtil {

    String[] readFileContent(String filepath) throws IOException;
}
