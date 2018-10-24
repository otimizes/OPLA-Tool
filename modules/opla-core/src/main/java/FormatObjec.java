import database.Database;
import results.Execution;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FormatObjec {

    public static void main(String args[]) throws Exception {
        Database.setPathToDB("/Users/elf/oplatool/db/oplatool.db");

        List<Double> c = new ArrayList<Double>();
        List<Double> f = new ArrayList<Double>();
        List<Double> p = new ArrayList<Double>();

        Statement statamentExecution = Database.getConnection().createStatement();
        ResultSet r = statamentExecution
                .executeQuery("SELECT objectives from objectives where experiement_id=4744218659 and execution_id=''");
        List<Execution> execs = new ArrayList<Execution>();

        while (r.next()) {
            String objs = r.getString("objectives").trim().replace("|", " ");
            String[] ov = objs.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ov.length; i++) {
                if (i == 0)
                    c.add(arredondar(Double.parseDouble(ov[i]), 4, 0));
                if (i == 1)
                    f.add(arredondar(Double.parseDouble(ov[i]), 4, 0));
                if (i == 2)
                    p.add(arredondar(Double.parseDouble(ov[i]), 4, 0));
            }
        }

        System.out.println(getMinValue(c));
        System.out.println(getMinValue(f));
        System.out.println(getMinValue(p));

    }

//    private static void time(String id) throws SQLException, ClassNotFoundException, MissingConfigurationException {
//	Statement statamentExecution = Database.getConnection().createStatement();
//	ResultSet r = statamentExecution.executeQuery("SELECT time, name, algorithm from executions JOIN experiments where experiement_id="+id + " and experiments.id = experiement_id");
//	long total = 0;
//	String name = r.getString("name") + " " + r.getString("algorithm");
//	while (r.next()) {
//	    total += TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(r.getString("time")));
//	}
//	System.out.println(name+ ":"+total);
//    }

    private static Double getMinValue(List<Double> f) {
        Double min = Double.MAX_VALUE;
        for (Double d : f) {
            if (d < min) {
                min = d;
            }
        }
        return min;
    }

    private static double arredondar(double valor, int casas, int ceilOrFloor) {
        double arredondado = valor;
        arredondado *= (Math.pow(10, casas));
        if (ceilOrFloor == 0) {
            arredondado = Math.ceil(arredondado);
        } else {
            arredondado = Math.floor(arredondado);
        }
        arredondado /= (Math.pow(10, casas));
        return arredondado;
    }

}
