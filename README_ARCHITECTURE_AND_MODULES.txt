================================================================================
          QUIZMASTER — ONLINE QUIZ PLATFORM
     ARCHITECTURE, MODULES, AND TECHNOLOGY EXPLAINED (SIMPLE GUIDE)
================================================================================

1. OVERVIEW
--------------------------------------------------------------------------------
QuizMaster is a college mini-project built using Java. It is a desktop 
application that allows students to take online timed quizzes and admins to 
manage quiz questions and review student results. 

The application is built using a "Layered Architecture". This means the project 
is split into simple, organized layers so that code for drawing buttons and 
windows (UI) is kept separate from code for saving data to the database (DB).


2. ARCHITECTURE OVERVIEW (THE LAYERED DESIGN)
--------------------------------------------------------------------------------
The project follows a 4-Layer Architecture:

  +------------------------------------------------------------------+
  |  1. UI LAYER (Java Swing & AWT)                                  |
  |     - Shows windows, forms, buttons, tables, and timers.        |
  +------------------------------------------------------------------+
                                  |
                                  v
  +------------------------------------------------------------------+
  |  2. SERVICE LAYER (Business Logic)                               |
  |     - Calculates quiz scores, calculates percentages, and        |
  |       verifies passwords.                                       |
  +------------------------------------------------------------------+
                                  |
                                  v
  +------------------------------------------------------------------+
  |  3. DAO LAYER (Data Access Objects)                              |
  |     - Prepares SQL queries (SELECT, INSERT, UPDATE, DELETE).     |
  +------------------------------------------------------------------+
                                  |
                                  v
  +------------------------------------------------------------------+
  |  4. JDBC LAYER & ORACLE DATABASE                                 |
  |     - Executes SQL against the Oracle Database and returns data. |
  +------------------------------------------------------------------+


3. BASIC MODULES & TECHNOLOGIES USED (AND WHY THEY ARE USED)
--------------------------------------------------------------------------------

[1] JAVA 17 (CORE PROGRAMMING LANGUAGE)
- What it is: The standard programming language used to write the entire application.
- Why it is used: Java is platform-independent, object-oriented, reliable, and 
  is standard for computer science college projects.

[2] JAVA SWING (GUI WINDOW MODULE)
- What it is: A built-in Java library (`javax.swing.*`) used to create visual 
  desktop applications.
- Key Components Used:
    - JFrame: The main window frames (LoginWindow, QuizWindow, DashboardWindow).
    - JButton: Clickable action buttons (Login, Submit, Next, Previous).
    - JLabel / JTextField / JPasswordField: Labels, text boxes, and password boxes.
    - JRadioButton & ButtonGroup: Multiple-choice option selection (A, B, C, D).
    - JTable & JScrollPane: Displays student results and question lists in neat tables.
    - javax.swing.Timer: Ticks every second to count down the quiz timer.
- Why it is used: It is included directly inside Java, requires no external web 
  browser or web server, and runs fast on Windows desktop computers.

[3] JAVA AWT (ABSTRACT WINDOW TOOLKIT)
- What it is: Java's foundational graphics and layout library (`java.awt.*`).
- Key Components Used:
    - Layout Managers (BorderLayout, FlowLayout, GridLayout): Arranges buttons 
      and labels neatly on screen without manually hardcoding screen coordinates.
    - Color & Font: Customizes colors (blue headers, green buttons, red timer) 
      and typography for a clean modern look.
- Why it is used: Swing relies on AWT for layouts, font styling, and color styling.

[4] JDBC (JAVA DATABASE CONNECTIVITY)
- What it is: A standard Java API (`java.sql.*`) that connects Java programs 
  to relational databases.
- Key Classes Used:
    - Connection: Manages the active line of communication to Oracle DB.
    - PreparedStatement: Sends SQL queries safely using parameters (`?`), preventing 
      SQL Injection security vulnerabilities.
    - ResultSet: Holds the data returned from database SQL SELECT queries.
- Why it is used: It allows Java code to send SQL commands directly to Oracle 
  Database without needing heavy enterprise frameworks.

[5] ORACLE DATABASE (THE BACKEND DATABASE)
- What it is: An enterprise-grade Relational Database Management System (RDBMS).
- Tables Used:
    - USERS: Stores admin and student account credentials.
    - CATEGORIES: Stores quiz subjects (Sports, History, Cinema, Science, etc.).
    - QUESTIONS: Stores questions, multiple-choice options, correct answers, and marks.
    - QUIZ_ATTEMPTS: Stores overall attempt summaries (obtained marks, percentage, dates).
    - ATTEMPT_ANSWERS: Stores detailed student choices for each question.
- Why it is used: It is a fixed, reliable relational database that permanently 
  stores scores and enables deep result analysis.

[6] ORACLE JDBC DRIVER (`ojdbc11.jar`)
- What it is: A software library provided by Oracle that translates Java JDBC 
  calls into network packets that Oracle Database understands.
- Why it is used: Without this JAR driver file, Java cannot communicate over TCP/IP 
  to Oracle Database.

[7] SHA-256 PASSWORD HASHING (`java.security.MessageDigest`)
- What it is: A cryptographic algorithm that turns plain passwords into 
  irreversible 64-character hash strings (e.g. `admin123` -> `240be518...`).
- Why it is used: Plain passwords are never stored in the database, protecting 
  user privacy and following cybersecurity best practices.

[8] APACHE MAVEN (`pom.xml`)
- What it is: A project management and build automation tool.
- Why it is used: Automatically manages external dependencies (like Oracle JDBC driver) 
  and compiles all source files with a single command.


4. SUMMARY OF WHY THIS ARCHITECTURE WAS CHOSEN
--------------------------------------------------------------------------------
1. Simple to Understand: Easy to explain during college viva examinations.
2. Lightweight: Runs locally on any PC without requiring Tomcat, Spring, or internet.
3. Modular: If you change a button in Swing UI, the Database layer remains untouched.
4. Production-Ready: Uses PreparedStatements, Password Hashing, and normalized DB design.
================================================================================
