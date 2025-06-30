SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS `own-02-library`;
CREATE DATABASE `own-02-library`;
USE `own-02-library`;

DROP TABLE IF EXISTS `Reservation`;
DROP TABLE IF EXISTS `Book`;
DROP TABLE IF EXISTS `Location`;
DROP TABLE IF EXISTS `Users`;
DROP TABLE IF EXISTS `Author`;
DROP TABLE IF EXISTS `Role`;

CREATE TABLE `Author` (
  `author_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(100) DEFAULT NULL,
  `last_name` VARCHAR(100) DEFAULT NULL,
  `bio` TEXT DEFAULT NULL,
  `birth_date` DATE DEFAULT NULL,
  `death_date` DATE DEFAULT NULL,
  `nationality` VARCHAR(100) DEFAULT NULL,
  `is_active` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) UNIQUE DEFAULT NULL,
  `email` VARCHAR(100) UNIQUE DEFAULT NULL,
  `password` VARCHAR(255) DEFAULT NULL,
  `registered_date` DATE DEFAULT NULL,
  `enabled` TINYINT(1) DEFAULT 1,
  `account_non_expired` TINYINT(1) DEFAULT 1,
  `account_non_locked` TINYINT(1) DEFAULT 1,
  `credentials_non_expired` TINYINT(1) DEFAULT 1,
  `photo_path` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Location` (
  `location_id` INT NOT NULL AUTO_INCREMENT,
  `shelf` VARCHAR(50) DEFAULT NULL,
  `section` VARCHAR(100) DEFAULT NULL,
  `floor` INT DEFAULT NULL,
  PRIMARY KEY (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Book` (
  `book_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) DEFAULT NULL,
  `isbn` VARCHAR(50) UNIQUE DEFAULT NULL,
  `genre` VARCHAR(50) DEFAULT NULL,
  `published_date` DATE DEFAULT NULL,
  `author_id` INT DEFAULT NULL,
  `location_id` INT DEFAULT NULL,
  `is_active` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`book_id`),
  CONSTRAINT `FK_BOOK_AUTHOR` FOREIGN KEY (`author_id`) REFERENCES `Author` (`author_id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_BOOK_LOCATION` FOREIGN KEY (`location_id`) REFERENCES `Location` (`location_id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Reservation` (
  `reservation_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT DEFAULT NULL,
  `book_id` INT DEFAULT NULL,
  `reserved_at` DATETIME DEFAULT NULL,
  `due_date` DATETIME DEFAULT NULL,
  `returned_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`reservation_id`),
  CONSTRAINT `FK_RESERVATION_USER` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`)
    ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_RESERVATION_BOOK` FOREIGN KEY (`book_id`) REFERENCES `Book` (`book_id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Role` (
  `user_id` INT NOT NULL,
  `role` VARCHAR(50) DEFAULT 'ROLE_USER',
  UNIQUE KEY (`user_id`, `role`),
  CONSTRAINT `FK_ROLE_USER` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `Author` (`first_name`, `last_name`, `bio`, `birth_date`, `death_date`, `nationality`, `is_active`) VALUES
('George', 'Orwell', 'George Orwell was a British writer and journalist known for his novels "1984" and "Animal Farm". His works explore themes of totalitarianism, surveillance, and social injustice. \n\nHe also wrote essays and worked as a critic, often reflecting on class and politics in society.', '1903-06-25', '1950-01-21', 'British', TRUE),
('J.K.', 'Rowling', 'J.K. Rowling is a British author best known for creating the Harry Potter series. Her books have sold millions of copies and been adapted into successful films. \n\nShe also writes crime novels under the pseudonym Robert Galbraith.', '1965-07-31', NULL, 'British', TRUE),
('J.R.R.', 'Tolkien', 'J.R.R. Tolkien was an English author and professor who created the fantasy world of Middle-earth. He is most famous for "The Hobbit" and "The Lord of the Rings" trilogy. \n\nHis works influenced the entire fantasy genre.', '1892-01-03', '1973-09-02', 'British', TRUE),
('Harper', 'Lee', 'Harper Lee was an American novelist best known for "To Kill a Mockingbird". Her novel explores themes of racism and justice in the American South. \n\nIt remains a classic of modern American literature.', '1926-04-28', '2016-02-19', 'American', TRUE),
('Jane', 'Austen', 'Jane Austen was an English novelist known for her insightful portrayals of early 19th-century British society. Her most famous works include "Pride and Prejudice" and "Sense and Sensibility". \n\nHer novels often explore themes of love, class, and morality.', '1775-12-16', '1817-07-18', 'British', TRUE),
('Ernest', 'Hemingway', 'Ernest Hemingway was an American novelist and journalist known for his terse writing style. He wrote classics such as "The Old Man and the Sea" and "A Farewell to Arms". \n\nHis adventurous life also influenced his fiction.', '1899-07-21', '1961-07-02', 'American', TRUE),
('F. Scott', 'Fitzgerald', 'F. Scott Fitzgerald was an American writer most famous for "The Great Gatsby". His works capture the spirit and excess of the Jazz Age. \n\nHe often explored themes of ambition, love, and disillusionment.', '1896-09-24', '1940-12-21', 'American', TRUE),
('Mary', 'Shelley', 'Mary Shelley was a British novelist best known for writing "Frankenstein". She began the novel as part of a storytelling challenge among friends. \n\nHer work is considered a cornerstone of science fiction.', '1797-08-30', '1851-02-01', 'British', TRUE),
('Mark', 'Twain', 'Mark Twain was an American writer and humorist known for "The Adventures of Tom Sawyer" and "Adventures of Huckleberry Finn". His sharp wit and social commentary made him a beloved figure. \n\nHe wrote with insight about American life and culture.', '1835-11-30', '1910-04-21', 'American', TRUE),
('Virginia', 'Woolf', 'Virginia Woolf was an English modernist author known for her experimental narrative techniques. Her novels include "Mrs Dalloway" and "To the Lighthouse". \n\nShe also contributed significantly to feminist literary criticism.', '1882-01-25', '1941-03-28', 'British', TRUE),
('Charles', 'Dickens', 'Charles Dickens was a British author celebrated for his vivid characters and depictions of Victorian society. His works include "Oliver Twist" and "Great Expectations". \n\nHe often highlighted social injustices and the plight of the poor.', '1812-02-07', '1870-06-09', 'British', TRUE),
('Leo', 'Tolstoy', 'Leo Tolstoy was a Russian novelist best known for "War and Peace" and "Anna Karenina". His writing explores themes of morality, spirituality, and Russian society. \n\nHe was also a moral philosopher and reformer.', '1828-09-09', '1910-11-20', 'Russian', TRUE),
('Edgar Allan', 'Poe', 'Edgar Allan Poe was an American writer known for his poetry and tales of mystery. His most famous works include "The Raven" and "The Tell-Tale Heart". \n\nHe helped develop the modern detective story.', '1809-01-19', '1849-10-07', 'American', TRUE),
('Gabriel', 'García Márquez', 'Gabriel García Márquez was a Colombian author and Nobel laureate. He is known for "One Hundred Years of Solitude" and his use of magical realism. \n\nHis works combine the fantastical with political and social commentary.', '1927-03-06', '2014-04-17', 'Colombian', TRUE),
('Herman', 'Melville', 'Herman Melville was an American novelist best known for "Moby-Dick". His writings often explore complex themes of obsession, fate, and human nature. \n\nHe also wrote short stories and poetry.', '1819-08-01', '1891-09-28', 'American', TRUE),
('Jules', 'Verne', 'Jules Verne was a French novelist and a pioneer of science fiction. His best-known works include "Journey to the Center of the Earth" and "Twenty Thousand Leagues Under the Sea". \n\nHe imagined technologies far ahead of his time.', '1828-02-08', '1905-03-24', 'French', TRUE),
('Karel', 'VonTester', 'Poor author. Archived', '2000-02-08', NULL, 'Czech', FALSE);

INSERT INTO `Location` (`shelf`, `section`, `floor`) VALUES
('A1', 'Fiction', 1),
('A2', 'Fantasy', 1),
('B1', 'Children', 1),
('B2', 'Classics', 2),
('C1', 'Mystery', 2),
('C2', 'History', 3),
('D1', 'SciFi', 3),
('D2', 'Biography', 3),
('E1', 'Comics', 1),
('E2', 'Education', 2),
('F1', 'Philosophy', 2),
('F2', 'Poetry', 1);

INSERT INTO `Book`
(`title`, `isbn`, `genre`, `published_date`, `author_id`, `location_id`, `is_active`)
VALUES
('1984', '9780451524935', 'Dystopian', '1949-06-08', 1, 1, TRUE),
('Animal Farm', '9780451526342', 'Political Satire', '1945-08-17', 1, 1, TRUE),
('Harry Potter and the Philosopher''s Stone', '9780747532699', 'Fantasy', '1997-06-26', 2, 3, TRUE),
('Harry Potter and the Chamber of Secrets', '9780747538493', 'Fantasy', '1998-07-02', 2, 3, TRUE),
('The Hobbit', '9780261103344', 'Fantasy', '1937-09-21', 3, 2, TRUE),
('The Lord of the Rings', '9780261103252', 'Fantasy', '1954-07-29', 3, 2, TRUE),
('To Kill a Mockingbird', '9780446310789', 'Drama', '1960-07-11', 4, 4, TRUE),
('Pride and Prejudice', '9780141040349', 'Romance', '1813-01-28', 5, 5, TRUE),
('Emma', '9780141192475', 'Romance', '1815-12-25', 5, 5, TRUE),
('Sense and Sensibility', '9780141199672', 'Romance', '1811-10-30', 5, 5, TRUE),
('Fantastic Beasts', '9781338132311', 'Fantasy', '2016-11-18', 2, 3, FALSE),
('The Silmarillion', '9780618126989', 'Mythology', '1977-09-15', 3, 2, TRUE),
('Coming Up for Air', '9780156196253', 'Fiction', '1939-06-12', 1, 1, TRUE),
('The Casual Vacancy', '9780316228534', 'Drama', '2012-09-27', 2, 3, TRUE),
('Unfinished Tales', '9780345320584', 'Fantasy', '1980-10-02', 3, 2, TRUE),
('Go Set a Watchman', '9780062409850', 'Drama', '2015-07-14', 4, 4, FALSE),
('Lady Susan', '9780140431024', 'Romance', '1871-01-01', 5, 5, TRUE),
('The Old Man and the Sea', '9780684801223', 'Fiction', '1952-09-01', 6, 6, TRUE),
('The Great Gatsby', '9780743273565', 'Tragedy', '1925-04-10', 7, 6, TRUE),
('Frankenstein', '9780486282114', 'Gothic', '1818-01-01', 8, 7, TRUE),
('Adventures of Huckleberry Finn', '9780486280615', 'Adventure', '1884-12-10', 9, 7, TRUE),
('Mrs Dalloway', '9780156628709', 'Modernist', '1925-05-14', 10, 8, TRUE),
('A Tale of Two Cities', '9781853260391', 'Historical Fiction', '1859-04-30', 11, 8, TRUE),
('War and Peace', '9780199232765', 'Historical Fiction', '1869-01-01', 12, 9, TRUE),
('The Raven and Other Poems', '9780140424624', 'Poetry', '1845-01-29', 13, 10, TRUE),
('One Hundred Years of Solitude', '9780060883287', 'Magic Realism', '1967-05-30', 14, 10, TRUE),
('Moby-Dick', '9781503280786', 'Adventure', '1851-11-14', 15, 11, TRUE),
('Journey to the Center of the Earth', '9780451532152', 'Science Fiction', '1864-11-25', 16, 11, TRUE),
('Twenty Thousand Leagues Under the Sea', '9780451531698', 'Science Fiction', '1870-06-20', 16, 11, TRUE),
('The Call of the Wild', '9781505255607', 'Adventure', '1903-01-01', 9, 7, TRUE),
('Poems by Edgar Allan Poe', '9780486282404', 'Poetry', '1845-01-29', 13, 10, TRUE),
('The Complete Short Stories of Ernest Hemingway', '9780684801224', 'Short Stories', '1938-01-01', 6, 6, TRUE);

INSERT INTO `Users` (`username`, `email`, `password`, `registered_date`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES
('john_doe', 'john@example.com', '$2a$12$VxxDeLr/5nax2b3ayef6/OCQ8C0nFenkAbaaLiTSq3wNU1dpnQRam', '2022-01-15', 1, 1, 1, 1),
('alice_w', 'alice@example.com', '$2a$10$PRXz5ZyWnQ.HCDOriGb9tuMOLIL4oqsv.exjjZYGUxYAQja8EL.VS', '2022-03-22', 1, 1, 1, 1),
('bob_smith', 'bob@example.com', '$2a$10$PRXz5ZyWnQ.HCDOriGb9tuMOLIL4oqsv.exjjZYGUxYAQja8EL.VS', '2022-05-10', 1, 1, 1, 1),
('charlie_b', 'charlie@example.com', '$2a$10$PRXz5ZyWnQ.HCDOriGb9tuMOLIL4oqsv.exjjZYGUxYAQja8EL.VS', '2023-01-01', 1, 1, 1, 1),
('emily_r', 'emily@example.com', '$2a$10$PRXz5ZyWnQ.HCDOriGb9tuMOLIL4oqsv.exjjZYGUxYAQja8EL.VS', '2023-02-20', 1, 1, 1, 1),
('chad_d', 'chad@example.com', '$2a$10$PRXz5ZyWnQ.HCDOriGb9tuMOLIL4oqsv.exjjZYGUxYAQja8EL.VS', '2024-02-20', 1, 1, 1, 1);

INSERT INTO `Role` (`user_id`, `role`) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER'),
(3, 'ROLE_USER'),
(4, 'ROLE_USER'),
(5, 'ROLE_USER');

INSERT INTO `Reservation` (`user_id`, `book_id`, `reserved_at`, `due_date`, `returned_at`) VALUES
(1, 1, '2024-01-01 10:00:00', '2024-01-15 10:00:00', '2024-01-12 11:00:00'),
(2, 4, '2024-02-05 12:00:00', '2024-02-20 12:00:00', '2024-02-19 14:00:00'),
(1, 17, '2024-05-01 10:00:00', '2024-05-15 10:00:00', '2024-05-13 09:00:00'),
(3, 19, '2024-05-03 14:00:00', '2024-05-17 14:00:00', '2024-05-16 16:00:00'),
(1, 18, '2024-05-10 15:00:00', '2024-05-24 15:00:00', '2024-05-22 14:30:00'),
(3, 17, '2024-05-12 10:00:00', '2024-05-26 10:00:00', '2024-05-23 09:30:00'),
(2, 23, '2024-05-16 10:30:00', '2024-05-30 10:30:00', '2024-05-29 11:00:00'),
(5, 24, '2024-05-19 15:30:00', '2024-06-02 15:30:00', '2024-06-01 14:00:00'),
(2, 25, '2024-05-21 11:30:00', '2024-06-04 11:30:00', '2024-06-03 10:00:00'),
(4, 26, '2024-05-23 13:00:00', '2024-06-06 13:00:00', '2024-06-05 09:00:00'),
(1, 2, '2025-05-09 12:36:23', '2025-06-07 10:07:39', NULL),
(1, 3, '2025-05-05 21:45:58', '2025-05-30 05:34:13', NULL),
(2, 5, '2025-05-14 19:52:24', '2025-05-28 19:22:46', NULL),
(3, 6, '2025-05-13 08:54:32', '2025-06-05 20:04:35', NULL),
(3, 7, '2025-05-13 08:28:59', '2025-05-24 05:14:38', NULL),
(4, 8, '2025-05-16 21:56:10', '2025-05-27 17:23:24', NULL),
(4, 9, '2025-05-08 20:32:15', '2025-05-29 17:49:17', NULL),
(4, 10, '2025-05-08 00:36:48', '2025-05-27 12:27:24', NULL),
(5, 11, '2025-05-06 16:32:38', '2025-05-22 03:53:23', NULL),
(5, 12, '2025-05-09 23:43:00', '2025-05-29 04:57:31', NULL),
(5, 1, '2025-05-16 01:31:18', '2025-05-30 17:56:54', NULL),
(2, 2, '2025-05-14 10:34:31', '2025-06-07 11:37:20', NULL),
(3, 3, '2025-05-10 11:11:54', '2025-05-27 21:53:32', NULL),
(2, 18, '2025-05-09 17:33:15', '2025-06-06 14:50:46', NULL),
(4, 20, '2025-05-16 01:21:46', '2025-05-28 00:01:57', NULL),
(5, 21, '2025-05-08 08:23:44', '2025-05-27 20:49:51', NULL),
(2, 20, '2025-05-13 09:58:31', '2025-06-06 07:26:38', NULL),
(4, 19, '2025-05-06 06:22:18', '2025-05-13 14:05:02', NULL),
(5, 21, '2025-05-16 06:43:47', '2025-05-26 13:46:41', NULL),
(1, 22, '2025-05-05 02:57:29', '2025-05-28 00:14:28', NULL),
(3, 22, '2025-05-15 10:15:51', '2025-06-09 02:08:53', NULL),
(4, 24, '2025-05-11 00:18:18', '2025-05-29 16:53:13', NULL),
(1, 25, '2025-05-06 05:16:57', '2025-06-01 01:33:23', NULL),
(3, 26, '2025-05-06 21:59:16', '2025-05-23 11:02:00', NULL),
(5, 27, '2025-05-07 04:52:39', '2025-06-06 04:27:29', NULL);

SET FOREIGN_KEY_CHECKS = 1;
