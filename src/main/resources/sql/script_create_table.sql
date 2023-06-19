CREATE TABLE IF NOT EXISTS Opus(
   Id_Opus SERIAL,
   name VARCHAR(20) NOT NULL,
   PRIMARY KEY(Id_Opus),
   UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS Platform(
   Id_Platform SERIAL,
   label VARCHAR(20) NOT NULL,
   PRIMARY KEY(Id_Platform),
   UNIQUE(label)
);

CREATE TABLE IF NOT EXISTS Stats(
   Id_Stats SERIAL,
   kda DECIMAL(15,2) NOT NULL,
   amount INT NOT NULL,
   Id_Opus INT NOT NULL,
   Id_Platform INT NOT NULL,
   PRIMARY KEY(Id_Stats),
   UNIQUE(kda),
   FOREIGN KEY(Id_Opus) REFERENCES Opus(Id_Opus),
   FOREIGN KEY(Id_Platform) REFERENCES Platform(Id_Platform)
);
