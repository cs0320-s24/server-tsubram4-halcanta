package edu.brown.cs.student.main.DataSource;

/** This class communicates errors with a requested datasource by wrapping the cause as a field. */
public class DataSourceException extends Exception {

  private final Throwable cause;

  public DataSourceException(String message) {
    super(message); // Exception message
    this.cause = null;
  }

  public DataSourceException(String message, Throwable cause) {
    super(message); // Exception message
    this.cause = cause;
  }

  public DataSourceException(Throwable cause) {
    this.cause = cause;
  }

  /**
   * Returns the Throwable provided as the root cause of this exception.
   *
   * @return the root cause Throwable
   */
  public Throwable getCause() {
    return this.cause;
  }
}
