package domain.trip.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TripIDGenerator {

    public static final String ID_FILE_PATH ="src/main/resources/trip/id.txt";

    /**
     * 새 TripDTO 객체 생성 시, id값 갱신하여 가져오는 메서드
     *
     * @return 현재 기록된 id값에 1을 더한 값
     */
    public static int getId(){
        //현재 기록된 id값을 가져온다.
        int currentId = readCurrentId();
        //1을 더한 후 다시 저장한다.
        int newId = currentId+1;
        saveNewId(newId);
        return newId;
    }

    /**
     * 현재 기록된 id값 조회하는 메서드
     *
     * @return 현재 기록된 id값
     */
    private static int readCurrentId() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(ID_FILE_PATH));
            String line = br.readLine();
            if(line!= null){
                return Integer.parseInt(line);
            }
        }catch (FileNotFoundException fileNotFoundException){
            try{
                FileWriter fw = new FileWriter(ID_FILE_PATH);
                fw.close();
            }catch (IOException ioException){
                ioException.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  0;
    }

    /**
     * 새로운 id값 저장하는 메서드
     *
     * @param newId 새로운 id값
     */
    private static void saveNewId(int newId){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(ID_FILE_PATH));
            bw.write(Integer.toString(newId));
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
