package Database;

public interface DataManipulation {

    public int addOneMovie(String str);
    public String allContinentNames();
    public String continentsWithCountryCount();
    public String FullInformationOfMoviesRuntime(int min, int max);
    public String findMovieById(int id);
    public String findMovieByTitle(String title);
    public void updatePeoplename(String BeforeReplacement, String AfterReplacement);
    public void delete(int id);
    public String findMovieById_Hashmap(String title, String dataFilepath);

    public void findMovieById_Hashmap(int id);
    public String findMoviesByTitleLimited10(String title);

    public static void loadCountryFromFile(String countrypath) {
    }

    String findMoviesByLimited10(String title);
    public  void joinMovies_And_Credits();
    public void joinMovies_And_Credits_Hashmap();
    public void joinMovies_And_Credits_MergeSort();
}
