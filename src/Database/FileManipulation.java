package Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FileManipulation implements DataManipulation {
    private ExecutorService threadPool;
    Map<Integer, String> titleIndex = new HashMap<>();
    Map<Integer, String> creditIndex = new HashMap<>();
    Map<String, String> countryIndex = new HashMap<>();

    //    {
//        FileWatcher.tartFileWatcher();
//    }
    public FileManipulation() {
        threadPool = Executors.newFixedThreadPool(10);//添加多线程
//        threadPool.submit(() -> {loadIndexFromFile("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\movies.txt");});
//        threadPool.submit(() -> {loadIndexFromFile("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\credit.txt");});
//        threadPool.submit(() -> {loadCountryFromFile("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\countries.txt");})
        loadIndexFromFile("src\\Database\\movies.txt");
        loadCreditFromfile("src\\Database\\credit.txt");
        loadCountryFromFile("src\\Database\\countries.txt");
//        threadPool.shutdown();
    }

    Path movies = Paths.get("src\\Database\\movies.txt");
    Path credit = Paths.get("src\\Database\\credit.txt");
    Path countries = Paths.get("src\\Database\\countries.txt");


//    { WatchService watchService = null;
//        try {
//            watchService = FileSystems.getDefault().newWatchService();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//
//        // 注册要监视的文件
//        try {
//            movies.getParent().register(watchService, ENTRY_MODIFY);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//        try {
//            credit.getParent().register(watchService, ENTRY_MODIFY);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//        try {
//            countries.getParent().register(watchService, ENTRY_MODIFY);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//
//        while (true) {
//            WatchKey key = null;
//            try {
//                key = watchService.take();
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//
//            for (WatchEvent<?> event : key.pollEvents()) {
//                if (event.kind() == ENTRY_MODIFY) {
//                    Path changedFile = (Path) event.context();
//                    if (changedFile.equals(movies.getFileName())) {
//                        loadIndexFromFile(movies.toString());
//                    } else if (changedFile.equals(credit.getFileName())) {
//                        loadCreditFromfile(credit.toString());
//                    } else if (changedFile.equals(countries.getFileName())) {
//                        loadCountryFromFile(countries.toString());
//                    }
//                    System.out.println("File modified: " + changedFile);
//                    System.exit(0);
//                }
//            }
//
//            boolean valid = key.reset();
//            if (!valid) {
//                break;
//            }
//        }
//    }


    //加载索引文件到内存
    //loadIndexFromFile("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\movies.txt");
    private synchronized ExecutorService getThreadPool() {
        if (threadPool == null) {
            threadPool = Executors.newFixedThreadPool(10);
        }
        return threadPool;
    }//使用懒加载方法初始化线程池


    public void loadIndexFromFile(String indexpath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(indexpath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] splitArray = line.split(";");
                int id = Integer.parseInt(splitArray[0]);
                String movieTitle = (splitArray.length > 1 && !splitArray[1].trim().equals("null")) ? splitArray[1].trim() : "";
                String country = (splitArray.length > 2 && !splitArray[2].trim().equals("null")) ? splitArray[2].trim() : "";
                //int yearReleased = (splitArray.length > 3 && !splitArray[3].trim().equals("null")) ? Integer.parseInt(splitArray[3].trim()) : 0;
                //int runtime = (splitArray.length > 4 && !splitArray[4].trim().equals("null")) ? Integer.parseInt(splitArray[4].trim()) : 0;
                String value = "movieId: ".concat(String.valueOf(id));
                String result = value.concat("; ").concat("movieTitle: ").concat(movieTitle).concat("; ").concat("country: ").concat(country).concat("; ");//.concat("yearReleased: ").concat(String.valueOf(yearReleased)).concat("; ").concat("runtime: ").concat(String.valueOf(runtime));
                titleIndex.put(id, result);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCreditFromfile(String creditpath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(creditpath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] splitArray = line.split(";");
                int movieId = Integer.parseInt(splitArray[0]);
                int peopleId = Integer.parseInt(splitArray[1]);
                String credit = splitArray[2];
                String result = "pepleId: ".concat(String.valueOf(peopleId)).concat("; ").concat("credit: ").concat(credit);
                creditIndex.put(movieId, result);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 原有的单线程读取逻辑
        //threadPool.shutdown();

    }

    public void loadCountryFromFile(String countrypath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(countrypath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] splitArray = line.split(";");
                String abrreviation = splitArray[0];
                String country = splitArray[1];
                String continent = splitArray[2];
                String value = "country: ".concat(country).concat("; ").concat("continent: ").concat(continent);
                countryIndex.put(abrreviation, value);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //threadPool.shutdown();
        // 原有的单线程读取逻辑
    }

    public boolean canDeleteMovie(int movieId) {
        return !creditIndex.containsKey(movieId);
    }
    //模拟数据库的外键操作，检查credit表格是否有关联的movieId导致不能删除

    @Override
    public int addOneMovie(String str) {
        try (FileWriter writer = new FileWriter("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\movies.txt", true)) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public String allContinentNames() {
        String line;
        int continentIndex = 2;
        Set<String> continentNames = new HashSet<>();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\countries.txt"))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                line = line.split(";")[continentIndex];
                if (!continentNames.contains(line)) {
                    sb.append(line).append("\n");
                    continentNames.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String continentsWithCountryCount() {
        String line;
        int continentIndex = 2;
        Map<String, Integer> continentCount = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\countries.txt"))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                line = line.split(";")[continentIndex];
                if (continentCount.containsKey(line)) {
                    continentCount.put(line, continentCount.get(line) + 1);
                } else {
                    continentCount.put(line, 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : continentCount.entrySet()) {
            sb.append(entry.getKey()).append("\t").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String FullInformationOfMoviesRuntime(int min, int max) {
        return null;
    }

    private Map<String, String> getCountryMap() {
        String line;
        String[] splitArray;
        int countryCodeIndex = 0, countryNameIndex = 1, continentIndex = 2;
        Map<String, String> rst = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\countries.txt"))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                splitArray = line.split(";");
                rst.put(splitArray[countryCodeIndex].trim(), String.format("%-18s", splitArray[countryNameIndex]) + splitArray[continentIndex]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rst;
    }

//    private List<FullInformation> getFullInformation(Map<String, String> countryMap, int min, int max) {
//        String line;
//        String[] splitArray;
//        List<FullInformation> list = new ArrayList<>();
//        int titleIndex = 1, countryIndex = 2, runTimeIndex = 4, runTime;
//
//        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\movies.txt"))) {
//            bufferedReader.readLine();
//            while ((line = bufferedReader.readLine()) != null) {
//                splitArray = line.split(";");
//
//                if (!"null".equals(splitArray[runTimeIndex])) {
//                    runTime = Integer.parseInt(splitArray[runTimeIndex]);
//                    if (runTime >= min && runTime <= max) {
//                        line = runTime + "\t" + countryMap.get(splitArray[countryIndex].trim()) + "\t"
//                                + splitArray[titleIndex] + "\n";
//                        list.add(new FullInformation(runTime, line));
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return list;
//    }

//    @Override
//    public String FullInformationOfMoviesRuntime(int min, int max) {
//        Map<String, String> countryMap = getCountryMap();
//        List<FullInformation> list = getFullInformation(countryMap, min, max);
//        list.sort(Comparator.comparing(f -> f.runTime));
//
//        StringBuilder sb = new StringBuilder();
//        for (FullInformation f : list) {
//            sb.append(f.information);
//        }
//
//        return sb.toString();
//    }

    @Override
    public String findMovieById(int id) {
        long starttime = System.nanoTime();
        String line;
        String[] splitArray;
        int idIndex = 0; // 假设 ID 在电影记录的第一列

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\movies.txt"))) {
            bufferedReader.readLine(); // 跳过标题行
            while ((line = bufferedReader.readLine()) != null) {
                splitArray = line.split(";");
                int movieId = Integer.parseInt(splitArray[idIndex].trim());

                if (movieId == id) {
                    String movieTitle = (splitArray.length > 1 && !splitArray[1].trim().equals("null")) ? splitArray[1].trim() : "";
                    String country = (splitArray.length > 2 && !splitArray[2].trim().equals("null")) ? splitArray[2].trim() : "";
                    int yearReleased = (splitArray.length > 3 && !splitArray[3].trim().equals("null")) ? Integer.parseInt(splitArray[3].trim()) : 0;
                    int runtime = (splitArray.length > 4 && !splitArray[4].trim().equals("null")) ? Integer.parseInt(splitArray[4].trim()) : 0;

                    StringBuilder movieInfo = new StringBuilder();
                    movieInfo.append("Movie ID: ").append(id).append("\n");
                    movieInfo.append("Title: ").append(movieTitle).append("\n");
                    movieInfo.append("Country: ").append(country).append("\n");
                    if (yearReleased == 0) {
                        movieInfo.append("Year Released: ").append("null").append("\n");
                    } else {
                        movieInfo.append("Year Released: ").append(yearReleased).append("\n");
                    }
                    if (runtime == 0) {
                        movieInfo.append("Runtime: ").append("null").append("\n");
                    } else {
                        movieInfo.append("Runtime: ").append(runtime).append("\n");
                    }
                    long endtime = System.nanoTime();
                    System.out.println("The execution time is  " + (endtime - starttime) + "  nanoseconds");


                    return movieInfo.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "Movie with ID " + id + " not found.";
    }

    public String findMovieById_MultiThread(int id) {

        long startTime = System.nanoTime();

        try {

            // 多线程读取文件
            List<String> lines = FileUtils.multiThreadRead("src\\Database\\movies.txt", 4);
            int count = 0;

            for (String line : lines) {
                if (count > 0) {
                    String[] splitArray = line.split(";");
                    int movieId = Integer.parseInt(splitArray[0].trim());

                    if (movieId == id) {
                        StringBuilder movieInfo = new StringBuilder();
                        movieInfo.append("Movie ID: ").append(id).append("\n");
                        long endTime = System.nanoTime();
                        System.out.println("Time: " + (endTime - startTime));
                        return movieInfo.toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Movie not found";
    }


    public String findMovieByTitle(String title) {
        String line;
        String splitArray[];
        int titleindex = 1;
        List<String> matchingMovies = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\movies.txt"))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                splitArray = line.split(";");
                boolean checkTitle = splitArray[titleindex].contains(title);
                if (checkTitle == true) {
                    int id = Integer.parseInt(splitArray[0]);
                    String movieTitle = (splitArray.length > 1 && !splitArray[1].trim().equals("null")) ? splitArray[1].trim() : "";
                    String country = (splitArray.length > 2 && !splitArray[2].trim().equals("null")) ? splitArray[2].trim() : "";
                    int yearReleased = (splitArray.length > 3 && !splitArray[3].trim().equals("null")) ? Integer.parseInt(splitArray[3].trim()) : 0;
                    int runtime = (splitArray.length > 4 && !splitArray[4].trim().equals("null")) ? Integer.parseInt(splitArray[4].trim()) : 0;
                    StringBuilder movieInfo = new StringBuilder();
                    movieInfo.append("Movie ID: ").append(id).append("\n");
                    movieInfo.append("Title: ").append(movieTitle).append("\n");
                    movieInfo.append("Country: ").append(country).append("\n");
                    if (yearReleased == 0) {
                        movieInfo.append("Year Released: ").append("null").append("\n");
                    } else {
                        movieInfo.append("Year Released: ").append(yearReleased).append("\n");
                    }
                    if (runtime == 0) {
                        movieInfo.append("Runtime: ").append("null").append("\n");
                    } else {
                        movieInfo.append("Runtime: ").append(runtime).append("\n");
                    }
                    matchingMovies.add(movieInfo.toString());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return matchingMovies.toString();
    }

    public void updatePeoplename(String BeforeReplacement, String AfterReplacement) {
        threadPool.submit(() -> {
            String filePath = "src\\Database\\people.txt";

            List<String> lines = readFromFile(filePath);

            List<String> modifiedLines = new ArrayList<>();
            Boolean linesTomodify = false;
            for (String line : lines) {
                if (line.contains(BeforeReplacement)) {
                    String modifiedLine = line.replaceAll("To", "TTOO");
                    modifiedLines.add(modifiedLine);
                    linesTomodify = true;
                } else {
                    modifiedLines.add(line);
                }
            }

            if (linesTomodify == true) {
                writeToFile(filePath, modifiedLines);
                loadIndexFromFile("src\\Database\\people.txt");//每一次更改操作都会自动更新索引
                System.out.println("File updated successfully.");
            } else {
                System.out.println("No such match");
            }
        });
    }

    private List<String> readFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private void writeToFile(String filePath, List<String> lines) {//利用了缓冲区实现对文件的块读取
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
//        threadPool.submit(() -> {
        String filePath = "src\\Database\\movies.txt";
        if (canDeleteMovie(id)) {
            // 读取文件
            List<String> lines = readFromFile(filePath);

            // 修改文件内容
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String splitArray[] = line.split(";");
                if (Integer.parseInt(splitArray[0]) == id) {
                    lines.remove(i);
                    break;
                }
            }
            // 将修改后的内容写回文件
            writeToFile(filePath, lines);
            loadIndexFromFile("src\\Database\\movies.txt");
            System.out.println("索引表已更新");
        } else {
            throw new ForeignKeyConstraintViolationException("在 \"movies\" 上的更新或删除操作违反了在 \"credits\" 上的外键约束 \"credits_movieid_fkey\"");
        }
//        });
    }

    public void delete_ByHashmap(int id) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src\\Database\\movies.txt"));
        bufferedWriter.flush();
        if (canDeleteMovie(id)) {
            titleIndex.remove(id);
        } else {
            throw new ForeignKeyConstraintViolationException("在 \"movies\" 上的更新或删除操作违反了在 \"credits\" 上的外键约束 \"credits_movieid_fkey\"");
        }
        for (Map.Entry<Integer, String> entry : titleIndex.entrySet()) {
            bufferedWriter.write(entry.getValue());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }


    @Override
    public String findMovieById_Hashmap(String title, String dataFilepath) {
        return null;
    }

    @Override
    public void findMovieById_Hashmap(int id) {
        threadPool.submit(() -> {
            long start = System.nanoTime();
            //loadIndexFromFile("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\movies.txt");
            String information = titleIndex.get(id);
            long end = System.nanoTime();
            long executionTimeInMilliseconds = (end - start);
//            System.out.println("Command executed in " + executionTimeInMilliseconds + " nanoseconds.");
            //System.out.println("Command executed in " + executionTimeInMilliseconds*1000000 + " nanoseconds.");
            System.out.println(information);
        });


//        class FullInformation {
//            int runTime;
//            String information;
//
//            FullInformation(int runTime, String information) {
//                this.runTime = runTime;
//                this.information = information;
//            }
    }

    @Override
    public String findMoviesByTitleLimited10(String title) {
        return null;

    }

    @Override
    public String findMoviesByLimited10(String title) {
        return null;
    }

    @Override
    public void joinMovies_And_Credits() {
        threadPool.submit(() -> {
            String mergedFilePath = "src\\Database\\Mergefile.txt";
            try {
                BufferedReader moviesReader = new BufferedReader(new FileReader("src\\Database\\movies.txt"));
                BufferedReader creditsReader = new BufferedReader(new FileReader("src\\Database\\credit.txt"));
                BufferedWriter mergedWriter = new BufferedWriter(new FileWriter(mergedFilePath));

                String moviesLine;
                String creditsLine;
                moviesReader.readLine();
                while ((moviesLine = moviesReader.readLine()) != null) {
                    String[] moviesData = moviesLine.split(";");
                    int movieId = Integer.parseInt(moviesData[0]);
                    creditsReader.readLine();
                    while ((creditsLine = creditsReader.readLine()) != null) {
                        String[] creditsData = creditsLine.split(";");
                        int creditMovieId = Integer.parseInt(creditsData[0].trim());

                        if (movieId == creditMovieId) {
                            //04 Merge the data from both files
                            mergedWriter.write(moviesLine.concat(";").concat("peopleid: ").concat(String.valueOf(creditsData[1])).concat(";").concat("credited_as: ").concat(creditsData[2]));
                            mergedWriter.newLine();
                        }
                    }
                    creditsReader.close();
                    creditsReader = new BufferedReader(new FileReader("src\\Database\\credit.txt"));
                }

                moviesReader.close();
                creditsReader.close();
                mergedWriter.close();

                System.out.println("Files merged successfully to " + mergedFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override//最终要用多线程处理
    public void joinMovies_And_Credits_Hashmap() {//直接利用Hashmap来进行快速查询与比较
        threadPool.submit(() -> {
            String mergefilePath = "C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\Mergefile_ByHashmap.txt";
            try {
                BufferedWriter mergeReader = new BufferedWriter(new FileWriter("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\Mergefile_ByHashmap.txt"));
                for (int i = 0; i < titleIndex.size(); i++) {
                    String movieslines = titleIndex.get(i);
                    if (movieslines != null) {
                        String[] splitArray_movies = movieslines.split(";");
                        creditIndex.get(i);
                        if (creditIndex.containsKey(i)) {
                            String creditslines = creditIndex.get(i);
                            String[] splitArray_credits = creditslines.split(";");
                            mergeReader.write(movieslines.concat(creditslines));
                            mergeReader.newLine();
                        }
                        //将两个concat
                    }

                }
                System.out.println("Files merged successfully to " + mergefilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void joinMovies_And_Credits_MergeSort() {
        //使用Merge_Join的前提是保证两个表的连接键是从小到大依次排列的

    }

    public void Credits_Sort() throws IOException {
        String creditspath = "C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\credit.txt";
        String arr[][] = HeapSort_ById.Buildheap_AndSort(creditspath);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\credit_Sorted.txt"));
        bufferedWriter.flush();
        for (int i = 0; i < arr[0].length; i++) {
            bufferedWriter.write(arr[1][i]);
            //bufferedWriter.write("111");
            bufferedWriter.newLine();
        }
        System.out.println("Successfully Sorted");
        bufferedWriter.close();
    }

    public void mergeSortedFiles(String file1, String file2, String outFile) throws IOException {

        List<String> mergedData = new ArrayList<>();

        try (BufferedReader reader1 = new BufferedReader(new FileReader(file1));
             BufferedReader reader2 = new BufferedReader(new FileReader(file2))) {
            reader1.readLine();
            reader2.readLine();

            String line1 = reader1.readLine();
            String line2 = reader2.readLine();

            while (line1 != null && line2 != null) {

                String[] split1 = line1.split(";");
                String[] split2 = line2.split(";");

                if (Integer.parseInt(split1[0]) < Integer.parseInt(split2[0])) {
                    line1 = reader1.readLine();
                } else if (Integer.parseInt(split1[0]) == Integer.parseInt(split2[0])) {
                    mergedData.add(line1.concat(line2));
                    line1 = reader1.readLine();
                    line2 = reader2.readLine();
                } else {
                    line2 = reader2.readLine();
                }

            }

            while (line1 != null) {
                mergedData.add(line1);
                line1 = reader1.readLine();
            }

            while (line2 != null) {
                mergedData.add(line2);
                line2 = reader2.readLine();
            }

            writeToFile(outFile, mergedData);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void close() {
        threadPool.shutdown();//关闭线程
    }

//    }
}


