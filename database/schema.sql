CREATE TABLE `Unit`
(
    `Id`    BIGINT      NOT NULL AUTO_INCREMENT,
    `Title` varchar(50) NOT NULL UNIQUE,
    PRIMARY KEY (`Id`)
);

CREATE TABLE `Account`
(
    `Id`       BIGINT       NOT NULL AUTO_INCREMENT,
    `Email`    varchar(255) NOT NULL UNIQUE,
    `Password` varchar(255) NOT NULL,
    PRIMARY KEY (`Id`)
);

CREATE TABLE `Role`
(
    `Id`    BIGINT       NOT NULL AUTO_INCREMENT,
    `Title` varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`Id`)
);

CREATE TABLE `Account_Role`
(
    `AccountId` BIGINT NOT NULL,
    `RoleId`    BIGINT NOT NULL,
    PRIMARY KEY (`AccountId`, `RoleId`)
);

CREATE TABLE `WorkType`
(
    `Id`    BIGINT       NOT NULL AUTO_INCREMENT,
    `Title` varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`Id`)
);

CREATE TABLE `WorkStatus`
(
    `Id`    BIGINT       NOT NULL AUTO_INCREMENT,
    `Title` varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`Id`)
);

CREATE TABLE `Performer`
(
    `Id`    BIGINT       NOT NULL AUTO_INCREMENT,
    `Title` varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`Id`)
);

CREATE TABLE `Work`
(
    `id`                   BIGINT        NOT NULL AUTO_INCREMENT,
    `TypeId`               BIGINT        NOT NULL,
    `WorkTitle`            VARCHAR(1024) NOT NULL,
    `ProductTitle`         VARCHAR(1024) NOT NULL,
    `UnitId`               BIGINT,
    `Amount`               INT           NOT NULL,
    `PerformerId`          BIGINT        NOT NULL,
    `ExpectedPaymentDate`  TIMESTAMP,
    `PaymentDate`          TIMESTAMP,
    `ExpectedDeliveryDate` TIMESTAMP,
    `DeliveryDate`         TIMESTAMP,
    `ExpectedFinishDate`   TIMESTAMP,
    `FinishDate`           TIMESTAMP,
    PRIMARY KEY (`id`)
);

CREATE TABLE `Project`
(
    `id`      BIGINT       NOT NULL AUTO_INCREMENT,
    `Title`   varchar(255) NOT NULL,
    `OwnerId` BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `WorkQueue`
(
    `Id`        BIGINT NOT NULL AUTO_INCREMENT,
    `WorkId`    BIGINT NOT NULL,
    `Order`     INT    NOT NULL,
    `ProjectId` BIGINT NOT NULL,
    PRIMARY KEY (`Id`)
);

ALTER TABLE `Account_Role`
    ADD CONSTRAINT `Account_Role_fk0` FOREIGN KEY (`AccountId`) REFERENCES `Account` (`Id`);

ALTER TABLE `Account_Role`
    ADD CONSTRAINT `Account_Role_fk1` FOREIGN KEY (`RoleId`) REFERENCES `Role` (`Id`);

ALTER TABLE `Work`
    ADD CONSTRAINT `Work_fk0` FOREIGN KEY (`TypeId`) REFERENCES `WorkType` (`Id`)
        ON DELETE CASCADE;

ALTER TABLE `Work`
    ADD CONSTRAINT `Work_fk1` FOREIGN KEY (`UnitId`) REFERENCES `Unit` (`Id`)
        ON DELETE CASCADE;

ALTER TABLE `Work`
    ADD CONSTRAINT `Work_fk2` FOREIGN KEY (`PerformerId`) REFERENCES `Performer` (`Id`)
        ON DELETE CASCADE;

ALTER TABLE `Project`
    ADD CONSTRAINT `Project_fk0` FOREIGN KEY (`OwnerId`) REFERENCES `Account` (`Id`)
        ON DELETE CASCADE;

ALTER TABLE `WorkQueue`
    ADD CONSTRAINT `WorkQueue_fk0` FOREIGN KEY (`WorkId`) REFERENCES `Work` (`id`)
        ON DELETE CASCADE;

ALTER TABLE `WorkQueue`
    ADD CONSTRAINT `WorkQueue_fk1` FOREIGN KEY (`ProjectId`) REFERENCES `Project` (`id`)
        ON DELETE CASCADE;
