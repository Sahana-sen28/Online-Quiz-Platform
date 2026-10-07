# QuizMaster — Online Quiz Platform

## 1. Project Overview
- Java Swing/AWT desktop application for online quizzes
- Admins add/manage questions, students take timed quizzes
- JDBC connects to Oracle Database
- College mini-project demonstrating Swing, JDBC, Oracle, layered architecture

## 2. Technology Stack
- Java 17, Swing/AWT, JDBC, Oracle Database, Oracle JDBC Driver (ojdbc11), Maven

## 3. Architecture
The application follows a layered architecture pattern:
```
Java Swing Frontend → Service Layer → DAO Layer → JDBC → Oracle Database
```
- **UI Layer:** Swing frames for user interaction
- **Service Layer:** Business logic, validation, quiz scoring, transactions
- **DAO Layer:** Database operations using PreparedStatement
- **JDBC:** Connection to Oracle via ojdbc driver

## 4. Database Requirements
- Oracle database must already exist with these 5 tables:
  - `USERS` (USER_ID, USERNAME, PASSWORD_HASH, FULL_NAME, ROLE, CREATED_AT)
  - `CATEGORIES` (CATEGORY_ID, CATEGORY_NAME)
  - `QUESTIONS` (QUESTION_ID, QUESTION_TEXT, CATEGORY_ID, DIFFICULTY, OPTION_A-D, CORRECT_OPTION, MARKS, ACTIVE_STATUS)
  - `QUIZ_ATTEMPTS` (ATTEMPT_ID, USER_ID, CATEGORY_ID nullable, START_TIME, END_TIME, TOTAL_QUESTIONS, ATTEMPTED_QUESTIONS, CORRECT_ANSWERS, INCORRECT_ANSWERS, UNANSWERED_QUESTIONS, TOTAL_MARKS, OBTAINED_MARKS, PERCENTAGE)
  - `ATTEMPT_ANSWERS` (ATTEMPT_ANSWER_ID, ATTEMPT_ID, QUESTION_ID, SELECTED_OPTION nullable, IS_CORRECT nullable, MARKS_OBTAINED)
- **ER Relationships:**
  - `USERS` (1) to (M) `QUIZ_ATTEMPTS`
  - `CATEGORIES` (1) to (M) `QUESTIONS`
  - `CATEGORIES` (1) to (M) `QUIZ_ATTEMPTS`
  - `QUIZ_ATTEMPTS` (1) to (M) `ATTEMPT_ANSWERS`
  - `QUESTIONS` (1) to (M) `ATTEMPT_ANSWERS`
- This application does NOT create or modify tables

## 5. Oracle Setup Verification
1. Open Oracle SQL Developer or SQL*Plus
2. Connect to your Oracle database
3. Run: `SELECT TABLE_NAME FROM USER_TABLES;`
4. Verify all 5 tables exist
5. Run: `SELECT * FROM CATEGORIES;` to verify categories
6. Run: `SELECT COUNT(*) FROM QUESTIONS;` to verify questions exist
7. Find your service name: check `tnsnames.ora` or use `lsnrctl status`

## 6. JDBC Driver Setup
- Maven automatically downloads `ojdbc11` from Maven Central
- No manual JAR download needed
- If Maven is unavailable, download `ojdbc11.jar` from Oracle and add to classpath

## 7. Configure Database Connection
- Open: `src/main/java/quizmaster/util/AppConfig.java`
- Change these three values:
  ```java
  DB_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";  // Change service name if needed
  DB_USERNAME = "your_oracle_username";
  DB_PASSWORD = "your_oracle_password";
  ```
- Common JDBC URL formats:
  - `jdbc:oracle:thin:@localhost:1521/XEPDB1` (pluggable database)
  - `jdbc:oracle:thin:@localhost:1521/XE` (Express Edition)
  - `jdbc:oracle:thin:@localhost:1521:XE` (SID format)
  - `jdbc:oracle:thin:@localhost:1521/ORCL` (Enterprise)
- The service name depends on YOUR Oracle installation

## 8. How Frontend Connects to Backend
**Example 1: Student Login**
1. Student enters username/password in `LoginFrame` (Swing UI)
2. `LoginFrame` calls `AuthenticationService.login(username, password)`
3. `AuthenticationService` hashes password using `PasswordUtil.hashPassword()`
4. `AuthenticationService` calls `UserDAO.findByUsername(username)`
5. `UserDAO` creates PreparedStatement: `SELECT * FROM USERS WHERE USERNAME = ?`
6. JDBC sends query to Oracle via the connection from `DBConnection.getConnection()`
7. Oracle returns result set
8. `UserDAO` maps result to `User` object
9. `AuthenticationService` compares hashes
10. If match → returns `User`; UI opens appropriate dashboard

**Example 2: Quiz Submission**
1. Student clicks Submit in `QuizFrame`
2. `QuizFrame` calls `QuizService.submitQuiz()` with answers
3. `QuizService` calculates scores (correct, incorrect, unanswered, marks, percentage)
4. `QuizService` opens a JDBC connection with auto-commit OFF
5. Inserts `QUIZ_ATTEMPTS` record
6. Inserts all `ATTEMPT_ANSWERS` records
7. Calls COMMIT (or ROLLBACK on failure)
8. Returns `QuizAttempt` object
9. `QuizFrame` opens `ResultFrame` showing scores

## 9. How to Run
### Prerequisites
- Java 17 or newer (JDK)
- Apache Maven 3.6+
- Oracle Database with tables created
- Oracle credentials

### Steps
1. Open terminal in project directory
2. Edit `AppConfig.java` with your Oracle credentials
3. Ensure Oracle Database is running
4. Compile: `mvn clean compile`
5. Run: `mvn exec:java`
6. Login screen appears

### Alternative (without Maven)
1. Download `ojdbc11.jar`
2. Compile: `javac -cp ojdbc11.jar -d out src/main/java/quizmaster/**/*.java`
3. Run: `java -cp out;ojdbc11.jar quizmaster.Main`

## 10. Password Handling (IMPORTANT)
- This application uses SHA-256 hashing for passwords
- New registrations automatically hash passwords
- EXISTING test accounts in the database may have PLAIN TEXT passwords
- To convert existing test passwords to SHA-256 format:
  1. Run: `mvn exec:java -Dexec.mainClass="quizmaster.util.PasswordUtil"`
  2. Enter the plain-text password
  3. Copy the SHA-256 hash
  4. Update in Oracle: `UPDATE USERS SET PASSWORD_HASH = '<hash>' WHERE USERNAME = '<username>';`
  5. `COMMIT;`
- Alternatively, register a new account through the application (this automatically hashes)

## 11. First-Time Testing
### Admin Testing
1. Login with admin credentials
2. Click "Manage Questions"
3. Add a new question with all fields
4. Edit the question
5. Search/filter questions
6. View Results page
7. View Statistics page

### Student Testing
1. Click "Register" from login screen
2. Create a new student account
3. Login with new account
4. Click "Start Quiz"
5. Select category, difficulty, number of questions
6. Take the timed quiz
7. Submit and view results
8. Check "My Results" for history

## 12. Common Errors and Fixes
- **ORA-01017:** Invalid username/password → Check DB_USERNAME and DB_PASSWORD
- **ORA-12514:** Listener does not know of service → Check service name in DB_URL
- **ORA-12541:** No listener → Oracle Listener not running, start it: `lsnrctl start`
- **ORA-17002:** IO Error → Oracle DB not running or wrong host/port
- **ClassNotFoundException: oracle.jdbc.OracleDriver** → JDBC driver not on classpath, run with Maven
- **No suitable driver** → Same as above
- **Connection refused** → Oracle not running or firewall blocking port 1521
- **Table or view does not exist** → Tables not created in the correct schema
- **Foreign key violation** → Trying to insert with invalid CATEGORY_ID or USER_ID
- **Not enough questions** → Add more questions to the selected category/difficulty

## 13. Demo Flow (5-10 minutes)
1. **Start** the application (`mvn exec:java`)
2. **Admin Login** with admin credentials
3. **Show Question Management** — demonstrate Add, Edit, filter by category
4. **Show Results** — view student attempt history
5. **Show Statistics** — total attempts, averages, category breakdown
6. **Logout**
7. **Student Registration** — create new student account
8. **Student Login**
9. **Start Quiz** — select category (e.g., Science & Technology), MEDIUM difficulty, 5 questions
10. **Take Quiz** — answer questions, show timer counting down, use Previous/Next
11. **Submit Quiz** — show result screen
12. **View History** — show past attempts in table
13. **Show Oracle** — run SELECT queries to show data stored in database


## 14. Project Structure
```text
quizmaster/
├── pom.xml                     # Maven configuration and dependencies
├── src/
│   └── main/
│       ├── java/
│       │   └── quizmaster/
│       │       ├── Main.java                 # Entry point
│       │       ├── dao/                      # Data Access Objects (DB operations)
│       │       │   ├── UserDAO.java
│       │       │   ├── CategoryDAO.java
│       │       │   ├── QuestionDAO.java
│       │       │   ├── QuizAttemptDAO.java
│       │       │   └── AttemptAnswerDAO.java
│       │       ├── model/                    # Data models (Entities)
│       │       │   ├── User.java
│       │       │   ├── Category.java
│       │       │   ├── Question.java
│       │       │   ├── QuizAttempt.java
│       │       │   └── AttemptAnswer.java
│       │       ├── service/                  # Business Logic
│       │       │   ├── AuthenticationService.java
│       │       │   └── QuizService.java
│       │       ├── ui/                       # Swing UI Frames
│       │       │   ├── LoginFrame.java
│       │       │   ├── AdminDashboard.java
│       │       │   ├── StudentDashboard.java
│       │       │   ├── QuizFrame.java
│       │       │   └── ... (other UI components)
│       │       └── util/                     # Utilities
│       │           ├── AppConfig.java        # DB credentials
│       │           ├── DBConnection.java     # JDBC connection logic
│       │           └── PasswordUtil.java     # SHA-256 hashing
│       └── resources/
│           └── (images, icons, if any)
```
