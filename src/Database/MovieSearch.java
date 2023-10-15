//package Database;
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MovieSearch {
//    Map<Integer,String> titleIndex = new HashMap<>();
//    private MovieSearch(){
//        //加载索引文件到内存
//        loadIndexFromFile("index.txt");
//    }
//    public void loadIndexFromFile(String indexpath){
//        try(BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))){
//            String line;
//            while ((line = reader.readLine())!=null){
//                String[] splitArray = line.split(";");
//                int id = Integer.parseInt(splitArray[0]);
//                String movieTitle = (splitArray.length > 1 && !splitArray[1].trim().equals("null")) ? splitArray[1].trim() : "";
//                String country = (splitArray.length > 2 && !splitArray[2].trim().equals("null")) ? splitArray[2].trim() : "";
//                int yearReleased = (splitArray.length > 3 && !splitArray[3].trim().equals("null")) ? Integer.parseInt(splitArray[3].trim()) : 0;
//                int runtime = (splitArray.length > 4 && !splitArray[4].trim().equals("null")) ? Integer.parseInt(splitArray[4].trim()) : 0;
//                String value = null;
//                value.concat("movieTitle: ").concat(movieTitle).concat("; ").concat("country: ").concat(country).concat("; ").concat("yearReleased: ").concat(String.valueOf(yearReleased)).concat("; ").concat("runtime: ").concat(runtime);
//                titleIndex.put(id,value);
//            }
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//
//    }
//}
