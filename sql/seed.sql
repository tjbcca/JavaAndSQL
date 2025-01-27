INSERT INTO Books(BookID,BookName, Author) VALUES
(1,'To Kill a Mockingbird', 'Harper Lee'),
(2,'1984', 'George Orwell'),
(3,'The Great Gatsby', 'F. Scott Fitzgerald'),
(4, 'The Catcher in the Rye', 'J.D. Salinger'),
(5, 'Moby-Dick', 'Herman Melville');

INSERT INTO Members(MemberName, BookID) VALUES
('John Doe', 1),
('Jane Doe', NULL),
('Don Quixote', 3),
('Jim Henson', NULL),
('Austin Powers', 2);