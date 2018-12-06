package learning;

import br.ufpr.dinf.gres.opla.entity.Objective;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import liquibase.util.csv.CSVReader;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import utils.MathUtils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ClusteringExperimentTest {

    /* SCRIPTS R PARA EXPERIMENTOS
     - BET
        Feature Driven, Class Coupling and Cohesion
        install.packages("scatterplot3d")
        library(scatterplot3d)

         - SEM CLUSTER
         x = c(1476.0,1895.0,1477.0,1471.0,1475.0,1477.0,1478.0,1471.0,1477.0,1471.0,1476.0,1471.0,1477.0,1477.0,1471.0,1478.0,1471.0,1478.0,1476.0,1478.0,1477.0,1470.0,1476.0,1476.0,1478.0,1471.0,1477.0,1477.0,1476.0,1478.0,1471.0,1476.0,1476.0,1475.0,1475.0,1476.0,1476.0,1478.0,1469.0,1469.0,1471.0,1477.0,1476.0,1470.0,1470.0,1476.0,1471.0,1477.0,1476.0,1478.0,1478.0,1477.0,1477.0,1476.0,1476.0,1470.0,1476.0,1477.0,1476.0,1477.0,1476.0,1475.0,1470.0,1475.0,1478.0,1478.0,1471.0,1469.0,1476.0,1477.0,1478.0,1477.0,1477.0,1476.0,1475.0,1476.0,1476.0,1478.0,1477.0,1471.0,1477.0,1478.0,1476.0,1477.0,1478.0,1477.0,1477.0,1470.0,1477.0,1476.0,1471.0,1478.0,1475.0,1476.0,1478.0,1470.0,1471.0,1475.0,1478.0,1477.0,1478.0,1471.0,1476.0,1477.0,1476.0,1474.0,1474.0,1477.0,1477.0,1477.0,1477.0,1476.0,1471.0,1478.0,1475.0,1476.0,1476.0,1476.0,1478.0,1485.0,1475.0,1476.0,1477.0,1477.0,1474.0,1471.0,1477.0,1475.0,1475.0,1475.0,1477.0,1476.0,1477.0,1477.0,1476.0,1469.0,1471.0,1478.0,1477.0,1477.0,1476.0,1496.0,1526.0,1503.0,1496.0,1508.0,1510.0,1537.0,1496.0,1513.0,1510.0,1527.0,1453.0,1504.0,1528.0,1417.0,1496.0,1560.0,1530.0,1498.0,1504.0,1510.0,1504.0,1563.0,1496.0,1504.0,1528.0,1561.0,1495.0,1545.0,1496.0,1504.0,1527.0,1560.0,1393.0,1508.0,1528.0,1510.0,1510.0,1510.0,1560.0,1504.0,1528.0,1510.0,1510.0,1528.0,1503.0,1508.0,1510.0,1507.0,1497.0,1504.0,1504.0,1560.0,1528.0,1528.0,1536.0,1504.0,1504.0,1496.0,1528.0,1527.0,1496.0,1528.0,1504.0,1495.0,1503.0,1573.0,1561.0,1508.0,1504.0,1504.0,1510.0,1536.0,1503.0,1510.0,1494.0,1563.0,1566.0,1560.0,1510.0,1504.0,1496.0,1573.0,1528.0,1536.0,1561.0,1497.0,1528.0,1528.0,1504.0,1417.0,1496.0,1504.0,1504.0,1510.0,1494.0,1504.0,1504.0,1504.0,1510.0,1560.0,1510.0,1510.0,1560.0,1507.0,1496.0,1528.0,1528.0,1496.0,1508.0,1573.0,1504.0,1518.0,1528.0,1461.0,1502.0,1510.0,1528.0,1502.0,1504.0,1504.0,1393.0,1428.0,1504.0,1502.0,1560.0,1519.0,1563.0,1528.0,1509.0,1510.0,1526.0,1485.0,1504.0,1510.0,1508.0,1504.0,1504.0,1498.0,1504.0,1508.0,1560.0,1528.0,1503.0,1528.0,1498.0,1510.0,1528.0,1563.0,1497.0,1536.0,1528.0,1504.0,1573.0,1485.0,1506.0,1506.0,1526.0,1504.0,1423.0,1573.0,1504.0,1428.0,1560.0,1508.0,1428.0,1536.0,1508.0,1504.0,1510.0,1563.0,1417.0,1528.0,1528.0,1503.0,1563.0,1563.0,1508.0,1563.0,1498.0,1528.0,1498.0,1504.0,1510.0,1503.0)
         y = c(118.0,554.0,115.0,116.0,117.0,115.0,116.0,116.0,116.0,116.0,118.0,116.0,115.0,115.0,116.0,116.0,116.0,116.0,118.0,116.0,116.0,115.0,115.0,118.0,116.0,116.0,115.0,115.0,118.0,116.0,116.0,118.0,115.0,118.0,117.0,118.0,118.0,116.0,118.0,118.0,119.0,115.0,118.0,115.0,115.0,118.0,116.0,115.0,115.0,116.0,116.0,115.0,115.0,119.0,115.0,115.0,118.0,115.0,118.0,116.0,119.0,118.0,116.0,118.0,116.0,116.0,119.0,118.0,115.0,115.0,116.0,115.0,115.0,118.0,119.0,121.0,118.0,116.0,115.0,116.0,116.0,116.0,121.0,118.0,119.0,115.0,116.0,116.0,115.0,118.0,116.0,116.0,118.0,119.0,116.0,115.0,116.0,117.0,119.0,115.0,116.0,116.0,115.0,116.0,115.0,121.0,121.0,115.0,116.0,115.0,115.0,121.0,116.0,116.0,119.0,122.0,119.0,118.0,116.0,121.0,118.0,121.0,115.0,116.0,121.0,116.0,115.0,117.0,117.0,118.0,115.0,118.0,116.0,116.0,118.0,119.0,116.0,116.0,116.0,115.0,118.0,420.0,530.0,487.0,417.0,178.0,176.0,530.0,417.0,418.0,176.0,530.0,522.0,418.0,455.0,522.0,417.0,515.0,547.0,492.0,418.0,176.0,421.0,590.0,417.0,418.0,455.0,593.0,486.0,478.0,417.0,418.0,455.0,515.0,482.0,181.0,458.0,176.0,176.0,176.0,515.0,418.0,455.0,176.0,179.0,455.0,418.0,182.0,179.0,418.0,418.0,418.0,418.0,518.0,455.0,455.0,478.0,421.0,418.0,417.0,458.0,455.0,420.0,455.0,418.0,486.0,418.0,587.0,593.0,178.0,418.0,418.0,176.0,478.0,418.0,176.0,489.0,590.0,587.0,587.0,176.0,418.0,492.0,590.0,455.0,478.0,593.0,418.0,458.0,455.0,418.0,522.0,486.0,418.0,487.0,176.0,489.0,421.0,418.0,418.0,176.0,515.0,179.0,176.0,515.0,177.0,417.0,455.0,455.0,495.0,178.0,515.0,421.0,175.0,455.0,543.0,421.0,176.0,455.0,421.0,418.0,176.0,482.0,483.0,418.0,421.0,587.0,176.0,590.0,455.0,175.0,176.0,458.0,583.0,176.0,176.0,179.0,418.0,421.0,489.0,176.0,179.0,518.0,458.0,179.0,455.0,489.0,176.0,455.0,590.0,489.0,478.0,455.0,418.0,587.0,583.0,181.0,181.0,458.0,418.0,483.0,515.0,418.0,483.0,515.0,178.0,483.0,478.0,178.0,418.0,176.0,593.0,522.0,455.0,527.0,418.0,593.0,590.0,178.0,593.0,489.0,458.0,489.0,421.0,179.0,418.0)
         z = c(98.0,80.0,100.0,100.0,99.0,100.0,99.0,100.0,99.0,100.0,98.0,100.0,100.0,100.0,100.0,99.0,100.0,99.0,98.0,99.0,99.0,101.0,100.0,98.0,99.0,100.0,100.0,100.0,98.0,99.0,100.0,98.0,100.0,98.0,99.0,98.0,98.0,99.0,99.0,99.0,99.0,100.0,98.0,101.0,101.0,98.0,100.0,100.0,100.0,99.0,99.0,100.0,100.0,98.0,100.0,101.0,98.0,100.0,98.0,99.0,98.0,99.0,100.0,98.0,99.0,99.0,99.0,99.0,101.0,100.0,99.0,100.0,100.0,98.0,98.0,97.0,98.0,99.0,100.0,100.0,99.0,99.0,97.0,99.0,98.0,100.0,99.0,100.0,100.0,98.0,100.0,99.0,99.0,98.0,99.0,101.0,100.0,99.0,98.0,100.0,99.0,100.0,101.0,99.0,100.0,97.0,97.0,100.0,99.0,100.0,100.0,97.0,100.0,99.0,98.0,97.0,98.0,98.0,99.0,97.0,98.0,97.0,100.0,99.0,97.0,100.0,100.0,99.0,99.0,98.0,100.0,98.0,99.0,99.0,98.0,99.0,100.0,99.0,99.0,100.0,98.0,82.0,80.0,82.0,83.0,96.0,97.0,80.0,83.0,82.0,97.0,80.0,83.0,82.0,81.0,83.0,83.0,79.0,80.0,83.0,82.0,97.0,81.0,80.0,83.0,82.0,81.0,79.0,83.0,80.0,83.0,82.0,81.0,79.0,84.0,95.0,80.0,97.0,97.0,97.0,79.0,82.0,81.0,97.0,96.0,81.0,82.0,95.0,96.0,83.0,83.0,82.0,82.0,78.0,81.0,81.0,80.0,81.0,82.0,83.0,80.0,81.0,82.0,81.0,82.0,83.0,82.0,79.0,79.0,96.0,82.0,82.0,97.0,80.0,82.0,97.0,82.0,80.0,79.0,79.0,97.0,82.0,83.0,80.0,81.0,80.0,79.0,83.0,80.0,81.0,82.0,83.0,83.0,82.0,82.0,97.0,82.0,81.0,82.0,82.0,97.0,79.0,96.0,97.0,79.0,97.0,83.0,81.0,81.0,82.0,96.0,79.0,81.0,98.0,81.0,83.0,81.0,97.0,81.0,81.0,82.0,97.0,84.0,85.0,82.0,81.0,79.0,97.0,80.0,81.0,98.0,97.0,80.0,82.0,97.0,97.0,96.0,82.0,81.0,84.0,97.0,96.0,78.0,80.0,97.0,81.0,84.0,97.0,81.0,80.0,84.0,80.0,81.0,82.0,79.0,82.0,95.0,95.0,80.0,82.0,86.0,79.0,82.0,85.0,79.0,96.0,85.0,80.0,96.0,82.0,97.0,79.0,83.0,81.0,81.0,82.0,79.0,80.0,96.0,79.0,84.0,80.0,84.0,81.0,96.0,82.0)
         colors = c(1476.0,"black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black")
         s3d = scatterplot3d(x, y, z, type = "p", color = colors, angle = 10, pch = 1, main = "scatterplot3d", zlab="Feature Driven", ylab="ACLASS", xlab="COE")
         hv = c(0.407209, 0.323401, 0.386058, 0.388186, 0.372919, 0.353859,     0.373962, 0.379551, 0.355527, 0.400275, 0.362495, 0.382301,     0.371036, 0.386933, 0.361358, 0.375651, 0.362005, 0.367474,     0.396773, 0.353165, 0.352628, 0.413356, 0.353165, 0.368163,     0.417212, 0.365668, 0.374662, 0.394289, 0.367543, 0.371868)
         shapiro.test(hv)

     - AGM
        Feature Driven, Class Coupling and Cohesion
        install.packages("scatterplot3d")
        library(scatterplot3d)

        - SEM CLUSTER
        x = c(685.0,684.0,707.0,670.0,706.0,685.0,697.0,658.0,685.0,664.0,694.0,733.0,694.0,699.0,712.0,699.0,697.0,711.0,736.0,735.0,699.0,713.0,712.0,651.0,658.0,733.0,717.0,711.0,697.0,676.0,711.0,676.0,732.0,711.0,711.0,699.0,735.0,729.0,787.0,713.0,705.0,787.0,710.0,699.0,735.0,711.0,703.0,786.0,735.0,711.0,711.0,711.0,708.0,676.0,735.0,785.0,728.0,667.0,720.0,785.0,733.0,713.0,737.0,711.0,699.0,731.0,676.0,711.0,735.0,699.0,786.0,711.0,728.0,699.0,713.0,711.0,653.0,787.0,713.0,713.0,735.0,704.0,693.0,735.0,704.0,779.0,707.0,713.0,703.0,787.0,717.0,712.0,710.0,717.0,735.0,667.0,725.0,726.0,735.0,705.0,705.0,676.0,711.0,713.0,735.0,725.0,717.0,733.0,735.0,785.0,779.0,712.0,712.0,735.0,689.0,713.0,785.0,728.0,779.0,711.0,713.0,712.0,676.0,699.0,705.0,719.0,735.0,735.0,712.0,737.0,787.0,778.0,735.0,711.0,726.0,735.0,732.0,785.0,736.0,711.0,713.0,699.0,699.0,711.0,711.0,688.0,711.0,734.0,735.0,735.0,726.0,735.0,710.0,737.0,705.0,705.0,649.0,735.0,729.0,787.0,698.0,711.0,712.0,723.0,649.0,727.0,690.0,694.0,785.0,777.0,779.0,694.0,711.0,735.0,676.0,787.0,734.0,735.0,777.0,667.0,735.0,712.0,778.0,697.0,736.0,717.0,717.0,778.0,727.0,778.0,700.0,698.0,676.0,729.0,737.0,697.0,785.0,687.0,699.0,735.0,735.0,735.0,735.0,672.0,676.0,734.0,786.0,729.0,711.0,704.0,658.0,778.0,697.0,711.0,735.0,708.0,712.0,735.0,735.0,735.0,737.0,778.0,737.0,779.0,737.0,699.0,711.0,712.0,736.0,683.0,737.0,735.0,707.0,667.0,787.0,711.0,697.0,730.0,676.0,734.0)
        y = c(12.0,12.0,16.0,11.0,23.0,12.0,27.0,11.0,12.0,22.0,22.0,21.0,16.0,22.0,23.0,22.0,16.0,23.0,27.0,21.0,22.0,23.0,23.0,14.0,22.0,27.0,17.0,17.0,16.0,15.0,17.0,12.0,21.0,23.0,17.0,22.0,21.0,27.0,33.0,23.0,22.0,33.0,17.0,22.0,21.0,17.0,16.0,33.0,21.0,17.0,17.0,17.0,17.0,15.0,21.0,33.0,27.0,15.0,21.0,33.0,21.0,22.0,27.0,23.0,22.0,21.0,12.0,17.0,27.0,22.0,33.0,17.0,27.0,22.0,23.0,17.0,14.0,33.0,23.0,22.0,21.0,23.0,16.0,21.0,17.0,33.0,22.0,23.0,16.0,33.0,17.0,23.0,17.0,17.0,21.0,15.0,20.0,18.0,21.0,22.0,23.0,15.0,17.0,23.0,27.0,20.0,17.0,27.0,21.0,33.0,33.0,23.0,23.0,21.0,22.0,23.0,33.0,27.0,33.0,17.0,23.0,23.0,15.0,22.0,23.0,23.0,21.0,21.0,23.0,27.0,33.0,33.0,21.0,17.0,18.0,27.0,18.0,33.0,27.0,23.0,17.0,22.0,22.0,17.0,17.0,23.0,17.0,21.0,21.0,21.0,18.0,21.0,23.0,27.0,16.0,23.0,11.0,21.0,27.0,33.0,22.0,17.0,23.0,18.0,11.0,21.0,22.0,16.0,33.0,33.0,33.0,16.0,17.0,27.0,15.0,33.0,10.0,27.0,33.0,15.0,27.0,23.0,33.0,16.0,27.0,17.0,16.0,33.0,21.0,33.0,19.0,22.0,15.0,27.0,27.0,16.0,33.0,16.0,22.0,21.0,21.0,21.0,21.0,15.0,15.0,27.0,33.0,27.0,17.0,23.0,22.0,33.0,16.0,17.0,27.0,23.0,23.0,27.0,27.0,21.0,27.0,33.0,27.0,33.0,27.0,22.0,17.0,23.0,27.0,22.0,27.0,21.0,17.0,15.0,33.0,17.0,16.0,28.0,15.0,10.0)
        z = c(31.0,32.0,31.0,32.0,29.0,31.0,28.0,31.0,31.0,29.0,30.0,27.0,29.0,28.0,27.0,28.0,29.0,27.0,26.0,27.0,28.0,27.0,27.0,30.0,28.0,26.0,28.0,28.0,29.0,30.0,28.0,30.0,27.0,27.0,28.0,28.0,27.0,26.0,25.0,27.0,28.0,25.0,28.0,28.0,27.0,28.0,30.0,25.0,27.0,28.0,28.0,28.0,29.0,30.0,27.0,25.0,26.0,29.0,28.0,25.0,27.0,28.0,26.0,27.0,28.0,27.0,30.0,28.0,26.0,28.0,25.0,28.0,26.0,28.0,27.0,28.0,30.0,25.0,27.0,28.0,27.0,27.0,30.0,27.0,30.0,25.0,28.0,27.0,30.0,25.0,28.0,27.0,28.0,28.0,27.0,29.0,30.0,28.0,27.0,28.0,27.0,30.0,28.0,27.0,26.0,30.0,28.0,26.0,27.0,25.0,25.0,27.0,27.0,27.0,29.0,27.0,25.0,26.0,25.0,28.0,27.0,27.0,30.0,28.0,27.0,27.0,27.0,27.0,27.0,26.0,25.0,25.0,27.0,28.0,28.0,26.0,28.0,25.0,26.0,27.0,29.0,28.0,28.0,28.0,29.0,29.0,28.0,27.0,27.0,27.0,28.0,27.0,27.0,26.0,29.0,27.0,30.0,27.0,26.0,25.0,28.0,28.0,27.0,28.0,30.0,27.0,28.0,29.0,25.0,25.0,25.0,29.0,29.0,26.0,30.0,25.0,32.0,26.0,25.0,29.0,26.0,27.0,25.0,29.0,26.0,28.0,29.0,25.0,27.0,25.0,30.0,28.0,31.0,26.0,26.0,29.0,25.0,30.0,28.0,27.0,27.0,27.0,27.0,30.0,30.0,26.0,25.0,26.0,28.0,27.0,28.0,25.0,29.0,28.0,26.0,27.0,27.0,26.0,26.0,27.0,26.0,25.0,26.0,25.0,26.0,28.0,29.0,27.0,26.0,29.0,26.0,27.0,29.0,31.0,25.0,29.0,29.0,26.0,30.0,32.0)
        colors = c("black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black","black")
        s3d = scatterplot3d(x, y, z, type = "p", color = colors, angle = 10, pch = 1, main = "scatterplot3d", zlab="Feature Driven", ylab="ACLASS", xlab="COE")
        hv = c(0.36303500, 0.37114300, 0.37314900, 0.38578200, 0.38849600, 0.34738400, 0.30941900, 0.31079100, 0.37191900, 0.48017800, 0.33253800, 0.31899900, 0.28454600, 0.41587100, 0.37155100, 0.36939100, 0.33452900, 0.48598100, 0.29346500, 0.30791700, 0.45778300, 0.41303000, 0.39402800, 0.34768200, 0.29199700, 0.29043300, 0.29512000, 0.42945000, 0.36761800, 0.32084000)
        shapiro.test(hv)


    */

    public static final Logger LOGGER = Logger.getLogger(ClusteringExperimentTest.class);

    private void test(SolutionSet run) throws IOException {
        List<Double> hypervolume = calculateHypervolume(run);
        System.out.println(hypervolume.toString().replace("[", "hv = c(").replace("]", ")"));
        System.out.println("shapiro.test(hv)");
        System.out.println("boxplot(hv)");
        List<List<Double>> listXYZ = getListXYZ(run);
        System.out.println("");
        System.out.println(listXYZ.get(0).toString().replace("[", "x = c(").replace("]", ")"));
        String s = Integer.toHexString(new Color(new Random().nextInt(0xFFFFFF)).getRGB());
        System.out.println(listXYZ.get(1).toString().replace("[", "y = c(").replace("]", ")"));
        System.out.println(listXYZ.get(2).toString().replace("[", "z = c(").replace("]", ")"));
        System.out.println(("colors = c(" + listXYZ.get(0).stream().map(c -> "\"" + "blue" + "\",").collect(Collectors.joining()) + ")").replace(",)", ")"));
        System.out.println("s3d = scatterplot3d(x, y, z, type = \"p\", angle = 10, pch = 1, main = \"scatterplot3d\", zlab=\"Feature Driven\", ylab=\"ACLASS\", xlab=\"COE\", color=colors)");
    }

    @Before
    public void init() {
//        Clustering.LOGGER.setLevel(Level.OFF);
    }

//    @Test
    public void agmOnDBSCAN() throws Exception {
        List<Objective> objectives = getObjectivesFromFile("agm_objectives_27112018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives);

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.DBSCAN);
        SolutionSet run = clustering.run();

        LOGGER.info("AGM DBSCAN");
        test(run);
    }

//    @Test
    public void agmOnKMeans() throws Exception {
        List<Objective> objectives = getObjectivesFromFile("agm_objectives_27112018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives);

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.KMEANS);
        SolutionSet run = clustering.run();

        LOGGER.info("AGM KMEANS");
        test(run);
    }

//    @Test
    public void betOnDBSCAN() throws Exception {
        List<Objective> objectives = getObjectivesFromFile("bet_objectives_02122018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives);

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.DBSCAN);
        SolutionSet run = clustering.run();

        LOGGER.info("BET DBSCAN");
        test(run);
    }


//    @Test
    public void betOnKMeans() throws Exception {
        List<Objective> objectives = getObjectivesFromFile("bet_objectives_02122018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives);

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.KMEANS);
        SolutionSet run = clustering.run();

        LOGGER.info("BET KMEANS");
        test(run);
    }

    private SolutionSet getSolutionSetFromObjectiveList(List<Objective> objectives) {
        SolutionSet solutionSet = new SolutionSet();
        objectives.forEach(objective -> {
            Solution solution = new Solution();
            solution.createObjective(3);
            String[] split = objective.getObjectives().split("\\|");
            solution.setObjective(0, Double.parseDouble(split[0]));
            solution.setObjective(1, Double.parseDouble(split[1]));
            solution.setObjective(2, Double.parseDouble(split[2]));
            solution.setSolutionName(objective.getSolutionName());
            solution.setExecutionId(objective.getExecution() != null ? objective.getExecution().getId() : 0);
            solution.setNumberOfObjectives(3);
            solutionSet.getSolutionSet().add(solution);
        });
        return solutionSet;
    }

    private List<Objective> getObjectivesFromFile(String filename) throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] nextLine;
        List<Objective> objectives = new ArrayList<>();
        while ((nextLine = reader.readNext()) != null) {
            objectives.add(new Objective(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5]));
        }
        return objectives;
    }

    private String getNormalizedNewObjectivesOfClusteredSolutions(SolutionSet run) {
        List<List<Double>> result = MathUtils.normalize(run);
        Long lastExec = null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            builder.append(result.get(i).toString().replace("[", "").replace("]", "").replaceAll(",", "") + "\n");
            if (!run.getSolutionSet().get(i).getExecutionId().equals(lastExec)) {
                lastExec = run.getSolutionSet().get(i).getExecutionId();
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private List<List<Double>> getListXYZ(SolutionSet result) {
        List<List<Double>> listXYZ = new ArrayList<>();
        listXYZ.add(new ArrayList<>());
        listXYZ.add(new ArrayList<>());
        listXYZ.add(new ArrayList<>());
        result.getSolutionSet().forEach(r -> {
            listXYZ.get(0).add(r.getObjective(0));
            listXYZ.get(1).add(r.getObjective(1));
            listXYZ.get(2).add(r.getObjective(2));
        });
        return listXYZ;
    }

    private List<Double> calculateHypervolume(SolutionSet run) throws IOException {
        String newObjectivesOfClusteredSolutions = getNormalizedNewObjectivesOfClusteredSolutions(run);
        File tempFile = File.createTempFile("opla-", "-test");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(newObjectivesOfClusteredSolutions);
        bw.close();

        List<Double> hypervolume = hypervolume("1.01 1.01 1.01", tempFile.toString());

        tempFile.deleteOnExit();
        return hypervolume;
    }

    public static List<Double> hypervolume(String referencePoint, String pathToFile) throws IOException {
        String hyperVolumeBin = getHome() + "/bins/./hv";
        //String hyperVolumeBin = UserHome.getOplaUserHome() + "bins/hv";
        ProcessBuilder builder = new ProcessBuilder(hyperVolumeBin, "-r", referencePoint, pathToFile);
        builder.redirectErrorStream(true);
        Process p = builder.start();


        return inheritIO(p.getInputStream());

    }

    private static String getHome() {
        return System.getProperties().get("user.home") + "/oplatool";
    }

    private static List<Double> inheritIO(final InputStream src) {
        List<Double> values = new ArrayList<>();

        Scanner sc = new Scanner(src);
        while (sc.hasNextLine())
            values.add(Double.valueOf(sc.nextLine()));

        return values;
    }
}
