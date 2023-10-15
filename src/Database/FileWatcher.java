//package Database;
//
//import java.nio.file.*;
//import java.nio.file.StandardWatchEventKinds.*;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.io.IOException;
//
//import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
//
// class FileWatcher {
//
//    public static void startFileWatcher() {
//        Path movies = Paths.get("src\\Database\\movies.txt");
//        Path credit = Paths.get("src\\Database\\credit.txt");
//        Path countries = Paths.get("src\\Database\\countries.txt");
//
//        try {
//            WatchService watchService = FileSystems.getDefault().newWatchService();
//
//            movies.getParent().register(watchService, ENTRY_MODIFY);
//            credit.getParent().register(watchService, ENTRY_MODIFY);
//            countries.getParent().register(watchService, ENTRY_MODIFY);
//
//            while (true) {
//                WatchKey key = watchService.take();
//
//                for (WatchEvent<?> event : key.pollEvents()) {
//                    if (event.kind() == ENTRY_MODIFY) {
//                        Path changedFile = (Path) event.context();
//                        if (changedFile.equals(movies.getFileName())) {
//                            loadIndexFromFile(movies.toString());
//                        } else if (changedFile.equals(credit.getFileName())) {
//                            loadCreditFromfile(credit.toString());
//                        } else if (changedFile.equals(countries.getFileName())) {
//                            loadCountryFromFile(countries.toString());
//                        }
//                        System.out.println("File modified: " + changedFile);
//                        System.exit(0);
//                    }
//                }
//
//                boolean valid = key.reset();
//                if (!valid) {
//                    break;
//                }
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
