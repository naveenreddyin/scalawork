# --- !Ups

CREATE TABLE `scalajobs`.`users` (
  `uid` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL COMMENT '	',
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC));


# --- !Downs

DROP TABLE `users`;