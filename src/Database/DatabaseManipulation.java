package Database;

import java.sql.*;

public class DatabaseManipulation implements DataManipulation {
    private Connection con = null;
    private ResultSet resultSet;

    private String host = "localhost";
    private String dbname = "cs307";
    private String user = "checker";
    private String pwd = "123456";
    private String port = "5432";


    private void getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (Exception e) {
            System.err.println("Cannot find the PostgreSQL driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            con = DriverManager.getConnection(url, user, pwd);

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    private void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int addOneMovie(String str) {
        getConnection();
        int result = 0;
        String sql = "insert into movies (title, country,year_released,runtime) " +
                "values (?,?,?,?)";
        String movieInfo[] = str.split(";");
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, movieInfo[0]);
            preparedStatement.setString(2, movieInfo[1]);
            preparedStatement.setInt(3, Integer.parseInt(movieInfo[2]));
            preparedStatement.setInt(4, Integer.parseInt(movieInfo[3]));
            System.out.println(preparedStatement.toString());

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public String allContinentNames() {
        getConnection();
        StringBuilder sb = new StringBuilder();
        String sql = "select continent from countries group by continent";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("continent") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return sb.toString();
    }

    @Override
    public String continentsWithCountryCount() {
        getConnection();
        StringBuilder sb = new StringBuilder();
        String sql = "select continent, count(*) countryNumber from countries group by continent;";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("continent") + "\t");
                sb.append(resultSet.getString("countryNumber"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return sb.toString();
    }

    @Override
    public String FullInformationOfMoviesRuntime(int min, int max) {
        getConnection();
        StringBuilder sb = new StringBuilder();
        String sql = "select m.title,c.country_name country,c.continent ,m.runtime " +
                "from movies m " +
                "join countries c on m.country=c.country_code " +
                "where m.runtime between ? and ? order by runtime;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, min);
            preparedStatement.setInt(2, max);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("runtime") + "\t");
                sb.append(String.format("%-18s", resultSet.getString("country")));
                sb.append(resultSet.getString("continent") + "\t");
                sb.append(resultSet.getString("title") + "\t");
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return sb.toString();
    }

    @Override
    public String findMovieById(int id) {
        return null;
    }

    @Override
    public String findMovieByTitle(String title) {
        getConnection();
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM movies WHERE title LIKE ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%" + title + "%"); // 使用通配符匹配部分标题
            long startTime = System.currentTimeMillis();
            resultSet = preparedStatement.executeQuery();
            long endTime = System.currentTimeMillis();

            long executionTime = endTime - startTime;
            System.out.println("SQL command execution time: " + executionTime + " milliseconds");

            while (resultSet.next()) {
                sb.append("Movie ID: ").append(resultSet.getInt("movieid")).append("\n");
                sb.append("Title: ").append(resultSet.getString("title")).append("\n");
                sb.append("Country: ").append(resultSet.getString("country")).append("\n");
                sb.append("Year Released: ").append(resultSet.getInt("year_released")).append("\n");
                sb.append("Runtime: ").append(resultSet.getInt("runtime")).append("\n");
                sb.append("---------------------\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        if (sb.length() == 0) {
            return "No movies found with the title: " + title;
        } else {
            return sb.toString();
        }
    }

    //    @Override
//    public String findMovieByTitle(String title) {
//        getConnection();
//        StringBuilder sb = new StringBuilder();
//        String sql = "SELECT * FROM movies WHERE title LIKE '%" + title + "%'"; // 直接将title拼接到SQL查询中
//
//        try {
//            // 创建Statement对象
//            Statement statement = con.createStatement();
//
//            // 获取执行前的时间戳
//            long startTime = System.currentTimeMillis();
//
//            // 执行查询
//            resultSet = statement.executeQuery(sql);
//
//            // 获取执行后的时间戳
//            long endTime = System.currentTimeMillis();
//
//            // 计算执行时间
//            long executionTime = endTime - startTime;
//            System.out.println("SQL command execution time: " + executionTime + " milliseconds");
//
//            while (resultSet.next()) {
//                sb.append("Movie ID: ").append(resultSet.getInt("movieid")).append("\n");
//                sb.append("Title: ").append(resultSet.getString("title")).append("\n");
//                sb.append("Country: ").append(resultSet.getString("country")).append("\n");
//                sb.append("Year Released: ").append(resultSet.getInt("year_released")).append("\n");
//                sb.append("Runtime: ").append(resultSet.getInt("runtime")).append("\n");
//                sb.append("---------------------\n");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeConnection();
//        }
//
//        if (sb.length() == 0) {
//            return "No movies found with the title: " + title;
//        } else {
//            return sb.toString();
//        }
//    }
    @Override
    public void updatePeoplename(String BeforeReplacement, String AfterReplacement) {

    }
    @Override
    public void delete(int id){

    }

    @Override
    public String findMovieById_Hashmap(String title, String dataFilepath) {
        return null;
    }

    @Override
    public void findMovieById_Hashmap(int id) {
    }
    @Override
    public String findMoviesByTitleLimited10(String title){
        getConnection(); // start connection
        String sql = "select m.title, c.country_name country,m.runtime,m.year_released\n"+
        "from movies m join countries c on m.country = c.country_code\n"+
                "where m.title like '%'||"+title+"||'%'limit 10;";// string
        //combination
        try {
            Statement statment = con.createStatement();
            resultSet = statment.executeQuery(sql);
            StringBuilder strb=new StringBuilder(); //combine multi-strings
            while (resultSet.next()){
                strb.append(String.format("%-20s\t",
                        resultSet.getString("country")));
                strb.append(resultSet.getInt("year_released")).append("\t");
                strb.append(resultSet.getInt("runtime")).append("\t");
                strb.append(resultSet.getString("title")).append("\n");
            }
            return strb.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            closeConnection(); // close connection
        }
        return null;
    }
    @Override
    public String findMoviesByLimited10(String title) {
        getConnection(); // start connection
        String sql = "select m.title, c.country_name country, m.runtime,m.year_released\n"+ "from movies m join countries c on m.country = c.country_code\n"+ "where m.title like '%'||?||'%'limit 10;";// string combination
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);//
                    preparedStatement.setString(1, title);// change here!
            resultSet = preparedStatement.executeQuery();// and here!
            StringBuilder strb=new StringBuilder(); //combine multi-strings
            while (resultSet.next()){
                strb.append(String.format("%-20s\t",
                        resultSet.getString("country")));
                strb.append(resultSet.getInt("year_released")).append("\t");
                strb.append(resultSet.getInt("runtime")).append("\t");
                strb.append(resultSet.getString("title")).append("\n");
            }
            return strb.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            closeConnection(); // close connection
        }
        return null;
    }

    @Override
    public void joinMovies_And_Credits() {

    }

    @Override
    public void joinMovies_And_Credits_Hashmap() {

    }

    @Override
    public void joinMovies_And_Credits_MergeSort() {

    }

    public void loadCountryFromFile(String countrypath){}


}
