-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
    'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema chickenfeed
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `accomplishment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `accomplishment`
(
  `round` INT(11) NOT NULL,
  PRIMARY KEY (`round`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ticket`
(
  `ticket_no`   BIGINT(20)   NOT NULL,
  `issue_time`  DATETIME(6)  NULL DEFAULT NULL,
  `ranking`     VARCHAR(20)      NULL DEFAULT NULL,
  `round`       INT(11)      NULL DEFAULT NULL,
  `ticket_type` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`ticket_no`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `ball`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ball`
(
  `ticket_no` BIGINT(20) NOT NULL,
  `manual`    BIT(1)     NULL DEFAULT NULL,
  `number`    INT(11)    NULL DEFAULT NULL,
  INDEX (`ticket_no`),
  FOREIGN KEY (`ticket_no`)
    REFERENCES `ticket` (`ticket_no`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `hibernate_sequences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hibernate_sequences`
(
  `sequence_name` VARCHAR(255) NOT NULL,
  `next_val`      BIGINT(20)   NULL DEFAULT NULL,
  PRIMARY KEY (`sequence_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `hits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hits`
(
  `round` INT(11) NOT NULL,
  `hit`   INT(11) NULL DEFAULT NULL,
  `rank`  INT(11) NOT NULL,
  PRIMARY KEY (`round`, `rank`),
  FOREIGN KEY (`round`)
    REFERENCES `accomplishment` (`round`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `win`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `win`
(
  `round`        INT(11)    NOT NULL,
  `bonus_number` INT(11)    NULL DEFAULT NULL,
  `draw_date`    DATE       NULL DEFAULT NULL,
  `numbers`      TINYBLOB   NULL DEFAULT NULL,
  `total_prize`  BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`round`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `prizes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prizes`
(
  `round`  INT(11)    NOT NULL,
  `prizes` BIGINT(20) NULL DEFAULT NULL,
  `rank`   INT(11)    NOT NULL,
  PRIMARY KEY (`round`, `rank`),
  FOREIGN KEY (`round`)
    REFERENCES `win` (`round`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_unicode_ci;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
