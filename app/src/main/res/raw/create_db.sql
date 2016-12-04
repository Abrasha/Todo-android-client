--DROP TABLE IF EXISTS servers;
CREATE TABLE 'servers' (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    protocol TEXT,
    hostname TEXT,
    port INT
);

INSERT INTO servers(protocol, hostname, port) VALUES('https', 'todo-server-application.herokuapp.com', 80);