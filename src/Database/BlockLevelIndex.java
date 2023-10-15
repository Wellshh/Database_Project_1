package Database;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BlockLevelIndex {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\Wells\\IdeaProjects\\New\\src\\Database\\movies.txt";
        int blockSize = 1000;
        try {
            Map<Integer, Long> blockIndex = createBlockLevelIndex(filePath, blockSize);
            int blockNumber = 8;
            long blockStart = blockIndex.getOrDefault(blockNumber, -1L);
            if (blockStart != -1) {
                System.out.println("Block " + blockNumber + " starts at position " + blockStart);
                String query = "Window";
                long startTime = System.nanoTime();

                String result = retrieveDataFromBlock(filePath, blockStart, query);
                long endTime = System.nanoTime();

                if (result != null) {
                    System.out.println("Found: " + result);
                } else {
                    System.out.println("Not found in Block " + blockNumber);
                }
                long elapsedTimeInNano = endTime - startTime;
                System.out.println("查询耗时（纳秒）：" + elapsedTimeInNano + " 纳秒");
                System.out.println("查询耗时（毫秒）：" + elapsedTimeInNano/1000000 + " 毫秒");
            } else {
                System.out.println("Block " + blockNumber + " not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Long> createBlockLevelIndex(String filePath, int blockSize) throws IOException {
        Map<Integer, Long> blockIndex = new HashMap<>();
        int blockNumber = 1;
        long currentPosition = 0;
        int lineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (lineCount > blockSize) {
                    blockIndex.put(blockNumber, currentPosition);
                    lineCount = 1;
                    blockNumber++;
                }
                currentPosition += line.length() + 1;
            }
        }

        blockIndex.put(blockNumber, currentPosition);

        return blockIndex;
    }

    public static String retrieveDataFromBlock(String filePath, long blockStart, String query) {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            file.seek(blockStart);

            String line;
            while ((line = file.readLine()) != null) {
                if (line.contains(query)) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
