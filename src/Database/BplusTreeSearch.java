package Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class BPlusTree {

    private class Node {
        String[] keys;
        String[] strings; // 存储电影字符串的数组
        Node[] children;
        int n;

        Node(int order) {
            keys = new String[order - 1];
            strings = new String[order - 1];
            children = new Node[order];
            n = 0;
        }
    }

    private Node root;
    private int order;

    public BPlusTree(int order) {
        this.order = order;
        root = new Node(order);
    }

    public void insert(String key, String value) {
        root = insert(root, key, value);
        // 实现插入逻辑
    }

    private Node insert(Node node, String key, String value) {
        if (node == null) {
            Node newNode = new Node(order);
            newNode.keys[0] = key;
            newNode.strings[0] = value;
            newNode.n = 1;
            return newNode;
        }

        int i;
        for (i = 0; i < node.n; i++) {
            if (key.compareTo(node.keys[i]) <= 0) {
                break;
            }
        }

        if (node.n == order - 1) {
            Node newNode = new Node(order);
            int mid = order / 2;

            for (int j = mid; j < order - 1; j++) {
                newNode.keys[j - mid] = node.keys[j];
                newNode.strings[j - mid] = node.strings[j];
                newNode.n++;
                if (node.n >= order - 1) {
                    throw new RuntimeException("Node is already full.");
                }
            }

            node.n = mid;
            if (i < mid) {
                node = insert(node, key, value);
            } else {
                newNode = insert(newNode, key, value);
            }
            String middleKey = newNode.keys[0];
            String middleValue = newNode.strings[0];
            for (int j = node.n; j > i; j--) {
                node.keys[j] = node.keys[j - 1];
                node.strings[j] = node.strings[j - 1];
            }
            node.keys[i] = middleKey;
            node.strings[i] = middleValue;
            node.n++;

            return node;
        } else {
            for (int j = node.n; j > i; j--) {
                node.keys[j] = node.keys[j - 1];
                node.strings[j] = node.strings[j - 1];
            }
            node.keys[i] = key;
            node.strings[i] = value;
            node.n++;
            return node;
        }
    }

    public String search(String key) {
        return search(root, key);
    }

    private String search(Node node, String key) {
        if (node == null) {
            return null;
        }

        int i;
        for (i = 0; i < node.n; i++) {
            if (key.compareTo(node.keys[i]) <= 0) {
                break;
            }
        }

        if (i < node.n && key.equals(node.keys[i])) {
            return node.strings[i];
        } else if (node.children[i] != null) {
            return search(node.children[i], key);
        } else {
            return null;
        }
    }
}

public class BplusTreeSearch {

    public static void main(String[] args) {

        Map<String, String> movies = readMovieDataFromFile("src\\Database\\movies.txt");

        BPlusTree index = new BPlusTree(1000000000);
        for (String movieId : movies.keySet()) {
            index.insert(movieId, movies.get(movieId));
        }

        String queryMovieId = "10000";

        long startTime = System.nanoTime();

        String movieString = index.search(queryMovieId);

        long endTime = System.nanoTime();

        if (movieString != null) {
            System.out.println("电影信息字符串：" + movieString);
        } else {
            System.out.println("未找到电影 " + queryMovieId);
        }


        long elapsedTimeInNano = endTime - startTime;
        System.out.println("查询耗时（纳秒）：" + elapsedTimeInNano + " 纳秒");
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTimeInMillis = (elapsedTimeInNano + 500_000) / 1_000_000; // 纳秒转换为毫秒，四舍五入
        System.out.println("查询耗时（毫秒）：" + elapsedTimeInMillis + " 毫秒");
    }

    private static Map<String, String> readMovieDataFromFile(String filePath) {
        Map<String, String> movies = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            long pos = 0;
            reader.readLine(); // 读取标题行并忽略
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String movieId = parts[0];
                String movieString = line; // 整行字符串
                movies.put(movieId, movieString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

}

