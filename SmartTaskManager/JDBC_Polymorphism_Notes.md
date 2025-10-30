# JDBC Structure and Driver Connection 

Understanding how JDBC connects your Java program to the database through drivers using OOP principles.

## 🧩 Big Picture (3 layers)
```
Your Java Program
        │
        ▼
JDBC API (Interfaces)  ← provided by Java (java.sql package)
        │
        ▼
JDBC Driver (Classes)  ← provided by vendor (e.g., PostgreSQL, MySQL)
        │
        ▼
Database (e.g., PostgreSQL Server)
```

---

## ⚙️ Step-by-step OOP Structure

### 1️⃣ JDBC Core Interfaces (provided by Java)
Think of these as contracts — they define what can be done, not how.

```java
// JDBC API (java.sql package)
public interface Driver {
    Connection connect(String url, Properties info);
}

public interface Connection {
    Statement createStatement();
}

public interface Statement {
    ResultSet executeQuery(String sql);
}

public interface ResultSet {
    boolean next();
    String getString(String columnLabel);
}
```

**🧠 OOP Concept:**
- These are interfaces, not actual code.
- Vendors (like PostgreSQL) provide implementations of these interfaces.

### 2️⃣ JDBC Driver Classes (provided by the vendor)
Example: PostgreSQL JDBC Driver (like postgresql-42.x.jar)

```java
// Vendor-specific implementation
public class PostgreSQLDriver implements Driver {
    @Override
    public Connection connect(String url, Properties info) {
        return new PostgreSQLConnection(url, info);
    }
}

public class PostgreSQLConnection implements Connection {
    @Override
    public Statement createStatement() {
        return new PostgreSQLStatement();
    }
}

public class PostgreSQLStatement implements Statement {
    @Override
    public ResultSet executeQuery(String sql) {
        return new PostgreSQLResultSet();
    }
}

public class PostgreSQLResultSet implements ResultSet {
    @Override
    public boolean next() {
        // moves to next row in the result
        return true;
    }

    @Override
    public String getString(String columnLabel) {
        // returns value from column
        return "value";
    }
}
```

**🧠 OOP Concept:**
- Each class implements a JDBC interface.
- Your program never directly talks to these classes — it only knows the interfaces.

### 3️⃣ DriverManager — The Connector Between Your Code and Driver

```java
public class DriverManager {
    private static List<Driver> registeredDrivers = new ArrayList<>();

    public static void registerDriver(Driver d) {
        registeredDrivers.add(d);
    }

    public static Connection getConnection(String url, String user, String password) {
        for (Driver d : registeredDrivers) {
            Connection conn = d.connect(url, new Properties());
            if (conn != null) return conn;
        }
        throw new SQLException("No suitable driver found");
    }
}
```

**🧠 OOP Concept:**
- **DriverManager acts as a Factory / Mediator.**
- **It holds all registered drivers.**
- **When you ask for a connection, it finds a driver that can handle your DB URL.**

---

## 💻 How Your Java Code Uses All This

```java
public class Demo {
    public static void main(String[] args) throws Exception {
        // Step 1: Load driver (registers itself)
        Class.forName("org.postgresql.Driver"); 

        // Step 2: Get connection
        Connection conn = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/testdb", "user", "password"
        );

        // Step 3: Create statement
        Statement stmt = conn.createStatement();

        // Step 4: Execute query
        ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

        // Step 5: Read results
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
    }
}
```

---

## 🧭 Diagram (Simplified Flow)
```
Your Program
   │
   │ uses
   ▼
DriverManager (Factory)
   │
   │ finds and calls
   ▼
PostgreSQLDriver (implements Driver)
   │
   │ returns
   ▼
PostgreSQLConnection (implements Connection)
   │
   │ creates
   ▼
PostgreSQLStatement (implements Statement)
   │
   │ executes SQL, gets data
   ▼
PostgreSQLResultSet (implements ResultSet)
```

---

## 🧱 Summary (OOP Mapping)

| OOP Concept | JDBC Element | Description |
|-------------|--------------|-------------|
| **Interface** | Connection, Statement, ResultSet, Driver | Define what operations are available |
| **Implementation Class** | PostgreSQLConnection, etc. | Concrete logic provided by the vendor |
| **Factory Pattern** | DriverManager | Creates correct connection |
| **Polymorphism** | Using Connection instead of PostgreSQLConnection | Allows switching DB easily |
| **Encapsulation** | Database logic hidden inside driver | Your program doesn't care how it works |

---

## 🔑 Key Takeaways

1. **Java provides the interfaces** (contracts) in the `java.sql` package
2. **Database vendors provide the implementations** (actual working code)
3. **DriverManager acts as a bridge** between your code and the vendor's driver
4. **Your code only knows about interfaces**, making it database-independent
5. **Polymorphism allows easy database switching** without changing your code