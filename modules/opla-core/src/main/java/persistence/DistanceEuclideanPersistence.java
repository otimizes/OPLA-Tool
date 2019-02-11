package persistence;

import exceptions.MissingConfigurationException;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DistanceEuclideanPersistence {

    public static void save(HashMap<String, Double> results, String experimentID) throws Exception {
        try {
            Statement statement = database.Database.getConnection().createStatement();

            for (Map.Entry<String, Double> entry : results.entrySet()) {
                StringBuilder query = new StringBuilder();
                query.append("INSERT INTO distance_euclidean(solution_name, experiment_id, ed) VALUES(");
                query.append("'");
                query.append(entry.getKey());
                query.append("',");
                query.append(experimentID);
                query.append(",");
                query.append(entry.getValue());
                query.append(")");

                statement.executeUpdate(query.toString());
            }
            statement.close();
        } catch (ClassNotFoundException | SQLException | MissingConfigurationException e) {
            e.printStackTrace();
        }
    }

}
