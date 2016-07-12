# --- !Ups
ALTER TABLE `scalajobs`.`user_profile`
ADD COLUMN `gender` INT(11) NOT NULL DEFAULT 0 AFTER `user_id`;
