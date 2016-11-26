--DROP TABLE IF EXISTS servers;
CREATE TABLE 'servers' (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    protocol TEXT,
    hostname TEXT,
    port INT
)