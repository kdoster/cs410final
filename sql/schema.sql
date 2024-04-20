CREATE DATABASE IF NOT EXISTS CS410Final; 
USE CS410Final; 

DROP TABLE IF EXISTS Class; 
DROP TABLE IF EXISTS Categories;
DROP TABLE IF EXISTS Assignments;
DROP TABLE IF EXISTS Students;
DROP TABLE IF EXISTS Enrolled;
DROP TABLE IF EXISTS Grades;

CREATE TABLE Classes (
    class_id INT PRIMARY KEY AUTO_INCREMENT,
    course_number VARCHAR(10) NOT NULL,
    term VARCHAR(10) NOT NULL,
    section_number INT NOT NULL,
    description TEXT
);

CREATE TABLE Categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    class_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    weight FLOAT NOT NULL,
    FOREIGN KEY (class_id) REFERENCES Classes(class_id)
);

CREATE TABLE Assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    class_id INT NOT NULL,
    category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    point_value INT NOT NULL,
    FOREIGN KEY (class_id) REFERENCES Classes(class_id),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);

CREATE TABLE Students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Enrolled (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    class_id INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (class_id) REFERENCES Classes(class_id)
);

CREATE TABLE Grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    assignment_id INT NOT NULL,
    grade FLOAT,
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (assignment_id) REFERENCES Assignments(assignment_id)
);
