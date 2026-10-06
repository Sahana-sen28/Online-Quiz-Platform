QUIZMASTER DATABASE Features, Purpose, Uniqueness, and Normalization

1.  PURPOSE OF THE DATABASE

The database is the backend data storage system for the QuizMaster
Online Quiz Platform.

Its main purpose is to store and manage all important information
required by the quiz application. This includes:

-   User accounts and their roles.
-   Quiz categories.
-   Quiz questions and their options.
-   Correct answers and marks.
-   Quiz attempts made by students.
-   Individual answers submitted by students.
-   Scores, percentages, and other result information.

The Java Swing application communicates with the Oracle database through
JDBC. The database therefore provides persistent storage so that
information remains available even after the application is closed.

The database also allows the application to retrieve questions,
authenticate users, store quiz results, display previous attempts, and
support administration of the question bank.

2.  MAIN DATABASE TABLES

The database contains five main tables:

A. USERS

Purpose: Stores information about users of the system.

Important attributes: - USER_ID - unique identifier for each user. -
USERNAME - login name. - PASSWORD_HASH - stored password
representation. - FULL_NAME - user’s name. - ROLE - identifies whether
the user is an ADMIN or STUDENT. - CREATED_AT - records when the account
was created.

Use in the application: - User registration and login. - Identifying
whether the logged-in user is an administrator or student. - Connecting
a student’s account with their quiz attempts.

B. CATEGORIES

Purpose: Stores the available quiz categories.

Examples: - Sports - Geopolitics - Current Affairs - History - Cinema -
Science & Technology - General Knowledge

Important attributes: - CATEGORY_ID - unique identifier. -
CATEGORY_NAME - name of the category.

Use in the application: The category table allows questions to be
organized and allows students to select a particular category or take a
mixed quiz.

C. QUESTIONS

Purpose: Stores the actual questions used in quizzes.

Important attributes: - QUESTION_ID - unique question identifier. -
QUESTION_TEXT - question. - CATEGORY_ID - category to which the question
belongs. - DIFFICULTY - EASY, MEDIUM, or HARD. - OPTION_A, OPTION_B,
OPTION_C, OPTION_D - four answer choices. - CORRECT_OPTION - correct
choice. - MARKS - marks assigned to the question. - ACTIVE_STATUS -
indicates whether the question is active.

Use in the application: - Admin can manage questions. - Students receive
questions when taking a quiz. - Category and difficulty can be used to
filter questions.

D. QUIZ_ATTEMPTS

Purpose: Stores information about each complete quiz attempt made by a
student.

Important attributes include: - ATTEMPT_ID - unique attempt
identifier. - USER_ID - student who took the quiz. - CATEGORY_ID -
selected category, when applicable. - START_TIME and END_TIME - quiz
timing. - TOTAL_QUESTIONS - number of questions. - ATTEMPTED_QUESTIONS -
number answered. - CORRECT_ANSWERS - number answered correctly. -
INCORRECT_ANSWERS - number answered incorrectly. -
UNANSWERED_QUESTIONS - number left unanswered. - TOTAL_MARKS - maximum
marks. - OBTAINED_MARKS - marks obtained. - PERCENTAGE - final
percentage.

Use in the application: Stores the overall result and history of each
quiz attempt.

E. ATTEMPT_ANSWERS

Purpose: Stores the answer given for each individual question during a
quiz attempt.

Important attributes: - ATTEMPT_ANSWER_ID - unique record identifier. -
ATTEMPT_ID - quiz attempt to which the answer belongs. - QUESTION_ID -
question being answered. - SELECTED_OPTION - option selected by the
student. - IS_CORRECT - whether the answer was correct. -
MARKS_OBTAINED - marks obtained for that question.

Use in the application: It provides detailed answer-level information
instead of storing only the final score.

3.  DATABASE RELATIONSHIPS

The main relationships are:

USERS 1-to-many QUIZ_ATTEMPTS

One user can make many quiz attempts, while each quiz attempt belongs to
one user.

CATEGORIES 1-to-many QUESTIONS

One category can contain many questions, while each question belongs to
one category.

QUIZ_ATTEMPTS 1-to-many ATTEMPT_ANSWERS

One quiz attempt can contain many individual answer records.

QUESTIONS 1-to-many ATTEMPT_ANSWERS

A question can appear in many attempts, and each answer record refers to
one question.

CATEGORIES 1-to-many QUIZ_ATTEMPTS

A category can be associated with many quiz attempts.

These relationships are maintained using primary keys and foreign keys.

4.  UNIQUE FEATURES OF THE DATABASE DESIGN

The uniqueness of this database is mainly in how it separates the quiz
system into logical and related entities.

A. Separation of question and attempt data

Questions are stored permanently in QUESTIONS, while a student’s answers
are stored separately in ATTEMPT_ANSWERS.

This means the same question can be used in many different quiz attempts
without duplicating the complete question information each time.

B. Detailed result storage

The database does not store only a final score.

QUIZ_ATTEMPTS stores overall results, while ATTEMPT_ANSWERS stores
question-level results.

Therefore, the application can determine: - Total questions. - Attempted
questions. - Correct answers. - Incorrect answers. - Unanswered
questions. - Marks obtained. - Percentage. - Which option the student
selected for each question.

C. Support for multiple categories

The CATEGORIES table allows questions to be organized into different
subjects or topics.

The application can therefore support: - Category-based quizzes. -
Difficulty-based quizzes. - Mixed quizzes.

For a mixed quiz, Java can select questions from multiple categories
instead of creating a separate “Mixed Quiz” category.

D. Difficulty and active status

Each question has a difficulty level and active status.

This allows the application to select questions based on difficulty and
allows administrators to deactivate questions without deleting their
database records.

E. Separate user roles

The USERS table contains a ROLE field that distinguishes ADMIN and
STUDENT users.

This allows the same database to support both: - Administrative
question/result management. - Student quiz participation and result
history.

F. Persistent quiz history

Quiz attempts remain stored in the database. This allows students to
view previous results and allows administrators to analyze stored
results.

G. Referential integrity

Foreign keys connect the tables. For example, a question cannot
reference a category that does not exist.

This helps maintain consistency between related records.

5.  DATABASE NORMALIZATION

Normalization was performed to reduce data redundancy, avoid unnecessary
repetition, improve data consistency, and make the database easier to
maintain.

The design is normalized up to Third Normal Form (3NF).

6.  FIRST NORMAL FORM (1NF)

The database satisfies 1NF because the attributes contain atomic values
and there are no repeating groups.

For example, QUESTIONS stores separate attributes for: - OPTION_A -
OPTION_B - OPTION_C - OPTION_D

Each field contains one value.

Each table also has a primary key that uniquely identifies each record.

The tables therefore have a clear row-and-column structure without
repeating groups.

7.  SECOND NORMAL FORM (2NF)

The database satisfies 2NF because the tables use single-attribute
primary keys:

-   USERS -> USER_ID
-   CATEGORIES -> CATEGORY_ID
-   QUESTIONS -> QUESTION_ID
-   QUIZ_ATTEMPTS -> ATTEMPT_ID
-   ATTEMPT_ANSWERS -> ATTEMPT_ANSWER_ID

Because the primary keys are single attributes, partial dependency is
not present.

The non-key attributes of each table depend on the primary key of that
table.

8.  THIRD NORMAL FORM (3NF)

The database achieves 3NF by separating independent information into
separate tables and removing transitive dependencies.

Example 1: Categories and Questions

Instead of storing:

QUESTION_ID QUESTION_TEXT CATEGORY_ID CATEGORY_NAME

inside QUESTIONS, the category information is separated:

CATEGORIES - CATEGORY_ID - CATEGORY_NAME

QUESTIONS - QUESTION_ID - QUESTION_TEXT - CATEGORY_ID

The QUESTIONS table stores CATEGORY_ID as a foreign key.

The category name is therefore stored only once in CATEGORIES.

This avoids repeating the same category name for every question in that
category.

Example 2: Users and Quiz Attempts

Instead of storing student details repeatedly for every quiz attempt,
user information is stored in USERS.

QUIZ_ATTEMPTS stores USER_ID as a foreign key.

Therefore:

USERS - USER_ID - USERNAME - FULL_NAME - ROLE

QUIZ_ATTEMPTS - ATTEMPT_ID - USER_ID - START_TIME - END_TIME -
OBTAINED_MARKS - PERCENTAGE

A student who takes many quizzes is still stored only once in USERS.

Example 3: Quiz Attempts and Individual Answers

Overall quiz information is stored in QUIZ_ATTEMPTS, while individual
question answers are stored in ATTEMPT_ANSWERS.

This avoids creating repeated columns such as:

QUESTION1 ANSWER1 QUESTION2 ANSWER2 QUESTION3 ANSWER3

inside QUIZ_ATTEMPTS.

Instead, each answer is represented by a separate ATTEMPT_ANSWERS
record.

9.  WHERE NORMALIZATION IS PERFORMED

Normalization can be clearly seen in the following separations:

USERS | | USER_ID v QUIZ_ATTEMPTS | | ATTEMPT_ID v ATTEMPT_ANSWERS | |
QUESTION_ID v QUESTIONS | | CATEGORY_ID v CATEGORIES

These separate tables ensure that each type of information is stored in
the appropriate place.

For example:

-   User information -> USERS
-   Category information -> CATEGORIES
-   Question information -> QUESTIONS
-   Overall quiz result -> QUIZ_ATTEMPTS
-   Individual answer information -> ATTEMPT_ANSWERS

10. BENEFITS OF NORMALIZATION IN THIS PROJECT

Normalization provides the following benefits:

-   Reduces duplicate data.
-   Prevents unnecessary repetition.
-   Improves data consistency.
-   Makes updates easier.
-   Helps prevent update, insertion, and deletion anomalies.
-   Makes relationships between entities clear.
-   Makes the database easier to maintain.
-   Allows the same question to be used in multiple quiz attempts.
-   Allows one user to have many quiz attempts without duplicating user
    information.
-   Allows detailed answer history to be stored separately from overall
    results.

11. OVERALL DATABASE PURPOSE AND DESIGN SUMMARY

The QuizMaster database is designed as a relational database for an
online quiz platform.

Its main purpose is to provide reliable and persistent storage for
users, questions, categories, quiz attempts, answers, and results.

The database is divided into five logical entities and connected through
primary and foreign keys. The separation of entities reduces redundancy
and supports a clean relational design.

The normalization process is visible mainly in the separation of USERS,
CATEGORIES, QUESTIONS, QUIZ_ATTEMPTS, and ATTEMPT_ANSWERS. The design
satisfies 1NF and 2NF and is organized to achieve 3NF by removing
repeating information and transitive dependencies.

The design also supports important project features such as role-based
users, category-based quizzes, mixed quizzes, difficulty selection,
timed attempts, detailed answer records, result history, and
administrative question management.

In summary, the database is not simply a place to store quiz questions.
It is structured to support the complete lifecycle of the quiz system:
user management, question management, quiz participation, answer
evaluation, result storage, and result analysis.
