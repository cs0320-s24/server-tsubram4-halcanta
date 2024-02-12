package edu.brown.cs.student.main.Star;

import edu.brown.cs.student.main.CreatorFromRow.CreatorFromRow;
import edu.brown.cs.student.main.CreatorFromRow.FactoryFailureException;
import java.util.List;

public class StarCreator implements CreatorFromRow<Star> {

  int potentialStarID;
  String potentialProperName;
  float potentialDatumX;
  float potentialDatumY;
  float potentialDatumZ;

  @Override
  public Star create(List<String> row, int requiredNumberOfFields) throws FactoryFailureException {
    try {

      if (row.size() != requiredNumberOfFields) {
        throw new FactoryFailureException(
            "The following row did not contain the "
                + "necessary number of elements to create a valid Star object:",
            row);
      }

      for (int i = 0; i < row.size(); i++) {
        switch (i) {
          case (0) -> {
            this.potentialStarID = Integer.valueOf(row.get(0));
          }
          case (1) -> {
            this.potentialProperName = row.get(1);
          }
          case (2) -> {
            this.potentialDatumX = Float.valueOf(row.get(2));
          }
          case (3) -> {
            this.potentialDatumY = Float.valueOf(row.get(3));
          }
          case (4) -> {
            this.potentialDatumZ = Float.valueOf(row.get(4));
          }
        }
      }
    } catch (FactoryFailureException e) {
      System.err.println(
          "The following row did not contain the necessary number of elements to form a Star");
    }

    Star newStar = new Star();
    newStar.Star(
        this.potentialStarID,
        this.potentialProperName,
        this.potentialDatumX,
        this.potentialDatumY,
        this.potentialDatumZ);
    return newStar;
  }
}
