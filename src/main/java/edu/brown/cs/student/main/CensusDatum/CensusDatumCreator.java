package edu.brown.cs.student.main.CensusDatum;

import edu.brown.cs.student.main.CreatorFromRow.CreatorFromRow;
import edu.brown.cs.student.main.CreatorFromRow.FactoryFailureException;
import java.util.List;

public class CensusDatumCreator implements CreatorFromRow<CensusDatum> {
  int pidRace;
  String pRace;
  int pidYr;
  int pYr;
  int phhibR;
  float phhibRM;
  String pGeo;
  String pidGeo;
  String psGeo;

  @Override
  public CensusDatum create(List<String> row, int requiredNumberOfFields)
      throws FactoryFailureException {
    try {

      if (row.size() != requiredNumberOfFields) {
        throw new FactoryFailureException(
            "The following row did not contain the "
                + "necessary number of elements to create a valid CensusDatum object:",
            row);
      }

      for (int i = 0; i < row.size(); i++) {
        switch (i) {
          case (0) -> {
            this.pidRace = Integer.valueOf(row.get(0));
          }
          case (1) -> {
            this.pRace = row.get(1);
          }
          case (2) -> {
            this.pidYr = Integer.valueOf(row.get(2));
          }
          case (3) -> {
            this.pYr = Integer.valueOf(row.get(3));
          }
          case (4) -> {
            this.phhibR = Integer.valueOf(row.get(4));
          }
          case (5) -> {
            this.phhibRM = Float.valueOf(row.get(5));
          }
          case (6) -> {
            this.pGeo = row.get(6);
          }
          case (7) -> {
            this.pidGeo = row.get(7);
          }
          case (8) -> {
            this.psGeo = row.get(8);
          }
        }
      }
    } catch (FactoryFailureException e) {
      System.err.println(
          "The following row did not contain the necessary number of elements to form a CensusDatum");
    }

    CensusDatum newInfo = new CensusDatum();
    newInfo.CensusDatum(
        this.pidRace,
        this.pRace,
        this.pidYr,
        this.pYr,
        this.phhibR,
        this.phhibRM,
        this.pGeo,
        this.pidGeo,
        this.psGeo);

    return newInfo;
  }
}
