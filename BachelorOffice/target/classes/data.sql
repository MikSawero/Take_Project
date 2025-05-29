-- Wstaw nauczycieli
INSERT INTO teachers (name, surname) VALUES 
('Anna', 'Nowak'),
('Jan', 'Kowalski');

-- Wstaw przedmioty (zakładamy, że nauczyciele mają ID 1 i 2)
INSERT INTO subjects (subject_name, teacher_id) VALUES 
('Matematyka', 1),
('Fizyka', 1),
('Biologia', 2);

-- Wstaw studentów
INSERT INTO students (name, surname) VALUES 
('Kasia', 'Zielińska'),
('Tomek', 'Wójcik'),
('Ola', 'Kamińska');

-- Wstaw oceny (zakładamy, że uczniowie mają ID 1, 2, 3, a przedmioty 1, 2, 3)
INSERT INTO grades (student_id, subject_id, grade, teacher_id) VALUES 
(1, 1, 5, 1),
(1, 2, 4, 1),
(2, 1, 3, 1),
(2, 3, 5, 2),
(3, 2, 2, 1),
(3, 3, 4, 2);

