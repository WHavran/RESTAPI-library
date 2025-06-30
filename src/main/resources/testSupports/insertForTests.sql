INSERT INTO `Author` (`first_name`, `last_name`, `bio`, `birth_date`, `death_date`, `nationality`, `is_active`) VALUES
('George', 'Orwell', 'George Orwell was a British writer and journalist known for his novels "1984" and "Animal Farm". His works explore themes of totalitarianism, surveillance, and social injustice. \n\nHe also wrote essays and worked as a critic, often reflecting on class and politics in society.', '1903-06-25', '1950-01-21', 'British', TRUE),
('J.K.', 'Rowling', 'J.K. Rowling is a British author best known for creating the Harry Potter series. Her books have sold millions of copies and been adapted into successful films. \n\nShe also writes crime novels under the pseudonym Robert Galbraith.', '1965-07-31', NULL, 'British', TRUE);

INSERT INTO `Book`
(`title`, `isbn`, `genre`, `published_date`, `author_id`, `location_id`, `is_active`) VALUES
('1984', '9780451524935', 'Dystopian', '1949-06-08', 1, 1, TRUE),
('Animal Farm', '9780451526342', 'Political Satire', '1945-08-17', 1, 1, TRUE),
('Harry Potter and the Philosopher''s Stone', '9780747532699', 'Fantasy', '1997-06-26', 2, 3, TRUE),
('Harry Potter and the Chamber of Secrets', '9780747538493', 'Fantasy', '1998-07-02', 2, 3, TRUE),

INSERT INTO `Location` (`shelf`, `section`, `floor`) VALUES
('A1', 'Fiction', 1),
('B1', 'Children', 1);

INSERT INTO `Reservation` (`user_id`, `book_id`, `reserved_at`, `due_date`, `returned_at`) VALUES
(1, 1, '2024-01-01 10:00:00', '2024-01-15 10:00:00', '2024-01-12 11:00:00'),
(1, 1, '2025-05-09 12:36:23', '2025-06-07 10:07:39', NULL),
(1, 3, '2025-05-05 21:45:58', '2025-05-30 05:34:13', NULL);

INSERT INTO `User` (`username`, `email`, `password`, `registered_date`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES
('john_doe', 'john@example.com', '$2a$10$PRXz5ZyWnQ.HCDOriGb9tuMOLIL4oqsv.exjjZYGUxYAQja8EL.VS', '2022-01-15', 1, 1, 1, 1);

INSERT INTO `Role` (`user_id`, `role`) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER');