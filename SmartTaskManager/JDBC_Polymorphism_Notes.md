# JDBC Polymorphism Simulation in Pure Java

This note explains how Java's `DriverManager` uses polymorphism to select the correct JDBC driver, using a pure Java simulation (no real database required). This helps you understand the core JDBC mechanism under the hood.

## 🎯 Learning Objectives
- Understand how JDBC uses polymorphism for driver selection
- Learn the driver registration and discovery mechanism
- Grasp the Service Provider Interface (SPI) pattern in JDBC
- Implement a complete JDBC simulation from scratch

---

## 1. Define the Common Interface (like `java.sql.Driver`)

```java
interface Driver {
    boolean acceptsURL(String url);
    Connection connect(String url);
}
```
- **Purpose:** Defines the contract for all database drivers.
- **Vendors** (PostgreSQL, MySQL, Oracle, etc.) must implement these methods.

---

## 2. Create Vendor Implementations

Each vendor provides its own driver by implementing the interface differently.

```java
class PostgreSQLDriver implements Driver {
    public boolean acceptsURL(String url) {
        return url.startsWith("jdbc:postgresql:");
    }
    public Connection connect(String url) {
        return new Connection("Connected to PostgreSQL DB!");
    }
}

class MySQLDriver implements Driver {
    public boolean acceptsURL(String url) {
        return url.startsWith("jdbc:mysql:");
    }
    public Connection connect(String url) {
        return new Connection("Connected to MySQL DB!");
    }
}
```
- **Each driver** checks if it can handle the given URL and returns a connection if so.

---

## 3. Dummy Connection Class

```java
class Connection {
    private String message;
    public Connection(String msg) { this.message = msg; }
    public void show() { System.out.println(message); }
}
```
- **Purpose:** Placeholder to simulate a real database connection.

---

## 4. DriverManager (Java’s Contract Manager)

This class keeps a list of all registered drivers and uses polymorphism to find the right one.

```java
import java.util.*;

class DriverManager {
    private static List<Driver> registeredDrivers = new ArrayList<>();

    public static void registerDriver(Driver d) {
        registeredDrivers.add(d);
    }

    public static Connection getConnection(String url) {
        for (Driver d : registeredDrivers) {
            if (d.acceptsURL(url)) { // Polymorphism in action!
                return d.connect(url);
            }
        }
        throw new RuntimeException("No suitable driver found!");
    }
}
```
- **Polymorphism:** `DriverManager` doesn't care about the specific driver type, just that it implements `Driver`.

---

## 5. Simulation (Putting It All Together)

```java
public class Demo {
    public static void main(String[] args) {
        // Register vendor drivers
        DriverManager.registerDriver(new PostgreSQLDriver());
        DriverManager.registerDriver(new MySQLDriver());

        // Request connection (DriverManager chooses the right driver)
        Connection c1 = DriverManager.getConnection("jdbc:postgresql://localhost/testdb");
        c1.show(); // Output: Connected to PostgreSQL DB!

        Connection c2 = DriverManager.getConnection("jdbc:mysql://localhost/testdb");
        c2.show(); // Output: Connected to MySQL DB!
    }
}
```

---

## Key Takeaways

### 🔑 Polymorphism in Action
- **Runtime Decision:** `DriverManager` doesn't know which specific driver it's using until runtime
- **Interface Abstraction:** All drivers look the same to `DriverManager` through the `Driver` interface
- **Flexible Extension:** New database vendors can add drivers without changing existing code

### 🔧 Real JDBC Mechanics
1. **Driver Loading:** `Class.forName("org.postgresql.Driver")` loads and auto-registers the driver
2. **Static Initialization:** Driver classes register themselves in static blocks
3. **Service Discovery:** Modern JDBC uses META-INF/services for automatic driver discovery
4. **URL Parsing:** Each driver examines the JDBC URL to determine compatibility

### 🚨 Common Issues & Solutions

#### "No suitable driver found" Error
```java
// Problem: Driver not in classpath
// Solution: Add JAR to classpath or use dependency management

// Problem: Wrong URL format
String wrongUrl = "postgresql://localhost:5432/db"; // Missing jdbc:
String correctUrl = "jdbc:postgresql://localhost:5432/db"; // Correct
```

#### Driver Registration Issues
```java
// Manual registration (if auto-registration fails)
DriverManager.registerDriver(new org.postgresql.Driver());

// Check registered drivers
Enumeration<Driver> drivers = DriverManager.getDrivers();
while(drivers.hasMoreElements()) {
    System.out.println(drivers.nextElement().getClass().getName());
}
```

### 📊 JDBC Architecture Flow
```
Application Code
       ↓
   DriverManager
       ↓
Driver Interface (Polymorphism Layer)
       ↓
Concrete Driver Implementation
       ↓
Database Specific Protocol
       ↓
    Database
```

### 🛠️ Enhanced Demo with Error Handling

```java
public class EnhancedJDBCDemo {
    public static void main(String[] args) {
        try {
            // Register drivers
            DriverManager.registerDriver(new PostgreSQLDriver());
            DriverManager.registerDriver(new MySQLDriver());
            
            // Test valid connections
            testConnection("jdbc:postgresql://localhost/testdb");
            testConnection("jdbc:mysql://localhost/testdb");
            
            // Test invalid URL (demonstrates polymorphism failure)
            testConnection("jdbc:oracle://localhost/testdb");
            
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
        }
    }
    
    private static void testConnection(String url) {
        try {
            Connection conn = DriverManager.getConnection(url);
            conn.show();
        } catch (RuntimeException e) {
            System.err.println("Failed for URL '" + url + "': " + e.getMessage());
        }
    }
}
```

### 🎓 Interview Questions & Answers

**Q: How does DriverManager know which driver to use?**
A: It iterates through registered drivers and calls `acceptsURL()` on each until one returns true.

**Q: What happens if multiple drivers accept the same URL?**
A: The first driver that accepts the URL is used (order matters in registration).

**Q: Why use interfaces instead of concrete classes?**
A: Enables polymorphism, loose coupling, and allows multiple vendors to provide implementations.

**Q: What's the difference between `Class.forName()` and manual registration?**
A: `Class.forName()` triggers static initialization which auto-registers the driver; manual registration gives explicit control.

### 🔗 Related Concepts
- **Service Provider Interface (SPI):** JDBC drivers implement the SPI pattern
- **Factory Pattern:** DriverManager acts as a factory for Connection objects
- **Strategy Pattern:** Different drivers represent different connection strategies
- **Dependency Injection:** Modern frameworks inject DataSource instead of using DriverManager

---

## 📋 JDBC Core Interfaces (Implemented by Vendors)

### 🔌 **java.sql.Driver**
```java
public interface Driver {
    Connection connect(String url, Properties info) throws SQLException;
    boolean acceptsURL(String url) throws SQLException;
    DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException;
    int getMajorVersion();
    int getMinorVersion();
    boolean jdbcCompliant();
    Logger getParentLogger() throws SQLFeatureNotSupportedException;
}
```
**Purpose:** Main interface that every JDBC driver must implement
**Vendor Example:** `org.postgresql.Driver`, `com.mysql.cj.jdbc.Driver`

### 🔗 **java.sql.Connection**
```java
public interface Connection extends Wrapper, AutoCloseable {
    Statement createStatement() throws SQLException;
    PreparedStatement prepareStatement(String sql) throws SQLException;
    CallableStatement prepareCall(String sql) throws SQLException;
    
    void setAutoCommit(boolean autoCommit) throws SQLException;
    boolean getAutoCommit() throws SQLException;
    void commit() throws SQLException;
    void rollback() throws SQLException;
    
    void close() throws SQLException;
    boolean isClosed() throws SQLException;
    
    DatabaseMetaData getMetaData() throws SQLException;
    void setReadOnly(boolean readOnly) throws SQLException;
    boolean isReadOnly() throws SQLException;
    
    void setCatalog(String catalog) throws SQLException;
    String getCatalog() throws SQLException;
    
    void setTransactionIsolation(int level) throws SQLException;
    int getTransactionIsolation() throws SQLException;
    
    Savepoint setSavepoint() throws SQLException;
    void rollback(Savepoint savepoint) throws SQLException;
}
```
**Purpose:** Represents a session with a specific database
**Vendor Example:** `org.postgresql.jdbc.PgConnection`, `com.mysql.cj.jdbc.ConnectionImpl`

### 📝 **java.sql.Statement**
```java
public interface Statement extends Wrapper, AutoCloseable {
    ResultSet executeQuery(String sql) throws SQLException;
    int executeUpdate(String sql) throws SQLException;
    boolean execute(String sql) throws SQLException;
    
    void close() throws SQLException;
    int getMaxFieldSize() throws SQLException;
    void setMaxFieldSize(int max) throws SQLException;
    
    int getMaxRows() throws SQLException;
    void setMaxRows(int max) throws SQLException;
    
    void setEscapeProcessing(boolean enable) throws SQLException;
    int getQueryTimeout() throws SQLException;
    void setQueryTimeout(int seconds) throws SQLException;
    
    void cancel() throws SQLException;
    SQLWarning getWarnings() throws SQLException;
    void clearWarnings() throws SQLException;
    
    void addBatch(String sql) throws SQLException;
    void clearBatch() throws SQLException;
    int[] executeBatch() throws SQLException;
    
    Connection getConnection() throws SQLException;
    ResultSet getGeneratedKeys() throws SQLException;
}
```
**Purpose:** Executes SQL statements and returns results
**Vendor Example:** `org.postgresql.jdbc.PgStatement`, `com.mysql.cj.jdbc.StatementImpl`

### 🎯 **java.sql.PreparedStatement**
```java
public interface PreparedStatement extends Statement {
    ResultSet executeQuery() throws SQLException;
    int executeUpdate() throws SQLException;
    
    void setNull(int parameterIndex, int sqlType) throws SQLException;
    void setBoolean(int parameterIndex, boolean x) throws SQLException;
    void setByte(int parameterIndex, byte x) throws SQLException;
    void setShort(int parameterIndex, short x) throws SQLException;
    void setInt(int parameterIndex, int x) throws SQLException;
    void setLong(int parameterIndex, long x) throws SQLException;
    void setFloat(int parameterIndex, float x) throws SQLException;
    void setDouble(int parameterIndex, double x) throws SQLException;
    void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException;
    void setString(int parameterIndex, String x) throws SQLException;
    void setBytes(int parameterIndex, byte[] x) throws SQLException;
    void setDate(int parameterIndex, Date x) throws SQLException;
    void setTime(int parameterIndex, Time x) throws SQLException;
    void setTimestamp(int parameterIndex, Timestamp x) throws SQLException;
    
    void clearParameters() throws SQLException;
    void setObject(int parameterIndex, Object x) throws SQLException;
    boolean execute() throws SQLException;
    void addBatch() throws SQLException;
    
    ParameterMetaData getParameterMetaData() throws SQLException;
}
```
**Purpose:** Pre-compiled SQL statement with parameter placeholders
**Vendor Example:** `org.postgresql.jdbc.PgPreparedStatement`, `com.mysql.cj.jdbc.ClientPreparedStatement`

### 📞 **java.sql.CallableStatement**
```java
public interface CallableStatement extends PreparedStatement {
    void registerOutParameter(int parameterIndex, int sqlType) throws SQLException;
    void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException;
    void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException;
    
    boolean wasNull() throws SQLException;
    
    String getString(int parameterIndex) throws SQLException;
    boolean getBoolean(int parameterIndex) throws SQLException;
    byte getByte(int parameterIndex) throws SQLException;
    short getShort(int parameterIndex) throws SQLException;
    int getInt(int parameterIndex) throws SQLException;
    long getLong(int parameterIndex) throws SQLException;
    float getFloat(int parameterIndex) throws SQLException;
    double getDouble(int parameterIndex) throws SQLException;
    BigDecimal getBigDecimal(int parameterIndex) throws SQLException;
    byte[] getBytes(int parameterIndex) throws SQLException;
    Date getDate(int parameterIndex) throws SQLException;
    Time getTime(int parameterIndex) throws SQLException;
    Timestamp getTimestamp(int parameterIndex) throws SQLException;
    Object getObject(int parameterIndex) throws SQLException;
    
    Array getArray(int parameterIndex) throws SQLException;
}
```
**Purpose:** Used to execute stored procedures with IN, OUT, and INOUT parameters
**Vendor Example:** `org.postgresql.jdbc.PgCallableStatement`, `com.mysql.cj.jdbc.CallableStatement`

### 📊 **java.sql.ResultSet**
```java
public interface ResultSet extends Wrapper, AutoCloseable {
    boolean next() throws SQLException;
    boolean previous() throws SQLException;
    boolean first() throws SQLException;
    boolean last() throws SQLException;
    boolean absolute(int row) throws SQLException;
    boolean relative(int rows) throws SQLException;
    
    void close() throws SQLException;
    boolean wasNull() throws SQLException;
    
    String getString(int columnIndex) throws SQLException;
    String getString(String columnLabel) throws SQLException;
    boolean getBoolean(int columnIndex) throws SQLException;
    byte getByte(int columnIndex) throws SQLException;
    short getShort(int columnIndex) throws SQLException;
    int getInt(int columnIndex) throws SQLException;
    long getLong(int columnIndex) throws SQLException;
    float getFloat(int columnIndex) throws SQLException;
    double getDouble(int columnIndex) throws SQLException;
    BigDecimal getBigDecimal(int columnIndex) throws SQLException;
    byte[] getBytes(int columnIndex) throws SQLException;
    Date getDate(int columnIndex) throws SQLException;
    Time getTime(int columnIndex) throws SQLException;
    Timestamp getTimestamp(int columnIndex) throws SQLException;
    Object getObject(int columnIndex) throws SQLException;
    
    int findColumn(String columnLabel) throws SQLException;
    ResultSetMetaData getMetaData() throws SQLException;
    Statement getStatement() throws SQLException;
    
    void updateString(int columnIndex, String x) throws SQLException;
    void updateInt(int columnIndex, int x) throws SQLException;
    void insertRow() throws SQLException;
    void updateRow() throws SQLException;
    void deleteRow() throws SQLException;
}
```
**Purpose:** Represents a table of data resulting from executing a query
**Vendor Example:** `org.postgresql.jdbc.PgResultSet`, `com.mysql.cj.jdbc.result.ResultSetImpl`

### 🗃️ **javax.sql.DataSource**
```java
public interface DataSource extends CommonDataSource, Wrapper {
    Connection getConnection() throws SQLException;
    Connection getConnection(String username, String password) throws SQLException;
}
```
**Purpose:** Factory for connections (preferred over DriverManager in production)
**Vendor Example:** `org.postgresql.ds.PGSimpleDataSource`, `com.mysql.cj.jdbc.MysqlDataSource`

### 🔍 **java.sql.DatabaseMetaData**
```java
public interface DatabaseMetaData extends Wrapper {
    String getURL() throws SQLException;
    String getUserName() throws SQLException;
    String getDatabaseProductName() throws SQLException;
    String getDatabaseProductVersion() throws SQLException;
    String getDriverName() throws SQLException;
    String getDriverVersion() throws SQLException;
    
    ResultSet getTables(String catalog, String schemaPattern, 
                       String tableNamePattern, String[] types) throws SQLException;
    ResultSet getColumns(String catalog, String schemaPattern, 
                        String tableNamePattern, String columnNamePattern) throws SQLException;
    ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException;
    ResultSet getIndexInfo(String catalog, String schema, String table, 
                          boolean unique, boolean approximate) throws SQLException;
    
    boolean supportsTransactions() throws SQLException;
    boolean supportsStoredProcedures() throws SQLException;
    int getMaxConnections() throws SQLException;
    int getMaxStatements() throws SQLException;
}
```
**Purpose:** Provides comprehensive information about the database
**Vendor Example:** `org.postgresql.jdbc.PgDatabaseMetaData`, `com.mysql.cj.jdbc.DatabaseMetaDataUsingInfoSchema`

### 📏 **java.sql.ResultSetMetaData**
```java
public interface ResultSetMetaData extends Wrapper {
    int getColumnCount() throws SQLException;
    boolean isAutoIncrement(int column) throws SQLException;
    boolean isCaseSensitive(int column) throws SQLException;
    boolean isSearchable(int column) throws SQLException;
    boolean isCurrency(int column) throws SQLException;
    int isNullable(int column) throws SQLException;
    boolean isSigned(int column) throws SQLException;
    int getColumnDisplaySize(int column) throws SQLException;
    String getColumnLabel(int column) throws SQLException;
    String getColumnName(int column) throws SQLException;
    String getSchemaName(int column) throws SQLException;
    int getPrecision(int column) throws SQLException;
    int getScale(int column) throws SQLException;
    String getTableName(int column) throws SQLException;
    String getCatalogName(int column) throws SQLException;
    int getColumnType(int column) throws SQLException;
    String getColumnTypeName(int column) throws SQLException;
    boolean isReadOnly(int column) throws SQLException;
    boolean isWritable(int column) throws SQLException;
    boolean isDefinitelyWritable(int column) throws SQLException;
    String getColumnClassName(int column) throws SQLException;
}
```
**Purpose:** Provides information about the columns in a ResultSet
**Vendor Example:** `org.postgresql.jdbc.PgResultSetMetaData`, `com.mysql.cj.jdbc.result.ResultSetMetaData`

### 🔧 **java.sql.ParameterMetaData**
```java
public interface ParameterMetaData extends Wrapper {
    int getParameterCount() throws SQLException;
    int isNullable(int param) throws SQLException;
    boolean isSigned(int param) throws SQLException;
    int getPrecision(int param) throws SQLException;
    int getScale(int param) throws SQLException;
    int getParameterType(int param) throws SQLException;
    String getParameterTypeName(int param) throws SQLException;
    String getParameterClassName(int param) throws SQLException;
    int getParameterMode(int param) throws SQLException;
}
```
**Purpose:** Provides information about the parameters of a PreparedStatement

---

## 🎯 **Vendor Implementation Examples**

### PostgreSQL Driver Implementations:
- `org.postgresql.Driver` implements `java.sql.Driver`
- `org.postgresql.jdbc.PgConnection` implements `java.sql.Connection`
- `org.postgresql.jdbc.PgStatement` implements `java.sql.Statement`
- `org.postgresql.jdbc.PgPreparedStatement` implements `java.sql.PreparedStatement`
- `org.postgresql.jdbc.PgResultSet` implements `java.sql.ResultSet`

### MySQL Driver Implementations:
- `com.mysql.cj.jdbc.Driver` implements `java.sql.Driver`
- `com.mysql.cj.jdbc.ConnectionImpl` implements `java.sql.Connection`
- `com.mysql.cj.jdbc.StatementImpl` implements `java.sql.Statement`
- `com.mysql.cj.jdbc.ClientPreparedStatement` implements `java.sql.PreparedStatement`
- `com.mysql.cj.jdbc.result.ResultSetImpl` implements `java.sql.ResultSet`

### Oracle Driver Implementations:
- `oracle.jdbc.OracleDriver` implements `java.sql.Driver`
- `oracle.jdbc.driver.T4CConnection` implements `java.sql.Connection`
- `oracle.jdbc.driver.OracleStatement` implements `java.sql.Statement`
- `oracle.jdbc.driver.OraclePreparedStatement` implements `java.sql.PreparedStatement`
- `oracle.jdbc.driver.OracleResultSet` implements `java.sql.ResultSet`

---

### 💡 Best Practices
1. **Use DataSource over DriverManager** in production applications
2. **Handle SQLException properly** with try-catch-finally or try-with-resources
3. **Use connection pooling** for better performance
4. **Validate JDBC URLs** before attempting connections
5. **Log driver registration** for debugging purposes
