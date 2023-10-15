package Database;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;

public class Client {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        try {
            //DataManipulation dm = new DataFactory().createDataManipulation(args[0]);
            FileManipulation Wells = new FileManipulation();
            Wells.findMovieById(11);
            Wells.findMovieById(5000);
            Wells.findMovieById(6060);
//            Wells.findMovieById_MultiThread(11);
//            String filePath = "src\\Database\\credit.txt";
//            int threadCount = 10;
//            List<String> contents = FileUtils.multiThreadRead(filePath, threadCount);

//            Wells.delete(51);
//            Wells.updatePeoplename("To","TTOO");
//            System.out.println(Wells.findMovieById(11));
            //System.out.println(dm.findMovieByTitle("Window"));
            //dm.updatePeoplename();
            // dm.delete(1);
//                Wells.findMovieById_Hashmap(4);
               //Wells.findMovieById_Hashmap(11);
//                Wells.findMovieById_Hashmap(7022);
//                Wells.delete(233);
//                Wells.updatePeoplename("Bo","Ao");
//                //System.out.println(Wells.findMoviesByTitleLimited10("'aba'"));
            //Wells.joinMovies_And_Credits_Hashmap();
            //Wells.Credits_Sort();
//            long a = System.nanoTime();
//            Wells.joinMovies_And_Credits();
//            long b = System.nanoTime();
//            System.out.println("Command executed in " + (b - a) / 1000000 + " milliseconds.");
//            long c = System.nanoTime();
//            Wells.joinMovies_And_Credits_Hashmap();
//            long d = System.nanoTime();
//            System.out.println("Command executed in " + (d - c) / 1000000 + " milliseconds.");
//            long e = System.nanoTime();
//            Wells.mergeSortedFiles("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\movies.txt", "C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\credit_Sorted.txt", "C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\Mergefile_ByMergerJoin.txt");
//            long f = System.nanoTime();
//            System.out.println("Command executed in " + (f-e)/1000000 + " milliseconds.");
//            Wells.close();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long endTime = System.nanoTime();
        long executionTimeInMilliseconds = (endTime - startTime) / 1000000;
        System.out.println("Command executed in " + executionTimeInMilliseconds + " milliseconds.");

    }
}

