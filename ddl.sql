BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `VENDOR` (
        `VID`        INTEGER PRIMARY KEY AUTOINCREMENT,
        `Name`        TEXT,
        `Location`        TEXT,
        `URL`        TEXT
);
CREATE TABLE IF NOT EXISTS `USER` (
        `UID`        INTEGER PRIMARY KEY AUTOINCREMENT,
        `Email`        TEXT,
        `Pword`        TEXT,
        `Fname`        TEXT,
        `Lname`        TEXT,
        `Photo`        BLOB,
        `Phone`        INTEGER,
        `Street`        TEXT,
        `City`        TEXT,
        `Postal`        INTEGER,
        `Country`        TEXT
);
CREATE TABLE IF NOT EXISTS `STUDIO` (
        `SID`        INTEGER PRIMARY KEY AUTOINCREMENT,
        `Name`        TEXT
);
CREATE TABLE IF NOT EXISTS `ROLE` (
        `RID`        INTEGER PRIMARY KEY AUTOINCREMENT,
        `Name`        TEXT
);
CREATE TABLE IF NOT EXISTS `PERSON` (
        `PID`        INTEGER PRIMARY KEY AUTOINCREMENT,
        `Fname`        TEXT,
        `Lname`        TEXT,
        `Photo`        BLOB,
        `DOB`        INTEGER
);
CREATE TABLE IF NOT EXISTS `ORDERS` (
        `Confirmation`        INTEGER PRIMARY KEY AUTOINCREMENT,
        `UID`        INTEGER,
        `MID`        INTEGER,
        `VID`        INTEGER,
        `Timestamp`        INTEGER,
        `Cost`        REAL,
        `Quantity`        INTEGER,
        `Showing`        INTEGER,
        `Expiration`        INTEGER,
        FOREIGN KEY (`UID`) REFERENCES USER (`UID`),
        FOREIGN KEY (`MID`) REFERENCES MOVIE (`MID`),
        FOREIGN KEY (`VID`) REFERENCES VENDOR (`VID`)
);
CREATE TABLE IF NOT EXISTS `MOVIE` (
        `MID`        INTEGER PRIMARY KEY AUTOINCREMENT,
        `Name`        TEXT,
        `Rdate`        INTEGER,
        `Genre`        INTEGER,
        `Studio`        INTEGER,
        FOREIGN KEY (`Genre`) REFERENCES GENRE (`GID`),
        FOREIGN KEY (`Studio`) REFERENCES STUDIO (`SID`)
);
CREATE TABLE IF NOT EXISTS `LOVE` (
        `UID`        INTEGER,
        `MID`        INTEGER,
        PRIMARY KEY(`UID`,`MID`),
        FOREIGN KEY (`UID`) REFERENCES USER (`UID`),
        FOREIGN KEY (`MID`) REFERENCES MOVIE (`MID`)
);
CREATE TABLE IF NOT EXISTS `IMAGE` (
        `MID`        INTEGER,
        `Image`        BLOB,
        PRIMARY KEY(`MID`,`Image`),
        FOREIGN KEY (`MID`) REFERENCES MOVIE (`MID`)
);
CREATE TABLE IF NOT EXISTS `GENRE` (
        `GID`        INTEGER PRIMARY KEY AUTOINCREMENT,
        `Name`        TEXT
);
CREATE TABLE IF NOT EXISTS `CREDIT` (
        `CID`        INTEGER PRIMARY KEY AUTOINCREMENT,
        `MID`        INTEGER,
        `PID`        INTEGER,
        `Role`        INTEGER,
        `Photo`        BLOB,
        `Character`        TEXT,
        FOREIGN KEY (`MID`) REFERENCES MOVIE (`MID`),
        FOREIGN KEY (`PID`) REFERENCES PERSON (`PID`),
        FOREIGN KEY (`Role`) REFERENCES ROLE (`RID`)
);
COMMIT;