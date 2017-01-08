package alphone.com.elihai.alphone;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by elihai on 22 ספטמבר 2015.
 */
public class Alphone {


    public static String[] searchPhone(File is,String pattren) {

        try {
            String result[] = new String[10];
            Scanner scan = new Scanner(is);

            String s = "";
            int x;

            for (x = 0; scan.hasNext() && x < 10; ) {
                s = scan.nextLine();
                if (s.contains(pattren)) {
                    result[x] = s;
                    x++;
                }
            }
            String result2[] = new String[x];
            for (int i = 0; i < x; i++)
                result2[i] = result[i];
            return result2;
        }catch (IOException e){}
        return null;

    }

    public static String[] getList(File is) {

        try {
            String result[] = new String[500];
            Scanner scan = new Scanner(is);
            int x;
            for (x = 0; scan.hasNext() && x < 500; x++) {
                result[x] = scan.nextLine();
            }
            String result2[] = new String[x - 1];
            for (int i = 0; i < x - 1; i++)
                result2[i] = result[i];
            return result2;
        }catch(IOException e){}
        return null;

    }
}





