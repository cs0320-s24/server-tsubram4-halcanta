package edu.brown.cs.student.main.CensusDatum;

public class CensusDatum {
  int idRace;
  String Race;
  int idYr;
  int Yr;
  int hhibR;
  float hhibRM;
  String Geo;
  String idGeo;
  String sGeo;

  public void CensusDatum(
      int idRace,
      String Race,
      int idYr,
      int Yr,
      int hhibR,
      float hhibRM,
      String Geo,
      String idGeo,
      String sGeo) {
    this.idRace = idRace;
    this.Race = Race;
    this.idYr = idYr;
    this.Yr = Yr;
    this.hhibR = hhibR;
    this.hhibRM = hhibRM;
    this.Geo = Geo;
    this.idGeo = idGeo;
    this.sGeo = sGeo;
  }
}
