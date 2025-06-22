INSERT INTO teachers (name, surname) VALUES 
('Anna', 'Nowak'),
('Jan', 'Kowalski');

INSERT INTO subjects (subject_name, teacher_id) VALUES 
('Matematyka', 1),
('Fizyka', 1),
('Biologia', 2);

INSERT INTO students (name, surname) VALUES 
('Kasia', 'Zielińska'),     
('Tomek', 'Wójcik'),        
('Ola', 'Kamińska'),        
('Bartek', 'Lewandowski'),  
('Natalia', 'Wiśniewska'),  
('Michał', 'Kaczmarek'),    
('Julia', 'Mazur'),         
('Piotr', 'Dąbrowski');     


INSERT INTO grades (student_id, subject_id, grade, teacher_id) VALUES 
(1, 1, 5.0, 1),
(1, 2, 4.0, 1),
(2, 1, 3.0, 1),
(2, 3, 5.0, 2),
(3, 2, 2.0, 1),
(3, 3, 4.0, 2),
(4, 1, 4.5, 1),
(4, 2, 3.0, 1),
(5, 1, 2.0, 1),
(5, 3, 4.0, 2),
(6, 1, 3.5, 1),
(6, 2, 2.0, 1),
(6, 3, 5.0, 2),
(7, 1, 4.0, 1),
(7, 3, 3.5, 2),
(8, 2, 2.0, 1),
(8, 3, 4.5, 2);

