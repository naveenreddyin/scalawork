# --- !Ups

create table `CAT` (`NAME` VARCHAR(255) NOT NULL PRIMARY KEY,`COLOR` VARCHAR(50) NOT NULL);

# --- !Downs

drop table `CAT`;
