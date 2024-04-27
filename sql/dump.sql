-- Insert sample data into Classes table
INSERT INTO Classes (course_number, term, section_number, description)
VALUES
    ('CS101', 'Spring', 101, 'Introduction to Computer Science'),
    ('CS201', 'Fall', 201, 'Data Structures'),
    ('CS301', 'Spring', 301, 'Algorithm Design');

-- Insert sample data into Categories table
INSERT INTO Categories (class_id, name, weight)
VALUES
    (1, 'Homework', 0.3),
    (1, 'Exams', 0.5),
    (2, 'Lab Assignments', 0.4),
    (2, 'Projects', 0.6),
    (3, 'Quizzes', 0.3),
    (3, 'Midterm', 0.4),
    (3, 'Final', 0.3);

-- Insert sample data into Assignments table
INSERT INTO Assignments (class_id, category_id, name, description, point_value)
VALUES
    (1, 1, 'Homework 1', 'Complete exercises 1-5', 100),
    (1, 1, 'Homework 2', 'Complete exercises 6-10', 100),
    (2, 3, 'Lab Assignment 1', 'Implement linked list', 150),
    (2, 3, 'Lab Assignment 2', 'Implement binary search tree', 150),
    (3, 5, 'Quiz 1', 'Covering chapters 1-3', 50),
    (3, 6, 'Midterm', 'Chapters 1-5', 100),
    (3, 7, 'Final', 'Comprehensive', 200);

-- Insert sample data into Students table
INSERT INTO Students (username, firstName, lastName)
VALUES
    ('student1', 'John', 'Doe'),
    ('student2', 'Jane', 'Smith'),
    ('student3', 'Alice', 'Johnson');

-- Insert sample data into Enrolled table
INSERT INTO Enrolled (student_id, class_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 2);

-- Insert sample data into Grades table
INSERT INTO Grades (student_id, assignment_id, grade)
VALUES
    (1, 1, 90),
    (1, 2, 85),
    (2, 1, 95),
    (2, 2, 80),
    (3, 3, 140),
    (3, 4, 135),
    (3, 5, 40),
    (3, 6, 90),
    (3, 7, 180);
