CREATE TABLE foo(
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL
);

SELECT * FROM foo;

INSERT INTO foo VALUES (?, ?);

INSERT INTO foo (id, name) VALUES (?, ?);
