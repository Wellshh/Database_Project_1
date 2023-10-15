package Database;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileUtils {

    public static List<String> multiThreadRead(String filePath, int threadCount) throws Exception {

        List<String> contents = new ArrayList<>();

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        long fileSize = new File(filePath).length();
        int chunkSize = (int) Math.ceil(fileSize * 1.0 / threadCount);

        List<Future<List<String>>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int startPos = i * chunkSize;
            Future<List<String>> future = pool.submit(new ReadFileTask(filePath, startPos, chunkSize));
            futures.add(future);
        }

        for (Future<List<String>> future : futures) {
            contents.addAll(future.get());
        }

        pool.shutdown();

        return contents;

    }

    static class ReadFileTask implements Callable<List<String>> {

        private String filePath;
        private int startPos;
        private int chunkSize;

        public ReadFileTask(String filePath, int startPos, int chunkSize) {
            this.filePath = filePath;
            this.startPos = startPos;
            this.chunkSize = chunkSize;
        }

        @Override
        public List<String> call() throws Exception {

            List<String> lines = new ArrayList<>();

            RandomAccessFile file = new RandomAccessFile(filePath, "r");
            file.seek(startPos);

            String line = null;
            while ((line = file.readLine()) != null) {
                lines.add(line);
            }

            file.close();

            return lines;

        }
    }
}