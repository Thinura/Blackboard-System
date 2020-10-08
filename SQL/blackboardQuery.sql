CREATE SCHEMA IF NOT EXISTS `blackboard_db` DEFAULT CHARACTER SET utf8 ;
USE `blackboard_db` ;

-- -------- Admin Table ----------
CREATE TABLE IF NOT EXISTS `blackboard_db`.`admin`(
`admin_id` INT NOT NULL,
`admin_name` varchar(255) NOT NULL,
`admin_address` varchar(255) NOT NULL,
`admin_contact_numnber` INT NOT NULL,
`admin_age` INT NULL,
`admin_password` varchar(255) NOT NULL,
PRIMARY KEY (admin_id)
);

-- -------- Student Table ----------
CREATE TABLE IF NOT EXISTS `blackboard_db`.`student`(
`student_id` INT NOT NULL,
`student_name` varchar(255) NOT NULL,
`student_address` varchar(255) NOT NULL,
`student_contact_numnber` INT NULL,
`student_age` INT NULL,
`student_password` varchar(255) NOT NULL,
`admin_id` INT,
PRIMARY KEY (student_id),
FOREIGN KEY (admin_id) REFERENCES admin(admin_id)
);

-- -------- Module Table ----------
CREATE TABLE IF NOT EXISTS `blackboard_db`.`module`(
`module_id` INT NOT NULL,
`module_name` varchar(255) NOT NULL,
`admin_id` INT,
PRIMARY KEY (module_id),
FOREIGN KEY (admin_id) REFERENCES admin(admin_id)
);

-- -------- Module_Student Table ----------
CREATE TABLE IF NOT EXISTS `blackboard_db`.`module_student`(
`module_student_id` INT AUTO_INCREMENT,
`student_id` INT,
`module_id` INT,
PRIMARY KEY (module_student_id),
FOREIGN KEY (student_id) REFERENCES student(student_id),
FOREIGN KEY (module_id) REFERENCES module(module_id)
);

-- -------- Instructor Table ----------
CREATE TABLE IF NOT EXISTS `blackboard_db`.`instructor`(
`instructor_id` INT NOT NULL,
`instructor_name` varchar(255) NOT NULL,
`instructor_address` varchar(255) NOT NULL,
`instructor_contact_numnber` INT NULL,
`instructor_age` INT NULL,
`instructor_password` varchar(255) NOT NULL,
`admin_id` INT,
PRIMARY KEY (instructor_id),
FOREIGN KEY (admin_id) REFERENCES admin(admin_id)
);

-- -------- Module_Instructor Table ----------
CREATE TABLE IF NOT EXISTS `blackboard_db`.`module_instructor`(
`module_instructor_id` INT AUTO_INCREMENT,
`instructor_id` INT,
`module_id` INT,
PRIMARY KEY (module_instructor_id),
FOREIGN KEY (instructor_id) REFERENCES instructor(instructor_id),
FOREIGN KEY (module_id) REFERENCES module(module_id)
);

-- -------- Coursework Table ----------
CREATE TABLE IF NOT EXISTS `blackboard_db`.`coursework`(
`coursework_id` INT NOT NULL,
`coursework_name` VARCHAR(255) NOT NULL,
`coursework_content` BLOB NOT NULL,
`admin_id` INT,
`module_id` INT,
PRIMARY KEY (coursework_id),
FOREIGN KEY (module_id) REFERENCES module(module_id),
FOREIGN KEY (admin_id) REFERENCES admin(admin_id)
);